package me.amuxix

import java.io._

import io.circe.parser._
import io.circe.syntax._
import io.circe.{Decoder, Encoder}
import me.amuxix.block.Block.Location
import me.amuxix.bukkit.Aethercraft
import me.amuxix.logging.Logger.info
import me.amuxix.runes.traits.Persistent
import me.amuxix.runes.waypoints._

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

  private val magicFolderName = Aethercraft.simpleVersion
  private val waypointsFileName = "Waypoints"
  private val reservoirsFileName = "Reservoirs"

  /**
    * Saves all runes to their respective files
    */
  def saveEverything(): Unit = {
    saveWaypoints(waypoints.values.toArray)
    save(Aethercraft.self.getDataFolder, reservoirsFileName, reservoirsFileName, EnergyReservoir.energyReservoirs) //Save Reservoirs
  }

  /**
    * Loads all runes it can find in the aethercraft folder in each world
    */
  def loadEverything(): Unit = {
    Aethercraft.server.getWorlds.forEach { world =>
      val magicFolder = new File(world.getWorldFolder, magicFolderName)
      if (thingsExist(magicFolder)) {
        info("Loading runes in " + world.getName)
        loadWaypoints(magicFolder)
      } else {
        info("No runes found in " + world.getName)
      }
    }
    loadReservoirs()
  }

  /**
    * Checks if the folder with things exists are there is at least one thing to be loaded
    * @param folder Folder with the possible things
    * @return True if there are things to load false otherwise
    */
  private def thingsExist(folder: File): Boolean = {
    folder.exists() && folder.isDirectory &&
      folder.listFiles().exists(f => f.isFile && (f.getName.endsWith(fileTermination) || f.getName.endsWith(backupTermination)))
  }

  /**
    * Saves all waypoints to files in the world folders
    * @param waypoints Array of all waypoints to save
    */
  private def saveWaypoints(waypoints: Array[GenericWaypoint]): Unit =
    waypoints.groupBy(_.center.world).foreach { case (world, waypointsInWorld) =>
      save(world.worldFolder, magicFolderName, waypointsFileName, waypointsInWorld)
    }

  /**
    * Loads all waypoints from the given file and logs the number of waypoints loaded
    * @param magicFile Folder that contains rune files
    * @throws Error is thrown when it fails to load
    */
  private def loadWaypoints(magicFile: File): Unit =
    load[GenericWaypoint](magicFile, waypointsFileName).foreach { waypoints =>
      this.waypoints = waypoints.map(w => w.signature -> w).toMap
      info(s"  - Loaded ${waypoints.size} $waypointsFileName")
    }


  private def loadReservoirs(): Unit = {
    val reservoirsFolder = new File(Aethercraft.self.getDataFolder, reservoirsFileName)
    if (thingsExist(reservoirsFolder)) {
      info("Loading reservoirs")
      load[(Player, EnergyReservoir)](reservoirsFolder, reservoirsFileName).foreach { reservoirs =>
        EnergyReservoir.energyReservoirs ++ reservoirs.toMap
        info(s"  - Loaded ${reservoirs.size} $reservoirsFileName")
      }
    }
  }

  private def save[T](saveFolder: File, folderName: String, fileName: String, things: Iterable[T])(implicit encoder: Encoder[Iterable[T]]): Unit = {
    val folder = new File(saveFolder, folderName)
    folder.mkdir() //Makes the dir if it does not exist, does nothing otherwise
    backupOldFile(folder, fileName)
    val bw = new BufferedWriter(new FileWriter(new File(folder, fileName + fileTermination)))
    bw.write(things.asJson.spaces2)
    bw.close()
  }

  /**
    * Creates a backup of the old file when saving.
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

  private def load[T](folder: File, fileName: String)(implicit decoder: Decoder[Iterable[T]]): Option[Iterable[T]] = {
    def decodeFromFile(file: File): Either[Throwable, Iterable[T]] = Try(Source.fromFile(file).mkString).toEither.flatMap(s => decode[Iterable[T]](s))
    val file = new File(folder, fileName + fileTermination)
    val backupFile = new File(folder, fileName + fileTermination + backupTermination)
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
}
