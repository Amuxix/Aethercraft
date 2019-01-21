package me.amuxix.runes

import me.amuxix.{Direction, Player}
import me.amuxix.block.Block
import me.amuxix.block.Block.Location
import me.amuxix.material.Material.{Chest, MagmaBlock, Obsidian}
/*import me.amuxix.block.blocks.{Chest => ChestBlock}
import me.amuxix.bukkit.inventory.Item
import me.amuxix.bukkit.block.blocks.Chest*/
import me.amuxix.inventory.Item
import me.amuxix.pattern._

object RunicChest extends RunePattern {
  val pattern: Pattern = Pattern(RunicChest.apply)(
    ActivationLayer(
      MagmaBlock, Obsidian, MagmaBlock,
      Obsidian,   Chest,    Obsidian,
      MagmaBlock, Obsidian, MagmaBlock
    )
  )
}

case class RunicChest(blocks: Array[Array[Array[Block]]], center: Location, creator: Player, direction: Direction, pattern: Pattern) extends Rune {
  /**
    * Should this rune use a true name if the activator is wearing one?
    */
  override protected[runes] val shouldUseTrueName: Boolean = true

  /**
    * Internal activate method that should contain all code to activate a rune.
    */
  override protected def onActivate(activationItem: Item): Unit = ???/*center.block match {
    case chest: Chest => chest.contents.foldLeft(0){
      case (acc, item) => acc + item.ener
    }
  }*/
}

