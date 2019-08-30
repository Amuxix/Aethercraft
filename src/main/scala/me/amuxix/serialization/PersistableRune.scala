package me.amuxix.serialization

import better.files.File
import cats.effect.IO
import cats.implicits.{catsStdInstancesForLazyList, catsStdInstancesForList, toFoldableOps, toTraverseOps}
import me.amuxix.logging.Logger.info
import me.amuxix.runes.waypoints.GenericWaypoint
import me.amuxix.{Aethercraft, World}


object PersistableRune {
  lazy val persistableRunes: LazyList[PersistableRune[_]] = LazyList(GenericWaypoint)

  lazy val saveAllRunes: IO[Unit] = persistableRunes.traverse_(_.saveRunes)

  lazy val loadAllRunes: IO[Unit] = Aethercraft.worlds.values.toList.traverse_ { world =>
    for {
      _ <- info(s"Loading runes in ${world.name}")
      loadMessage <- persistableRunes.traverse(_.loadFromWorld(world)).map(_.mkString("\n"))
      _ <- info(loadMessage)
    } yield ()
  }
}

abstract class PersistableRune[Rune] extends GenericPersistable[Rune] {
  protected def runes: Map[World, Map[String, Rune]]

  def runeMagicFolder(world: World): File = GenericPersistable.worldMagicFolders(world) / persistablesName

  private def loadFromWorld(world: World): IO[String] =
    loadAll(runeMagicFolder(world), updateWithLoaded).map(size => s"  - Loaded $size $persistablesName.")

  private lazy val saveAll: IO[Unit] =
    runes.toList.traverse_ {
      case (world, fileNameToRune) =>
        val magicFolder = runeMagicFolder(world)
        fileNameToRune.toList.traverse_ {
          case (fileName, rune) =>
            saveToFile(magicFolder, fileName, rune)
        }
    }

  lazy val saveRunes: IO[Unit] =
    for {
      _ <- info(s"Saving all $persistablesName")
      _ <- saveAll
    } yield ()
}
