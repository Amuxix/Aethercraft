package me.amuxix

import java.util.UUID

import io.circe.generic.auto._
import io.circe.{Decoder, Encoder}
import me.amuxix.material.Solid
import org.bukkit.{World, Location => BLocation}

import scala.math.{pow, sqrt}

/**
  * Created by Amuxix on 30/12/2016.
  */
object Position {
  import scala.math.Numeric.DoubleAsIfIntegral
  implicit val doubleAsIfIntegral = DoubleAsIfIntegral //Allows this method to be implicitly used by bukkitEntity2Position

  implicit def bukkitLocation2Position(location: BLocation): Position[Double] =
    Position(location.getWorld, Vector3(location.getX, location.getY, location.getZ))

  implicit def doublePosition2IntPosition(position: Position[Double]): Position[Int] = position.toIntPosition

  implicit val encodePositionInt: Encoder[Position[Int]] = Encoder.forProduct2("world", "coordinates")(r =>
    (r.world.getUID, r.coordinates)
  )

  implicit val decodePositionInt: Decoder[Position[Int]] = Decoder.forProduct2("world", "coordinates")((worldID: UUID, coordinates: Vector3[Int]) => {
    val world = Runecraft.server.getWorld(worldID)
    Position[Int](world, coordinates)
  })
}
case class Position[T : Integral](world: World, coordinates: Vector3[T]) {
  def x: T = coordinates.x
  def y: T = coordinates.y
  def z: T = coordinates.z

  def toIntPosition(implicit ev: T =:= Double): Position[Int] = Position[Int](world, coordinates.toIntVector)

  def +(vector: Vector3[T]): Position[T] = Position(world, coordinates + vector)
  def -(vector: Vector3[T]): Position[T] = Position(world, coordinates - vector)

  def block(implicit ev: T =:= Int): Block = {
    world.getBlockAt(coordinates.x, coordinates.y, coordinates.z)
  }

  /**
    * Checks if this position and the position above this have blocks that players can be on, ie: the blocks are lava, or air, or reeds.
    * @return true if a player can fit
    */
  def canFitPlayer(implicit ev: T =:= Int): Boolean = this.block.material.isInstanceOf[Solid] == false && (this.asInstanceOf[Position[Int]] + Up).block.material.isInstanceOf[Solid] == false

  def distance(t: Position[T]): Double = {
    import Integral.Implicits._
    sqrt(pow((x - t.x).toDouble(), 2) + pow((y - t.y).toDouble(), 2) + pow((z - t.z).toDouble(), 2))
  }

  override def toString: String = {"[" + world.getName + "@" + coordinates.toString + "]"}

  def canEqual(other: Any): Boolean = other.isInstanceOf[Position[T]]

  override def equals(other: Any): Boolean = other match {
    case position: Position[T] =>
      (position canEqual this) &&
        world.getUID == position.world.getUID && coordinates.equals(position.coordinates)
    case _ => false
  }

  override def hashCode(): Int = {
    val state = Seq(world.getUID, coordinates)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }
}
