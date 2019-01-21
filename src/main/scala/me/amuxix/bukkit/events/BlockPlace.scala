package me.amuxix.bukkit.events

import me.amuxix.Player
import me.amuxix.block.Block
import me.amuxix.bukkit.block.{Block => BukkitBlock}
import me.amuxix.bukkit.inventory.Item
import me.amuxix.bukkit.{Player => BPlayer}
import org.bukkit.event.block.{BlockPlaceEvent => BukkitBlockPlaceEvent}
import org.bukkit.inventory.EquipmentSlot.HAND
import org.bukkit.inventory.ItemStack

class BlockPlace(block: Block, replacedBlock: Block, player: Player) extends
  BukkitBlockPlaceEvent(
    block.asInstanceOf[BukkitBlock].bukkitForm.getBlock,
    replacedBlock.asInstanceOf[BukkitBlock].bukkitForm,
    replacedBlock.asInstanceOf[BukkitBlock].bukkitForm.getBlock,
    player.itemInMainHand.fold[ItemStack](null)(_.asInstanceOf[Item].bukkitForm),
    player.asInstanceOf[BPlayer].bukkitForm,
    true,
    HAND
  )
