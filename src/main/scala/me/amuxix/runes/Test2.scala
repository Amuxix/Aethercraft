package me.amuxix.runes

import me.amuxix.pattern._
import me.amuxix.runes.traits.Consumable
import me.amuxix.util.Block.Location
import me.amuxix.util.{Block, Matrix4, Player}
import org.bukkit.Material.ENDER_STONE

/**
  * Created by Amuxix on 01/12/2016.
  */
object Test2 extends RunePattern {
  val pattern = Pattern(Test2.apply, width = 5)(
    ActivationLayer(
      ENDER_STONE, NotInRune, ENDER_STONE, NotInRune, ENDER_STONE,
      NotInRune, ENDER_STONE, NotInRune, ENDER_STONE, NotInRune,
      ENDER_STONE, NotInRune, ENDER_STONE, NotInRune, ENDER_STONE,
      NotInRune, ENDER_STONE, NotInRune, ENDER_STONE, NotInRune,
      ENDER_STONE, NotInRune, ENDER_STONE, NotInRune, ENDER_STONE
    )
  )
}
case class Test2(location: Location, activator: Player, blocks: Array[Array[Array[Block]]], rotation: Matrix4, pattern: Pattern)
  extends Rune with Consumable {

}
