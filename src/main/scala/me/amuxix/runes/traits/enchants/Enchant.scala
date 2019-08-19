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
  def incompatibleEnchants: Set[Enchant] = Set.empty

  /** The validation to check if the item can be enchanted. */
  def itemValidation(item: Item): Boolean

  /** The description of possible items that can be enchanted by this rune to be given as error if it fails.*/
  val itemDescription: String

  /**
    * Check if the given item can be enchanted
    * @param item Item to check
    * @return None if it can be enchanted, otherwise a Some with an error message
    */
  def canEnchant(item: Item): Option[String] = Option.unless(itemValidation(item))(s"This rune can only be applied to $itemDescription.")

  Enchant.enchants :+= this
}

object Enchant {
  var enchants: List[Enchant] = List.empty

  var blockBreakEnchants: List[Enchant with BlockBreakTrigger] = List.empty
  var blockPlaceEnchants: List[Enchant with BlockPlaceTrigger] = List.empty

  var blockInteractEnchants: List[Enchant with BlockInteractTrigger] = List.empty
  var blockDamageEnchants: List[Enchant with BlockDamageTrigger] = List.empty
  var airInteractEnchants: List[Enchant with AirInteractTrigger] = List.empty
  var airSwingEnchants: List[Enchant with AirSwingTrigger] = List.empty

  val enchantPrefix = "§k§r§5§o"
  val enchantStateSeparator = " §k"
}