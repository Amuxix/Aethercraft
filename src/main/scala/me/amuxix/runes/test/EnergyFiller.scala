package me.amuxix.runes.test

import cats.data.EitherT
import cats.effect.IO
import me.amuxix.inventory.Item
import me.amuxix.material.Material.{RedstoneBlock, RedstoneTorch}
import me.amuxix.pattern._
import me.amuxix.position.BlockPosition
import me.amuxix.runes.Rune
import me.amuxix.{Direction, Matrix4, Player}

object EnergyFiller extends RunePattern[EnergyFiller] {
  // format: off
  override val layers = List(
    ActivationLayer(
      NotInRune, RedstoneTorch, NotInRune,
      RedstoneTorch, RedstoneBlock, RedstoneTorch,
      NotInRune, RedstoneTorch, NotInRune
    ),
  )
  // format: on
}

case class EnergyFiller(
  center: BlockPosition,
  creator: Player,
  direction: Direction,
  rotation: Matrix4,
  pattern: Pattern
) extends Rune {
  /**
    * Should this rune use a true name if the activator is wearing one?
    */
  override protected[runes] val shouldUseTrueName: Boolean = false

  /**
    * Internal activate method that should contain all code to activate a rune.
    */
  override protected def onActivate(activationItem: Option[Item]): EitherT[IO, String, Boolean] =
    activator.addEnergy(activator.energyCap).map(_ => true)

}
