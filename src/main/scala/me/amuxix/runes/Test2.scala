package me.amuxix.runes

import me.amuxix.material.Material.{EndStone, Glass}
import me.amuxix.pattern._
import me.amuxix.runes.traits.Consumable
import me.amuxix.util.{Block, Matrix4, Vector3}
import org.bukkit.event.player.PlayerInteractEvent

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

case class Test2(event: PlayerInteractEvent, blocks: Array[Array[Array[Block]]], rotation: Matrix4, rotationCenter: Vector3[Int], pattern: Pattern)
  extends Rune
          with Consumable {
}
