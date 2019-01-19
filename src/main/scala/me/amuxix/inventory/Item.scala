package me.amuxix.inventory

import me.amuxix.inventory.items.PlayerHead
import me.amuxix.logging.Logger
import me.amuxix.material.Material
import me.amuxix.material.Material.{BukkitMaterialOps, PlayerHead => PlayerHeadMaterial}
import me.amuxix.runes.traits.enchants.Enchant
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

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
  def apply(material: Material, amount: Int): Item = apply(new ItemStack(material.toBukkitMaterial, amount))
  def apply(itemStack: ItemStack): Item = {
    itemStack.getType.toMaterial match {
      case PlayerHeadMaterial =>
        Logger.info("Creating head")
        new PlayerHead(itemStack)
      case _ => new Item(itemStack)
    }
  }
}
class Item protected[inventory] (protected[inventory] val itemStack: ItemStack) {
  val material: Material = itemStack.getType.toMaterial

  def amount: Int = itemStack.getAmount

  def amount_=(i: Int): Unit = itemStack.setAmount(i)

  def meta: Option[ItemMeta] = Option(itemStack.getItemMeta)

  def meta_=(meta: ItemMeta): Unit = itemStack.setItemMeta(meta)

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

  def addToLore(string: String): Unit = {
    lore = lore match {
      case None => Seq(string)
      case Some(lore) => lore :+ string
    }
  }

  def loreContains(string: String): Boolean =
    lore match {
      case None => false
      case Some(l) => l.contains(string)
    }

  def addRuneEnchant(enchant: Enchant): Boolean = if (enchant.canEnchant(material)) {
    addToLore(enchant.name + "§k§r")
    true
  } else {
    false
  }

  def hasRuneEnchant(enchant: Enchant): Boolean = {
    loreContains(enchant.name + "§k§r")
  }

  def hasDisplayName: Boolean = meta.exists(_.hasDisplayName)

  def displayName: Option[String] = meta.map(_.getDisplayName)

  def displayName_=(name: String): Unit =
    meta = {
      meta.map { newMeta =>
        newMeta.setDisplayName(name)
        newMeta
      }.orNull
    }
}
