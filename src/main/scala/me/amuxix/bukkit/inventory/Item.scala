package me.amuxix.bukkit.inventory

import cats.data.{EitherT, OptionT}
import cats.effect.IO
import cats.implicits.catsStdInstancesForOption
import cats.implicits.toTraverseOps
import io.circe.{Decoder, Encoder}
import io.circe.parser.parse
import io.circe.syntax.EncoderOps
import me.amuxix.bukkit.BukkitForm
import me.amuxix.bukkit.Material.{BukkitMaterialOps, MaterialOps}
import me.amuxix.bukkit.inventory.items.PlayerHead
import me.amuxix.material.Material
import me.amuxix.material.Material.{PlayerHead => PlayerHeadMaterial}
import me.amuxix.runes.traits.enchants.Enchant
import me.amuxix.{Aetherizeable, inventory}
import me.amuxix.runes.traits.enchants.Enchant.{enchantPrefix, enchantStateSeparator}
import me.amuxix.material.Properties.ItemProperty
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

import scala.jdk.CollectionConverters._
import scala.collection.immutable.HashMap

/**
  * Created by Amuxix on 01/02/2017.
  */
/**
  * This represents an item that can be present in an inventory
  */
object Item {
  implicit class BukkitItemStackOps(item: ItemStack) extends Aetherizeable[Item] {
    def aetherize: Item = Item(item)
  }

  def apply(material: Material with ItemProperty): Item = new ItemStack(material.bukkitForm).aetherize
  def apply(material: Material with ItemProperty, amount: Int): Item = new ItemStack(material.bukkitForm, amount).aetherize

  private val materials: Map[Material with ItemProperty, ItemStack => Item] = HashMap(
    PlayerHeadMaterial -> (new PlayerHead(_))
  )

  /**
    * Creates a new Item depending on the material of the ItemStack
    * @param itemStack The Itemstack to wrap
    * @return An Item of a specific type if a type for the given material exists or a generic otherwise
    */
  def apply(itemStack: ItemStack): Item =
    materials.getOrElse(itemStack.getType.aetherize.asInstanceOf[Material with ItemProperty], new Item(_))(itemStack)

}

protected[bukkit] class Item protected(itemStack: ItemStack) extends inventory.Item with BukkitForm[ItemStack] {
  override val material: Material with ItemProperty = itemStack.getType.aetherize.asInstanceOf[Material with ItemProperty]

  override def amount: Int = itemStack.getAmount

  override def setAmount(i: Int): IO[Unit] = IO(itemStack.setAmount(i))

  def destroyAll: IO[Unit] = setAmount(0)

  override def destroy(amount: Int): IO[Unit] = setAmount(this.amount - amount)

  protected lazy val maybeMeta: Option[ItemMeta] = Option(itemStack.getItemMeta)

  protected def setMeta(meta: ItemMeta): IO[Unit] = IO(itemStack.setItemMeta(meta))

  private lazy val lore: List[String] =
    maybeMeta.flatMap { meta =>
      Option(meta.getLore).map(_.asScala)
    }.toList.flatten

  private def setLore(lore: List[String]): EitherT[IO, String, Unit] =
    OptionT(maybeMeta.traverse { meta =>
      meta.setLore(lore.asJava)
      setMeta(meta)
    }).toRight("Failed to set lore.")

  private def addToLore(string: String): EitherT[IO, String, Unit] = setLore(lore :+ string)

  override def addCurses(): EitherT[IO, String, Unit] =
    OptionT(maybeMeta.traverse { meta =>
      meta.addEnchant(Enchantment.BINDING_CURSE, 1, false)
      meta.addEnchant(Enchantment.VANISHING_CURSE, 1, false)
      setMeta(meta)
    }).toRight("Failed to add curses.")

  override def enchants: Set[Enchant] = Enchant.enchants.filter(hasRuneEnchant).toSet

  override def isEnchanted: Boolean = enchants.nonEmpty

  override def hasRuneEnchant(enchant: Enchant): Boolean = lore.exists(_.startsWith(enchantPrefix + enchant.name))

  private def addRuneEnchantWithLoreString(enchant: Enchant, loreString: String): EitherT[IO, String, Unit] = {
    val incompatibleEnchant = enchants.collectFirst {
      case existingEnchant if enchant.incompatibleEnchants.contains(existingEnchant) =>
        s"${existingEnchant.name} is incompatible with ${enchant.name}"
    }
    val enchantPresent = Option.when(enchants.contains(enchant))(s"$name already enchanted with ${enchant.name}")
    enchant.canEnchant(this)
      .orElse(incompatibleEnchant)
      .orElse(enchantPresent)
      .fold(addToLore(loreString))(EitherT.leftT(_))
  }

  override def addRuneEnchant(enchant: Enchant): EitherT[IO, String, Unit] = addRuneEnchantWithLoreString(enchant, enchantPrefix + enchant.name)

  override def addRuneEnchantWithState[State: Encoder](enchant: Enchant, enchantState: State): EitherT[IO, String, Unit] =
    addRuneEnchantWithLoreString(enchant, enchantPrefix + enchant.name + enchantStateSeparator + enchantState.asJson.noSpaces)

  override def findEnchantState[State : Decoder](enchant: Enchant): Either[String, State] =
    for {
      enchantString <- lore.find(_.startsWith(enchantPrefix + enchant.name)).toRight("Enchant not found.")
      stateString <- enchantString.split(enchantStateSeparator).lift(1).toRight("State not found.")
      stateJson <- parse(stateString).left.map(_ => "Error")
      state <- stateJson.as[State].left.map(_.message)
    } yield state

  override def disenchant: EitherT[IO, String, Unit] = setLore(lore.filterNot(_.startsWith(enchantPrefix)))

  override def name: String = displayName.filter(_ => hasDisplayName).getOrElse(material.name)

  override def hasDisplayName: Boolean = maybeMeta.exists(_.hasDisplayName)

  override def displayName: Option[String] = maybeMeta.map(_.getDisplayName)

  override def setDisplayName(name: String): EitherT[IO, String, Unit] =
    OptionT(maybeMeta.traverse { meta =>
      meta.setDisplayName(name)
      setMeta(meta)
    }).toRight("Failed to set display name.")

  override def bukkitForm: ItemStack = itemStack
}
