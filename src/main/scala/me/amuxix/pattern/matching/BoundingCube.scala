package me.amuxix.pattern.matching

import me.amuxix.pattern.Pattern
import me.amuxix.util.{Block, Location, Vector3}
import org.bukkit.World

import scala.collection.immutable.SortedSet
import scala.math.Numeric.DoubleAsIfIntegral

/**
  * Created by Amuxix on 21/11/2016.
  */
case class BoundingCube(center: Location, possiblePatterns: Set[Pattern]) {
	/*val dimensions: Vector3 = possiblePatterns.foldLeft((0, 0, 0)) { case (acc, pattern) =>
		maxByValue(acc, pattern.boundingCubeDimensions)
	}*/
  val dimension: Int = possiblePatterns.foldLeft(0) { case(acc, patttern) => math.max(acc, patttern.largestDimension) }
  private val vector: Vector3[Double] = Vector3[Double](dimension.toDouble, dimension.toDouble, dimension.toDouble)(DoubleAsIfIntegral)
  val cubeOrigin: Location = center - vector / 2
	val blocks = Array.tabulate[Block](dimension, dimension, dimension){
		case (x, y, z) => center.world.getBlockAt(x, y, z)
	}

  def getBlock(position: Vector3[Int]): Block = {
    blocks(position.x)(position.y)(position.z)
  }

	def maxByValue(a: (Int, Int, Int), b: (Int, Int, Int)): (Int, Int, Int) = {
		val (ay, ax, az) = a
		val (by, bx, bz) = b
		(ay max by, ax max bx, az max bz)
	}
}