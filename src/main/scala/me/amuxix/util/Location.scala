package me.amuxix.util

import org.bukkit.World

/**
  * Created by Amuxix on 21/11/2016.
  */
case class Location(world: World, coordinates: Vector3) {

	def +(vector: Vector3): Location = {
		Location(world, coordinates + vector)
	}

	def -(vector: Vector3): Location = {
		Location(world, coordinates - vector)
	}
}
