package me.amuxix.bukkit.inventory

import cats.data.{EitherT, OptionT}
import cats.effect.IO
import cats.implicits.catsStdInstancesForOption
import cats.implicits.toTraverseOps
import me.amuxix.bukkit.BukkitForm
import me.amuxix.bukkit.Material.{BukkitMaterialOps, MaterialOps}
import me.amuxix.bukkit.inventory.items.PlayerHead
import me.amuxix.material.Material
import me.amuxix.material.Material.{PlayerHead => PlayerHeadMaterial}
import me.amuxix.runes.traits.enchants.Enchant
import me.amuxix.{Aetherizeable, inventory}
import me.amuxix.bukkit.inventory.Item.enchantNameSuffix
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

  val enchantNameSuffix = "§k§r"

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

  private def loreContains(string: String): Boolean = lore.exists(_.contains(string))

  override def addCurses(): EitherT[IO, String, Unit] =
    OptionT(maybeMeta.traverse { meta =>
      meta.addEnchant(Enchantment.BINDING_CURSE, 1, false)
      meta.addEnchant(Enchantment.VANISHING_CURSE, 1, false)
      setMeta(meta)
    }).toRight("Failed to add curses.")

  override def enchants: Set[Enchant] = Enchant.enchants.filter(hasRuneEnchant).toSet

  override def hasRuneEnchant(enchant: Enchant): Boolean = loreContains(enchant.name + enchantNameSuffix)

  override def addRuneEnchant(enchant: Enchant): EitherT[IO, String, Unit] = {
    val incompatibleEnchant = enchants.collectFirst {
      case existingEnchant if enchant.incompatibleEnchants.contains(existingEnchant) =>
        s"${existingEnchant.name} is incompatible with ${enchant.name}"
    }
    enchant.canEnchant(this)
      .orElse(incompatibleEnchant)
      .fold(addToLore(enchant.name + enchantNameSuffix))(EitherT.leftT(_))
  }

  override def hasDisplayName: Boolean = maybeMeta.exists(_.hasDisplayName)

  override def displayName: Option[String] = maybeMeta.map(_.getDisplayName)

  override def setDisplayName(name: String): EitherT[IO, String, Unit] =
    OptionT(maybeMeta.traverse { meta =>
      meta.setDisplayName(name)
      setMeta(meta)
    }).toRight("Failed to set display name.")

  override def bukkitForm: ItemStack = itemStack
}
