package me.amuxix.runes

import me.amuxix.block.Block.Location
import me.amuxix.{Player, _}
import me.amuxix.block.{Block, BlockUtils}
import me.amuxix.bukkit._
import me.amuxix.inventory.Item
import me.amuxix.material.Material.{Air, Glass}
import me.amuxix.pattern._
import me.amuxix.runes.traits.Tiered
import org.bukkit.ChatColor

/**
  * Created by Amuxix on 02/01/2017.
  */
object Compass extends RunePattern {
  val pattern: Pattern = Pattern(Compass.apply)(
    ActivationLayer(
      Tier, Air, Tier,
      Air, Tier, Air,
      Tier, Air, Tier
    )
  )
}

case class Compass(blocks: Array[Array[Array[Block]]], center: Location, creator: Player, direction: Direction, pattern: Pattern) extends Tiered {

  override protected def onActivate(activationItem: Item): Boolean = {
    //These lines below change the compass to make a sort of an arrow pointing north
    val arrowForming: Map[Block, Vector3[Int]] = Map(
      (center - SouthEast).block -> South,
      (center - SouthWest).block -> South,
      center.block -> North
    )
    if (BlockUtils.moveSeveralBy(arrowForming, activator)) {
      if (tierMaterial == Glass) {
        //If player used glass show the current version.
        activationMessage = Aethercraft.fullVersion
      }
    } else {
      activationMessage = ChatColor.RED + "Something blocks you from activating this compass."
    }
    true
  }

  override val shouldUseTrueName: Boolean = false
}
