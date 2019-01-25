package me.amuxix.runes.test

import me.amuxix.block.Block
import me.amuxix.block.Block.Location
import me.amuxix.{Direction, Matrix4, Player}
import me.amuxix.inventory.Item
import me.amuxix.material.Material.{EndStone, Glass}
import me.amuxix.pattern._
import me.amuxix.runes.traits._
import me.amuxix.runes.Rune

/**
  * Created by Amuxix on 26/11/2016.
  */
/**
  * Test rune, does nothing pattern might chance
  */

object Test extends RunePattern {
  val pattern: Pattern = Pattern(Test.apply, verticality = true)(
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
}

case class Test(center: Location, creator: Player, direction: Direction, rotation: Matrix4, pattern: Pattern) extends Rune with Tiered with Consumable with Linkable {
  /**
    * Checks whether the signature is valid for this rune and notifies player if it is not and why
    *
    * @return true if signature is valid, false otherwise
    */
  override def validateSignature: Option[String] = None

  override protected def onActivate(activationItem: Option[Item]): Either[String, Boolean] = Right(true)

  /**
    * Should this rune use a true name if the activator is wearing one?
    */
  override val shouldUseTrueName: Boolean = false
}