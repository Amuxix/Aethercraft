package me.amuxix.bukkit.block.blocks

import me.amuxix.block.blocks
import me.amuxix.block.Block.Location
import me.amuxix.bukkit.block.Block
import me.amuxix.bukkit.inventory.Inventory.BukkitInventoryOps
import me.amuxix.inventory.Inventory
import me.amuxix.material.Material.Chest
import org.bukkit.block.{Chest => BukkitChest}

private[bukkit] class Chest protected[block](location: Location) extends Block(location, Chest) with blocks.Chest {
  private val chest = state.asInstanceOf[BukkitChest]

  override protected def inventory: Inventory = chest.getBlockInventory.aetherize
}
