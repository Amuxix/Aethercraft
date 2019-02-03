package me.amuxix.bukkit.listeners

import cats.data.EitherT
import cats.effect.IO
import me.amuxix.Direction
import me.amuxix.Player
import me.amuxix.bukkit.Player.BukkitPlayerOps
import me.amuxix.bukkit.block.Block
import me.amuxix.bukkit.block.Block.BukkitBlockOps
import me.amuxix.bukkit.inventory.Item
import me.amuxix.bukkit.inventory.Item.BukkitItemStackOps
import me.amuxix.runes.traits.enchants.Enchant._
import org.bukkit.event.{Cancellable, EventHandler, Listener => BukkitListener}
import org.bukkit.event.EventPriority.LOWEST
import org.bukkit.event.block.Action._
import org.bukkit.event.block.{BlockBreakEvent, BlockPlaceEvent}
import org.bukkit.event.player.PlayerInteractEvent

object EnchantListener extends BukkitListener {
  private def runEnchantsAndCancel(enchants: Stream[EitherT[IO, String, Boolean]], event: Cancellable, player: Player): Unit = {
    val cancel = enchants
      .map { _.value.map {
        case Left(error) =>
          player.notifyError(error)
          false
        case Right(cancel) => cancel
      }}
      .foldLeft(event.isCancelled)(_ || _.unsafeRunSync())
    event.setCancelled(cancel)
  }

  @EventHandler(priority = LOWEST)
  def onBlockBreakEvent(event: BlockBreakEvent): Unit = {
    //This will run all triggers set by enchants and cancel the event if any of them cancels the event
    val player = event.getPlayer.aetherize
    val enchants = blockBreakEnchants.map(_.onBlockBreak(player, event.getBlock.aetherize))
    runEnchantsAndCancel(enchants, event, player)
  }

  @EventHandler(priority = LOWEST)
  def onBlockPlaceEvent(event: BlockPlaceEvent): Unit = {
    //This will run all triggers set by enchants and cancel the event if any of them cancels the event
    val player: Player = event.getPlayer.aetherize
    val placedBlock: Block = event.getBlock.aetherize
    val itemPlaced: Option[Item] = Option(event.getItemInHand).map(_.aetherize)
    val enchants = blockPlaceEnchants.map(_.onBlockPlace(player, placedBlock, event.getBlockAgainst.aetherize, itemPlaced))
    runEnchantsAndCancel(enchants, event, player)
  }

  @EventHandler(priority = LOWEST)
  def onPlayerInteract(event: PlayerInteractEvent): Unit = {
    val player: Player = event.getPlayer.aetherize
    val blockFace: Direction = event.getBlockFace
    val itemInHand: Option[Item] = Option(event.getItem).map(_.aetherize)
    //This will run all triggers set by enchants and cancel the event if any of them cancels the event
    val enchants = event.getAction match {
      case RIGHT_CLICK_BLOCK =>
        blockInteractEnchants.map(_.onBlockInteract(player, itemInHand, event.getClickedBlock.aetherize, blockFace))
      case LEFT_CLICK_BLOCK =>
        blockDamageEnchants.map(_.onBlockDamage(player, itemInHand, event.getClickedBlock.aetherize, blockFace))
      case RIGHT_CLICK_AIR =>
        airInteractEnchants.map(_.onAirInteract(player, itemInHand))
      case LEFT_CLICK_AIR =>
        airSwingEnchants.map(_.onAirSwing(player, itemInHand))
      case _ => Stream.empty//Do Nothing
    }
    runEnchantsAndCancel(enchants, event, player)
  }
}
