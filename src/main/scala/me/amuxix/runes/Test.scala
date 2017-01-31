package me.amuxix.runes

import me.amuxix.material.Material.{EndStone, Glass}
import me.amuxix.pattern._
import me.amuxix.runes.traits.{Consumable, Linkable, Tiered}

/**
  * Created by Amuxix on 26/11/2016.
  */
/**
  * Test rune, does nothing pattern might chance
  */

object Test extends RunePattern {
  val pattern: Pattern = Pattern(Test.apply, width = 3, numberOfMirroredAxis = false, verticality = true)(
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

case class Test(parameters: RuneParameters, pattern: Pattern)
  extends Rune(parameters)
          with Tiered
          with Consumable
          with Linkable {
  /**
    * Checks whether the signature is valid for this rune and notifies player if it is not and why
    *
    * @param player player to be notified in case of signature being invalid
    * @return true if signature is valid, false otherwise
    */
  override def validateSignature(): Boolean = true

  override protected def innerActivate(): Unit = Unit
}