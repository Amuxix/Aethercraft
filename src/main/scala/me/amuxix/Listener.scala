package me.amuxix

import me.amuxix.IntegrityMonitor.checkIntegrityAfterBlockDestruction
import me.amuxix.pattern.matching.Matcher
import me.amuxix.runes.exceptions.RuneInitializationException
import me.amuxix.util.Player.bukkitPlayer2Player
import org.bukkit.ChatColor
import org.bukkit.event.EventHandler
import org.bukkit.event.block._
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot.HAND

object Listener extends org.bukkit.event.Listener {

  @EventHandler
  def onPlayerInteract(event: PlayerInteractEvent): Unit = {
    //This gets fired Twice in a row, once for main hand and one for the off hand
    if (event.getAction == Action.RIGHT_CLICK_BLOCK && event.getHand == HAND && event.getPlayer.getInventory.getItemInMainHand.getType.isBlock == false) {
      try {
        if (Runecraft.persistentRunes.contains(event.getClickedBlock)) {
          //There is a rune at this location, update it.
          Runecraft.persistentRunes(event.getClickedBlock).update(event.getPlayer)
        } else {
          //Look for new runes
          //Cancel the interact event if a rune was found
          val maybeRune = Matcher.lookForRunesAt(event)
          if (maybeRune.isDefined) {
            maybeRune.get.notifyActivator()
            maybeRune.get.logRuneActivation()
            event.setCancelled(true)
          }
        }
      } catch {
        case ex: RuneInitializationException => bukkitPlayer2Player(event.getPlayer).sendMessage(ChatColor.RED + ex.textError)
      }
    }
  }

  @EventHandler
  def onBlockBreakEvent(event: BlockBreakEvent): Unit = {
    if (event.isCancelled == false) {
      checkIntegrityAfterBlockDestruction(event.getBlock, event.getPlayer)
    }
  }

  @EventHandler
  def onBlockBurnEvent(event: BlockBurnEvent): Unit = {
    if (event.isCancelled == false) {
      checkIntegrityAfterBlockDestruction(event.getBlock)
    }
  }

  @EventHandler
  def onBlockFadeEvent(event: BlockFadeEvent): Unit = {
    if (event.isCancelled == false) {
      checkIntegrityAfterBlockDestruction(event.getBlock)
    }
  }

  /*@EventHandler
  def onBlockPistonEvent(event: BlockPistonEvent): Unit = {
    //make pistons unable to move rune blocks? Or just have them destroy runes?

  }*/

}
