package me.amuxix.bukkit.inventory

import me.amuxix._
import org.bukkit.inventory.{Inventory => BukkitInventory}

import scala.collection.JavaConverters._
import scala.util.Try

/**
  * Created by Amuxix on 01/02/2017.
  */
object Inventory {
  implicit class BukkitInventoryOps(inventory: BukkitInventory) extends Aetherizeable[Inventory] {
    def aetherize: Inventory = Inventory(inventory)
  }
}
/**
  * A decorator to bukkit inventory
  * @param inv Bukkit Inventory
  */
private[bukkit] case class Inventory(inv: BukkitInventory) extends inventory.Inventory {
  def isFull: Boolean = inv.firstEmpty() == -1

  override def contents: Seq[Item] = inv.getContents.flatMap(stack => Option(stack).map(Item(_)))

  override def replaceContentsOf(inventory: me.amuxix.inventory.Inventory): Unit = {
    inventory.asInstanceOf[Inventory].inv.setContents(inv.getContents)
    clear()
  }

  /**
    * Clears the whole inventory
    */
  override def clear(): Unit = inv.clear()

  /**
    * Adds an item to the inventory if it can fit there.
    *
    * @param item Item to be added
    *
    * @return None if item was added, Some with error otherwise
    */
  override def add(item: inventory.Item): Option[String] =
    Try(item.asInstanceOf[Item]).toOption.flatMap { i =>
      inv.addItem(i.bukkitForm).asScala.headOption.map(_ => "Inventory is full")
    }
}
