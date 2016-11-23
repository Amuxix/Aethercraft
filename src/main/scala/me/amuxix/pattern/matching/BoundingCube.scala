package me.amuxix.pattern.matching

import me.amuxix.pattern.Pattern
import org.bukkit.World
import me.amuxix.util.{Axis, Block, Location, Vector3, Y}

import scala.collection.immutable.SortedSet
import scala.runtime.Tuple2Zipped

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
		for {
			layer <- 0 to blocks.length
			line <- 0 to blocks(0).length
			block <- 0 to blocks(0)(0).length
		} {
			axis match {
				case Y =>

			}
		}
	}

	def rotateCCW(axis: Axis) = {

	}

}