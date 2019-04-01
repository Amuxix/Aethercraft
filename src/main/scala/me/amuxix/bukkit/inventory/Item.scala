package me.amuxix.bukkit.inventory

import cats.data.OptionT
import cats.effect.IO
import me.amuxix.bukkit.BukkitForm
import me.amuxix.bukkit.Material.{BukkitMaterialOps, MaterialOps}
import me.amuxix.bukkit.inventory.items.PlayerHead
import me.amuxix.material.Material
import me.amuxix.material.Material.{PlayerHead => PlayerHeadMaterial}
import me.amuxix.runes.traits.enchants.Enchant
import me.amuxix.{Aetherizeable, inventory}
import me.amuxix.bukkit.inventory.Item.enchantNameSuffix
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

import scala.collection.JavaConverters._
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

  def apply(material: Material): Item = new ItemStack(material.bukkitForm).aetherize
  def apply(material: Material, amount: Int): Item = new ItemStack(material.bukkitForm, amount).aetherize

  private val materials: Map[Material, ItemStack => Item] = HashMap(
    PlayerHeadMaterial -> (new PlayerHead(_))
  )

  /**
    * Creates a new Item depending on the material of the ItemStack
    * @param itemStack The Itemstack to wrap
    * @return An Item of a specific type if a type for the given material exists or a generic otherwise
    */
  def apply(itemStack: ItemStack): Item =
    materials.getOrElse(itemStack.getType.aetherize, new Item(_))(itemStack)

  val enchantNameSuffix = "§k§r"

}

protected[bukkit] class Item protected(itemStack: ItemStack) extends inventory.Item with BukkitForm[ItemStack] {
  override val material: Material = itemStack.getType.aetherize

  override def amount: Int = itemStack.getAmount

  override def setAmount(i: Int): IO[Unit] = IO(itemStack.setAmount(i))

  def destroyAll: IO[Unit] = setAmount(0)

  override def destroy(amount: Int): IO[Unit] = setAmount(this.amount - amount)

  protected lazy val meta: ItemMeta = itemStack.getItemMeta

  protected def setMeta(meta: ItemMeta): IO[Unit] = IO(itemStack.setItemMeta(meta))

  private lazy val lore: Option[List[String]] = Option(meta.getLore).map(_.asScala.toList)

  private def setLore(lore: List[String]): IO[Unit] = {
    meta.setLore(lore.asJava)
    setMeta(meta)
  }

  private def addToLore(string: String): IO[Unit] = setLore(lore.fold(List(string))(_ :+ string))

  private def loreContains(string: String): Boolean = lore.exists(_.contains(string))

  override def enchants: Set[Enchant] = Enchant.enchants.filter(hasRuneEnchant).toSet

  override def hasRuneEnchant(enchant: Enchant): Boolean = loreContains(enchant.name + enchantNameSuffix)

  override def addRuneEnchant(enchant: Enchant): OptionT[IO, String] = {
    val incompatibleEnchant = enchants.collectFirst {
      case existingEnchant if enchant.incompatibleEnchants.contains(existingEnchant) =>
        s"${existingEnchant.name} is incompatible with ${enchant.name}"
    }
    enchant.canEnchant(this)
      .orElse(incompatibleEnchant)
      .fold {
        OptionT(addToLore(enchant.name + enchantNameSuffix).map(_ => Option.empty[String]))
      }(OptionT.pure(_))
  }

  override def hasDisplayName: Boolean = meta.hasDisplayName

  override def displayName: String = meta.getDisplayName

  override def setDisplayName(name: String): IO[Unit] = {
    meta.setDisplayName(name)
    setMeta(meta)
  }

  override def bukkitForm: ItemStack = itemStack
}
