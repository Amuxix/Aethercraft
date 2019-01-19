package me.amuxix.logging

import me.amuxix.Aethercraft

/**
  * Created by Amuxix on 15/12/2016.
  */
object Logger {
  def info(text: => String): Unit = {
    Aethercraft.logger.info(text)
  }
  def info(text: Any): Unit = {
    Aethercraft.logger.info(text.toString)
  }
  def trace(text: => String): Unit = {
    Aethercraft.logger.finest(text)
  }

  def severe(text: => String): Unit = {
    Aethercraft.logger.severe(text)
  }
}