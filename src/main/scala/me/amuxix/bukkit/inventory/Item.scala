package me.amuxix.bukkit.inventory

import me.amuxix.bukkit.BukkitForm
import me.amuxix.bukkit.Material.{BukkitMaterialOps, MaterialOps}
import me.amuxix.bukkit.inventory.items.PlayerHead
import me.amuxix.material.Material
import me.amuxix.material.Material.{PlayerHead => PlayerHeadMaterial}
import me.amuxix.runes.traits.enchants.Enchant
import me.amuxix.{Aetherizeable, inventory}
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

}

protected[bukkit] class Item protected(itemStack: ItemStack) extends inventory.Item with BukkitForm[ItemStack] {
  override val material: Material = itemStack.getType.aetherize

  override def amount: Int = itemStack.getAmount

  override def amount_=(i: Int): Unit = itemStack.setAmount(i)

  def destroy(): Unit = itemStack.setAmount(0)

  protected def meta: Option[ItemMeta] = Option(itemStack.getItemMeta)

  protected def meta_=(meta: ItemMeta): Unit = itemStack.setItemMeta(meta)

  private def lore: Option[Seq[String]] =
    for {
      m <- meta
      lore <- Option(m.getLore)
    } yield lore.asScala

  private def lore_=(lore: Seq[String]): Unit =
    meta = {
      meta.map { newMeta =>
        newMeta.setLore(lore.asJava)
        newMeta
      }.orNull
    }

  private def addToLore(string: String): Unit = lore = lore.toSeq.flatMap(_ :+ string)

  private def loreContains(string: String): Boolean =
    lore.exists(_.contains(string))

  override def addRuneEnchant(enchant: Enchant): Option[String] =
    enchant.canEnchant(this).toRight(addToLore(enchant.name + "§k§r")).toOption

  override def hasRuneEnchant(enchant: Enchant): Boolean = loreContains(enchant.name + "§k§r")

  override def hasDisplayName: Boolean = meta.exists(_.hasDisplayName)

  override def displayName: Option[String] = meta.map(_.getDisplayName)

  override def displayName_=(name: String): Unit =
    meta = {
      meta.map { newMeta =>
        newMeta.setDisplayName(name)
        newMeta
      }.orNull
    }

  override def bukkitForm: ItemStack = itemStack
}
