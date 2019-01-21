package me.amuxix.inventory


trait Inventory {
  def isFull: Boolean

  def contents: Seq[Item]

  def contains(item: Item): Boolean = contents.contains(item)

  //TODO: Have this check if item can fill existing stacks even if inventory is full
  /**
    * Adds an item to the inventory if it can fit there.
    * @param item Item to be added
    * @return true if inventory had space to fit the item
    */
  def add(item: Item): Boolean

  /**
    * Clears the whole inventory
    */
  def clear(): Unit
}
