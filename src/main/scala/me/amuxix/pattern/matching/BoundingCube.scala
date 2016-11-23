package me.amuxix.pattern.matching

import me.amuxix.pattern.Pattern
import me.amuxix.util.{Axis, Location, Vector3}
import org.bukkit.World

import scala.collection.immutable.SortedSet

/**
  * Created by Amuxix on 21/11/2016.
  */
class BoundingCube(world: World, possiblePatterns: SortedSet[Pattern], center: Location) {
	private val dimensions: Vector3 = possiblePatterns.foldLeft((0, 0, 0)) { case (acc, pattern) =>
		maxByValue(acc, pattern.boundingCubeDimensions)
	}
	val cubeOrigin = center - dimensions / 2
	val blocks = Array.tabulate(dimensions.x, dimensions.y, dimensions.z){
		case (x, y, z) => world.getBlockAt(x, y, z)
	}

	def maxByValue(a: (Int, Int, Int), b: (Int, Int, Int)): (Int, Int, Int) = {
		val (ay, ax, az) = a
		val (by, bx, bz) = b
		(ay max by, ax max bx, az max bz)
	}

	def rotateCW(axis: Axis) = {
    
	}

	def rotateCCW(axis: Axis) = {

	}

}