package me.amuxix.runes

import me.amuxix._
import me.amuxix.inventory.Item
import me.amuxix.material.Material.{Air, Glass}
import me.amuxix.pattern._
import me.amuxix.runes.traits.Tiered
import org.bukkit.ChatColor

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

case class Compass(parameters: Parameters, pattern: Pattern)
  extends Rune(parameters) with Tiered {
  private var blocksMoved = false
  override protected def notifyActivator(): Unit = {
    if (blocksMoved) {
      super.notifyActivator()
      if (tierType == Glass) {
        //If player used glass show the current version.
        activator.sendNotification(Runecraft.fullVersion)
      }
    } else {
      activator.sendNotification(ChatColor.RED + "Something blocks you from activating this compass.")
    }
  }

  override protected def innerActivate(activationItem: Item): Unit = {
    //These lines below change the compass to make a sort of an arrow pointing north
    val arrowForming: Map[Block, Vector3[Int]] = Map((center - SouthEast).block -> South, (center - SouthWest).block -> South, center.block -> North)
    blocksMoved = BlockUtils.moveSeveralBy(arrowForming, activator)
  }

  override val shouldUseTrueName: Boolean = false
}
