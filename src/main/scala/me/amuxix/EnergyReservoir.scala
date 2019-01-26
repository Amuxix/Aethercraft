package me.amuxix

import cats.data.OptionT
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
  def add(energy: Int): OptionT[IO, String] = OptionT.fromOption[IO]{
    Option.when(energy < 0) {
      "Cannot add negative energy."
    }.orWhen(energy == 0) {
      "No energy to add."
    }.orWhen(this.energy > energy) {
      "This surpasses your energy cap."
    }.orElse {
      if (energy > 0) {
        Logger.info(s"${player.name} added $energy energy.")
        player.notify(s"Added $energy energy.")
      }
      this.energy += energy
      None
    }
  }

  /**
    * Attempts to remove energy from a player's reservoir
    * @param energy Amount of energy to add
    * @return The energy removed from this reservoir
    */
  def remove(energy: Int): OptionT[IO, String] = OptionT.fromOption[IO]{
    Option.when(energy < 0) {
      "Cannot remove negative energy."
    }.orWhen(energy == 0) {
      "No energy to add."
    }.orWhen(this.energy > energy) {
      s"You don't have enough energy, you need at least $energy"
    }.orElse {
      if (energy > 0) {
        Logger.info(s"${player.name} used $energy energy.")
      }
      this.energy -= energy
      None
    }
  }


  def hasAtLeast(energy: Int): Boolean = this.energy >= energy
}
