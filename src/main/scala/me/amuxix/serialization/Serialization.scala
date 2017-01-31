package me.amuxix.serialization

import java.io.{BufferedWriter, File, FileWriter}

import io.circe.parser._
import io.circe.syntax._
import io.circe.{Decoder, Encoder, Error}
import me.amuxix.Runecraft
import me.amuxix.logging.Logger
import me.amuxix.logging.Logger.info
import me.amuxix.runes.Rune
import me.amuxix.runes.waypoints.{Waypoint, WaypointTrait}

import scala.io.Source

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
      if (magicFolder.exists()) {
        info("Loading runes in " + world.getName)
        loadRunesInWorld(magicFolder, waypointsFileName, loadWaypoints)
      } else {
        info("No runes found in " + world.getName)
      }
    })
  }

  /**
    * Loads the runes of a given type found in the magic folder
    * @param magicFolder Folder that contains all rune files, default name is Runecraft v#, where # is version number
    * @param fileName Name of the file that contains runes that the loader knows how to load
    * @param loader Loader that knows how to load a specific type of rune
    */
  private def loadRunesInWorld(magicFolder: File, fileName: String, loader: (File) => Unit): Unit = {
    val file = new File(magicFolder, fileName + fileTermination)
    val backupFile = new File(magicFolder, fileName + fileTermination + backupTermination)
    try {
      if (file.exists()) {
        try {
          loader(file)
        } catch {
          case _: Error => loader(backupFile)
        }
      } else if (backupFile.exists()) {
        loader(backupFile)
      }
    } catch {
      case _: Error => Logger.severe(s"Fail when loading ${fileName}s")
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
    * Loads all waypoints from the given file
    * @param waypointFile File that contains the waypoints
    * @throws Error is thrown when it fails to load
    */
  private def loadWaypoints(waypointFile: File): Unit = {
    val waypointsJSON = Source.fromFile(waypointFile).mkString
    decode[Array[Rune with WaypointTrait]](waypointsJSON) match {
      case Left(error) => throw error
      case Right(waypoints) =>
        Runecraft.waypoints ++= waypoints.map(w => w.signature -> w)
        info(s"  - Loaded ${waypoints.length} Waypoints")
    }
  }
}
