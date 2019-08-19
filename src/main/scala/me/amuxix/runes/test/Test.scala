package me.amuxix.runes.test

import cats.data.EitherT
import cats.effect.IO
import me.amuxix.inventory.Item
import me.amuxix.material.Material.{EndStone, Glass}
import me.amuxix.pattern._
import me.amuxix.position.BlockPosition
import me.amuxix.runes.Rune
import me.amuxix.runes.traits._
import me.amuxix.{Direction, Matrix4, Player}

/**
  * Created by Amuxix on 26/11/2016.
  */
/**
  * Test rune, does nothing pattern might chance
  */

object Test extends RunePattern[Test] {
  override val castableVertically = true
  // format: off
  override val layers = List(
    ActivationLayer(
      EndStone, NotInRune, EndStone,
      NotInRune, EndStone, NotInRune,
      EndStone, NotInRune, EndStone
    ), Layer(
      Tier, Signature, Tier,
      Glass, Glass, Glass,
      Glass, Glass, Glass
    )
  )
  // format: on
}

case class Test(
  center: BlockPosition,
  creator: Player,
  direction: Direction,
  rotation: Matrix4,
  pattern: Pattern
) extends Rune
    with LinkableTiered
    with ConsumableBlocks {

  override protected def onActivate(activationItem: Option[Item]): EitherT[IO, String, Boolean] = EitherT.rightT(true)

  /**
    * Should this rune use a true name if the activator is wearing one?
    */
  override val shouldUseTrueName: Boolean = false
}
