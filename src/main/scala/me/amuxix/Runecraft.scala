package me.amuxix

import java.util.logging.Logger

import me.amuxix.material.Tier
import me.amuxix.runes.Rune
import me.amuxix.runes.traits.Persistent
import me.amuxix.runes.waypoints.WaypointTrait
import me.amuxix.serialization.Serialization
import me.amuxix.util.Block.Location
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.{Bukkit, Server}

import scala.collection.immutable.HashMap

object Runecraft {
  var logger: Logger = _
  var server: Server = _
  var self: Runecraft = _

  var persistentRunes = HashMap.empty[Location, Rune with Persistent]

  /**
    *                                                   PERSISTENT RUNES
    * Lists of persistent runes that will be serialized
    */
  /** The map key is the [[me.amuxix.runes.traits.Linkable.signature]] of the waypoint */
  var waypoints = Map.empty[Int, Rune with WaypointTrait]

  lazy val fullVersion: String = Runecraft.self.getDescription.getFullName
  lazy val simpleVersion: String = Runecraft.self.getDescription.getFullName.split("-").head
}

/**
  * Created by Amuxix on 21/11/2016.
  */
class Runecraft extends JavaPlugin {
	/**
	  * This register this file as a listener to all of bukkit events.
	  */
	override def onEnable(): Unit = {
		Bukkit.getPluginManager.registerEvents(Listener, this)
    Runecraft.logger = getLogger
    Runecraft.server = getServer
    Runecraft.self = this
    new Tier()
    Serialization.loadRunes()
	}

  override def onDisable(): Unit = {
    Serialization.saveRunes()
  }
}