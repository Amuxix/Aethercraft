package me.amuxix.util

import java.util.UUID

import io.circe.generic.semiauto._
import io.circe.{Decoder, Encoder}
import me.amuxix.Runecraft
import org.bukkit.entity.{Player => BPlayer}
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause.PLUGIN
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
  def teleport[T : Integral](target: Position[T], pitch: Float, yaw: Float): Unit = getPlayer match {
    case Left(_) =>
    case Right(player) =>
      val x: java.lang.Double = new java.lang.Double(target.x.toString)
      val y: java.lang.Double = new java.lang.Double(target.y.toString)
      val z: java.lang.Double = new java.lang.Double(target.z.toString)
      player.teleport(new org.bukkit.Location(target.world, x, y, z, float2Float(yaw), float2Float(pitch)), PLUGIN)
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
    * Sends a message in green to the player.
    * @param text text to send
    */
  def sendMessage(text: String): Unit = getPlayer.toOption.foreach(_.sendMessage(ChatColor.GREEN + text))

  def location: Option[Player.Location] = getPlayer match {
    case Left(_) => None
    case Right(player) => Some(player)
  }

  def name: String = getPlayer match {
    case Left(player) => player.getName
    case Right(player) => player.getName
  }

  protected[util] def getPlayer: Either[OfflinePlayer, BPlayer] = {
    val offlinePlayer = Runecraft.server.getOfflinePlayer(uniqueID)
    if (offlinePlayer.isOnline) {
      Right(offlinePlayer.getPlayer)
    } else {
      Left(offlinePlayer)
    }
  }
}
