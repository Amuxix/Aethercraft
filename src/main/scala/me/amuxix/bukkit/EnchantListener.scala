package me.amuxix.bukkit

import me.amuxix.inventory.Item
import me.amuxix.material.Material.Air
import me.amuxix.runes.traits.enchants.Enchant._
import me.amuxix.{Block, Direction, Player}
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
    event.setCancelled(blockBreakEnchants.foldLeft(false)(_ || _.onBlockBreak(event.getPlayer, event.getBlock.toBlock)))
  }

  @EventHandler(priority = LOWEST)
  def onBlockPlaceEvent(event: BlockPlaceEvent): Unit = {
    //This will run all triggers set by enchants and cancel the event if any of them cancels the event
    val player: Player = event.getPlayer
    val placedBlock: Block = event.getBlock.toBlock
    val air = Item(Air)
    val mainHandItem = player.itemInMainHand.getOrElse(air)
    val offHandItem = player.itemInOffHand.getOrElse(air)
    val itemPlaced = (mainHandItem.material, offHandItem.material) match {
      case (mainHand, offHand) if mainHand == placedBlock.material || offHand == Air => mainHandItem
      case (mainHand, offHand) if offHand == placedBlock.material || mainHand == Air => offHandItem
      case _ => air //TODO: Fix this once 1.13 API is out
    }
    event.setCancelled(blockPlaceEnchants.foldLeft(false)(_ || _.onBlockPlace(player, placedBlock, event.getBlockAgainst.toBlock, itemPlaced)))
  }

  @EventHandler(priority = LOWEST)
  def onPlayerInteract(event: PlayerInteractEvent): Unit = {
    val player: Player = event.getPlayer
    val blockFace: Direction = event.getBlockFace
    val air = Item(Air)
    val itemInHand = if (event.getHand == HAND) {
      player.itemInMainHand.getOrElse(air)
    } else {
      player.itemInOffHand.getOrElse(air)
    }
    //This will run all triggers set by enchants and cancel the event if any of them cancels the event
    event.getAction match {
      case RIGHT_CLICK_BLOCK =>
        event.setCancelled(blockInteractEnchants.foldLeft(false)(_ || _.onBlockInteract(player, itemInHand, event.getClickedBlock.toBlock, blockFace)))
      case LEFT_CLICK_BLOCK =>
        event.setCancelled(blockDamageEnchants.foldLeft(false)(_ || _.onBlockDamage(player, itemInHand, event.getClickedBlock.toBlock, blockFace)))
      case RIGHT_CLICK_AIR =>
        event.setCancelled(airInteractEnchants.foldLeft(false)(_ || _.onAirInteract(player, itemInHand)))
      case LEFT_CLICK_AIR =>
        event.setCancelled(airSwingEnchants.foldLeft(false)(_ || _.onAirSwing(player, itemInHand)))
      case _ => //Do Nothing
    }
  }
}
