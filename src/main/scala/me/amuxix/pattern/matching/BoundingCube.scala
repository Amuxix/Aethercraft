package me.amuxix.pattern.matching

import me.amuxix.logging.Logger.trace
import me.amuxix.pattern.Pattern
import me.amuxix.util.Block.Location
import me.amuxix.util.{Block, Vector3}


/**
  * Created by Amuxix on 21/11/2016.
  */
case class BoundingCube(centerLocation: Location, possiblePatterns: Set[Pattern]) {
	/*val dimensions: Vector3 = possiblePatterns.foldLeft((0, 0, 0)) { case (acc, pattern) =>
		maxByValue(acc, pattern.boundingCubeDimensions)
	}*/
  val dimension: Int = possiblePatterns.foldLeft(0) { case(acc, patttern) => math.max(acc, patttern.largestDimension) }
  private val vector: Vector3[Int] = Vector3(dimension, dimension, dimension)
  val center: Vector3[Int] = vector / 2
  val cubeOrigin: Location = centerLocation - center
  trace("Center: " + centerLocation)
  trace("Cube Origin: " + cubeOrigin)
	val blocks = Array.tabulate[Block](dimension, dimension, dimension){
		case (x, y, z) => (cubeOrigin + Vector3(x, y, z)).block
	}

  def getBlock(position: Vector3[Int]): Block = {
    trace("Getting block at: (" + position.x + ", " + position.y + ", " + position.z + ")")
    blocks(position.x)(position.y)(position.z)
  }

	def maxByValue(a: (Int, Int, Int), b: (Int, Int, Int)): (Int, Int, Int) = {
		val (ay, ax, az) = a
		val (by, bx, bz) = b
		(ay max by, ax max bx, az max bz)
	}
}