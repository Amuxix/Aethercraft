package me.amuxix.logging

import me.amuxix.bukkit.Aethercraft

/**
  * Created by Amuxix on 15/12/2016.
  */
object Logger {
  def info(text: Any): Unit = {
    Aethercraft.logger.info(text.toString)
  }

  def trace(text: Any): Unit = {
    Aethercraft.logger.finest(text.toString)
  }

  def severe(text: Any): Unit = {
    Aethercraft.logger.severe(text.toString)
  }
}