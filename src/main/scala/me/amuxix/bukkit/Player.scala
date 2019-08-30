package me.amuxix.bukkit

import java.util.UUID

import cats.data.OptionT
import cats.effect.IO
import me.amuxix
import me.amuxix._
import me.amuxix.bukkit.Location.{BukkitEntityPositionOps, EntityPositionOps}
import me.amuxix.bukkit.inventory.PlayerInventory.BukkitInventoryOps
import me.amuxix.bukkit.inventory.{Item, PlayerInventory}
import me.amuxix.position.EntityPosition
import org.bukkit.entity.{Player => BPlayer}
import org.bukkit.{ChatColor, GameMode, OfflinePlayer}

/**
  * Created by Amuxix on 22/11/2016.
  */
object Player {
  implicit class BukkitPlayerOps(player: OfflinePlayer) extends Aetherizeable[amuxix.Player] {
    override def aetherize: amuxix.Player = amuxix.Player(player.getUniqueId)
  }
}

class Player(val uuid: UUID) extends Entity with amuxix.Player with BukkitForm[BPlayer] {

  private[bukkit] def player: Either[OfflinePlayer, BPlayer] = {
    val offlinePlayer = Bukkit.server.getOfflinePlayer(uuid)
    if (offlinePlayer.isOnline) {
      Right(offlinePlayer.getPlayer)
    } else {
      Left(offlinePlayer)
    }
  }

  override def teleport(target: EntityPosition, pitch: Float, yaw: Float): OptionT[IO, String] =
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
  override def notify(text: String): IO[Unit] = IO(player.foreach(_.sendMessage(ChatColor.GREEN.toString + text)))

  /**
    * Shows an error to this player
    *
    * @param text Message to be sent
    */
  override def notifyError(text: String): IO[Unit] = notify(ChatColor.DARK_RED.toString + text)

  override def position: Option[EntityPosition] = player.map(_.getLocation.aetherize).toOption

  override def name: Option[String] = Option(player.fold(_.getName, _.getName))

  override def nameOrUUID: String = name.getOrDefault(uuid.toString)

  override def isOnline: Boolean = player.isRight

  override def inventory: Option[PlayerInventory] = player.toOption.map(_.getInventory.aetherize)

  override def helmet: Option[Item] = inventory.flatMap(_.helmet)

  override def itemInMainHand: Option[Item] = inventory.map(_.itemInMainHand)

  override def itemInOffHand: Option[Item] = inventory.map(_.itemInOffHand)

  private def gameMode: Option[GameMode] = player.toOption.map(_.getGameMode)

  /**
    * Check if player is online and in creative mode
    * @return True if player is online and in creative mode, false if player is offline or not in creative mode
    */
  override def inCreativeMode: Boolean = gameMode.contains(GameMode.CREATIVE)

  /**
    * Check if player is online and in survival mode
    * @return True if player is online and in survival mode, false if player is offline or not in survival mode
    */
  override def inSurvivalMode: Boolean = gameMode.contains(GameMode.SURVIVAL)

  override def bukkitForm: BPlayer = player.toOption.orNull
}
