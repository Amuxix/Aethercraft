package me.amuxix.runes.test

import me.amuxix.block.Block
import me.amuxix.block.Block.Location
import me.amuxix.inventory.Item
import me.amuxix.material.Material.{EndStone, Glass}
import me.amuxix.pattern._
import me.amuxix.runes.Rune
import me.amuxix.runes.traits.Consumable
import me.amuxix.{Direction, Player}

/**
  * Created by Amuxix on 01/12/2016.
  */
object Test2 extends RunePattern {
  val pattern: Pattern = Pattern(Test2.apply, activatesWith = { case material if material.isSword => true })(
    ActivationLayer(
      Glass, NotInRune, EndStone, NotInRune, Glass,
      NotInRune, Glass, NotInRune, Glass, NotInRune,
      Glass, NotInRune, Glass, NotInRune, Glass,
      NotInRune, Glass, NotInRune, Glass, NotInRune,
      Glass, NotInRune, Glass, NotInRune, Glass
    )
  )
}

case class Test2(blocks: Array[Array[Array[Block]]], center: Location, creator: Player, direction: Direction, pattern: Pattern) extends Rune with Consumable {
  override protected def onActivate(activationItem: Item): Boolean = true

  /**
    * Should this rune use a true name if the activator is wearing one?
    */
  override val shouldUseTrueName: Boolean = true
}
