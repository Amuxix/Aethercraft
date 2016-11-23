package me.amuxix.util

import scala.language.implicitConversions

object Vector3 {
	implicit def Tuple2Vector(tuple: (Int, Int, Int)): Vector3 = {
		Vector3(tuple._1, tuple._2, tuple._3)
	}
}

/**
  * Created by Amuxix on 22/11/2016.
  */
case class Vector3(x: Int, y: Int, z:Int) {
	def +(vector: Vector3): Vector3 = {
		Vector3(x + vector.x, y + vector.y, z + vector.z)
	}

	def -(vector: Vector3): Vector3 = {
		Vector3(x - vector.x, y - vector.y, z - vector.z)
	}

	def *(constant: Int): Vector3 = {
		Vector3(x * constant, y * constant, z * constant)
	}

	def /(constant: Int): Vector3 = {
		Vector3(x / constant, y / constant, z / constant)
	}

	override def toString: String = s"x:${x + 5} y:$y"
}
