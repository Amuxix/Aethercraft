package me.amuxix

import me.amuxix.IntegrityMonitor.{checkIntegrityAfterBlockDestruction, checkIntegrityAfterBlockPlacement}
import me.amuxix.pattern.matching.Matcher
import me.amuxix.runes.exceptions.RuneInitializationException
import me.amuxix.util.Player.bukkitPlayer2Player
import me.amuxix.util.{Anonymous, Player}
import org.bukkit.event.EventHandler
import org.bukkit.event.block._
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot.HAND
import org.bukkit.{ChatColor, Material}

object Listener extends org.bukkit.event.Listener {

  @EventHandler
  def onPlayerInteract(event: PlayerInteractEvent): Unit = {
    //This gets fired Twice in a row, once for main hand and one for the off hand
    val itemInHand: Material = event.getPlayer.getInventory.getItemInMainHand.getType
    val player: Player = event.getPlayer
    if (event.getAction == Action.RIGHT_CLICK_BLOCK && event.getHand == HAND && (itemInHand.isBlock == false || itemInHand == Material.AIR)) {
      try {
        if (Runecraft.persistentRunes.contains(event.getClickedBlock)) {
          //There is a rune at this location, update it.
          Runecraft.persistentRunes(event.getClickedBlock).update(player)
        } else {
          //Look for new runes
          //Cancel the interact event if a rune was found
          val maybeRune = Matcher.lookForRunesAt(event.getClickedBlock, player, event.getBlockFace)
          if (maybeRune.isDefined) {
            val rune = maybeRune.get
            rune.activate()
            event.setCancelled(true)
          }
        }
      } catch {
        case ex: RuneInitializationException => player.sendMessage(ChatColor.RED + ex.textError)
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
