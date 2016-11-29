package me.amuxix.util

import org.bukkit.Material
import org.bukkit.block.{Block => BukkitBlock}

object Block {
  implicit def BukkitBlock2Block(bukkitBlock: BukkitBlock): Block = Block(bukkitBlock.getLocation())
}

/**
  * Created by Amuxix on 22/11/2016.
  */
case class Block(location: Location) {
	def getType: Material = ???
}
