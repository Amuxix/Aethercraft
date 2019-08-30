package me.amuxix.runes.traits.enchants

import io.circe.{KeyDecoder, KeyEncoder}
import me.amuxix.Named
import me.amuxix.inventory.Item

import scala.collection.immutable.HashMap

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
/*  implicit val encoder: Encoder[Enchant] = Encoder.forProduct1("name")(enchant => enchant.name)
  implicit val decoder: Decoder[Enchant] = Decoder.forProduct1[Enchant, String]("name")(enchantName =>
    enchantNameToEnchantMap.getOrDefault(enchantName, throw new Exception(s"Invalid enchant $enchantName"))
  )*/

  implicit val keyEncoder: KeyEncoder[Enchant] = new KeyEncoder[Enchant] {
    override def apply(key: Enchant): String = key.name
  }

  implicit val keyDecoder: KeyDecoder[Enchant] = new KeyDecoder[Enchant] {
    override def apply(key: String): Option[Enchant] = enchantNameToEnchantMap.get(key)
  }

  var enchants: List[Enchant] = List.empty

  lazy val enchantNameToEnchantMap: Map[String, Enchant] = enchants.map( enchant =>
    enchant.name -> enchant
  ).to(HashMap)

  var blockBreakEnchants: List[Enchant with BlockBreakTrigger] = List.empty
  var blockPlaceEnchants: List[Enchant with BlockPlaceTrigger] = List.empty

  var blockInteractEnchants: List[Enchant with BlockInteractTrigger] = List.empty
  var blockDamageEnchants: List[Enchant with BlockDamageTrigger] = List.empty
  var airInteractEnchants: List[Enchant with AirInteractTrigger] = List.empty
  var airSwingEnchants: List[Enchant with AirSwingTrigger] = List.empty

  val enchantPrefix = "§k§r§5§o"
  val enchantStateSeparator = " §k"
}