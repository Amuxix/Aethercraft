package me.amuxix.runes

import me.amuxix.Runecraft
import me.amuxix.material.Material.{Air, Glass}
import me.amuxix.pattern._
import me.amuxix.runes.traits.Tiered
import me.amuxix.util._

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

case class Compass(parameters: RuneParameters, pattern: Pattern)
  extends Rune(parameters) with Tiered {


  //These lines below change the compass to make a sort of an arrow pointing north
  (center - SouthEast).block.move(South, activator) //This moves the block at the NorthEast corner 1 block to the south
  (center - SouthWest).block.move(South, activator) //This moves the block at the NorthWest corner 1 block to the south
  center.block.move(North, activator) //This moves the center block 1 block to the north

  override def notifyActivator(): Unit = {
    super.notifyActivator()
    if (tierType == Glass) {
      //If player used glass show the current version.
      activator.sendMessage(Runecraft.self.getDescription.getFullName)
    }
  }
}
