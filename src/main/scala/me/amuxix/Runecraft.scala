package me.amuxix

import java.util.logging.Logger

import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.{Bukkit, Server}

object Runecraft{
  var logger: Logger = _
  var server: Server = _
  var self: Runecraft = _
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
