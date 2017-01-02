package me.amuxix.util

import java.util.UUID

import me.amuxix.Runecraft
import org.bukkit.OfflinePlayer
import org.bukkit.entity.{Player => BPlayer}

/**
  * Created by Amuxix on 22/11/2016.
  */
object Player {
  implicit def bukkitPlayer2Player(bukkitPlayer: BPlayer): Player = {
    Player(bukkitPlayer.getUniqueId, bukkitPlayer.getLocation.getPitch, bukkitPlayer.getLocation.getYaw)
  }
  type Location = Position[Double]
}
case class Player(uniqueID: UUID, pitch: Double, yaw: Double) extends Entity {

  def sendMessage(text: String): Unit = getPlayer.toOption.foreach(_.sendMessage(text))

  def location: Option[Player.Location] = getPlayer match {
    case Left(_) => None
    case Right(player) => Some(player)
  }

  def name: String = getPlayer match {
    case Left(player) => player.getName
    case Right(player) => player.getName
  }

  private def getPlayer: Either[OfflinePlayer, BPlayer] = {
    val offlinePlayer = Runecraft.server.getOfflinePlayer(uniqueID)
    if (offlinePlayer.isOnline) {
      Right(offlinePlayer.getPlayer)
    } else {
      Left(offlinePlayer)
    }
  }
}
