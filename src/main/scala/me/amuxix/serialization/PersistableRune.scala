package me.amuxix.serialization

import better.files.File
import cats.effect.IO
import cats.implicits.{catsStdInstancesForList, toFoldableOps}
import me.amuxix.logging.Logger.info
import me.amuxix.runes.waypoints.GenericWaypoint
import me.amuxix.{Aethercraft, Named, World}


object PersistableRune {
  val persistableRunes: List[PersistableRune[_]] = List(GenericWaypoint)

  val saveAllRunes: IO[Unit] = persistableRunes.traverse_(_.saveRunes)

  val loadAllRunes: IO[Unit] = Aethercraft.worlds.values.toList.traverse_ { world =>
    for {
      _ <- info(s"Loading runes in ${world.name}")
      loadMessage <- persistableRunes.traverse_(_.loadFromWorld(world))
      _ <- info(s"  - $loadMessage")
    } yield ()
  }
}

abstract class PersistableRune[Rune] extends Persistable[Rune] with Named {
  protected def runes: Map[World, Map[String, Rune]]

  override protected val persistablesName: String = name

  def runeMagicFolder(world: World): File = Persistable.worldMagicFolders(world) / (persistablesName + Persistable.fileTermination)

  private def loadFromWorld(world: World): IO[String] =
    loadAll(runeMagicFolder(world), updateWithLoaded).map(size => s"Loaded $size $persistablesName.")


  private val saveAll: IO[Unit] =
    runes.toList.traverse_ {
      case (world, fileNameToRune) =>
        val magicFolder = runeMagicFolder(world)
        fileNameToRune.toList.traverse_ {
          case (fileName, rune) =>
            saveToFile(magicFolder, fileName, rune)
        }
    }

  val saveRunes: IO[Unit] =
    for {
      _ <- info(s"Saving all $persistablesName")
      _ <- saveAll
    } yield ()
}
