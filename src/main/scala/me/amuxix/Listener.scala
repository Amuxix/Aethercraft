package me.amuxix

import java.util.UUID

import me.amuxix.Block.Location
import me.amuxix.IntegrityMonitor.{checkIntegrityAfterBlockDestruction, checkIntegrityAfterBlockPlacement}
import me.amuxix.Player.bukkitPlayer2Player
import me.amuxix.Position.bukkitLocation2Position
import me.amuxix.exceptions.InitializationException
import me.amuxix.material.{Material, Solid}
import me.amuxix.pattern.matching.Matcher
import org.bukkit.ChatColor
import org.bukkit.event.EventHandler
import org.bukkit.event.block._
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot.{HAND, OFF_HAND}

object Listener extends org.bukkit.event.Listener {
  /**
    * This maps each player to last location they activated a rune at.
    * This is used to cancel off hand interact events.
    */
  var lastActivatedRune: Map[UUID, Location] = Map.empty

  @EventHandler
  def onPlayerInteract(event: PlayerInteractEvent): Unit = {
    //This gets fired Twice in a row, once for main hand and one for the off hand
    if (event.getAction == Action.RIGHT_CLICK_BLOCK) {
      val clickedBlockLocation: Location = bukkitLocation2Position(event.getClickedBlock.getLocation)
      val player: Player = event.getPlayer
      if (event.getHand == OFF_HAND && lastActivatedRune.contains(player.uniqueID) && lastActivatedRune(player.uniqueID) == clickedBlockLocation) {
        //If the player activates a rune with the main hand and places a block with the offhand we cancel the block placement.
        event.setCancelled(true)
        return
      }
      val itemInHand: Material = event.getPlayer.getInventory.getItemInMainHand.getData
      if (event.getHand == HAND && itemInHand.isInstanceOf[Solid] == false) {
        try {
          if (Runecraft.persistentRunes.contains(clickedBlockLocation)) {
            //There is a rune at this location, update it.
            lastActivatedRune += (player.uniqueID -> clickedBlockLocation)
            event.setCancelled(true)
            Runecraft.persistentRunes(clickedBlockLocation).update(player) //This can throw initialization exceptions
          } else {
            //Look for new runes
            val maybeRune = Matcher.lookForRunesAt(clickedBlockLocation, player, event.getBlockFace)
            if (maybeRune.isDefined) {
              lastActivatedRune += (player.uniqueID -> clickedBlockLocation)
              val rune = maybeRune.get
              event.setCancelled(true) //Rune was found, cancel the original event.
              rune.activate() //This can throw initialization exceptions
            }
          }
        } catch {
          case ex: InitializationException => player.sendMessage(ChatColor.RED + ex.textError)
        }
      }
    }
  }

  /**
    * Monitor priority is the last to be called, and should not change event outcome, at this point
    * all other plugins should have done their thing.
    * Having ignoredCanceled set to true means this event will NOT be called when the event is canceled
    */
  @EventHandler(priority = org.bukkit.event.EventPriority.MONITOR, ignoreCancelled = true)
  def onBlockBreakEvent(event: BlockBreakEvent): Unit = {
    checkIntegrityAfterBlockDestruction(event.getBlock, event.getPlayer)
  }

  /**
    * Monitor priority is the last to be called, and should not change event outcome, at this point
    * all other plugins should have done their thing.
    * Having ignoredCanceled set to true means this event will NOT be called when the event is canceled
    */
  @EventHandler(priority = org.bukkit.event.EventPriority.MONITOR, ignoreCancelled = true)
  def onBlockBurnEvent(event: BlockBurnEvent): Unit = {
    checkIntegrityAfterBlockDestruction(event.getBlock, Anonymous)
  }

  /**
    * Monitor priority is the last to be called, and should not change event outcome, at this point
    * all other plugins should have done their thing.
    * Having ignoredCanceled set to true means this event will NOT be called when the event is canceled
    */
  @EventHandler(priority = org.bukkit.event.EventPriority.MONITOR, ignoreCancelled = true)
  def onBlockFadeEvent(event: BlockFadeEvent): Unit = {
    checkIntegrityAfterBlockDestruction(event.getBlock, Anonymous)
  }

  /*@EventHandler
  def onBlockPistonEvent(event: BlockPistonEvent): Unit = {
    //make pistons unable to move rune blocks? Or just have them destroy runes?

  }*/

  /**
    * Monitor priority is the last to be called, and should not change event outcome, at this point
    * all other plugins should have done their thing.
    * Having ignoredCanceled set to true means this event will NOT be called when the event is canceled
    */
  @EventHandler(priority = org.bukkit.event.EventPriority.MONITOR, ignoreCancelled = true)
  def onBlockPlaceEvent(event: BlockPlaceEvent): Unit = {
    checkIntegrityAfterBlockPlacement(event.getBlockPlaced, event.getPlayer)
  }
}
