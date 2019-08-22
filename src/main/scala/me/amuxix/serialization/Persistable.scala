package me.amuxix.serialization

import java.nio.file.StandardOpenOption

import better.files.{File, FileExtensions}
import cats.data.EitherT
import cats.effect.IO
import cats.implicits.{catsStdInstancesForList, toFlatMapOps, toFoldableOps}
import io.circe.parser.decode
import io.circe.syntax.EncoderOps
import io.circe.{Decoder, Encoder}
import me.amuxix.logging.Logger.{info, severe}
import me.amuxix.{Aethercraft, EnergyReservoir, World}

/**
  * Created by Amuxix on 27/01/2017.
  */
object Persistable {
  lazy val magicFolderName: String = Aethercraft.simpleVersion
  lazy val worldMagicFolders: Map[World, File] = Aethercraft.worlds.values.map { world =>
    world -> world.worldFolder.toScala.createChild(magicFolderName, asDirectory = true)
  }.toMap

  val fileTermination = ".rune"
  val backupTermination = ".backup"

  val persistables: List[Persistable[_]] = PersistableRune.persistableRunes ++ List(EnergyReservoir)

  val saveEverything: IO[Unit] = for {
    _ <- PersistableRune.saveAllRunes
    _ <- EnergyReservoir.saveReservoirs
  } yield ()

  val loadEverything: IO[Unit] = for {
    _ <- PersistableRune.loadAllRunes
    _ <- EnergyReservoir.loadReservoirs
  } yield ()
}

abstract class Persistable[T] {
  implicit val encoder: Encoder[T]
  implicit val decoder: Decoder[T]

  protected def saveAll(persistables: Map[String, T], folder: File): IO[Unit] =
    persistables.toList.traverse_ {
      case (fileName, thing) => saveToFile(folder, fileName, thing)
  }

  protected def loadAll(folder: File, updateWithLoaded: (String, T) => Unit): IO[Int] = {
    val things: List[File] = folder.list(_.extension.contains(extension)).toList
    things.traverse_ { file =>
      loadFromFile(file).map(_.fold(())(updateWithLoaded(file.nameWithoutExtension, _)))
    }
      .map(_ => things.size)
  }

  private def backupFile(file: File): IO[Unit] =
    IO {
      if (file.exists) {
        file.moveTo(file.path.resolveSibling(file.name + Persistable.backupTermination))(File.CopyOptions(overwrite = true))
      }
    }

  protected def saveToFile(folder: File, fileName: String, thing: T): IO[Unit] = {
    val file = folder.createChild(fileName + extension)
    backupFile(file).map { _ =>
      file.bufferedWriter(openOptions = Seq(StandardOpenOption.CREATE, StandardOpenOption.WRITE))
        .foreach(_.write(thing.asJson.spaces2))
    }
  }

  protected def loadFromFile(file: File): IO[Option[T]] =
    EitherT.fromEither[IO](decode[T](file.contentAsString))
      .recoverWith {
        case _ =>
          EitherT(info(s"Using backup file for ${file.name}.").map { _ =>
            decode[T](file.sibling(file.name + Persistable.backupTermination).contentAsString)
          })
      }
      .fold(
        error => severe(error).map(_ => None),
        decoded => IO(Some(decoded))
      )
      .flatten

  protected val persistablesName: String

  protected val extension: String = ".rune"

  def saveOneAsync(folder: File, fileName: String, thing: T) = IO(Aethercraft.runTaskAsync(
    saveToFile(folder, fileName, thing)
  ))

  protected def updateWithLoaded(fileName: String, thing: T): Unit
}