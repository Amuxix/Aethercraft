package me.amuxix.runes

import me.amuxix.pattern._
import me.amuxix.runes.traits.{Consumable, Linkable, Tiered}
import me.amuxix.util.Block.Location
import me.amuxix.util.{Block, Matrix4, Player}
import org.bukkit.Material.ENDER_STONE

/**
  * Created by Amuxix on 26/11/2016.
  */
  /**
    * Test rune, does nothing pattern might chance
    */

object Test extends RunePattern {
    val pattern = Pattern(Test.apply, width = 3)(
      ActivationLayer(
        NotInRune, ENDER_STONE, NotInRune,
        ENDER_STONE, NotInRune, ENDER_STONE,
        NotInRune, ENDER_STONE, NotInRune
      ), Layer (
        Tier, Signature, Tier,
        ENDER_STONE, ENDER_STONE, ENDER_STONE,
        ENDER_STONE, ENDER_STONE, ENDER_STONE
      )
    )
}
case class Test(location: Location, activator: Player, blocks: Array[Array[Array[Block]]], rotation: Matrix4, pattern: Pattern)
  extends Rune
    with Tiered
    with Consumable
    with Linkable {
}