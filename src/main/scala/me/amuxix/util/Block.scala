package me.amuxix.util

import me.amuxix.util.Block.Location
import org.bukkit.Material
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

  def setType(material: Material) = location.world.getBlockAt(location.x, location.y, location.z).setType(material)

  /**
    * Consumes this block and gives energy to the player
    * @param player Player who receives the energy for consuming this block
    */
  def consume(player: Player) = setType(Material.STONE)
}
