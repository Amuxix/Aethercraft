package me.amuxix.inventory

import me.amuxix.material.Material
import me.amuxix.runes.traits.enchants.Enchant

trait Item {
  val material: Material

  def amount: Int

  def amount_=(i: Int): Unit

  def destroy(): Unit

  def hasRuneEnchant(enchant: Enchant): Boolean

  def addRuneEnchant(enchant: Enchant): Boolean

  def hasDisplayName: Boolean

  def displayName: Option[String]

  def displayName_=(name: String): Unit
}
