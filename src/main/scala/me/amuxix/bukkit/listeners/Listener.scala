package me.amuxix.bukkit.listeners

import java.util.UUID

import me.amuxix.Serialization
import me.amuxix.block.Block.Location
import me.amuxix.bukkit.Location.BukkitIntPositionOps
import me.amuxix.bukkit.Player.BukkitPlayerOps
import me.amuxix.bukkit._
import me.amuxix.bukkit.inventory.Item
import me.amuxix.bukkit.inventory.Item.BukkitItemStackOps
import me.amuxix.pattern.matching.Matcher
import org.bukkit.ChatColor
import org.bukkit.entity.EntityType.DROPPED_ITEM
import org.bukkit.entity.{Item => BItem}
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority._
import org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDamageEvent.DamageCause._
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot.{HAND, OFF_HAND}

import scala.collection.JavaConverters._

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
      val clickedBlockLocation: Location = event.getClickedBlock.getLocation.aetherize
      val player: Player = event.getPlayer.aetherize
      if (event.getHand == OFF_HAND) {
        //If the player activates a rune with the main hand and places a block with the offhand we cancel the block placement.
        val cancel = lastActivatedRune.exists(_.==((player.uuid, clickedBlockLocation)))
        event.setCancelled(cancel)
        lastActivatedRune -= player.uuid
      } else {
        val itemInHand: Item = event.getPlayer.getInventory.getItemInMainHand.aetherize
        if (event.getHand == HAND && itemInHand.material.isSolid == false) {
          (if (Serialization.persistentRunes.contains(clickedBlockLocation)) {
            //There is a rune at this location, update it.
            Some(Serialization.persistentRunes(clickedBlockLocation).update(player))
          } else {
            //Look for new runes
            Matcher.lookForRunesAt(clickedBlockLocation, player, event.getBlockFace)
              .map(_.activate(itemInHand))
          }).map {
            case Right(cancel) => event.setCancelled(cancel)
            case Left(error) => player.notifyError(error)
          }.foreach(_ => lastActivatedRune += player.uuid -> clickedBlockLocation)
        }
      }
    }
  }

  @EventHandler(priority = NORMAL, ignoreCancelled = true)
  def onEntityDamageEvent(event: EntityDamageEvent): Unit = {
    if (event.getEntityType == DROPPED_ITEM && (event.getCause == FIRE || event.getCause == LAVA)) {
      val entity = event.getEntity
      val item = entity.asInstanceOf[BItem].getItemStack.aetherize
      if (item.material.hasEnergy) {
        val itemPosition = entity.getLocation.aetherize
        findClosestPlayerTo(itemPosition, Configuration.maxBurnDistance).foreach { player =>
          item.consume(player)
        }
      }
    }

  }

  private def findClosestPlayerTo(where: Location, maxDistance: Double): Option[Player] = {
    Aethercraft.server.getOnlinePlayers.asScala
      .foldLeft(Option.empty[(Player, Double)]) { case (None, player) =>
        val distance = player.getLocation.aetherize.distance(where)
        if (distance <= maxDistance) {
          Some((player.aetherize, distance))
        } else {
          None
        }
      case (closest @ Some((_, closestPlayerDistance)), player) =>
        val distance = player.getLocation.aetherize.distance(where)
        if (distance < closestPlayerDistance) {
          Some((player.aetherize, distance))
        } else {
          closest
        }
      }.map(_._1)
  }
}
