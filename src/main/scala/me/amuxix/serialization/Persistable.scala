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
  protected def persistables: Map[String, T]

  lazy val save: IO[Unit] =
    for {
      _ <- info(s"Saving all $persistablesName")
      _ <- saveAll(persistables, folder)
    } yield ()

  lazy val load: IO[Unit] =
    for {
      amount <- loadAll(folder, updateWithLoaded)
      _ <- info(s"Loaded $amount $persistablesName")
    } yield ()
}
