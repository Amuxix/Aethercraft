package me.amuxix.util

import org.bukkit.World
import scala.math.Numeric.DoubleAsIfIntegral

/**
  * Created by Amuxix on 21/11/2016.
  */
object Location {
  implicit def bukkitLocation2Location(bukkitLocation: org.bukkit.Location): Location = {
    Location(bukkitLocation.getWorld, Vector3(bukkitLocation.getX, bukkitLocation.getY, bukkitLocation.getZ)(DoubleAsIfIntegral))
  }
}

case class Location(world: World, coordinates: Vector3[Double]) {

	def +(vector: Vector3[Double]): Location = {
		Location(world, coordinates + vector)
	}

	def -(vector: Vector3[Double]): Location = {
		Location(world, coordinates - vector)
	}
}
