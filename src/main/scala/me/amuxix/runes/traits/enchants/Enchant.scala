package me.amuxix.runes.traits.enchants

import me.amuxix.Named
import me.amuxix.inventory.Item

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

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

  def incompatibleEnchants: Set[Enchant]

  Enchant.enchants.append(this)
}

object Enchant {
  val enchants: ListBuffer[Enchant] = mutable.ListBuffer.empty[Enchant]

  val blockBreakEnchants: ListBuffer[Enchant with BlockBreakTrigger] = ListBuffer.empty
  val blockPlaceEnchants: ListBuffer[Enchant with BlockPlaceTrigger] = ListBuffer.empty

  val blockInteractEnchants: ListBuffer[Enchant with BlockInteractTrigger] = ListBuffer.empty
  val blockDamageEnchants: ListBuffer[Enchant with BlockDamageTrigger] = ListBuffer.empty
  val airInteractEnchants: ListBuffer[Enchant with AirInteractTrigger] = ListBuffer.empty
  val airSwingEnchants: ListBuffer[Enchant with AirSwingTrigger] = ListBuffer.empty
}