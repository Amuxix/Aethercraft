package me.amuxix.inventory

import me.amuxix.inventory.items.PlayerHead
import me.amuxix.material.Material
import me.amuxix.material.Material.{materialData2Material, PlayerHead => PlayerHeadMaterial}
import me.amuxix.runes.traits.Enchant

import scala.collection.JavaConverters._

/**
  * Created by Amuxix on 01/02/2017.
  */
/**
  * This represents an item that can be present in an inventory
  */
object Item {
  implicit def itemStack2Item(itemStack: ItemStack): Item = apply(itemStack)
  implicit def item2ItemStack(item: Item): ItemStack = item.itemStack
  def apply(material: Material): Item = apply(material, 1)
  def apply(material: Material, amount: Int): Item = apply(new ItemStack(material.getItemType, amount))
  def apply(itemStack: ItemStack): Item = {
    materialData2Material(itemStack.getData) match {
      case PlayerHeadMaterial => new PlayerHead(itemStack)
      case _ => new Item(itemStack)
    }
  }
}
class Item protected[inventory] (protected[inventory] val itemStack: ItemStack) {
  val material: Material = itemStack.getData

  def amount: Int = itemStack.getAmount

  def amount_=(i: Int): Unit = itemStack.setAmount(i)

  def durabilityMissing: Int = itemStack.getDurability

  def meta: ItemMeta = itemStack.getItemMeta

  def meta_=(meta: ItemMeta): Unit = itemStack.setItemMeta(meta)

  def lore: Option[Seq[String]] = Option(meta.getLore).map(_.asScala)

  def lore_=(lore: Seq[String]): Unit = {
    meta = {
      val newMeta = meta
      newMeta.setLore(lore.asJava)
      newMeta
    }
  }

  def addToLore(string: String): Unit = {
    lore = lore match {
      case None => Seq(string)
      case Some(l) => l :+ string
    }
  }

  def loreContains(string: String): Boolean = {
    lore match {
      case None => false
      case Some(l) => l.contains(string)
    }
  }

  def addRuneEnchant(enchant: Enchant): Unit = addToLore(enchant.name + "§k§r")

  def hasRuneEnchant(enchant: Enchant): Boolean = {
    loreContains(enchant.name + "§k§r")
  }

  def hasDisplayName: Boolean = meta.hasDisplayName

  def displayName: String = meta.getDisplayName

  def displayName_=(name: String): Unit = {
    meta = {
      val newMeta = meta
      newMeta.setDisplayName(name)
      newMeta
    }
  }
}
