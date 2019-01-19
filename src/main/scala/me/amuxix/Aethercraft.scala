package me.amuxix

import java.util.logging.Logger

import me.amuxix.Block.Location
import me.amuxix.bukkit._
import me.amuxix.material._
import me.amuxix.runes.Rune
import me.amuxix.runes.traits.Persistent
import me.amuxix.runes.waypoints.WaypointTrait
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.{Bukkit, Server}

import scala.collection.immutable.HashMap

object Aethercraft {
  var logger: Logger = _
  var server: Server = _
  var self: Aethercraft = _

  var persistentRunes = HashMap.empty[Location, Rune with Persistent]

  /**
    *                                                   PERSISTENT RUNES
    * Lists of persistent runes that will be serialized
    */
  /** The map key is the [[me.amuxix.runes.traits.Linkable.signature]] of the waypoint */
  var waypoints = Map.empty[Int, Rune with WaypointTrait]

  lazy val fullVersion: String = Aethercraft.self.getDescription.getFullName
  lazy val simpleVersion: String = Aethercraft.self.getDescription.getFullName.split("-").head
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
    Material.values
      .filterNot(material => material.isInstanceOf[NoEnergy] || material.energy.nonEmpty)
      .foreach { material =>
        logging.Logger.info(s"Missing energy for $material")
        Recipe.recipes.filter(_.result == material).foreach(logging.Logger.info)
      }
    Serialization.loadRunes()
	}

  override def onDisable(): Unit = {
    Serialization.saveRunes()
  }
}