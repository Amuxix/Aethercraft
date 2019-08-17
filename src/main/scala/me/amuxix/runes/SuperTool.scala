package me.amuxix.runes

import cats.data.EitherT
import cats.effect.IO
import cats.implicits.{catsStdInstancesForList, toTraverseOps}
import me.amuxix.block.Block
import me.amuxix.inventory.Item
import me.amuxix.material.Material.{RedstoneTorch, RedstoneWire}
import me.amuxix.material.Generic.{Composition, Tool => GenericTool}
import me.amuxix.material.Material
import me.amuxix.material.Properties.BreakableBlockProperty
import me.amuxix.pattern._
import me.amuxix.position.BlockPosition
import me.amuxix.runes.traits.enchants.{BlockBreakTrigger, Enchant}
import me.amuxix.runes.traits.{ConsumableBlocks, Tool}
import me.amuxix.{=|>, Direction, Matrix4, Player}

/**
  * Created by Amuxix on 01/02/2017.
  */
object SuperTool extends RunePattern[SuperTool] with Enchant with BlockBreakTrigger {
  override val runeCreator: RuneCreator = SuperTool.apply
  override val activatesWith: Option[Item] =|> Boolean = {
    case Some(item) if item.material.isTool => true
  }
  // format: off
  override val layers: List[ActivationLayer] = List(
    ActivationLayer(
      RedstoneWire,  RedstoneWire, RedstoneTorch, RedstoneWire, RedstoneWire,
      RedstoneWire,  NotInRune,    RedstoneWire,  NotInRune,    RedstoneWire,
      RedstoneTorch, RedstoneWire, NotInRune,     RedstoneWire, RedstoneTorch,
      RedstoneWire,  NotInRune,    RedstoneWire,  NotInRune,    RedstoneWire,
      RedstoneWire,  RedstoneWire, RedstoneTorch, RedstoneWire, RedstoneWire,
    )
  )
  // format: on

  override def canEnchant(item: Item): Option[String] =
    Option.unless(item.material.isTool)("This rune can only be applied to tools.")

  override def incompatibleEnchants: Set[Enchant] = Set.empty


  /** This should run the effect of the enchant and return whether to cancel the event or not */
  override def onBlockBreak(
    player: Player,
    itemInHand: Option[Item],
    brokenBlock: Block
  ): EitherT[IO, String, Boolean] =
    brokenBlock.material match {
      case breakableBlockMaterial: Material with BreakableBlockProperty =>
        itemInHand
          .filter(_.hasRuneEnchant(SuperTool))
          .fold(EitherT.rightT[IO, String](false)) { itemInHand =>
            itemInHand.material match {
              case tool: GenericTool with Composition if breakableBlockMaterial.isAppropriateTool(tool) =>
                brokenBlock.allNeighbours
                  .filter(_.material == breakableBlockMaterial)
                  .traverse(_.breakUsing(player, itemInHand))
                  .map(_.head)
                  .toLeft(false)

              case _ => EitherT.pure(false)
            }
          }
      case _ => EitherT.pure(false)
    }
}

case class SuperTool(
  center: BlockPosition,
  creator: Player,
  direction: Direction,
  rotation: Matrix4,
  pattern: Pattern
) extends Rune
    with ConsumableBlocks
    with Tool {

  /**
    * Internal activate method that should contain all code to activate a rune.
    */
  override protected def onActivate(activationItem: Option[Item]): EitherT[IO, String, Boolean] = {
    activationMessage = activationItem.fold(activationMessage)(_.name + s" has been enchanted with ${this.name}")

    EitherT.fromOption[IO](activationItem, "No activation tool.").flatMap(_.addRuneEnchant(SuperTool)).map(_ => true)
  }

  /**
    * Should this rune use a true name if the activator is wearing one?
    */
  override val shouldUseTrueName: Boolean = true
}
