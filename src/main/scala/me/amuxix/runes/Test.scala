package me.amuxix.runes

import me.amuxix.pattern._
import me.amuxix.runes.traits.{Consumable, Linkable, Tiered}
import me.amuxix.util.{Block, Matrix4, Player, Vector3}
import org.bukkit.Material.{ENDER_STONE, GLASS}
import org.bukkit.event.player.PlayerInteractEvent

/**
  * Created by Amuxix on 26/11/2016.
  */
/**
  * Test rune, does nothing pattern might chance
  */

object Test extends RunePattern {
  val pattern: Pattern = Pattern(Test.apply, width = 3, numberOfMirroredAxis = false, verticality = true)(
    ActivationLayer(
      ENDER_STONE, NotInRune, ENDER_STONE,
      NotInRune, ENDER_STONE, NotInRune,
      ENDER_STONE, NotInRune, ENDER_STONE
    ), Layer(
      Tier, Signature, Tier,
      GLASS, GLASS, GLASS,
      GLASS, GLASS, GLASS
    )
  )
}

case class Test(event: PlayerInteractEvent, blocks: Array[Array[Array[Block]]], rotation: Matrix4, rotationCenter: Vector3[Int], pattern: Pattern)
  extends Rune
          with Tiered
          with Consumable
          with Linkable {

  /**
    * Checks whether the signature is valid for this rune and notifies player if it is not and why
    *
    * @param player player to be notified in case of signature being invalid
    * @return true if signature is valid, false otherwise
    */
  override def validateSignature(player: Player): Boolean = true
}