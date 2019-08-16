package me.amuxix

import java.io._

import cats.data.{EitherT, OptionT}
import cats.effect.IO
import io.circe.parser._
import io.circe.syntax._
import io.circe.{Decoder, Encoder}
import me.amuxix.logging.Logger.info
import me.amuxix.runes.waypoints._

import scala.io.Source
import scala.util.Try

/**
  * Created by Amuxix on 27/01/2017.
  */

object Serialization {
  lazy val magicFolderName: String = Aethercraft.simpleVersion
  lazy val worlds: Iterable[(String, File)] = Aethercraft.worlds.values.map(w => (w.name, w.worldFolder))
  var reservoirsFile: File = _

  private val fileTermination = ".rune"
  private val backupTermination = ".old"

  private val waypointsFileName = "Waypoints"
  private val reservoirsFileName = "Reservoirs"

  def saveEverythingSync: IO[Unit] =
    for {
      _ <- saveWaypoints(false)
      _ <- saveReservoirs(false)
    } yield ()

  /**
    * Loads all runes it can find in the aethercraft folder in each world
    */
  def loadEverything: IO[Unit] =
    for {
      _ <- loadAllRules
      _ <- loadReservoirs
    } yield ()

  private def loadAllRules = {
    worlds.foldLeft(IO.unit) { case (acc, (name, folder)) =>
      val magicFolder = new File(folder, magicFolderName)
      acc.flatMap { _ =>
        if (thingsExist(magicFolder)) {
          for {
            _ <- info(s"Loading runes in $name")
            _ <- loadWaypoints(magicFolder)
          } yield ()
        } else {
          info(s"No runes found in $name")
        }
      }
    }
  }

  /**
    * Checks if the folder with things exists are there is at least one thing to be loaded
    *
    * @param folder Folder with the possible things
    *
    * @return True if there are things to load false otherwise
    */
  private def thingsExist(folder: File): Boolean = {
    folder.exists() && folder.isDirectory &&
      folder.listFiles().exists(f => f.isFile && (f.getName.endsWith(fileTermination) || f.getName.endsWith(backupTermination)))
  }

  /**
    * Saves all waypoints to files in the world folders
    */
  def saveWaypoints(async: Boolean): IO[Unit] =
    Waypoint.waypoints.values.groupBy(_.center.world).foldLeft(IO.unit) { case (acc, (world, waypointsInWorld)) =>
      acc.flatMap { _ =>
        save(world.worldFolder, magicFolderName, waypointsFileName, waypointsInWorld)(async)
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
        Waypoint.waypoints = waypoints.map(w => w.signature -> w).toMap
      }
      info(s"  - Loaded ${Waypoint.waypoints.size} $waypointsFileName")
    }

  def saveReservoirs(async: Boolean): IO[Unit] = {
    save(reservoirsFile, reservoirsFileName, reservoirsFileName, EnergyReservoir.energyReservoirs)(async)
  }


  private def loadReservoirs: IO[Unit] = {
    val reservoirsFolder = new File(reservoirsFile, reservoirsFileName)
    if (thingsExist(reservoirsFolder)) {
      load[(Player, EnergyReservoir)](reservoirsFolder, reservoirsFileName).map { reservoirs =>
        EnergyReservoir.energyReservoirs ++ reservoirs.toMap
        info(s"Loaded ${reservoirs.size} $reservoirsFileName")
      }.value.map(_ => ())
    } else {
      IO.unit
    }
  }

  private def save[T](saveFolder: File, folderName: String, fileName: String, things: Iterable[T])(async: Boolean)(implicit encoder: Encoder[Iterable[T]]): IO[Unit] = {
    def innerSave = {
      val folder = new File(saveFolder, folderName)
      val file = new File(folder, fileName + fileTermination)
      val saveFile = IO {
        val bw = new BufferedWriter(new FileWriter(file))
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

    if (async) {
      IO(Aethercraft.runTaskAsync(innerSave))
    } else {
      innerSave
    }
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
      val backupFile =
        new File(folder, fileName + fileTermination + backupTermination)
      decodeFromFile(file).toOption orElse {
        OptionT(decodeFromFile(backupFile).value.flatMap {
          case Right(r) =>
            info(s"Using backup file for $fileName.").map { _ =>
              Some(r)
            }
          case Left(_: FileNotFoundException) => IO(None)
          case Left(e)                        => IO[Option[Iterable[T]]](throw e)
        })
      }
  }
}
