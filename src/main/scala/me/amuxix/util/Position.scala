package me.amuxix.util

import org.bukkit.block.{Block => BBlock}
import org.bukkit.entity.{Entity => BEntity}
import org.bukkit.{World, Location => BLocation}

import scala.math.{pow, sqrt}

/**
  * Created by Amuxix on 30/12/2016.
  */
object Position {
  import scala.math.Numeric.DoubleAsIfIntegral
  implicit val doubleAsIfIntegral = DoubleAsIfIntegral //Allows this method to be implicitly used by bukkitEntity2Position
  implicit def bukkitEntity2Position(bukkitEntity: BEntity): Position[Double] = {
    val location: BLocation = bukkitEntity.getLocation
    Position(bukkitEntity.getWorld, Vector3(location.getX, location.getY ,location.getZ))
  }

  implicit def bukkitBlock2Position(bukkitBlock: BBlock): Position[Int] = {
    val location: BLocation = bukkitBlock.getLocation
    Position(bukkitBlock.getWorld, Vector3(location.getBlockX, location.getBlockY ,location.getBlockZ))
  }
}
case class Position[T : Integral](world: World, coordinates: Vector3[T]) {
  def x: T = coordinates.x
  def y: T = coordinates.y
  def z: T = coordinates.z

  def +(vector: Vector3[T]): Position[T] = Position(world, coordinates + vector)
  def -(vector: Vector3[T]): Position[T] = Position(world, coordinates - vector)

  def block(implicit ev: T =:= Int): Block = {
    world.getBlockAt(coordinates.x, coordinates.y, coordinates.z)
  }

  /**
    * Checks if this position and the position above this have blocks that players can be on, ie: the blocks are lava, or air, or reeds.
    * @return true if a player can fit
    */
  def canFitPlayer(implicit ev: T =:= Int): Boolean = this.block.getType.isSolid == false && (this.asInstanceOf[Position[Int]] + Up).block.getType.isSolid == false

  def distance(t: Position[T]): Double = {
    import Integral.Implicits._
    sqrt(pow((x - t.x).toDouble(), 2) + pow((y - t.y).toDouble(), 2) + pow((z - t.z).toDouble(), 2))
  }

  override def toString: String = {"[" + world.getName + "@" + coordinates.toString + "]"}

  def equals(position: Position[T]): Boolean = world.getUID == position.world.getUID && coordinates.equals(position.coordinates)
}
