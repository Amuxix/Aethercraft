package me.amuxix.logging

import me.amuxix.Runecraft

/**
  * Created by Amuxix on 15/12/2016.
  */
object Logger {
  def info(text: => String): Unit = {
    Runecraft.logger.info(text)
  }
  def trace(text: => String): Unit = {
    Runecraft.logger.finest(text)
  }

  def severe(text: => String): Unit = {
    Runecraft.logger.severe(text)
  }
}