package me.amuxix

import java.util.UUID

import com.github.ghik.silencer.silent
import io.circe.generic.semiauto._
import io.circe.{Decoder, Encoder}
import me.amuxix.Player.Location
import me.amuxix.inventory.Item
import org.bukkit.entity.{Player => BPlayer}
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause.PLUGIN
import org.bukkit.inventory.PlayerInventory
import org.bukkit.{ChatColor, OfflinePlayer}

/**
  * Created by Amuxix on 22/11/2016.
  */
object Player {
  implicit def bukkitPlayer2Player(bukkitPlayer: BPlayer): Player = {
    Player(bukkitPlayer.getUniqueId)
  }

  type Location = Position[Double]
  implicit val encodePlayer: Encoder[Player] = deriveEncoder
  implicit val decodePlayer: Decoder[Player] = deriveDecoder
}
case class Player(uniqueID: UUID) extends Entity {
  def teleport[T : Integral](target: Position[T], pitch: Float, yaw: Float): Unit = getPlayer.toOption.foreach {
    import Integral.Implicits._
    val destination = new org.bukkit.Location(target.world, target.x.toDouble(), target.y.toDouble(), target.z.toDouble(), yaw, pitch)
    _.teleport(destination)

  }

  def pitch: Float = getPlayer match {
    case Left(_) => 0
    case Right(player) => player.getLocation.getPitch
  }

  def yaw: Float = getPlayer match {
    case Left(_) => 0
    case Right(player) => player.getLocation.getYaw
  }

  /**
    * Shows a message in the action bar position for the player.
    * @param text Message to be sent
    */
  def sendNotification(text: String): Unit = getPlayer.toOption.foreach(JSONMessage.create(text).color(ChatColor.GREEN).actionbar(_))

  /**
    * Sends a message to the player.
    * @param text Message to be sent
    */
  def sendMessage(text: String): Unit = getPlayer.toOption.foreach(_.sendMessage(text))

  def location: Option[Location] = getPlayer.toOption.map(_.getLocation)

  def name: String = getPlayer match {
    case Left(player) => player.getName
    case Right(player) => player.getName
  }

  def inventory: Option[PlayerInventory] = getPlayer.toOption.map(_.getInventory)

  protected[events] def getPlayer: Either[OfflinePlayer, BPlayer] = {
    val offlinePlayer = Runecraft.server.getOfflinePlayer(uniqueID)
    if (offlinePlayer.isOnline) {
      Right(offlinePlayer.getPlayer)
    } else {
      Left(offlinePlayer)
    }
  }

  def helmet(): Option[Item] = inventory collect {
    case inventory if inventory.getHelmet != null => inventory.getHelmet
  }
}
