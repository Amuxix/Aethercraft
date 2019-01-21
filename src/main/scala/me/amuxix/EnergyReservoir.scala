package me.amuxix

import me.amuxix.logging.Logger

import scala.collection.mutable

object EnergyReservoir {
  private val energyReservoirs = mutable.Map.empty[Player, EnergyReservoir]

  def apply(player: Player): EnergyReservoir = energyReservoirs.getOrElseUpdate(player, new EnergyReservoir(player))
}

class EnergyReservoir private(player: Player, private var energy: Int = 0) {

  /**
    * Attempts to add energy to a player's reservoir, notifies the player of the amount added
    * @param energy Amount of energy to add
    * @return The energy added to this reservoir
    */
  def add(energy: Int): Int = {
    require(energy >= 0)
    if (energy > 0) {
      Logger.info(s"${player.name} added $energy energy.")
      player.notify(s"Added $energy energy.")
    }
    this.energy += energy
    energy
  }

  /**
    * Attempts to remove energy from a player's reservoir
    * @param energy Amount of energy to add
    * @return The energy removed from this reservoir
    */
  def remove(energy: Int): Int = {
    require(energy >= 0)
    if (energy > 0) {
      Logger.info(s"${player.name} used $energy energy.")
    }
    val removed = energy min this.energy
    this.energy -= energy
    removed
  }

  def hasAtLeast(energy: Int): Boolean = this.energy >= energy
}
