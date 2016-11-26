package me.amuxix.util

import org.bukkit.World

/**
  * Created by Amuxix on 21/11/2016.
  */
object Location {
  implicit def bukkitLocation2Location(bukkitLocation: org.bukkit.Location): Location = {
    Location(bukkitLocation.getWorld, (bukkitLocation.getX, bukkitLocation.getY, bukkitLocation.getZ))
  }
}

case class Location(world: World, coordinates: Vector3) {

	def +(vector: Vector3): Location = {
		Location(world, coordinates + vector)
	}

	def -(vector: Vector3): Location = {
		Location(world, coordinates - vector)
	}
}
