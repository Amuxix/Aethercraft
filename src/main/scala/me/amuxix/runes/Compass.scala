package me.amuxix.runes

import me.amuxix.Runecraft
import me.amuxix.pattern._
import me.amuxix.runes.traits.Tiered
import me.amuxix.util._
import org.bukkit.Material.{AIR, GLASS}
import org.bukkit.event.player.PlayerInteractEvent

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

case class Compass(event: PlayerInteractEvent, blocks: Array[Array[Array[Block]]], rotation: Matrix4, rotationCenter: Vector3[Int], pattern: Pattern)
  extends Rune with Tiered {
  //These lines below change the compass to make a sort of an arrow pointing north
  (center - SouthEast).getBlock.move(South) //This moves the block at the NorthEast corner 1 block to the south
  (center - SouthWest).getBlock.move(South) //This moves the block at the NorthWest corner 1 block to the south
  center.getBlock.move(North) //This moves the center block 1 block to the north
  if (tierType == GLASS) {
    //If player used glass show the current version.
    activator.sendMessage(Runecraft.self.getDescription.getFullName)
  }
}
