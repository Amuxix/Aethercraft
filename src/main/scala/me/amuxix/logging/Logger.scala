package me.amuxix.logging

import me.amuxix.Runecraft

/**
  * Created by Amuxix on 15/12/2016.
  */
object Logger {
  def log(text: String): Unit = {
    Runecraft.getLogger.info(text)
  }
}