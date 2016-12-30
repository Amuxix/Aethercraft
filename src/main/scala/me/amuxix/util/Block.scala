package me.amuxix.util

import me.amuxix.util.Block.Location
import org.bukkit.Material
import org.bukkit.block.{Block => BukkitBlock}

/**
  * Created by Amuxix on 22/11/2016.
  */
object Block {
  implicit def BukkitBlock2Block(bukkitBlock: BukkitBlock): Block = Block(bukkitBlock, bukkitBlock.getType)
  type Location = Position[Int]
}

case class Block(location: Location, material: Material) {
	def getType: Material = material
}
