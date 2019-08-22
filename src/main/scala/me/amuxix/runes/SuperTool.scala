package me.amuxix.runes

import cats.data.EitherT
import cats.effect.IO
import cats.implicits.{catsStdInstancesForList, toTraverseOps}
import me.amuxix.block.Block
import me.amuxix.bukkit.Configuration
import me.amuxix.inventory.Item
import me.amuxix.material.Generic.{Composition, Tool => GenericTool}
import me.amuxix.material.Material
import me.amuxix.material.Material.{RedstoneTorch, RedstoneWire}
import me.amuxix.material.Properties.BreakableBlockProperty
import me.amuxix.pattern._
import me.amuxix.position.BlockPosition
import me.amuxix.runes.traits.Tool
import me.amuxix.runes.traits.enchants.{BlockBreakTrigger, Enchant, RuneEnchant}
import me.amuxix.{Direction, Matrix4, Player}

/**
  * Created by Amuxix on 01/02/2017.
  */
object SuperTool extends RunePattern[SuperTool] with Enchant with BlockBreakTrigger {
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
  /** The validation to check if the item can be enchanted. */
  override def itemValidation(item: Item): Boolean = item.material.isTool

  /** The description of possible items that can be enchanted by this rune to be given as error if it fails. */
  override val itemDescription: String = "tools"

  /** This should run the effect of the enchant and return whether to cancel the event or not */
  override def onBlockBreak(
    player: Player,
    itemInHand: Item,
    brokenBlock: Block
  ): EitherT[IO, String, Boolean] =
    brokenBlock.material match {
      case breakableBlockMaterial: Material with BreakableBlockProperty =>
        itemInHand.material match {
          case tool: GenericTool with Composition if breakableBlockMaterial.isAppropriateTool(tool) =>
            val possibleBlocks = brokenBlock.allNeighbours
              .filter(_.material == breakableBlockMaterial)
            brokenBlock.faceNeighbours.collect {
              case faceNeighbour if possibleBlocks.contains(faceNeighbour) =>
                faceNeighbour +: faceNeighbour.faceNeighbours.collect {
                  case edgeNeighbour if possibleBlocks.contains(edgeNeighbour) =>
                    edgeNeighbour +: edgeNeighbour.faceNeighbours.collect {
                    case vertexNeighbour if possibleBlocks.contains(vertexNeighbour) => vertexNeighbour
                  }
                }.flatten
            }.flatten
              .traverse { block =>
                for {
                  _ <- player.removeEnergy(Configuration.blockBreak)
                  _ <- block.breakUsing(player, itemInHand).toLeft(())
                } yield ()
              }
              .map(_ => false)

          case _ => EitherT.pure(false)
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
) extends Rune with Tool with RuneEnchant {
  override val enchant: Enchant = SuperTool
}
