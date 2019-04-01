package me.amuxix.runes

import cats.data.{EitherT, OptionT}
import cats.effect.IO
import cats.implicits._
import me.amuxix.{Direction, Matrix4, OptionObjectOps, Player}
import me.amuxix.block.Block
import me.amuxix.block.Block.Location
import me.amuxix.inventory.Item
import me.amuxix.material.Material.{Redstone, RedstoneTorch}
import me.amuxix.pattern._
import me.amuxix.runes.traits.{ConsumableBlocks, Tool}
import me.amuxix.runes.traits.enchants.{BlockBreakTrigger, Enchant}

/**
  * Created by Amuxix on 01/02/2017.
  */
object SuperTool extends RunePattern with Enchant with BlockBreakTrigger {
  val pattern: Pattern = Pattern(SuperTool.apply, activatesWith = { case Some(item) if item.material.isTool => true })(
    ActivationLayer(
      Redstone,        NotInRune, RedstoneTorch,   NotInRune, Redstone,
      NotInRune,       Tier,      Redstone,        Tier,      NotInRune,
      RedstoneTorch,   Redstone,  NotInRune,       Redstone,  RedstoneTorch,
      NotInRune,       Tier,      Redstone,        Tier,      NotInRune,
      Redstone,        NotInRune, RedstoneTorch,   NotInRune, Redstone
    )
  )

  override def canEnchant(item: Item): Option[String] = Option.unless(item.material.isTool)("This rune can only be applied to tools.")

  override def incompatibleEnchants: Set[Enchant] = Set.empty

  /*
    sealed trait Tool extends Generic with Durable
  trait Axe extends Tool
  trait Hoe extends Tool
  trait Pickaxe extends Tool
  trait Shovel extends Tool
  trait Shears extends Tool
   */

  /** This should run the effect of the enchant and return whether to cancel the event or not */
  override def onBlockBreak(player: Player, itemInHand: Option[Item], brokenBlock: Block): EitherT[IO, String, Boolean] = {
    itemInHand
      .filter(_.hasRuneEnchant(SuperTool))
      .fold(EitherT.rightT[IO, String](false)) { item =>
        brokenBlock.allNeighbours.map(_.breakUsing(player, item)).sequence
      }
  }
}

case class SuperTool(center: Location, creator: Player, direction: Direction, rotation: Matrix4, pattern: Pattern) extends Rune with ConsumableBlocks with Tool {

  /**
    * Internal activate method that should contain all code to activate a rune.
    */
  override protected def onActivate(activationItem: Option[Item]): EitherT[IO, String, Boolean] =
    OptionT.fromOption[IO](activationItem)
      .flatMap(_.addRuneEnchant(SuperTool))
      .toLeft(true)

  /**
    * Should this rune use a true name if the activator is wearing one?
    */
  override val shouldUseTrueName: Boolean = true
}