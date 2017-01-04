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

  /**
    * @param rotationMatrix Matrix that defines the rotation to be applied
    * @param about Point about which this vector should be rotated
    * @return This vector rotated by the given matrix about the given point
    */
  def rotateAbout(rotationMatrix: Matrix4, about: Vector3[Int])(implicit ev: T =:= Int): Vector3[Int] = {
    Matrix4.IDENTITY.translate(about) * rotationMatrix * Matrix4.IDENTITY.translate(about * -1) * this.asInstanceOf[Vector3[Int]]
  }

  override def toString: String = "(" + x + ", " + y + ", " + z + ")"

  def equals(vector: Vector3[T]): Boolean = x == vector.x && y == vector.y && y == vector.y
}
