package me.amuxix.inventory

import org.bukkit.inventory.PlayerInventory

/**
  * Created by Amuxix on 01/02/2017.
  */
object Inventory {
  implicit def playerInventory2Inventory(inv: PlayerInventory): Inventory = Inventory(inv)
}
/**
  * A decorator to bukkit inventory
  * @param inv Bukkit Inventory
  */
case class Inventory(inv: PlayerInventory) {
  def isFull: Boolean = inv.firstEmpty() == -1

  def contains(item: Item): Boolean = inv.contains(item.itemStack)

  //TODO: Have this check if item can fill existing stacks even if inventory is full
  /**
    * Adds an item to the inventory if it can fit there.
    * @param item Item to be added
    * @return true if inventory had space to fit the item
    */
  def add(item: Item): Boolean = if (isFull) {
    false
  } else {
    inv.addItem(item.itemStack)
    true
  }
}
