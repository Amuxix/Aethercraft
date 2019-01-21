package me.amuxix.bukkit.inventory

import me.amuxix.{Aetherizeable, inventory}
import org.bukkit.inventory.{Inventory => BukkitInventory}
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

  override def contents: Seq[Item] = inv.getContents.toSeq.map(Item(_))

  /**
    * Clears the whole inventory
    */
  override def clear(): Unit = inv.clear()

  //TODO: Have this check if item can fill existing stacks even if inventory is full
  /**
    * Adds an item to the inventory if it can fit there.
    *
    * @param item Item to be added
    *
    * @return true if inventory had space to fit the item
    */
  override def add(item: inventory.Item): Boolean =
    if (isFull) {
      false
    } else {
      inv.addItem(item.asInstanceOf[Item].bukkitForm)
      true
    }
}
