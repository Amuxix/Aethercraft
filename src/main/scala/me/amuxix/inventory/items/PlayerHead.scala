package me.amuxix.inventory.items

import me.amuxix.Player
import me.amuxix.inventory.Item

trait PlayerHead extends Item {

  def hasOwner: Boolean

  def owner: Option[Player]

  def owner_=(player: Player): Unit

  def isTrueNameOf(player: Player): Boolean
}
