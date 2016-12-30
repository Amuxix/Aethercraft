package me.amuxix.util

object Vector3 {
  import scala.math.Numeric.DoubleAsIfIntegral
  implicit val doubleAsIfIntegral = DoubleAsIfIntegral //Allows this method to be implicitly used by Tuple32Vector
	implicit def Tuple32Vector[T: Integral](tuple: (T, T, T)): Vector3[T] = {
		Vector3(tuple._1, tuple._2, tuple._3)
	}
}

/**
  * Created by Amuxix on 22/11/2016.
  */
case class Vector3[T : Integral](x: T, y: T, z: T) {
  import Integral.Implicits._
	def +(vector: Vector3[T]): Vector3[T] = Vector3(x + vector.x, y + vector.y, z + vector.z)
	def -(vector: Vector3[T]): Vector3[T] = Vector3(x - vector.x, y - vector.y, z - vector.z)
	def *(constant: T): Vector3[T] = Vector3(x * constant, y * constant, z * constant)
	def /(constant: T): Vector3[T] = Vector3(x / constant, y / constant, z / constant)

  override def toString: String = "(" + x + ", " + y + ", " + z + ")"
}
