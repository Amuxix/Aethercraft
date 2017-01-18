package me.amuxix

import me.amuxix.runes.Rune
import me.amuxix.runes.traits.Persistent
import me.amuxix.util.{Block, Player}
import org.bukkit.ChatColor

/**
  * Created by Amuxix on 03/01/2017.
  */
object IntegrityMonitor {
  //Maps bocks to the rune which they are tied to, modifying these blocks destroys the rune.
  private var monitoredRunes = Map.empty[Seq[Block], Rune with Persistent]

  /**
    * Registers a persistent rune for its blocks to be monitored
    * @param rune Rune whose blocks should be monitored
    */
  def addRune(rune: Rune with Persistent): Unit = {
    monitoredRunes += rune.monitoredBlocks -> rune
  }

  /**
    * Checks if a rune was destroyed by destroying the given block, notifies the player who destroyed the rune
    * @param block Block that was destroyed
    * @param player Player who destroyed it
    */
  def checkIntegrityAfterBlockDestruction(block: Block, player: Player): Unit = {
    /**
      * Destroys the rune if the block destroyed was part of the rune and notifies the player who destroyed the block
      */
    def removeAndNotify(blocks: Seq[Block]): Unit = {
      if (blocks.contains(block)) {
        val rune: Rune with Persistent = monitoredRunes(blocks)
        rune.destroyRune()
        monitoredRunes -= blocks
        Runecraft.persistentRunes -= rune.center
        player.sendMessage(ChatColor.RED + rune.getClass.getSimpleName + " destroyed!")
      }
    }
    monitoredRunes.keys.foreach(removeAndNotify)
  }

  /**
    * Checks if a block placement would destroy a rune by making its pattern invalid
    * @param block
    */
  def checkValidPlacing(block: Block, player: Player): Unit = {

  }
}
