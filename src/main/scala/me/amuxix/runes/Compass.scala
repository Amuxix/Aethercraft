package me.amuxix.runes

import cats.data.EitherT
import cats.effect.IO
import me.amuxix.block.{Block, BlockUtils}
import me.amuxix.inventory.Item
import me.amuxix.material.Material.{Air, Glass}
import me.amuxix.pattern.{Tier, _}
import me.amuxix.position.{BlockPosition, Vector3}
import me.amuxix.runes.traits.Tiered
import me.amuxix.{Player, _}

/**
  * Created by Amuxix on 02/01/2017.
  */
object Compass extends RunePattern[Compass] {
  override val runeCreator: RuneCreator = Compass.apply
  // format: off
  override val layers: List[BaseLayer] = List(
    ActivationLayer(
      Tier, Air, Tier,
      Air, Tier, Air,
      Tier, Air, Tier
    )
  )
  // format: on
}

case class Compass(
  center: BlockPosition,
  creator: Player,
  direction: Direction,
  rotation: Matrix4,
  pattern: Pattern
) extends Tiered {

  override protected def onActivate(activationItem: Option[Item]): EitherT[IO, String, Boolean] = {
    //These lines below change the compass to make a sort of an arrow pointing north
    val arrowForming: Map[Block, Vector3[Int]] = Map(
      (center - SouthEast).block -> South,
      (center - SouthWest).block -> South,
      center.block -> North
    )
    BlockUtils.moveSeveralBy(arrowForming, activator).toLeft {
      if (tierMaterial == Glass) {
        //If player used glass show the current version.
        activationMessage = Aethercraft.fullVersion
      }
      true
    }
  }

  override val shouldUseTrueName: Boolean = false
}
