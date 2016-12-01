package me.amuxix

import org.bukkit.event.EventHandler
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

object Listener extends org.bukkit.event.Listener {

  @EventHandler
  def onPlayerInteract(event: PlayerInteractEvent) = {
    if (event.getAction == Action.RIGHT_CLICK_BLOCK) {

    }
  }
}
