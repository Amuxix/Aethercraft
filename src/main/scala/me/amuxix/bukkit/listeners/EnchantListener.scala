package me.amuxix.bukkit.listeners

import cats.Traverse
import cats.data.EitherT
import cats.effect.IO
import cats.implicits._
import me.amuxix.bukkit.Player.BukkitPlayerOps
import me.amuxix.bukkit.block.Block
import me.amuxix.bukkit.block.Block.BukkitBlockOps
import me.amuxix.bukkit.events.BlockBreak
import me.amuxix.bukkit.inventory.Item
import me.amuxix.bukkit.inventory.Item.BukkitItemStackOps
import me.amuxix.runes.traits.enchants.Enchant._
import me.amuxix.{Direction, Player}
import org.bukkit.event.Event.Result._
import org.bukkit.event.EventPriority.LOWEST
import org.bukkit.event.block.Action._
import org.bukkit.event.block.{BlockBreakEvent, BlockPlaceEvent}
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.{Event, EventHandler, Listener => BukkitListener}

object EnchantListener extends BukkitListener {
  private def runEnchants[F[_] : Traverse](enchants: F[EitherT[IO, String, Boolean]], player: Player): Boolean = {
    enchants
      .map { enchant =>
        enchant.fold(
          error => player.notifyError(error).map(_ => false),
          cancel => IO(cancel)
        )
          .flatten
      }
        .foldLeft(false)(_ || _.unsafeRunSync())
  }

  @EventHandler(priority = LOWEST)
  def onBlockBreakEvent(event: BlockBreakEvent): Unit = {
    //This will run all triggers set by enchants and cancel the event if any of them cancels the event
    if (!event.isInstanceOf[BlockBreak]) { //Avoid chain reactions
      val player = event.getPlayer.aetherize
      player.itemInMainHand.foreach { itemInHand =>
        val enchants = blockBreakEnchants.map(_.onBlockBreak(player, itemInHand, event.getBlock.aetherize))
        event.setCancelled(runEnchants(enchants, player))
      }
    }
  }

  @EventHandler(priority = LOWEST)
  def onBlockPlaceEvent(event: BlockPlaceEvent): Unit = {
    //This will run all triggers set by enchants and cancel the event if any of them cancels the event
    val player: Player = event.getPlayer.aetherize
    val placedBlock: Block = event.getBlock.aetherize
    val itemPlaced: Option[Item] = Option(event.getItemInHand).map(_.aetherize)
    val enchants = blockPlaceEnchants.map(_.onBlockPlace(player, placedBlock, event.getBlockAgainst.aetherize, itemPlaced))
    event.setCancelled(runEnchants(enchants, player))
  }

  @EventHandler(priority = LOWEST)
  def onPlayerInteract(event: PlayerInteractEvent): Unit = {
    val player: Player = event.getPlayer.aetherize
    val blockFace: Direction = event.getBlockFace
    //This will run all triggers set by enchants and cancel the event if any of them cancels the event
    Option(event.getItem).collect {
      //Don't run enchants if useItemInHand is denied
      case item if event.useItemInHand == ALLOW || event.useItemInHand == DEFAULT => item.aetherize
    }
      .foreach { itemInHand =>
        val enchants = event.getAction match {
          case RIGHT_CLICK_BLOCK =>
            blockInteractEnchants.collect {
              case enchant if itemInHand.hasRuneEnchant(enchant) =>
                enchant.onBlockInteract(player, itemInHand, event.getClickedBlock.aetherize, blockFace)
            }
          case LEFT_CLICK_BLOCK =>
            blockDamageEnchants.collect {
              case enchant if itemInHand.hasRuneEnchant(enchant) =>
                enchant.onBlockDamage(player, itemInHand, event.getClickedBlock.aetherize, blockFace)
            }
          case RIGHT_CLICK_AIR =>
            airInteractEnchants.collect {
              case enchant if itemInHand.hasRuneEnchant(enchant) =>
                enchant.onAirInteract(player, itemInHand)
            }
          case LEFT_CLICK_AIR =>
            airSwingEnchants.collect {
              case enchant if itemInHand.hasRuneEnchant(enchant) =>
                enchant.onAirSwing(player, itemInHand)
            }
          case PHYSICAL => List.empty//Do Nothing
        }
        //Cancel block interact if any enchant was run.
        event.setUseInteractedBlock(if(enchants.nonEmpty) DENY else DEFAULT)
        //Cancel use item in hand if any enchant requires it
        event.setUseItemInHand(if (runEnchants(enchants, player)) DENY else DEFAULT)
      }
  }
}
