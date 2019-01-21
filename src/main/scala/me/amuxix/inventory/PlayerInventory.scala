package me.amuxix.inventory

trait PlayerInventory extends Inventory {
  def helmet: Option[Item]

  def itemInMainHand: Option[Item]

  def itemInOffHand: Option[Item]
}
