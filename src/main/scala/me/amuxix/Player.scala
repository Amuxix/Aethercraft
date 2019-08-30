package me.amuxix

import java.util.UUID

import cats.data.{EitherT, OptionT}
import cats.effect.IO
import io.circe.{Decoder, Encoder}
import me.amuxix.bukkit.Configuration
import me.amuxix.inventory.{Item, PlayerInventory}
import me.amuxix.position.EntityPosition

object Player {
  implicit val encoder: Encoder[Player] = Encoder.forProduct1("uuid")(_.uuid)
  implicit val decoder: Decoder[Player] = Decoder.forProduct1("uuid")(Player.apply)

  private var players: Map[UUID, Player] = Map.empty

  def apply(uuid: UUID): Player =
    players.get(uuid).fold[Player] {
      val player = new bukkit.Player(uuid)
      players += (uuid -> player)
      player
    }(identity)
}

trait Player {
  def uuid: UUID

  protected def teleport(target: EntityPosition, pitch: Float, yaw: Float): OptionT[IO, String]

  def teleportTo(target: EntityPosition, pitch: Float, yaw: Float): EitherT[IO, String, Unit] = for {
    position <- EitherT.fromOption[IO](position, "Could not find player position.")
    _ <- removeEnergy((position.distance(target).fold(10000D)(identity) * Configuration.move).toInt)
    _ <- teleport(target, pitch, yaw).toLeft(())
  } yield ()

  def pitch: Float

  def yaw: Float

  /**
    * Shows a notification to this player
    *
    * @param text Message to be sent
    */
  def notify(text: String): IO[Unit]

  /**
    * Shows an error to this player
    *
    * @param text Message to be sent
    */
  def notifyError(text: String): IO[Unit]

  def position: Option[EntityPosition]

  def name: Option[String]

  def nameOrUUID: String

  def isOnline: Boolean

  def inventory: Option[PlayerInventory]

  def add(item: Item): OptionT[IO, String] = OptionT.fromOption[IO](inventory).flatMap(_.add(item))

  def helmet: Option[Item]

  def itemInMainHand: Option[Item]

  def itemInOffHand: Option[Item]

  /**
    * Check if player is online and in creative mode
    * @return True if player is online and in creative mode, false if player is offline or not in creative mode
    */
  def inCreativeMode: Boolean

  /**
    * Check if player is online and in survival mode
    * @return True if player is online and in survival mode, false if player is offline or not in survival mode
    */
  def inSurvivalMode: Boolean

  private lazy val energyReservoir: EnergyReservoir = EnergyReservoir(this)

  def addMaximumEnergyFrom(consumables: List[(List[Consumable#ConsumeIO], Option[Consumable#ConsumeIO])]): EitherT[IO, String, Energy] = energyReservoir.addMaximumEnergyFrom(consumables)

  def addEnergy(energy: Energy): EitherT[IO, String, Energy] = energyReservoir.add(energy)

  def hasAtLeast(energy: Energy): Boolean = energyReservoir.hasAtLeast(energy)

  def removeEnergy(energy: Energy): EitherT[IO, String, Energy] = energyReservoir.remove(energy)

  def energyCap: Energy = energyReservoir.cap
}
