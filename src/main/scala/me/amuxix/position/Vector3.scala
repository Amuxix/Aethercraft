package me.amuxix.position

import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}
import Integral.Implicits._

object Vector3 {
	/*implicit def Tuple32Vector3[T: Integral](tuple: (T, T, T)): Vector3[T] = {
		Vector3(tuple._1, tuple._2, tuple._3)
	}*/
	implicit val encoder: Encoder[Vector3[Int]] = deriveEncoder
	implicit val decoder: Decoder[Vector3[Int]] = deriveDecoder
}

/**
  * Created by Amuxix on 22/11/2016.
  */
case class Vector3[T : Integral](x: T, y: T, z: T) {
	def +(vector: Vector3[T]): Vector3[T] = Vector3(x + vector.x, y + vector.y, z + vector.z)
	def -(vector: Vector3[T]): Vector3[T] = Vector3(x - vector.x, y - vector.y, z - vector.z)
	def *(constant: T): Vector3[T] = Vector3(x * constant, y * constant, z * constant)
	def /(constant: T): Vector3[T] = Vector3(x / constant, y / constant, z / constant)

  override def toString: String = s"($x, $y, $z)"
}
