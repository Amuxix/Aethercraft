package me.amuxix.inventory

import cats.data.{EitherT, OptionT}
import cats.effect.IO
import io.circe.{Decoder, Encoder}
import me.amuxix.material.Material
import me.amuxix.material.Properties.ItemProperty
import me.amuxix.runes.traits.enchants.Enchant
import me.amuxix.{Consumable, Energy}

trait Item extends Consumable {
  val material: Material with ItemProperty

  def amount: Int

  def setAmount(i: Int): IO[Unit]

  def destroyAll: IO[Unit]

  def destroy(amount: Int): IO[Unit]

  def addVanillaCurses(): EitherT[IO, String, Unit]

  def enchants: Set[Enchant]

  def isEnchanted: Boolean

  def hasRuneEnchant(enchant: Enchant): Boolean

  def addRuneEnchantWithState[State : Encoder](enchant: Enchant, enchantState: State): EitherT[IO, String, Unit]

  def addRuneEnchant(enchant: Enchant): EitherT[IO, String, Unit]

  def findEnchantState[State : Decoder](enchant: Enchant): Either[String, State]

  def disenchant: EitherT[IO, String, Unit]

  def name: String

  def hasDisplayName: Boolean

  def displayName: Option[String]

  def setDisplayName(name: String): EitherT[IO, String, Unit]

  /**
    * @return The value of this item or None if item has no energy
    */
  def energy: Option[Energy] = material.energy.map(_ * amount)

  /**
    * Consumes the item if it has Some energy (even 0), does nothing otherwise
    * @return The energy given to the player, 0 if item had None energy
    */
  /*override def consumeAtomically: Option[(Energy, OptionT[IO, String])] = energy.map { energy =>
    energy -> OptionT(destroyAll.map(_ => Option.empty[String]))
  }*/

  /**
    * Returns a List tuples of energy and an IO that consumes a consumable that gives that energy.
    */
  override def consume: List[(List[ConsumeIO], Option[ConsumeIO])] = material.energy.toList.map { energy =>
    val consumeItems = List.fill(amount)(energy -> OptionT(destroy(1).map(_ => Option.empty[String])))
    (consumeItems, None)
  }

    /*ListMap(material.energy.toList.flatMap { energy =>
    List.fill(amount)(energy -> OptionT(destroy(1).map(_ => Option.empty[String])))
  }: _*)*/
}
