package me.amuxix.bukkit

import me.amuxix._
import me.amuxix.bukkit.World.BukkitWorldOps
import org.bukkit.{Location => BukkitLocation}

private[bukkit] object Location {
  import scala.math.Numeric.DoubleAsIfIntegral
  private implicit val doubleAsIfIntegral = DoubleAsIfIntegral

  implicit class BukkitDoublePositionOps(location: BukkitLocation) extends Aetherizeable[Position[Double]] {
    def aetherize: Position[Double] = Position[Double](location.getWorld.aetherize, Vector3[Double](location.getX, location.getY, location.getZ))
  }

  implicit class BukkitIntPositionOps(location: BukkitLocation) extends Aetherizeable[Position[Int]] {
    def aetherize: Position[Int] = Position[Int](location.getWorld.aetherize, Vector3[Int](location.getX.toInt, location.getY.toInt, location.getZ.toInt))
  }

  implicit class PositionIntOps(position: Position[Int]) extends BukkitForm[BukkitLocation] {
    override def bukkitForm: BukkitLocation = {
      val world: org.bukkit.World = position.world.asInstanceOf[World].bukkitForm
      new BukkitLocation(world, position.x, position.y, position.z)
    }
  }

  implicit class PositionDoubleOps(position: Position[Double]) extends BukkitForm[BukkitLocation] {
    override def bukkitForm: BukkitLocation = {
      val world: org.bukkit.World = position.world.asInstanceOf[World].bukkitForm
      new BukkitLocation(world, position.x, position.y, position.z)
    }
  }
}

