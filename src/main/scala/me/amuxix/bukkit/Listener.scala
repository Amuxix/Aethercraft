package me.amuxix.bukkit

import java.util.UUID

import me.amuxix.Block.Location
import me.amuxix.exceptions.InitializationException
import me.amuxix.inventory.Item
import me.amuxix.pattern.matching.Matcher
import me.amuxix.{Player, Aethercraft}
import org.bukkit.ChatColor
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority.LOWEST
import org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot.{HAND, OFF_HAND}

object Listener extends org.bukkit.event.Listener {
  /**
    * This maps each player to last location they activated a rune at.
    * This is used to cancel off hand interact events.
    */
  var lastActivatedRune: Map[UUID, Location] = Map.empty

  @EventHandler(priority = LOWEST)
  def onPlayerInteract(event: PlayerInteractEvent): Unit = {
    //This gets fired Twice in a row, once for main hand and one for the off hand
    if (event.getAction == RIGHT_CLICK_BLOCK) {
      val clickedBlockLocation: Location = bukkitLocation2Position(event.getClickedBlock.getLocation)
      val player: Player = event.getPlayer
      if (event.getHand == OFF_HAND && lastActivatedRune.exists(_.==((player.uniqueID, clickedBlockLocation)))) {
        //If the player activates a rune with the main hand and places a block with the offhand we cancel the block placement.
        event.setCancelled(true)
      } else {
        val itemInHand: Item = event.getPlayer.getInventory.getItemInMainHand
        if (event.getHand == HAND && itemInHand.material.isSolid == false) {
          try {
            if (Aethercraft.persistentRunes.contains(clickedBlockLocation)) {
              //There is a rune at this location, update it.
              lastActivatedRune += player.uniqueID -> clickedBlockLocation
              event.setCancelled(true)
              Aethercraft.persistentRunes(clickedBlockLocation).update(player) //This can throw initialization exceptions
            } else {
              //Look for new runes
              Matcher.lookForRunesAt(clickedBlockLocation, player, event.getBlockFace, itemInHand.material)
                .foreach { rune =>
                  lastActivatedRune += player.uniqueID -> clickedBlockLocation
                  event.setCancelled(true) //Rune was found, cancel the original event.
                  rune.activate(itemInHand) //This can throw initialization exceptions
                }
            }
          } catch {
            case ex: InitializationException => player.sendNotification(ChatColor.RED + ex.textError)
          }
        }
      }
    }
  }
}
