package me.amuxix.bukkit

import me.amuxix.Aetherizeable
import me.amuxix.bukkit.World.BukkitWorldOps
import me.amuxix.position.{BlockPosition, EntityPosition, Position, Vector3}
import org.bukkit.{Location => BukkitLocation}
import me.amuxix.doubleAsIfIntegral

private[bukkit] object Location {
  implicit class BukkitEntityPositionOps(location: BukkitLocation) extends Aetherizeable[Position[Double]] {
    def aetherize: EntityPosition = EntityPosition(location.getWorld.aetherize, Vector3(location.getX, location.getY, location.getZ))
  }

  implicit class BukkitBlockPositionOps(location: BukkitLocation) extends Aetherizeable[Position[Int]] {
    def aetherize: BlockPosition = BlockPosition(location.getWorld.aetherize, Vector3(location.getX.toInt, location.getY.toInt, location.getZ.toInt))
  }

  implicit class BlockPositionOps(position: BlockPosition) extends BukkitForm[BukkitLocation] {
    override def bukkitForm: BukkitLocation = {
      val world: org.bukkit.World = position.world.asInstanceOf[World].bukkitForm
      new BukkitLocation(world, position.x, position.y, position.z)
    }
  }

  implicit class EntityPositionOps(position: EntityPosition) extends BukkitForm[BukkitLocation] {
    override def bukkitForm: BukkitLocation = {
      val world: org.bukkit.World = position.world.asInstanceOf[World].bukkitForm
      new BukkitLocation(world, position.x, position.y, position.z)
    }
  }
}

