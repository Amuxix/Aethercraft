package me.amuxix

import io.circe.{Decoder, Encoder}
import me.amuxix.block.Block
import me.amuxix.block.Block.Location

import scala.math.{pow, sqrt}

/**
  * Created by Amuxix on 30/12/2016.
  */
object Position {
  implicit val encodePositionInt: Encoder[Position[Int]] = Encoder.forProduct2("world", "coordinates")(position =>
    (position.world, position.coordinates)
  )

  implicit val decodePositionInt: Decoder[Position[Int]] = Decoder.forProduct2[Position[Int], World, Vector3[Int]]("world", "coordinates")(Position[Int])
}

case class Position[T : Integral](world: World, coordinates: Vector3[T]) {
  val x: T = coordinates.x
  val y: T = coordinates.y
  val z: T = coordinates.z

  def toIntPosition(implicit ev: T =:= Double): Position[Int] = Position[Int](world, coordinates.toIntVector)
  def toDoublePosition(implicit ev: T =:= Int): Position[Double] = Position[Double](world, coordinates.toDoubleVector)

  def +(vector: Vector3[T]): Position[T] = Position(world, coordinates + vector)
  def -(vector: Vector3[T]): Position[T] = Position(world, coordinates - vector)

  def block(implicit ev: T =:= Int): Block = {
    ev(_) // there I used it
    world.blockAt(this.asInstanceOf[Location])
  }

  /**
    * Checks if this position and the position above this have blocks that players can be on, ie: the blocks are lava, or air, or reeds.
    * @return true if a player can fit
    */
  def canFitPlayer(implicit ev: T =:= Int): Boolean = block.material.isSolid == false && (this.asInstanceOf[Position[Int]] + Up).block.material.isSolid == false

  def distance(t: Position[T]): Double = {
    import Integral.Implicits._
    sqrt(pow((x - t.x).toDouble(), 2) + pow((y - t.y).toDouble(), 2) + pow((z - t.z).toDouble(), 2))
  }

  override def toString: String = {"[" + world.name + "@" + coordinates.toString + "]"}

  def canEqual(other: Any): Boolean = other.isInstanceOf[Position[T]]

  override def equals(other: Any): Boolean = other match {
    case position: Position[T] =>
      (position canEqual this) &&
        world.uuid == position.world.uuid && coordinates.equals(position.coordinates)
    case _ => false
  }

  override def hashCode(): Int = {
    val state = Seq(world.uuid, coordinates)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }
}
