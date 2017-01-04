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

  def addRune(rune: Rune with Persistent): Unit = {
    monitoredRunes += rune.monitoredBlocks -> rune
  }

  def checkIntegrityAfterBlockDestruction(block: Block, player: Player): Unit = {
    /**
      * Destroys the rune if the block destroyed was part of the rune
      */
    def remove(blocks: Seq[Block]): Unit = {
      if (blocks.contains(block)) {
        val rune: Rune with Persistent = monitoredRunes(blocks)
        rune.destroyRune()
        monitoredRunes -= blocks
        Runecraft.persistentRunes -= rune.center
        player.sendMessage(ChatColor.RED + rune.getClass.getSimpleName + " destroyed!")
      }
    }
    monitoredRunes.keys.foreach(remove)
  }

  def checkIntegrityAfterBlockDestruction(block: Block): Unit = {
    /**
      * Destroys the rune if the block destroyed was part of the rune
      */
    def remove(blocks: Seq[Block]): Unit = {
      if (blocks.contains(block)) {
        val rune: Rune with Persistent = monitoredRunes(blocks)
        rune.destroyRune()
        monitoredRunes -= blocks
        Runecraft.persistentRunes -= rune.center
      }
    }
    monitoredRunes.keys.foreach(remove)
  }
}
