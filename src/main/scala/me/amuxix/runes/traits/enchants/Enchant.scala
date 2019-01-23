package me.amuxix.runes.traits.enchants

import me.amuxix.Named
import me.amuxix.inventory.Item

/**
  * Created by Amuxix on 09/02/2017.
  */

/**
  * This is used by the companion object of runes that add enchants to items
  */
trait Enchant extends Named {
  /**
    * Check if the given item can be enchanted
    * @param item Item to check
    * @return None if it can be enchanted, otherwise a Some with an error message
    */
  def canEnchant(item: Item): Option[String]
}

object Enchant {
  var blockBreakEnchants = Seq.empty[Enchant with BlockBreakTrigger]
  var blockPlaceEnchants = Seq.empty[Enchant with BlockPlaceTrigger]

  var blockInteractEnchants = Seq.empty[Enchant with BlockInteractTrigger]
  var blockDamageEnchants = Seq.empty[Enchant with BlockDamageTrigger]
  var airInteractEnchants = Seq.empty[Enchant with AirInteractTrigger]
  var airSwingEnchants = Seq.empty[Enchant with AirSwingTrigger]
}