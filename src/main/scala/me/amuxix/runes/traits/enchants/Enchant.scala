package me.amuxix.runes.traits.enchants

import me.amuxix.Named
import me.amuxix.material.Material

/**
  * Created by Amuxix on 09/02/2017.
  */

/**
  * This is used by the companion object of runes that add enchants to items
  */
trait Enchant extends Named {
  def canEnchant(material: Material): Boolean
}

object Enchant {
  var blockBreakEnchants = Seq.empty[Enchant with BlockBreakTrigger]
  var blockPlaceEnchants = Seq.empty[Enchant with BlockPlaceTrigger]

  var blockInteractEnchants = Seq.empty[Enchant with BlockInteractTrigger]
  var blockDamageEnchants = Seq.empty[Enchant with BlockDamageTrigger]
  var airInteractEnchants = Seq.empty[Enchant with AirInteractTrigger]
  var airSwingEnchants = Seq.empty[Enchant with AirSwingTrigger]
}