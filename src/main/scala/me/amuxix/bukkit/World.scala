package me.amuxix.bukkit

import java.io.File
import java.util.UUID

import me.amuxix
import me.amuxix.Aetherizeable
import me.amuxix.block.Block.Location
import me.amuxix.bukkit.block.Block.BukkitBlockOps
import org.bukkit.{World => BukkitWorld}

import scala.collection.mutable

object World {
  private val worlds = mutable.Map.empty[UUID, World]

  implicit class BukkitWorldOps(world: BukkitWorld) extends Aetherizeable[World] {
    def aetherize: World = worlds.getOrElseUpdate(world.getUID, new World(world))
  }
}

private[bukkit] class World(val world: BukkitWorld) extends amuxix.World with BukkitForm[BukkitWorld] {
  override def uuid: UUID = world.getUID

  override def blockAt(location: Location): block.Block = world.getBlockAt(location.x, location.y, location.z).aetherize

  override def name: String = world.getName

  override def bukkitForm: BukkitWorld = world

  override def worldFolder: File = world.getWorldFolder

  override def equals(other: Any): Boolean = other match {
    case that: World => this.uuid == that.uuid
    case _ => false
  }

  override def hashCode(): Int = uuid.hashCode()
}