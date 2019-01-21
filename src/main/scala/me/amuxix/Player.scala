package me.amuxix

import java.util.UUID

import io.circe.{Decoder, Encoder}
import me.amuxix.Player.Location
import me.amuxix.bukkit.{Player => BPlayer}
import me.amuxix.inventory.{Item, PlayerInventory}

object Player {
  type Location = Position[Double]
  implicit val encoder: Encoder[Player] = Encoder.forProduct1("uuid")(_.uuid)
  implicit val decoder: Decoder[Player] = Decoder.forProduct1("uuid")(BPlayer.apply)
}

trait Player {
  def uuid: UUID

  def teleportTo(target: Location, pitch: Float, yaw: Float): Unit

  def pitch: Float

  def yaw: Float

  /**
    * Shows a message in the action bar position for the player.
    *
    * @param text Message to be sent
    */
  def sendNotification(text: String): Unit

  /**
    * Sends a message to the player.
    *
    * @param text Message to be sent
    */
  def sendMessage(text: String): Unit

  def location: Option[Any]

  def name: String

  def inventory: Option[PlayerInventory]

  def helmet: Option[Item]

  def itemInMainHand: Option[Item]

  def itemInOffHand: Option[Item]
}
