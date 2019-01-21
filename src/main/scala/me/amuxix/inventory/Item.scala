package me.amuxix.inventory

import me.amuxix.Player
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

  /**
    * @return The value of this item or None if item has no energy
    */
  def energy: Option[Int] = material.energy.map(_ * amount)

  /**
    * Consumes the item if it has Some energy (even 0), does nothing otherwise
    * @param player Player to give the energy to
    * @return The energy given to the player, 0 if item had None energy
    */
  def consume(player: Player): Int = energy.fold(0) { energy =>
    player.addEnergy(energy)
    destroy()
    energy
  }
}
