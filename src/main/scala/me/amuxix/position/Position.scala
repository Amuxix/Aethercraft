package me.amuxix.position

import cats.effect.IO
import me.amuxix.World

import scala.math.{pow, sqrt}

/**
  * Created by Amuxix on 30/12/2016.
  */
abstract class Position[T : Numeric](val world: World, coordinates: Vector3[T]) {
  val x: T
  val y: T
  val z: T

  def +(vector: Vector3[T]): Position[T]
  def -(vector: Vector3[T]): Position[T]

  def distance(t: Position[T]): Option[Double] = {
    import Numeric.Implicits._
    Option.when(world == t.world)(sqrt(pow((x - t.x).toDouble, 2) + pow((y - t.y).toDouble, 2) + pow((z - t.z).toDouble, 2)))
  }

  def strikeLightning: IO[Unit]

  def strikeLightningEffect: IO[Unit]

  override def toString: String = {"[" + world.name + "@" + coordinates.toString + "]"}

/*  def canEqual(other: Any): Boolean = other.isInstanceOf[Position]

  override def equals(other: Any): Boolean = other match {
    case position: Position[Double] =>
      (position canEqual this) &&
        world.uuid == position.world.uuid && coordinates.equals(position.coordinates)
    case _ => false
  }*/

/*  override def hashCode(): Int = {
    val state = Seq(world.uuid, coordinates)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }*/
}
