package me.amuxix.inventory.meta

import me.amuxix.inventory.meta.VanillaEnchant.VanillaEnchant
import me.amuxix.runes.traits.enchants.Enchant

trait ItemMeta {

  def enchants: Map[Enchant, Int]

  def enchants_=(enchants: Map[Enchant, Int]): Unit


  def addVanillaCurses(): Unit

  def hasVanillaEnchants: Boolean

  def vanillaEnchants: Map[VanillaEnchant, Int]

  def addVanillaEnchant(enchant: VanillaEnchant, level: Int = 1): Unit


  def hasLocalizedName: Boolean

  def localizedName: String

  def localizedName_=(name: String): Unit


  def hasLore: Boolean

  def lore: List[String]

  def lore_=(lore: List[String]): Unit


  def hasCustomModelData: Boolean

  def customModelData: Int

  def customModelData_=(customModel: Int): Unit


/*  def itemFlags: Set[ItemFlag]

  def itemFlags_=(flags: Set[ItemFlag]): Unit*/


/*  def attributeModifiers: Map[Attribute, AttributeModifier]

  def attributeModifiers_=(modifiers: Map[Attribute, AttributeModifier]): Unit*/


  def isUnbreakable: Boolean

  def setUnbreakable(unbreakable: Boolean): Unit


  def hasDisplayName: Boolean

  def displayName: String

  def displayName_=(name: String): Unit
}
