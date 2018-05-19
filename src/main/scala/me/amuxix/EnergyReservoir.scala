package me.amuxix

import scala.collection.mutable

object EnergyReservoir {
  val energyReservoirs = mutable.Map.empty[Player, EnergyReservoir]
}

class EnergyReservoir(player: Player, var energy: Int = 0) {
  EnergyReservoir.energyReservoirs += player -> this
}
