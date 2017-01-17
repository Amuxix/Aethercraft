package me.amuxix

import java.util.logging.Logger

import me.amuxix.material.Tier
import me.amuxix.runes.Rune
import me.amuxix.runes.teleports.WaypointTrait
import me.amuxix.runes.traits.Persistent
import me.amuxix.util.Block.Location
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.{Bukkit, Server}

object Runecraft{
  var logger: Logger = _
  var server: Server = _
  var self: Runecraft = _

  /** The map key is the [[me.amuxix.runes.traits.Linkable.signature]] of the waypoint */
  var waypoints = Map.empty[Int, Rune with WaypointTrait]

  var persistentRunes = Map.empty[Location, Persistent]
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
	}
}