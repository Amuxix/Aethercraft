package me.amuxix.inventory

trait PlayerInventory extends Inventory {
  def helmet: Option[Item]

  def itemInMainHand: Item

  def itemInOffHand: Item
}
