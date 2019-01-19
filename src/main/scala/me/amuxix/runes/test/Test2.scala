package me.amuxix.runes.test

import me.amuxix.inventory.Item
import me.amuxix.material.Material.{EndStone, Glass}
import me.amuxix.material.Sword
import me.amuxix.pattern._
import me.amuxix.runes.traits.Consumable
import me.amuxix.runes.{Parameters, Rune}

/**
  * Created by Amuxix on 01/12/2016.
  */
object Test2 extends RunePattern {
  /*if (ficheiroDeRunasActivas tem this.name) {
    Matcher.runes += this
  }*/
  val pattern: Pattern = Pattern(Test2.apply, activatesWith = { case _: Sword => true })(
    ActivationLayer(
      Glass, NotInRune, EndStone, NotInRune, Glass,
      NotInRune, Glass, NotInRune, Glass, NotInRune,
      Glass, NotInRune, Glass, NotInRune, Glass,
      NotInRune, Glass, NotInRune, Glass, NotInRune,
      Glass, NotInRune, Glass, NotInRune, Glass
    )
  )
}

case class Test2(parameters: Parameters, pattern: Pattern) extends Rune with Consumable {
  override protected def onActivate(activationItem: Item): Unit = ()

  /**
    * Should this rune use a true name if the activator is wearing one?
    */
  override val shouldUseTrueName: Boolean = true
}
