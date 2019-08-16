package me.amuxix.inventory.items

import cats.data.EitherT
import cats.effect.IO
import me.amuxix.Player
import me.amuxix.inventory.Item

trait PlayerHead extends Item {

  def hasOwner: Boolean

  def owner: Option[Player]

  def setOwner(player: Player): EitherT[IO, String, Unit]

  def isTrueNameOf(player: Player): Boolean
}
