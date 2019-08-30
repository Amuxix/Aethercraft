package me.amuxix.serialization

import better.files.File
import cats.effect.IO
import cats.implicits.{catsStdInstancesForList, toFoldableOps}
import me.amuxix.EnergyReservoir
import me.amuxix.builder.Builder
import me.amuxix.logging.Logger.info

object Persistable {
  val persistables: List[Persistable[_]] = List(Builder, EnergyReservoir)

  lazy val saveAllPersistables: IO[Unit] = persistables.traverse_(_.save)

  lazy val loadAllPersistables: IO[Unit] = persistables.traverse_(_.load)
}

abstract class Persistable[T] extends GenericPersistable[T] {
  protected val folder: File

  lazy val save: IO[Unit] =
    for {
      _ <- info(s"Saving all $persistablesName")
      _ <- saveAll(persistables.map(thing => getFileName(thing) -> thing), folder)
    } yield ()

  lazy val load: IO[Unit] =
    for {
      amount <- loadAll(folder, updateWithLoaded)
      _ <- amount.fold(IO.unit)(amount => info(s"Loaded $amount $persistablesName"))
    } yield ()

  override def getFile(thing: T): File = folder / (getFileName(thing) + extension)
}
