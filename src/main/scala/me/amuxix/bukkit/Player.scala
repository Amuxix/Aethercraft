package me.amuxix.bukkit

import java.util.UUID

import cats.data.OptionT
import cats.effect.IO
import me.amuxix
import me.amuxix.Player.Location
import me.amuxix._
import me.amuxix.bukkit.Location.{BukkitDoublePositionOps, PositionDoubleOps}
import me.amuxix.bukkit.inventory.PlayerInventory.BukkitInventoryOps
import me.amuxix.bukkit.inventory.{Item, PlayerInventory}
import me.amuxix.notification.JSONMessage
import org.bukkit.{ChatColor, OfflinePlayer}
import org.bukkit.entity.{Player => BPlayer}
import org.bukkit.event.inventory.InventoryType

import scala.collection.mutable

/**
  * Created by Amuxix on 22/11/2016.
  */
object Player {
  private val players = mutable.Map.empty[UUID, Player]

  implicit class BukkitPlayerOps(player: BPlayer) extends Aetherizeable[Player] {
    override def aetherize: Player = players.getOrElseUpdate(player.getUniqueId, Player(player.getUniqueId))
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

  override def teleportTo(target: Location, pitch: Float, yaw: Float): OptionT[IO, String] =
    OptionT.fromOption(player.fold(_ => Some("Player is offline"), player => {
      val destination = target.bukkitForm
      destination.setPitch(pitch)
      destination.setYaw(yaw)
      Option.unless(player.teleport(destination))(Aethercraft.defaultFailureMessage)
    }))

  override def pitch: Float = player.fold(_ => 0, _.getLocation.getPitch)

  override def yaw: Float = player.fold(_ => 0, _.getLocation.getYaw)

  /**
    * Shows a message in the action bar position for the player.
    * @param text Message to be sent
    */
  override def notify(text: String): IO[Unit] = IO(player.foreach(_.sendMessage(ChatColor.GREEN + text)))

  /**
    * Shows an error to this player
    *
    * @param text Message to be sent
    */
  override def notifyError(text: String): IO[Unit] = notify(ChatColor.DARK_RED + text)

  override def location: Option[Location] = player.map(_.getLocation.aetherize).toOption

  override def name: String = player.fold(_.getName, _.getName)

  override def inventory: Option[PlayerInventory] = player.toOption.map(_.getInventory.aetherize)

  override def helmet: Option[Item] = inventory.flatMap(_.helmet)

  override def itemInMainHand: Option[Item] = inventory.flatMap(_.itemInMainHand)

  override def itemInOffHand: Option[Item] = inventory.flatMap(_.itemInOffHand)

  override def bukkitForm: BPlayer = player.toOption.orNull


}
