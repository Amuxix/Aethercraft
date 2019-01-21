package me.amuxix.bukkit

import java.util.UUID

import me.amuxix
import me.amuxix._
import me.amuxix.Player.Location
import me.amuxix.bukkit.Location.{BukkitDoublePositionOps, PositionDoubleOps}
import me.amuxix.bukkit.inventory.PlayerInventory.BukkitInventoryOps
import me.amuxix.bukkit.inventory.{Item, PlayerInventory}
import me.amuxix.notification.JSONMessage
import org.bukkit.entity.{Player => BPlayer}
import org.bukkit.{ChatColor, OfflinePlayer}

/**
  * Created by Amuxix on 22/11/2016.
  */
object Player {
  implicit class BukkitPlayerOps(player: BPlayer) extends Aetherizeable[Player] {
    override def aetherize: Player = Player(player.getUniqueId)
  }
}
private[bukkit] case class Player(uuid: UUID) extends Entity with amuxix.Player with BukkitForm[BPlayer] {

  private def player: Either[OfflinePlayer, BPlayer] = {
    val offlinePlayer = Aethercraft.server.getOfflinePlayer(uuid)
    if (offlinePlayer.isOnline) {
      Right(offlinePlayer.getPlayer)
    } else {
      Left(offlinePlayer)
    }
  }

  override def teleportTo(target: Location, pitch: Float, yaw: Float): Unit = player.foreach { player =>
    val destination = target.bukkitForm
    destination.setPitch(pitch)
    destination.setYaw(yaw)
    player.teleport(destination)
  }

  override def pitch: Float = player.fold(_ => 0, _.getLocation.getPitch)

  override def yaw: Float = player.fold(_ => 0, _.getLocation.getYaw)

  /**
    * Shows a message in the action bar position for the player.
    * @param text Message to be sent
    */
  override def sendNotification(text: String): Unit = player.foreach(player => JSONMessage.create(text).color(ChatColor.GREEN).actionbar(player))

  /**
    * Sends a message to the player.
    * @param text Message to be sent
    */
  override def sendMessage(text: String): Unit = player.foreach(_.sendMessage(text))

  override def location: Option[Location] = player.map(_.getLocation.aetherize).toOption

  override def name: String = player.fold(_.getName, _.getName)

  override def inventory: Option[PlayerInventory] = player.toOption.map(_.getInventory.aetherize)

  override def helmet: Option[Item] = inventory.flatMap(_.helmet)

  override def itemInMainHand: Option[Item] = inventory.flatMap(_.itemInMainHand)

  override def itemInOffHand: Option[Item] = inventory.flatMap(_.itemInOffHand)

  override def bukkitForm: BPlayer = player.toOption.orNull
}
