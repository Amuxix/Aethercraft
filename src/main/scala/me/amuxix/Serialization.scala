package me.amuxix

import java.io._

import io.circe.parser._
import io.circe.syntax._
import io.circe.{Decoder, Encoder}
import me.amuxix.logging.Logger.info
import me.amuxix.runes.Rune
import me.amuxix.runes.waypoints.{Waypoint, WaypointTrait}

import scala.io.Source
import scala.util.Try

/**
  * Created by Amuxix on 27/01/2017.
  */
object Serialization {
  private val fileTermination = ".rune"
  private val backupTermination = ".old"

  private val runecraftMagicFileName = Runecraft.simpleVersion
  private val waypointsFileName = "Waypoints"

  //Encoders
  implicit val encodeWaypoints: Encoder[Rune with WaypointTrait] =
    Encoder.forProduct5("blocks", "center", "activator", "direction", "signature")(w => (w.blocks, w.center, w.activator, w.direction, w.signature))

  //Deciders
  implicit val decodeWaypoints: Decoder[Rune with WaypointTrait] =
    Decoder.forProduct5("blocks", "center", "activator", "direction", "signature")(Waypoint.deserialize)

  /**
    * Creates a backup of the old file when loading.
    * @param worldFolder Folder of the world we are backing up
    * @param fileName File we are backing up
    */
  private def backupOldFile(worldFolder: File, fileName: String): Unit = {
    val file = new File(worldFolder, fileName + fileTermination)
    if (file.exists()) {
      val backupFile = new File(worldFolder, fileName + fileTermination + backupTermination)
      backupFile.delete()
      file.renameTo(backupFile)
    }
  }

  /**
    * Saves all runes to their respective files
    */
  def saveRunes(): Unit = {
    saveWaypoints(Runecraft.waypoints.values.toArray)
  }

  /**
    * Loads all runes it can find in the runecraft folder in each world
    */
  def loadRunes(): Unit = {
    Runecraft.server.getWorlds.forEach(world => {
      val magicFolder = new File(world.getWorldFolder, runecraftMagicFileName)
      if (magicFolder.exists() && magicFolder.isDirectory && magicFolder.listFiles().exists(f =>
          f.isFile && (f.getName.endsWith(fileTermination) || f.getName.endsWith(backupTermination)))) {
        info("Loading runes in " + world.getName)
        loadWaypoints(magicFolder)
      } else {
        info("No runes found in " + world.getName)
      }
    })
  }

  private def runesInWorld[T](magicFolder: File, fileName: String)(implicit decoder: Decoder[Array[T]]): Option[Array[T]] = {
    def decodeFromFile(file: File): Either[Throwable, Array[T]] = Try(Source.fromFile(file).mkString).toEither.flatMap(s => decode[Array[T]](s))
    val file = new File(magicFolder, fileName + fileTermination)
    val backupFile = new File(magicFolder, fileName + fileTermination + backupTermination)
    decodeFromFile(file).toOption orElse {
      decodeFromFile(backupFile) match {
        case Right(r) =>
          info(s"Using backup file for $fileName.")
          Some(r)
        case Left(_: FileNotFoundException) => None
        case Left(e) => throw e
      }
    }
  }

  /**
    * Saves all waypoints to files in the world folders
    * @param waypoints Array of all waypoints to save
    */
  private def saveWaypoints(waypoints: Array[Rune with WaypointTrait]): Unit = {
    waypoints.groupBy(_.center.world).foreach{ case (world, waypointsInWorld) =>
      val magicFolder = new File(world.getWorldFolder, runecraftMagicFileName)
      magicFolder.mkdir() //Makes the dir if it does not exist, does nothing otherwise
      backupOldFile(magicFolder, waypointsFileName)
      val waypointFile = new File(magicFolder, waypointsFileName + fileTermination)
      val bw = new BufferedWriter(new FileWriter(waypointFile))
      bw.write(waypointsInWorld.asJson.spaces2)
      bw.close()
    }
  }

  /**
    * Loads all waypoints from the given file and logs the number of waypoints loaded
    * @param magicFile Folder that contains rune files
    * @throws Error is thrown when it fails to load
    */
  private def loadWaypoints(magicFile: File): Unit = {
    val waypoints = runesInWorld[Rune with WaypointTrait](magicFile, waypointsFileName)
    if (waypoints.isDefined) {
      Runecraft.waypoints ++= waypoints.get.map(w => w.signature -> w)
      info(s"  - Loaded ${waypoints.get.length} $waypointsFileName")
    }
  }
}
