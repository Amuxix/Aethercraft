package me.amuxix.inventory

import cats.data.OptionT
import cats.effect.IO
import me.amuxix.{Consumable, Energy}


trait Inventory extends Consumable {
  def isFull: Boolean

  def isEmpty: Boolean = contents.isEmpty

  def contents: List[Item]

  /**
    * Replaces the contents of the given inventory with the contents of this one.
    * @param inventory Inventory to replace the contents of
    */
  def replaceContentsOf(inventory: Inventory): IO[Unit]

  def contains(item: Item): Boolean = contents.contains(item)

  /**
    * Adds an item to the inventory if it can fit there.
    * @param item Item to be added
    * @return true if inventory had space to fit the item
    */
  def add(item: Item): OptionT[IO, String]

  /**
    * Removes all items.
    */
  def clear(): IO[Unit]

  /**
    * @return The sum of the energy values of all items in this Inventory
    */
  def energy: Energy = contents.to(LazyList).flatMap(_.energy).sum

  /**
    * Removes or replaces this from wherever it may be and returns its energy value
    *
    * @return The energy value
    */
  //override def consumeAtomically: Option[(Energy, OptionT[IO, String])] = Consumable.consumeAtomically[List](contents)

  /**
    * Returns a List of IO to consume each part individually, this is consumed from left to right
    */
  override def consume: List[(List[ConsumeIO], Option[ConsumeIO])] = contents.flatMap(_.consume)
}
