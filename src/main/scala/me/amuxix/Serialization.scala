package me.amuxix

import java.io._

import cats.data.{EitherT, OptionT}
import cats.effect.IO
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
import scala.collection.JavaConverters._

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
  def saveEverything: IO[Unit] = {
    saveWaypoints(waypoints.values.toArray)
    save(Aethercraft.self.getDataFolder, reservoirsFileName, reservoirsFileName, EnergyReservoir.energyReservoirs) //Save Reservoirs
  }

  /**
    * Loads all runes it can find in the aethercraft folder in each world
    */
  def loadEverything: IO[Unit] = {
    val loadRunes = Aethercraft.server.getWorlds.asScala.foldLeft(IO.unit) { (acc, world) =>
      val magicFolder = new File(world.getWorldFolder, magicFolderName)
      acc.flatMap { _ =>
        if (thingsExist(magicFolder)) {
          info("Loading runes in " + world.getName)
          for {
            _ <- loadWaypoints(magicFolder)
          } yield ()
        } else {
          info("No runes found in " + world.getName)
        }
      }
    }
    for {
      _ <- loadRunes
      _ <- loadReservoirs
    } yield ()

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
  private def saveWaypoints(waypoints: Array[GenericWaypoint]): IO[Unit] =
    waypoints.groupBy(_.center.world).foldLeft(IO.unit) { case (acc, (world, waypointsInWorld)) =>
      acc.flatMap { _ =>
        save(world.worldFolder, magicFolderName, waypointsFileName, waypointsInWorld)
      }
    }

  /**
    * Loads all waypoints from the given file and logs the number of waypoints loaded
    * @param magicFile Folder that contains rune files
    * @throws Error is thrown when it fails to load
    */
  private def loadWaypoints(magicFile: File): IO[Unit] =
    load[GenericWaypoint](magicFile, waypointsFileName).value.flatMap { option =>
      option.foreach{ waypoints =>
        this.waypoints = waypoints.map(w => w.signature -> w).toMap
      }
      info(s"  - Loaded ${waypoints.size} $waypointsFileName")
    }


  private def loadReservoirs: IO[Unit] = {
    val reservoirsFolder = new File(Aethercraft.self.getDataFolder, reservoirsFileName)
    if (thingsExist(reservoirsFolder)) {
      load[(Player, EnergyReservoir)](reservoirsFolder, reservoirsFileName).map { reservoirs =>
        EnergyReservoir.energyReservoirs ++ reservoirs.toMap
        info(s"Loaded ${reservoirs.size} $reservoirsFileName")
      }.value.map(_ => ())
    } else {
      IO.unit
    }
  }

  private def save[T](saveFolder: File, folderName: String, fileName: String, things: Iterable[T])(implicit encoder: Encoder[Iterable[T]]): IO[Unit] = {
    val folder = new File(saveFolder, folderName)
    val saveFile = IO{
      val bw = new BufferedWriter(new FileWriter(new File(folder, fileName + fileTermination)))
      try {
        bw.write(things.asJson.spaces2)
      } finally {
        bw.close()
      }
    }
    for {
      _ <- IO(folder.mkdir()) //Makes the dir if it does not exist, does nothing otherwise
      _ <- backupOldFile(folder, fileName)
      _ <- saveFile
    } yield ()
  }

  /**
    * Creates a backup of the old file when saving.
    *
    * @param worldFolder Folder of the world we are backing up
    * @param fileName File we are backing up
    */
  private def backupOldFile(worldFolder: File, fileName: String): IO[Unit] = IO{
    val file = new File(worldFolder, fileName + fileTermination)
    if (file.exists()) {
      val backupFile = new File(worldFolder, fileName + fileTermination + backupTermination)
      backupFile.delete()
      file.renameTo(backupFile)
    }
  }

  private def load[T](folder: File, fileName: String)(implicit decoder: Decoder[Iterable[T]]): OptionT[IO, Iterable[T]] = {
    def decodeFromFile(file: File): EitherT[IO, Throwable, Iterable[T]] =
      EitherT.fromEither(Try(Source.fromFile(file).mkString).toEither.flatMap(s => decode[Iterable[T]](s)))
    val file = new File(folder, fileName + fileTermination)
    val backupFile = new File(folder, fileName + fileTermination + backupTermination)
    decodeFromFile(file).toOption orElse {
      OptionT(decodeFromFile(backupFile).value.flatMap {
        case Right(r) =>
          info(s"Using backup file for $fileName.").map { _ =>
            Some(r)
          }
        case Left(_: FileNotFoundException) => IO(None)
        case Left(e) => IO[Option[Iterable[T]]](throw e)
      })
    }
  }
}
