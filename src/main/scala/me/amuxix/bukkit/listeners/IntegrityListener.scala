package me.amuxix.bukkit.listeners

import me.amuxix.IntegrityMonitor.{checkIntegrityAfterBlockDestruction, checkIntegrityAfterBlockPlacement}
import me.amuxix.bukkit.Anonymous
import me.amuxix.bukkit.block.Block.BukkitBlockOps
import me.amuxix.bukkit.Player.BukkitPlayerOps
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority.MONITOR
import org.bukkit.event.block._

object IntegrityListener extends org.bukkit.event.Listener {

  /**
    * Monitor priority is the last to be called, and should not change event outcome, at this point
    * all other plugins should have done their thing.
    * Having ignoredCanceled set to true means this method will NOT be called when the event is canceled
    */
  @EventHandler(priority = MONITOR, ignoreCancelled = true)
  def onBlockBreakEvent(event: BlockBreakEvent): Unit = {
    checkIntegrityAfterBlockDestruction(event.getBlock.aetherize, event.getPlayer.aetherize)
  }

  /**
    * Monitor priority is the last to be called, and should not change event outcome, at this point
    * all other plugins should have done their thing.
    * Having ignoredCanceled set to true means this method will NOT be called when the event is canceled
    */
  @EventHandler(priority = MONITOR, ignoreCancelled = true)
  def onBlockBurnEvent(event: BlockBurnEvent): Unit = {
    checkIntegrityAfterBlockDestruction(event.getBlock.aetherize, Anonymous)
  }

  /**
    * Monitor priority is the last to be called, and should not change event outcome, at this point
    * all other plugins should have done their thing.
    * Having ignoredCanceled set to true means this method will NOT be called when the event is canceled
    */
  @EventHandler(priority = MONITOR, ignoreCancelled = true)
  def onBlockFadeEvent(event: BlockFadeEvent): Unit = {
    checkIntegrityAfterBlockDestruction(event.getBlock.aetherize, Anonymous)
  }

  /*@EventHandler(priority = MONITOR, ignoreCancelled = true)
  def onBlockPistonEvent(event: BlockPistonEvent): Unit = {
    //make pistons unable to move rune blocks? Or just have them destroy runes?

  }*/

  /**
    * Monitor priority is the last to be called, and should not change event outcome, at this point
    * all other plugins should have done their thing.
    * Having ignoredCanceled set to true means this method will NOT be called when the event is canceled
    */
  @EventHandler(priority = MONITOR, ignoreCancelled = true)
  def onBlockPlaceEvent(event: BlockPlaceEvent): Unit = {
    checkIntegrityAfterBlockPlacement(event.getBlockPlaced.aetherize, event.getPlayer.aetherize)
  }
}
