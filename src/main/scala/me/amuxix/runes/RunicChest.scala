package me.amuxix.runes

import cats.data.EitherT
import cats.effect.IO
import me.amuxix.block.Block.Location
import me.amuxix.block.blocks.Chest
import me.amuxix.inventory.Item
import me.amuxix.material.Material.{Chest, MagmaBlock, Obsidian}
import me.amuxix.pattern._
import me.amuxix.{Direction, Matrix4, Player}

object RunicChest extends RunePattern {
  val pattern: Pattern = Pattern(RunicChest.apply)(
    ActivationLayer(
      MagmaBlock, Obsidian, MagmaBlock,
      Obsidian,   Chest,    Obsidian,
      MagmaBlock, Obsidian, MagmaBlock
    )
  )
}

case class RunicChest(center: Location, creator: Player, direction: Direction, rotation: Matrix4, pattern: Pattern) extends Rune {
  /**
    * Should this rune use a true name if the activator is wearing one?
    */
  override protected[runes] val shouldUseTrueName: Boolean = true

  /**
    * Internal activate method that should contain all code to activate a rune.
    */
  override protected def onActivate(activationItem: Option[Item]): EitherT[IO, String, Boolean] = EitherT {
    center.block.asInstanceOf[Chest].consume.value.flatMap {
      case Some(0) => IO(Right(false)) //No energy to add, open the chest
      case Some(energy) => activator.addEnergy(energy).map(_ => true).value
      case _ => IO(Right(false))
    }
  }
}

