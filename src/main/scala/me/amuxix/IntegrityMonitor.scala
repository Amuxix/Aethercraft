package me.amuxix

import me.amuxix.logging.Logger.info
import me.amuxix.runes.Rune
import me.amuxix.runes.traits.{Persistent, Tiered}
import me.amuxix.util.{Block, Player}

import scala.collection.immutable.HashMap

/**
  * Created by Amuxix on 03/01/2017.
  */
object IntegrityMonitor {
  //Maps bocks to the rune which they are tied to, modifying these blocks destroys the rune.
  private var destructionBlocks: HashMap[Block, Rune with Persistent] = HashMap.empty[Block, Rune with Persistent]
  private var buildBlocks: HashMap[Block, Rune with Persistent] = HashMap.empty[Block, Rune with Persistent]

  /**
    * Registers a persistent rune for its blocks to be monitored
    * @param rune Rune whose blocks should be monitored
    */
  def addRune(rune: Rune with Persistent): Unit = {
    destructionBlocks ++= rune.monitoredDestroyBlocks.map(_ -> rune)
    buildBlocks ++= rune.monitoredBuildBlocks.map(_ -> rune)
    Runecraft.persistentRunes += rune.center -> rune
  }

  def removeRune(rune: Rune with Persistent): Unit = {
    rune.destroyRune()
    destructionBlocks --= rune.monitoredDestroyBlocks
    buildBlocks --= rune.monitoredBuildBlocks
    Runecraft.persistentRunes -= rune.center
  }

  /**
    * Checks if a rune was destroyed by destroying the given block, notifies the player who destroyed the rune
    * @param block Block that was destroyed
    * @param player Player who destroyed it
    */
  def checkIntegrityAfterBlockDestruction(block: Block, player: Player): Unit = {
    if (destructionBlocks.contains(block)) {
      val rune: Rune with Persistent = destructionBlocks(block)
      removeRune(rune)
      player.sendMessage(rune.destroyMessage)
    }
  }

  /**
    * Checks if a block placement would destroy a rune by making its pattern invalid
    * @param block
    */
  def checkIntegrityAfterBlockPlacement(block: Block, player: Player): Unit = {
    buildBlocks.keys.foreach(b => info(b.toString + " " + b.hashCode()))
    if (buildBlocks.contains(block)) {
      val rune: Rune with Persistent = buildBlocks(block)
      val runeMaterials = rune match {
        case tiered: Tiered => rune.pattern.patternMaterials + tiered.tierType
        case _ => rune.pattern.patternMaterials
      }
      if (runeMaterials.contains(block.material)) {
        removeRune(rune)
        player.sendMessage(rune.destroyMessage)
      }
    }
  }
}
