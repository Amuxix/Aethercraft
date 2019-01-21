package me.amuxix.bukkit.listeners

import me.amuxix.Direction
import me.amuxix.bukkit.Player
import me.amuxix.bukkit.Player.BukkitPlayerOps
import me.amuxix.bukkit.block.Block
import me.amuxix.bukkit.block.Block.BukkitBlockOps
import me.amuxix.runes.traits.enchants.Enchant._
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority.LOWEST
import org.bukkit.event.block.Action._
import org.bukkit.event.block.{BlockBreakEvent, BlockPlaceEvent}
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot.HAND

object EnchantListener extends org.bukkit.event.Listener {

  @EventHandler(priority = LOWEST)
  def onBlockBreakEvent(event: BlockBreakEvent): Unit = {
    //This will run all triggers set by enchants and cancel the event if any of them cancels the event
    event.setCancelled(blockBreakEnchants.foldLeft(false)(_ || _.onBlockBreak(event.getPlayer.aetherize, event.getBlock.aetherize)))
  }

  @EventHandler(priority = LOWEST)
  def onBlockPlaceEvent(event: BlockPlaceEvent): Unit = {
    //This will run all triggers set by enchants and cancel the event if any of them cancels the event
    val player: Player = event.getPlayer.aetherize
    val placedBlock: Block = event.getBlock.aetherize
    val itemPlaced = if (event.getHand == HAND) {
      player.itemInMainHand
    } else {
      player.itemInOffHand
    }
    event.setCancelled(blockPlaceEnchants.foldLeft(false)(_ || _.onBlockPlace(player, placedBlock, event.getBlockAgainst.aetherize, itemPlaced)))
  }

  @EventHandler(priority = LOWEST)
  def onPlayerInteract(event: PlayerInteractEvent): Unit = {
    val player: Player = event.getPlayer.aetherize
    val blockFace: Direction = event.getBlockFace
    val itemInHand = if (event.getHand == HAND) {
      player.itemInMainHand
    } else {
      player.itemInOffHand
    }
    //This will run all triggers set by enchants and cancel the event if any of them cancels the event
    event.getAction match {
      case RIGHT_CLICK_BLOCK =>
        event.setCancelled(blockInteractEnchants.foldLeft(false)(_ || _.onBlockInteract(player, itemInHand, event.getClickedBlock.aetherize, blockFace)))
      case LEFT_CLICK_BLOCK =>
        event.setCancelled(blockDamageEnchants.foldLeft(false)(_ || _.onBlockDamage(player, itemInHand, event.getClickedBlock.aetherize, blockFace)))
      case RIGHT_CLICK_AIR =>
        event.setCancelled(airInteractEnchants.foldLeft(false)(_ || _.onAirInteract(player, itemInHand)))
      case LEFT_CLICK_AIR =>
        event.setCancelled(airSwingEnchants.foldLeft(false)(_ || _.onAirSwing(player, itemInHand)))
      case _ => //Do Nothing
    }
  }
}
