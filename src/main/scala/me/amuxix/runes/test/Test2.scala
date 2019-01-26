package me.amuxix.runes.test

import cats.data.EitherT
import cats.effect.IO
import me.amuxix.block.Block.Location
import me.amuxix.inventory.Item
import me.amuxix.material.Material.{EndStone, Glass}
import me.amuxix.pattern._
import me.amuxix.runes.Rune
import me.amuxix.runes.traits.ConsumableBlocks
import me.amuxix.{Direction, Matrix4, Player}

/**
  * Created by Amuxix on 01/12/2016.
  */
object Test2 extends RunePattern {
  val pattern: Pattern = Pattern(Test2.apply, activatesWith = { case Some(item) if item.material.isSword => true })(
    ActivationLayer(
      Glass, NotInRune, EndStone, NotInRune, Glass,
      NotInRune, Glass, NotInRune, Glass, NotInRune,
      Glass, NotInRune, Glass, NotInRune, Glass,
      NotInRune, Glass, NotInRune, Glass, NotInRune,
      Glass, NotInRune, Glass, NotInRune, Glass
    )
  )
}

case class Test2(center: Location, creator: Player, direction: Direction, rotation: Matrix4, pattern: Pattern) extends Rune with ConsumableBlocks {
  override protected def onActivate(activationItem: Option[Item]): EitherT[IO, String, Boolean] = EitherT.rightT(true)

  /**
    * Should this rune use a true name if the activator is wearing one?
    */
  override val shouldUseTrueName: Boolean = true
}
