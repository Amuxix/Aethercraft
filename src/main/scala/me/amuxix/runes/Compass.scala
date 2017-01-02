package me.amuxix.runes

import me.amuxix.Runecraft
import me.amuxix.pattern._
import me.amuxix.runes.traits.Tiered
import me.amuxix.util.Block.Location
import me.amuxix.util.{Block, Matrix4, Player, Vector3}
import org.bukkit.Material.{AIR, GLASS}

/**
  * Created by Amuxix on 02/01/2017.
  */
object Compass extends RunePattern {
  val pattern = Pattern(Compass.apply, width = 3)(
    ActivationLayer(
      Tier, AIR, Tier,
      AIR, Tier, AIR,
      Tier, AIR, Tier
    )
  )
}

case class Compass(location: Location, activator: Player, blocks: Array[Array[Array[Block]]], rotation: Matrix4, rotationCenter: Vector3[Int], pattern: Pattern)
  extends Rune
  with Tiered {
  //These lines below change the compass to make a sort of an arrow pointing north
  blocks(0)(0)(0).setType(AIR)
  blocks(2)(0)(0).setType(AIR)
  blocks(1)(0)(1).setType(AIR)
  blocks(0)(0)(1).setType(getTierType)
  blocks(2)(0)(1).setType(getTierType)
  blocks(1)(0)(0).setType(getTierType)
  if (getTierType == GLASS) {
    //If player used glass show the current version.
    activator.sendMessage(Runecraft.self.getDescription.getFullName)
  }
}
