package me.amuxix.block.blocks

import me.amuxix.inventory.{Inventory, Item}

trait Chest extends Inventory {

  def inventory: Inventory

  override def isFull: Boolean = inventory.isFull

  override def contents: Seq[Item] = inventory.contents

  override def moveContentsTo(inventory: Inventory): Unit = this.inventory.moveContentsTo(inventory)

  /**
    * Adds an item to the inventory if it can fit there.
    *
    * @param item Item to be added
    *
    * @return true if inventory had space to fit the item
    */
  override def add(item: Item): Boolean = inventory.add(item)

  /**
    * Clears the whole inventory
    */
  override def clear(): Unit = inventory.clear()
}
