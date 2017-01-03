package me.amuxix.util

import me.amuxix.util.Block.Location
import org.bukkit.Material
import org.bukkit.Material.AIR
import org.bukkit.block.{BlockState, Block => BukkitBlock}

/**
  * Created by Amuxix on 22/11/2016.
  */
object Block {
  implicit def BukkitBlock2Block(bukkitBlock: BukkitBlock): Block = Block(bukkitBlock, bukkitBlock.getType, bukkitBlock.getState)
  type Location = Position[Int]
}

case class Block(location: Location, material: Material, state: BlockState) {
	def getType: Material = material

  def setType(material: Material): Unit = location.world.getBlockAt(location.x, location.y, location.z).setType(material)

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
    if (target.getBlock.getType == AIR) {
      target.getBlock.setType(this.getType)
      setType(AIR)
      true
    } else {
      false
    }
  }

  /**
    * Consumes this block and gives energy to the player
    * @param player Player who receives the energy for consuming this block
    */
  def consume(player: Player) = setType(Material.STONE)
}
