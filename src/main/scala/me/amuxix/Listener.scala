package me.amuxix

import me.amuxix.pattern.matching.Matcher
import org.bukkit.event.EventHandler
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot.HAND

object Listener extends org.bukkit.event.Listener {

  @EventHandler
  def onPlayerInteract(event: PlayerInteractEvent) = {
    //This gets fired Twice in a row, once for main hand and one for the off hand
    if (event.getAction == Action.RIGHT_CLICK_BLOCK && event.getHand == HAND && event.getPlayer.getInventory.getItemInMainHand.getType.isBlock == false) {
      if (Matcher.lookForRunesAt(event).isDefined) {
        event.setCancelled(true)
        //Cancel event from offhand if rune is activated
      }
    }
  }


}
