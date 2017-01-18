package me.amuxix.util

import me.amuxix.material.Material
import me.amuxix.material.Material.{Air, Stone}
import me.amuxix.util.Block.Location
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
    state.setData(material)
    state.update(true)
  }


  /**
    * Attempts to move this block by the displacement vector.
    * @param displacementVector Vector that defines the move.
    * @return true if the move was successful, false otherwise.
    */
  def move(displacementVector: Vector3[Int]): Boolean = {
    val target: Location = location + displacementVector
    moveTo(target)
  }

  /**
    * Attempts to move this block to the target location.
    * @param target Location where the block should be moved to.
    * @return true if the move was successful, false otherwise.
    */
  def moveTo(target: Location): Boolean = {
    if (target.block.material == Air) {
      target.block.setMaterial(this.material)
      setMaterial(Air)
      true
    } else {
      false
    }
  }

  /**
    * Consumes this block and gives energy to the player
    * @param player Player who receives the energy for consuming this block
    */
  def consume(player: Player): Unit = setMaterial(Stone)

  def equals(block: Block): Boolean = location.equals(block.location)
}
