package me.amuxix
import java.io.File
import java.util.UUID
import java.util.logging.Logger

import cats.effect.IO
import cats.implicits._
import me.amuxix.bukkit.Bukkit
import me.amuxix.logging.Logger.info
import me.amuxix.material.Recipe

import scala.collection.mutable
import scala.concurrent.ExecutionContext.global
import scala.concurrent.Future

object Aethercraft {
  val defaultFailureMessage = "Some unknown force blocks you."

  var logger: Logger = _

  var fullVersion: String = _
  lazy val simpleVersion: String = fullVersion.split("-").head
  val players = mutable.Map.empty[UUID, Player]
  implicit val ec = global

  def runTaskSync(task: IO[Unit]): Unit = Bukkit.runTaskSync(task)

  def runTimedTask(tasks: List[IO[Unit]], interval: Int): Unit = {
    tasks.foldLeft(0) {
      (delay, task) =>
        Bukkit.runTaskLater(task, delay)
        delay + interval
    }
  }

  def runTaskAsync(task: IO[Unit]): Unit = Future(task)

  var worlds: Map[UUID, World] = _

  def getWorld(uuid: UUID): World = worlds(uuid)

  /**
    * Method that loads everything the plugin needs.
    * @param logger Logger to use for logging.
    * @param version The version
    * @param worlds List of Worlds the server has loaded.
    * @param reservoirsFolder Folder to store player's energy reservoirs
    * @param saveDefaultConfig IO to generate default config.
    * @param registerEvents IO that registers all event listeners.
    */
  def load[S, A](
    logger: Logger,
    version: String,
    worlds: List[World],
    reservoirsFolder: File,
    saveDefaultConfig: IO[Unit],
    registerEvents: IO[Unit]
  ): Unit = {
    this.logger = logger
    this.fullVersion = version
    this.worlds = worlds.map(w => w.uuid -> w).toMap
    Serialization.reservoirsFile = reservoirsFolder

    val updateEnergies = IO {
      while(Recipe.recipes.count(_.updateResultEnergy) > 0) {
        //Keep updating energy from recipes while at least one energy value is changed.
      }
    }

    val load: IO[Unit] = for {
      _ <- saveDefaultConfig
      _ <- updateEnergies
      _ <- checkMissingMaterialEnergy
      _ <- Serialization.loadEverything
      _ <- registerEvents
      _ <- info(s"Successfully loaded $version")
    } yield ()

    Aethercraft.runTaskAsync(load)
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
