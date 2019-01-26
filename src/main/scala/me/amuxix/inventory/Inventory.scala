package me.amuxix.inventory

import cats.data.OptionT
import cats.effect.IO
import cats.implicits._
import me.amuxix.Consumable


trait Inventory extends Consumable {
  def isFull: Boolean

  def isEmpty: Boolean = contents.isEmpty

  def contents: Seq[Item]

  /**
    * Replaces the contents of the given inventory with the contents of this one.
    * @param inventory Inventory to replace the contents of
    */
  def replaceContentsOf(inventory: Inventory): Unit

  def contains(item: Item): Boolean = contents.contains(item)

  /**
    * Adds an item to the inventory if it can fit there.
    * @param item Item to be added
    * @return true if inventory had space to fit the item
    */
  def add(item: Item): Option[String]

  /**
    * Removes all items.
    */
  def clear(): Unit

  /**
    * @return The sum of the energy values of all items in this Inventory
    */
  def energy: Int = contents.foldLeft(0){
    case (acc, item) => acc + item.energy.getOrElse(0)
  }

  /**
    * Removes or replaces this from wherever it may be and returns its energy value
    *
    * @return The energy value
    */
  override def consume: OptionT[IO, Int] = Consumable.consume[List](contents.toList)
}
