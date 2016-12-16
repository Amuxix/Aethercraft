package me.amuxix

import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

/**
  * Created by Amuxix on 21/11/2016.
  */
object Runecraft extends JavaPlugin {
	/**
	  * This register this file as a listener to all of bukkit events.
	  */
	override def onEnable() = {
		Bukkit.getPluginManager.registerEvents(Listener, this)
	}
}
