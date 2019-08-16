package me.amuxix.runes

import cats.data.EitherT
import cats.effect.IO
import me.amuxix.block.blocks.Chest
import me.amuxix.inventory.Item
import me.amuxix.material.Material.{Chest, MagmaBlock, Obsidian}
import me.amuxix.pattern._
import me.amuxix.position.BlockPosition
import me.amuxix.{=|>, Direction, Energy, Matrix4, Player}

object RunicChest extends RunePattern[RunicChest] {
  override val runeCreator: RuneCreator = RunicChest.apply
  override val activatesWith: Option[Item] =|> Boolean = { case _ => true }

  // format: off
  val layers: List[BaseLayer] = List(
    ActivationLayer(
      MagmaBlock, Obsidian, MagmaBlock,
      Obsidian,   Chest,    Obsidian,
      MagmaBlock, Obsidian, MagmaBlock
    )
  )
  // format: on
}

case class RunicChest(
  center: BlockPosition,
  creator: Player,
  direction: Direction,
  rotation: Matrix4,
  pattern: Pattern
) extends Rune {
  /**
    * Should this rune use a true name if the activator is wearing one?
    */
  override protected[runes] val shouldUseTrueName: Boolean = true

  /**
    * Internal activate method that should contain all code to activate a rune.
    */
  override protected def onActivate(activationItem: Option[Item]): EitherT[IO, String, Boolean] =
    EitherT {
      activator.addMaximumEnergyFrom(center.block.asInstanceOf[Chest].consume).value.flatMap[Either[String, Boolean]] {
        case Right(Energy(0)) => IO(Right(false)) //No energy to add, open the chest
        case Right(_)         => IO(Right(true))
        case _                => IO(Right(false))
      }
    }
}
