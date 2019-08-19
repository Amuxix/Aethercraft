package me.amuxix.runes.traits.enchants

import cats.data.EitherT
import cats.effect.IO
import me.amuxix.inventory.Item
import me.amuxix.runes.Rune

trait RuneEnchant { this: Rune =>
  val enchant: Enchant

  def addRuneEnchant(item: Item): EitherT[IO, String, Unit] = item.addRuneEnchant(enchant)

  override protected def onActivate(activationItem: Option[Item]): EitherT[IO, String, Boolean] = {
    activationMessage = activationItem.fold(activationMessage)(_.name + s" has been enchanted with ${this.name}")

    //Enchants should also cancel the event to prevent the enchant triggering on the same use event as enchants get picked up after rune activation
    EitherT.fromOption[IO](activationItem, "No item to enchant.").flatMap(addRuneEnchant).map(_ => true)
  }

  override def validateActivationItem(activationItem: Option[Item]): Option[String] =
    activationItem match {
      case Some(item) if enchant.itemValidation(item) => None
      case _ => Some(s"This rune must be activated with ${enchant.itemDescription}")
    }

  override val shouldUseTrueName: Boolean = true
}
