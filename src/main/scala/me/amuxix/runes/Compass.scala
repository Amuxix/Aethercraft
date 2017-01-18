package me.amuxix.runes

import me.amuxix.Runecraft
import me.amuxix.material.Material.{Air, Glass}
import me.amuxix.pattern._
import me.amuxix.runes.traits.Tiered
import me.amuxix.util._
import org.bukkit.event.player.PlayerInteractEvent

/**
  * Created by Amuxix on 02/01/2017.
  */
object Compass extends RunePattern {
  val pattern: Pattern = Pattern(Compass.apply, width = 3)(
    ActivationLayer(
      Tier, Air, Tier,
      Air, Tier, Air,
      Tier, Air, Tier
    )
  )
}

case class Compass(event: PlayerInteractEvent, blocks: Array[Array[Array[Block]]], rotation: Matrix4, rotationCenter:
Vector3[Int], pattern: Pattern)
  extends Rune with Tiered {


  //These lines below change the compass to make a sort of an arrow pointing north
  (center - SouthEast).block.move(South) //This moves the block at the NorthEast corner 1 block to the south
  (center - SouthWest).block.move(South) //This moves the block at the NorthWest corner 1 block to the south
  center.block.move(North) //This moves the center block 1 block to the north

  override def notifyActivator(): Unit = {
    super.notifyActivator()
    if (tierType == Glass) {
      //If player used glass show the current version.
      activator.sendMessage(Runecraft.self.getDescription.getFullName)
    }
  }
}
