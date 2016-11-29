package me.amuxix.runes

import me.amuxix.pattern._
import me.amuxix.runes.traits.{Consumable, Linkable, Tiered}
import me.amuxix.util.{Block, Location, Matrix4, Player}
import org.bukkit.Material.ENDER_STONE

/**
  * Created by Amuxix on 26/11/2016.
  */
/**
  * Test rune, does nothing pattern might chance
  */
case class Test(location: Location, activator: Player, blocks: Array[Array[Array[Block]]], rotation: Matrix4)
  extends Rune
    with Tiered
    with Consumable
    with Linkable {

  val pattern = Pattern(width = 3)(
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
