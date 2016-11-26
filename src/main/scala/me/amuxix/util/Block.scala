package me.amuxix.util

import org.bukkit.Material

object Block {
  implicit def BukkitBlock2Block(bukkitBlock: org.bukkit.block.Block): Unit = {
    Block(bukkitBlock.getLocation())
  }
}

/**
  * Created by Amuxix on 22/11/2016.
  */
case class Block(location: Location) {
	def getType: Material = ???
}
