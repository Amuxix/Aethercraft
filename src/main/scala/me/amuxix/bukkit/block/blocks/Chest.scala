package me.amuxix.bukkit.block.blocks

import me.amuxix.block.blocks
import me.amuxix.bukkit.block.Block
import me.amuxix.bukkit.inventory.Inventory.BukkitInventoryOps
import me.amuxix.inventory.Inventory
import me.amuxix.material.Material.Chest
import me.amuxix.position.BlockPosition
import org.bukkit.block.{Chest => BukkitChest}

private[bukkit] class Chest protected[block](location: BlockPosition) extends Block(location, Chest) with blocks.Chest {
  private val chest = state.asInstanceOf[BukkitChest]

  override def inventory: Inventory = chest.getInventory.aetherize
}
