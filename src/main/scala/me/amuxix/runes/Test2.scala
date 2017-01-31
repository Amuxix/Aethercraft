package me.amuxix.runes

import me.amuxix.material.Material.{EndStone, Glass}
import me.amuxix.pattern._
import me.amuxix.runes.traits.Consumable

/**
  * Created by Amuxix on 01/12/2016.
  */
object Test2 extends RunePattern {
  val pattern: Pattern = Pattern(Test2.apply, width = 5, numberOfMirroredAxis = false)(
    ActivationLayer(
      Glass, NotInRune, EndStone, NotInRune, Glass,
      NotInRune, Glass, NotInRune, Glass, NotInRune,
      Glass, NotInRune, Glass, NotInRune, Glass,
      NotInRune, Glass, NotInRune, Glass, NotInRune,
      Glass, NotInRune, Glass, NotInRune, Glass
    )
  )
}

case class Test2(parameters: RuneParameters, pattern: Pattern)
  extends Rune(parameters)
          with Consumable {
  override protected def innerActivate(): Unit = Unit
}
