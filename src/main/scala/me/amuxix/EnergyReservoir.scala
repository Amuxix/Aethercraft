package me.amuxix

import cats.data.{EitherT, OptionT}
import cats.effect.IO
import io.circe.{Decoder, Encoder}
import me.amuxix.logging.Logger

import scala.collection.mutable

object EnergyReservoir {
  implicit val encoder: Encoder[EnergyReservoir] = Encoder.forProduct3("player", "energy", "cap")(reservoir =>
    (reservoir.player, reservoir.energy, reservoir.cap)
  )
  implicit val decoder: Decoder[EnergyReservoir] = Decoder.forProduct3[EnergyReservoir, Player, Int, Int]("player", "energy", "cap"){new EnergyReservoir(_, _, _)}

  val defaultCap: Int = 100000

  val energyReservoirs = mutable.Map.empty[Player, EnergyReservoir]

  def apply(player: Player): EnergyReservoir = energyReservoirs.getOrElseUpdate(player, new EnergyReservoir(player))
}

class EnergyReservoir private(private val player: Player, private var energy: Int = 0, private val cap: Int = EnergyReservoir.defaultCap) {

  /**
    * Attempts to add energy to a player's reservoir, notifies the player of the amount added
    * @param energy Amount of energy to add
    * @return The energy added to this reservoir
    */
  def add(energy: Int): EitherT[IO, String, Int] =
    if (energy < 0) {
      EitherT.leftT("Cannot add negative energy.")
    } else if (energy == 0) {
      EitherT.rightT(this.energy)
    } else if (this.energy + energy > cap) {
      EitherT.leftT("This surpasses your energy cap.")
    } else {
      this.energy += energy
      EitherT.liftF {
        for {
          _ <- Logger.info(s"${player.name} added $energy energy.")
          _ <- player.notify(s"Added $energy energy.")
        } yield this.energy
      }
    }

  /**
    * Attempts to remove energy from a player's reservoir
    * @param energy Amount of energy to add
    * @return The energy removed from this reservoir
    */
  def remove(energy: Int): EitherT[IO, String, Int] =
    if (energy < 0) {
      EitherT.leftT("Cannot add negative energy.")
    } else if (energy == 0) {
      EitherT.rightT(this.energy)
    } else if (this.energy > energy) {
      EitherT.leftT(s"You don't have enough energy, you need at least $energy")
    } else {
      this.energy -= energy
      EitherT.liftF(Logger.info(s"${player.name} used $energy energy.").map(_ => this.energy))
    }


  def hasAtLeast(energy: Int): Boolean = this.energy >= energy
}
