package me.amuxix.util

/**
  * Created by Amuxix on 23/01/2017.
  */
case class Rotation(rotationMatrix: Matrix4, rotationCenter: Vector3[Int]) {
  /**
    * Rotates the given point about the rotation center applying the rotation matrix
    * @param point Point to rotate
    * @return The rotated points
    */
  def rotate(point: Vector3[Int]): Vector3[Int] = {
    Matrix4.IDENTITY.translate(rotationCenter) * rotationMatrix * Matrix4.IDENTITY.translate(rotationCenter * -1) * point
  }
}
