package me.amuxix

import java.util.logging.Logger

import me.amuxix.runes.Rune
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.{Bukkit, Server}

object Runecraft{
  var logger: Logger = _
  var server: Server = _
  var self: Runecraft = _

  // These waypoints fit stuff with a maximum size of:
  // small - objects
  // medium - entities
  // large - blocks(faiths and stuff)
  // The map key is the signature of the waypoint (currently an hashcode)
  var smallWaypoints = Map.empty[Int, Rune]
  var mediumWaypoints = Map.empty[Int, Rune]
  var largeWaypoints = Map.empty[Int, Rune]
}

/**
  * Created by Amuxix on 21/11/2016.
  */
class Runecraft extends JavaPlugin {
	/**
	  * This register this file as a listener to all of bukkit events.
	  */
	override def onEnable() = {
		Bukkit.getPluginManager.registerEvents(Listener, this)
    Runecraft.logger = getLogger
    Runecraft.server = getServer
    Runecraft.self = this
	}
}
