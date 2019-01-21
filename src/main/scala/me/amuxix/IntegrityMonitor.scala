package me.amuxix

import me.amuxix.block.Block
import me.amuxix.runes.Rune
import me.amuxix.runes.traits.{Persistent, Tiered}

import scala.collection.immutable.HashMap

/**
  * Created by Amuxix on 03/01/2017.
  */
object IntegrityMonitor {
  //Maps bocks to the rune which they are tied to, modifying these blocks destroys the rune.
  private var destructionBlocks: HashMap[Block, Rune with Persistent] = HashMap.empty
  private var buildBlocks: HashMap[Block, Rune with Persistent] = HashMap.empty

  /**
    * Registers a persistent rune for its blocks to be monitored
    * @param rune Rune whose blocks should be monitored
    */
  def addRune(rune: Rune with Persistent): Unit = {
    destructionBlocks ++= rune.monitoredDestroyBlocks.map(_ -> rune)
    buildBlocks ++= rune.monitoredBuildBlocks.map(_ -> rune)
    Serialization.persistentRunes += rune.center -> rune
  }

  /**
    * Removes a rune from the monitored runes.
    * @param rune Rune we are removing.
    */
  def removeRune(rune: Rune with Persistent): Unit = {
    rune.destroyRune()
    destructionBlocks --= rune.monitoredDestroyBlocks
    buildBlocks --= rune.monitoredBuildBlocks
    Serialization.persistentRunes -= rune.center
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
      player.sendNotification(rune.destroyMessage)
    }
  }

  /**
    * Checks if a block placement would destroy a rune by making its pattern invalid
    * @param block block that was placed.
    * @param player Player that placed the block.
    */
  def checkIntegrityAfterBlockPlacement(block: Block, player: Player): Unit = {
    if (buildBlocks.contains(block)) {
      val rune: Rune with Persistent = buildBlocks(block)
      val runeMaterials = rune match {
        case tiered: Tiered => rune.pattern.patternMaterials + tiered.tierType
        case _ => rune.pattern.patternMaterials
      }
      if (runeMaterials.contains(block.material)) {
        removeRune(rune)
        player.sendNotification(rune.destroyMessage)
      }
    }
  }
}
