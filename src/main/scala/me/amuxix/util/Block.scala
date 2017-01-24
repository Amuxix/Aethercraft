package me.amuxix.util

import me.amuxix.Runecraft
import me.amuxix.material.Material.{Air, Stone}
import me.amuxix.material.{Crushable, Material}
import me.amuxix.util.Block.Location
import me.amuxix.util.events.{RunecraftBreakEvent, RunecraftPlaceEvent}
import org.bukkit.block.{BlockState, Block => BukkitBlock}

/**
  * Created by Amuxix on 22/11/2016.
  */
object Block {
  implicit def BukkitBlock2Block(bukkitBlock: BukkitBlock): Block = Block(bukkitBlock, bukkitBlock.getState.getData, bukkitBlock.getState)
  type Location = Position[Int]
}

case class Block(location: Location, material: Material, state: BlockState) {
  def setMaterial(material: Material): Unit = {
    state.setType(material.getItemType)
    state.setData(material)
    state.update(true)
  }

  /**
    * Attempts to move this block by the displacement vector.
    * @param displacementVector Vector that defines the move.
    * @return true if the move was successful, false otherwise.
    */
  def move(displacementVector: Vector3[Int], player: Player): Boolean = {
    val target: Location = location + displacementVector
    moveTo(target, player)
  }

  /**
    * Attempts to move this block to the target location.
    * @param target Location where the block should be moved to.
    * @return true if the move was successful, false otherwise.
    */
  def moveTo(target: Location, player: Player): Boolean = {
    if (canMoveTo(target, player)) {
      target.block.setMaterial(this.material)
      setMaterial(Air)
      true
    } else {
      false
    }
  }

  /**
    * Checks if the player can move this block to the target location, it check if the block can be destroyed at
    * the original location and placed at the target.
    * @param target Target of the move
    * @param player Player who triggered the move
    * @return true if the player can move this block, false otherwise
    */
  def canMoveTo(target: Location, player: Player): Boolean = {
    if (target.block.material.isInstanceOf[Crushable]) {
      val placeEvent = RunecraftPlaceEvent(target, material, player)
      val breakEvent = RunecraftBreakEvent(location, player)
      Runecraft.server.getPluginManager.callEvent(placeEvent)
      Runecraft.server.getPluginManager.callEvent(breakEvent)
      if (breakEvent.isCancelled == false && placeEvent.isCancelled == false && placeEvent.canBuild) {
        return true
      }
    }
    false
  }

  /**
    * Consumes this block and gives energy to the player
    *
    * @param player Player who receives the energy for consuming this block
    */
  def consume(player: Player): Unit = setMaterial(Stone)

  def equals(block: Block): Boolean = location.equals(block.location)
}
