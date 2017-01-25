package me.amuxix.runes.traits

import me.amuxix.IntegrityMonitor
import me.amuxix.pattern.NotInRune
import me.amuxix.runes.Rune
import me.amuxix.util.{Block, Player}
import org.bukkit.ChatColor

/**
  * Created by Amuxix on 26/11/2016.
  */
/**
  * Rune that has some persistent world effect that should be serialized
  */
trait Persistent {
  this: Rune =>
  /** Blocks to be monitored, if destroyed the rune breaks
    */
  lazy val monitoredDestroyBlocks: Seq[Block] = this match {
    case tiered: Tiered => nonSpecialBlocks ++ tiered.tierBlocks
    case _ => nonSpecialBlocks
  }

  /** Blocks to be monitored for building, if a material that is part of the rune is placed here the rune is destroyed
    */
  lazy val monitoredBuildBlocks: Seq[Block] = specialBlocks(NotInRune)

  /**
    * Destroys the rune effect. This should undo all lasting effects this rune introduced
    */
  def destroyRune(): Unit

  /**
    * Updates the rune with the new changes and notifies the player who triggered the update
    *
    * @param player Player who triggered the update
    */
  def update(player: Player): Unit

  val destroyMessage: String = ChatColor.RED + name + " destroyed!"

  /** Adds the list of given blocks to the list of monitored blocks, if any of this blocks is destroyed, the rune
    * they create is destroyed */
  IntegrityMonitor.addRune(this)
}