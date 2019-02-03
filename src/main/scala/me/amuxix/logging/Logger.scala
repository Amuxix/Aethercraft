package me.amuxix.logging

import cats.effect.IO
import me.amuxix.Aethercraft

/**
  * Created by Amuxix on 15/12/2016.
  */
object Logger {
  def info(text: Any): IO[Unit] = {
    IO(Aethercraft.logger.info(text.toString))
  }

  def trace(text: Any): Unit = {
    IO(Aethercraft.logger.finest(text.toString))
  }

  def severe(text: Any): Unit = {
    IO(Aethercraft.logger.severe(text.toString))
  }
}