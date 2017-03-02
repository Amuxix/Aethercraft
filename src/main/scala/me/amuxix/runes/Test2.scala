package me.amuxix.runes

import me.amuxix.inventory.Item
import me.amuxix.material.GenericSword
import me.amuxix.material.Material.{EndStone, Glass}
import me.amuxix.pattern._
import me.amuxix.runes.traits.Consumable

/**
  * Created by Amuxix on 01/12/2016.
  */
object Test2 extends RunePattern {
  /*if (ficheiroDeRunasActivas tem this.name) {
    Matcher.runes += this
  }*/
  val pattern: Pattern = Pattern(Test2.apply, activatesWith = { case _: GenericSword => true })(
    ActivationLayer(
      Glass, NotInRune, EndStone, NotInRune, Glass,
      NotInRune, Glass, NotInRune, Glass, NotInRune,
      Glass, NotInRune, Glass, NotInRune, Glass,
      NotInRune, Glass, NotInRune, Glass, NotInRune,
      Glass, NotInRune, Glass, NotInRune, Glass
    )
  )
}

case class Test2(parameters: Parameters, pattern: Pattern)
  extends Rune(parameters)
          with Consumable {
  override protected def innerActivate(activationItem: Item): Unit = Unit

  /**
    * Should this rune use a true name if the activator is wearing one?
    */
  override val shouldUseTrueName: Boolean = true
}
