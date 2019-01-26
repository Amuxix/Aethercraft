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
  var blockBreakEnchants: Stream[Enchant with BlockBreakTrigger] = Stream.empty
  var blockPlaceEnchants: Stream[Enchant with BlockPlaceTrigger] = Stream.empty

  var blockInteractEnchants: Stream[Enchant with BlockInteractTrigger] = Stream.empty
  var blockDamageEnchants: Stream[Enchant with BlockDamageTrigger] = Stream.empty
  var airInteractEnchants: Stream[Enchant with AirInteractTrigger] = Stream.empty
  var airSwingEnchants: Stream[Enchant with AirSwingTrigger] = Stream.empty
}