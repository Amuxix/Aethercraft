package me.amuxix.bukkit

import java.util.UUID
import java.util.logging.Logger

import cats.effect.IO
import me.amuxix._
import me.amuxix.bukkit.listeners._
import me.amuxix.material._
import me.amuxix.bukkit.World.BukkitWorldOps
import me.amuxix.logging.Logger.info
import org.bukkit.event.Event
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.{Bukkit, Server}

object Aethercraft {
  var logger: Logger = _
  var server: Server = _
  var self: Aethercraft = _

  lazy val fullVersion: String = Aethercraft.self.getDescription.getFullName
  lazy val simpleVersion: String = Aethercraft.self.getDescription.getFullName.split("-").head

  val defaultFailureMessage = "Some unknown force blocks you."

  def callEvent(event: Event): Unit = server.getPluginManager.callEvent(event)
  def getWorld(uuid: UUID): World = server.getWorld(uuid).aetherize

  def runTask(task: => Unit): Unit = {
    Aethercraft.server.getScheduler.runTask(self, (() => task): Runnable)
  }
}

/**
  * Created by Amuxix on 21/11/2016.
  */
class Aethercraft extends JavaPlugin {
	/**
	  * This register this file as a listener to all of bukkit events.
	  */
	override def onEnable(): Unit = {
		Bukkit.getPluginManager.registerEvents(Listener, this)
		Bukkit.getPluginManager.registerEvents(EnchantListener, this)
		Bukkit.getPluginManager.registerEvents(IntegrityListener, this)
    Aethercraft.logger = getLogger
    Aethercraft.server = getServer
    Aethercraft.self = this
    saveDefaultConfig()
    while(Recipe.recipes.count(_.updateResultEnergy) > 0) {
      //Keep updating energy from recipes while at least one energy value is changed.
    }
    val load = for {
      _ <- checkMissingMaterialEnergy
      _ <- Serialization.loadEverything
    } yield ()

    load.unsafeRunSync()
	}

  private def checkMissingMaterialEnergy = {
    material.Material.values.filterNot(material => material.hasNoEnergy || material.energy.nonEmpty).foldLeft(IO.unit) { (acc, material) =>
      acc.flatMap { _ =>
        info(s"Missing energy for $material").flatMap { _ =>
          Recipe.recipes.filter(_.result == material).foldLeft(IO.unit) { (acc, recipe) => acc.flatMap(_ => info(recipe))
          }
        }
      }
    }
  }

  override def onDisable(): Unit = {
    Serialization.saveEverything.unsafeRunSync()
  }
}