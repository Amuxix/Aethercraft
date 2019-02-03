package me.amuxix.block.blocks

import cats.data.OptionT
import cats.effect.IO
import me.amuxix.inventory.{Inventory, Item}

trait Chest extends Inventory {

  def inventory: Inventory

  override def isFull: Boolean = inventory.isFull

  override def contents: List[Item] = inventory.contents

  override def replaceContentsOf(inventory: Inventory): IO[Unit] = this.inventory.replaceContentsOf(inventory)

  /**
    * Adds an item to the inventory if it can fit there.
    *
    * @param item Item to be added
    *
    * @return true if inventory had space to fit the item
    */
  override def add(item: Item): OptionT[IO, String] = inventory.add(item)

  /**
    * Clears the whole inventory
    */
  override def clear(): IO[Unit] = inventory.clear()
}
