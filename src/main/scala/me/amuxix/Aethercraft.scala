package me.amuxix
import java.util.UUID
import java.util.logging.Logger

import better.files.File
import cats.effect.IO
import cats.implicits.{catsStdInstancesForList, toFoldableOps, toTraverseOps}
import me.amuxix.builder.Builder
import me.amuxix.bukkit.Bukkit
import me.amuxix.logging.Logger.info
import me.amuxix.material.Recipe
import me.amuxix.pattern.RunePattern
import me.amuxix.runes._
import me.amuxix.runes.test._
import me.amuxix.runes.waypoints.Waypoint
import me.amuxix.serialization.Persistable

import scala.concurrent.ExecutionContext.global
import scala.concurrent.Future
import scala.util.Try

object Aethercraft {
  val defaultFailureMessage = "Some unknown force blocks you."
  val approvedTesters: List[UUID] = List("a398585d-b180-457e-a018-fcd5d04a18fc").flatMap(uuid => Try(UUID.fromString(uuid)).toOption)
  val testRunes: LazyList[RunePattern[_]] = LazyList(Test, Test2, Mover)
  val activeRunes: LazyList[RunePattern[_]] = LazyList(Waypoint, Teleporter, Compass, TrueName, RunicChest, SuperTool,
    Disenchanter, MagicEightBall, Divination, Portkey)

  var logger: Logger = _
  var builderTaskId: Int = _
  var dataFolder: File = _

  var fullVersion: String = _
  lazy val simpleVersion: String = fullVersion.split("-").head
  var players: Map[UUID, Player] = Map.empty
  implicit val ec = global

  def runTaskSync(task: IO[Unit]): Unit = Bukkit.runTaskSync(task)

  def runTimedTask(tasks: List[IO[Unit]], interval: Int): Unit = {
    tasks.foldLeft(0) {
      (delay, task) =>
        Bukkit.runTaskLater(task, delay)
        delay + interval
    }
  }

  def runTaskAsync(task: IO[Unit]): Unit = Future(task.unsafeRunSync())

  /**
    * Setups a repeating task
    * @param task Function that generates the task to run
    * @param period Period in server ticks of the task
    * @return Task id number (-1 if scheduling failed)
    */
  def setupRepeatingTask(task: IO[Unit], period: Int): Int = Bukkit.setupRepeatingTask(task, period)

  var worlds: Map[UUID, World] = _

  def getWorld(uuid: UUID): World = worlds(uuid)

  /**
    * Method that loads everything the plugin needs.
    * @param logger Logger to use for logging.
    * @param version The version
    * @param worlds List of Worlds the server has loaded.
    * @param dataFolder Folder to store player's energy reservoirs
    * @param saveDefaultConfig IO to generate default config.
    * @param registerEvents IO that registers all event listeners.
    */
  def load[S, A](
    logger: Logger,
    version: String,
    worlds: List[World],
    dataFolder: File,
    saveDefaultConfig: IO[Unit],
    registerEvents: IO[Unit]
  ): Unit = {
    this.logger = logger
    this.fullVersion = version
    this.worlds = worlds.map(w => w.uuid -> w).toMap
    Aethercraft.dataFolder = dataFolder

    val updateEnergies = IO {
      while(Recipe.recipes.count(_.updateResultEnergy()) > 0) {
        //Keep updating energy from recipes while at least one energy value is changed.
      }
    }

    val startBuilder = IO {
      builderTaskId = setupRepeatingTask(Builder.handleTasks, 1)
    }

    val load: IO[Unit] = for {
      _ <- saveDefaultConfig
      _ <- updateEnergies
      _ <- checkMissingMaterialEnergy
      _ <- Persistable.loadEverything
      _ <- registerEvents
      _ <- startBuilder
      _ <- info(s"Successfully loaded $version")
    } yield ()

    Aethercraft.runTaskSync(load)
  }


/*  private def listMaterialEnergies: IO[Unit] = {
    material.Material.values.toList.traverse(info).map(_ => ())
  }*/

  private def checkMissingMaterialEnergy: IO[Unit] = {
    material.Material.values.filterNot(material => material.hasNoEnergy || material.energy.nonEmpty).toList.traverse_ { material =>
      for {
        _ <- info(s"Missing energy for $material")
        _ <- Recipe.recipes.filter(_.result == material).toList.traverse(info)
      } yield ()
    }
  }
}
