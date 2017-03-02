package me.amuxix

import java.util.UUID

import com.github.ghik.silencer.silent
import io.circe.generic.semiauto._
import io.circe.{Decoder, Encoder}
import me.amuxix.Player.Location
import me.amuxix.inventory.Item
import org.bukkit.entity.{Player => BPlayer}

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

  @silent def named(name: String): Option[Player] = {
    Option(Runecraft.server.getPlayer(name))
  }
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

  def location: Option[Location] = getPlayer match {
    case Left(_) => None
    case Right(player) => Some(player.getLocation)
  }

  def name: String = getPlayer match {
    case Left(player) => player.getName
    case Right(player) => player.getName
  }

  def inventory: Option[PlayerInventory] = getPlayer match {
    case Left(_) => None
    case Right(player) => Some(player.getInventory)
  }

  def getPlayer: Either[OfflinePlayer, BPlayer] = {
    val offlinePlayer = Runecraft.server.getOfflinePlayer(uniqueID)
    if (offlinePlayer.isOnline) {
      Right(offlinePlayer.getPlayer)
    } else {
      Left(offlinePlayer)
    }
  }

  def helmet(): Option[Item] = if (inventory.isDefined && inventory.get.getHelmet != null) { //getHelmet can return null if not wearing any helmet
    Some(inventory.get.getHelmet)
  } else {
    None
  }
}
