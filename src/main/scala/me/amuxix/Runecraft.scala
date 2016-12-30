package me.amuxix

import java.util.logging.Logger

import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

object Runecraft {
  // Hack to circumvent the fact we do not have access to the instance of the class Runecraft
  var logger: Logger = _
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

	}
}
