package me.amuxix.inventory

import cats.data.OptionT
import cats.effect.IO
import me.amuxix.Consumable
import me.amuxix.material.Material
import me.amuxix.runes.traits.enchants.Enchant

trait Item extends Consumable {
  val material: Material

  def amount: Int

  def amount_=(i: Int): Unit

  def destroy(): Unit

  def hasRuneEnchant(enchant: Enchant): Boolean

  def addRuneEnchant(enchant: Enchant): Option[String]

  def hasDisplayName: Boolean

  def displayName: Option[String]

  def displayName_=(name: String): Unit

  /**
    * @return The value of this item or None if item has no energy
    */
  def energy: Option[Int] = material.energy.map(_ * amount)

  /**
    * Consumes the item if it has Some energy (even 0), does nothing otherwise
    * @return The energy given to the player, 0 if item had None energy
    */
  override def consume: OptionT[IO, Int] = OptionT.fromOption {
    energy.map { energy =>
      destroy()
      energy
    }
  }
}
