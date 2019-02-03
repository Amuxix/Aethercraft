package me.amuxix.inventory.items

import cats.effect.IO
import me.amuxix.Player
import me.amuxix.inventory.Item

trait PlayerHead extends Item {

  def hasOwner: Boolean

  def owner: Option[Player]

  def setOwner(player: Player): IO[Unit]

  def isTrueNameOf(player: Player): Boolean
}
