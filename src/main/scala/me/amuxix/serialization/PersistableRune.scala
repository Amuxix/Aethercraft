package me.amuxix.serialization

import better.files.File
import cats.effect.IO
import cats.implicits.{catsStdInstancesForLazyList, catsStdInstancesForList, toFoldableOps, toTraverseOps}
import me.amuxix.logging.Logger.info
import me.amuxix.runes.Rune
import me.amuxix.runes.waypoints.GenericWaypoint
import me.amuxix.{Aethercraft, World}


object PersistableRune {
  lazy val persistableRunes: LazyList[PersistableRune[_]] = LazyList(GenericWaypoint)

  lazy val saveAllRunes: IO[Unit] = persistableRunes.traverse_(_.saveRunes)

  lazy val loadAllRunes: IO[Unit] = Aethercraft.worlds.values.toList.traverse_ { world =>
    val worldLoadMessage = s"Loading runes in ${world.name}"
    for {
      loadMessages <- persistableRunes.traverse(_.loadFromWorld(world)).map(_.flatten)
      _ <- if (loadMessages.nonEmpty) (worldLoadMessage +: loadMessages).traverse_(info) else info(s"No runes found in ${world.name}")
    } yield ()
  }
}

abstract class PersistableRune[R <: Rune] extends GenericPersistable[R] {

  def runeMagicFolder(world: World): File = GenericPersistable.worldMagicFolders(world) / persistablesName

  private def loadFromWorld(world: World): IO[Option[String]] =
    loadAll(runeMagicFolder(world), updateWithLoaded).map(_.map(amount => s"  - Loaded $amount $persistablesName."))

  private lazy val saveAll: IO[Unit] =
    persistables.traverse_ { rune =>
      saveToFile(getFile(rune), rune)
    }

  lazy val saveRunes: IO[Unit] =
    for {
      _ <- info(s"Saving all $persistablesName")
      _ <- saveAll
    } yield ()

  override def getFile(rune: R): File = runeMagicFolder(rune.center.world) / (getFileName(rune) + extension)

  def updateFileName(rune: R, oldFile: File): IO[Unit] = IO {
    val file = getFile(rune)
    oldFile.moveTo(file)(File.CopyOptions(overwrite = true))
    val backup = oldFile.sibling(oldFile.name + GenericPersistable.backupTermination)
    if (backup.exists) {
      backup.moveTo(file.sibling(file.name + GenericPersistable.backupTermination))(File.CopyOptions(overwrite = true))
    }
  }
}
