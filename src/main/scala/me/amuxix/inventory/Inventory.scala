package me.amuxix.inventory

import me.amuxix.Player

trait Inventory {
  def isFull: Boolean

  def contents: Seq[Item]

  def moveContentsTo(inventory: Inventory): Unit

  def contains(item: Item): Boolean = contents.contains(item)

  //TODO: Have this check if item can fill existing stacks even if inventory is full
  /**
    * Adds an item to the inventory if it can fit there.
    * @param item Item to be added
    * @return true if inventory had space to fit the item
    */
  def add(item: Item): Boolean

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
    * Consumes all items with Some energy
    * @param player Player to give the energy to
    * @return The energy given to the player
    */
  def consumeContents(player: Player): Int = {
    val totalEnergy = contents.foldLeft(0) {
      case (acc, item) => acc + item.energy.fold(0) { energy =>
        item.destroy()
        energy
      }
    }
    player.addEnergy(totalEnergy)
  }
}
