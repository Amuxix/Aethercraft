package me.amuxix.serialization

import java.nio.file.StandardOpenOption

import better.files.{File, FileExtensions}
import cats.data.EitherT
import cats.effect.IO
import cats.implicits.{catsStdInstancesForList, toFlatMapOps, toFoldableOps}
import io.circe
import io.circe.parser.decode
import io.circe.syntax.EncoderOps
import io.circe.{Decoder, Encoder}
import me.amuxix.logging.Logger.{info, severe}
import me.amuxix.{Aethercraft, World}

/**
  * Created by Amuxix on 27/01/2017.
  */
object GenericPersistable {
  lazy val magicFolderName: String = Aethercraft.simpleVersion
  lazy val worldMagicFolders: Map[World, File] = Aethercraft.worlds.values.map { world =>
    world -> world.worldFolder.toScala.createChild(magicFolderName, asDirectory = true)
  }.toMap

  val fileTermination = ".rune"
  val backupTermination = ".backup"

  lazy val saveEverything: IO[Unit] = for {
    _ <- PersistableRune.saveAllRunes
    _ <- Persistable.saveAllPersistables
  } yield ()

  lazy val loadEverything: IO[Unit] = for {
    _ <- PersistableRune.loadAllRunes
    _ <- Persistable.loadAllPersistables
  } yield ()
}

abstract class GenericPersistable[T] extends {
  implicit val encoder: Encoder[T]
  implicit val decoder: Decoder[T]

  protected def saveAll(persistables: Map[String, T], folder: File): IO[Unit] =
    persistables.toList.traverse_ {
      case (fileName, thing) => saveToFile(folder, fileName, thing)
  }

  protected def loadAll(folder: File, updateWithLoaded: (String, T) => Unit): IO[Int] = {
    if (folder.exists) {
      val things: List[File] = folder
        //This will get all runes as well as all backups
        .list(_.extension(includeAll = true).exists(_.contains(extension)))
        .toList
        .groupBy(_.nameWithoutExtension(includeAll = true))
        .values
        //This keeps the file with the shortest name, backups should always have longer names as they are appended with the backup extension
        .map(_.minBy(_.name.length))
        .toList

      things.traverse_ { file =>
        loadFromFile(file)
          .fold(
            error => severe(error).map(_ => None),
            decoded => IO(Some(decoded))
          )
          .flatten
          .map(_.fold(())(updateWithLoaded(file.nameWithoutExtension, _)))
      }
        .map(_ => things.size)
    } else {
      IO(0)
    }
  }

  private def backupFile(file: File): IO[Unit] =
    IO {
      if (file.exists) {
        file.moveTo(file.path.resolveSibling(file.name + GenericPersistable.backupTermination))(File.CopyOptions(overwrite = true))
      }
    }

  protected def saveToFile(folder: File, fileName: String, thing: T, backup: Boolean = true): IO[Unit] = {
    val file = folder / (fileName + extension)
    (if (backup) {
      backupFile(file)
    } else {
      IO.unit
    })
      .map { _ =>
        file.createIfNotExists(createParents = true).bufferedWriter(openOptions = Seq(StandardOpenOption.CREATE, StandardOpenOption.WRITE))
          .foreach(_.write(thing.asJson.spaces2))
      }
  }

  protected def loadFromFile(file: File): EitherT[IO, circe.Error, T] =
    if (file.extension.contains(GenericPersistable.backupTermination)) {
      EitherT(info(s"Using backup file for ${file.name}.").map { _ =>
        decode[T](file.sibling(file.name).contentAsString)
      }).flatMapF { thing =>
        saveToFile(file.parent, file.nameWithoutExtension(includeAll = true), thing, backup = false)
          .map(_ => Right(thing))
      }
    } else {
      EitherT.fromEither[IO](decode[T](file.contentAsString)).recoverWith {
        //This will try to recover from the backup if this is not a backup
        case _ if !file.extension.contains(GenericPersistable.backupTermination) =>
          loadFromFile(file.sibling(file.name + GenericPersistable.backupTermination))
      }
    }


  protected val persistablesName: String

  protected val extension: String = ".rune"

  def saveOneAsync(folder: File, fileName: String, thing: T) = IO(Aethercraft.runTaskAsync(
    saveToFile(folder, fileName, thing)
  ))

  protected def updateWithLoaded(fileName: String, thing: T): Unit
}