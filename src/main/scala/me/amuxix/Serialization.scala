package me.amuxix

import java.io._

import io.circe.parser._
import io.circe.syntax._
import io.circe.{Decoder, Encoder}
import io.circe.generic.auto._
import me.amuxix.block.Block
import me.amuxix.block.Block.Location
import me.amuxix.bukkit.Aethercraft
import me.amuxix.exceptions.DeserializationException
import me.amuxix.logging.Logger.info
import me.amuxix.runes.Rune
import me.amuxix.runes.traits.Persistent
import me.amuxix.runes.waypoints._
import me.amuxix.runes.waypoints.WaypointSize.Medium

import scala.collection.immutable.HashMap
import scala.io.Source
import scala.util.Try

/**
  * Created by Amuxix on 27/01/2017.
  */
object Serialization {
  private val fileTermination = ".rune"
  private val backupTermination = ".old"

  /**
    * Lists of persistent runes that will be serialized
    */
  var persistentRunes = HashMap.empty[Location, Persistent]

  /** The map key is the [[me.amuxix.runes.traits.Linkable.signature]] of the waypoint */
  var waypoints = Map.empty[Int, GenericWaypoint]

  private val magicFileName = Aethercraft.simpleVersion
  private val waypointsFileName = "Waypoints"

  //Encoders
  implicit val encodeWaypoints: Encoder[GenericWaypoint] =
    Encoder.forProduct6("blocks", "center", "activator", "direction", "signature", "size")(w => (w.blocks, w.center, w.activator, w.direction, w.signature, w.size))

  //Decoders
  implicit val decodeWaypoints: Decoder[GenericWaypoint] =
    Decoder.forProduct6[GenericWaypoint, Array[Array[Array[Block]]], Location, Player, Direction, Int, WaypointSize]("blocks", "center", "activator", "direction", "signature", "size") {
      case (blocks, center, activator, direction, signature, `Medium`) =>
        Waypoint.deserialize(blocks, center, activator, direction, signature)
      case _ => throw DeserializationException("a Waypoint")
    }

  /**
    * Creates a backup of the old file when loading.
    *
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
    saveWaypoints(waypoints.values.toArray)
  }

  /**
    * Loads all runes it can find in the aethercraft folder in each world
    */
  def loadRunes(): Unit = {
    Aethercraft.server.getWorlds.forEach(world => {
      val magicFolder = new File(world.getWorldFolder, magicFileName)
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
  private def saveWaypoints(waypoints: Array[Rune with GenericWaypoint]): Unit = {
    waypoints.groupBy(_.center.world).foreach{ case (world, waypointsInWorld) =>
      val magicFolder = new File(world.worldFolder, magicFileName)
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
    val waypoints = runesInWorld[Rune with GenericWaypoint](magicFile, waypointsFileName)
    if (waypoints.isDefined) {
      this.waypoints ++= waypoints.get.map(w => w.signature -> w)
      info(s"  - Loaded ${waypoints.get.length} $waypointsFileName")
    }
  }
}
