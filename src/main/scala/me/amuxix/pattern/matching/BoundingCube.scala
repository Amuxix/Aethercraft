package me.amuxix.pattern.matching

import me.amuxix.logging.Logger.trace
import me.amuxix.pattern.Pattern
import me.amuxix.util.Block.Location
import me.amuxix.util.{Block, Vector3}

/**
  * Created by Amuxix on 21/11/2016.
  */
case class BoundingCube(centerLocation: Location, possiblePatterns: List[Pattern]) {
  val dimension: Int = possiblePatterns.foldLeft(0) { case(acc, pattern) => acc max pattern.largestDimension}
  val center: Vector3[Int] = Vector3(dimension, dimension, dimension) / 2
  val cubeOrigin: Location = centerLocation - center
  trace("Center: " + centerLocation)
  trace("Cube Origin: " + cubeOrigin)
  trace("Cube size: " + dimension)
	val blocks: Array[Array[Array[Block]]] = Array.tabulate[Block](dimension, dimension, dimension){
		case (x, y, z) => (cubeOrigin + Vector3(x, y, z)).block
	}
  trace(s"Cube dimensions: (${blocks.length}, ${blocks.head.length}, ${blocks.head.head.length})")

  def blockAt(position: Vector3[Int]): Block = {
    trace(s"Bounding cube getting block at: (${position.x}, ${position.y}, ${position.z})")
    blocks(position.x)(position.y)(position.z)
  }
}