package me.amuxix.runes.traits

import me.amuxix.runes.Rune
import me.amuxix.util.{Block, Player}
import me.amuxix.{IntegrityMonitor, Runecraft}

/**
  * Created by Amuxix on 26/11/2016.
  */
/**
  * Rune that has some persistent world effect that should be serialized
  */
trait Persistent {
  this: Rune =>
  /** Blocks to be monitored in case of destruction
    * Needs to be set as lazy to avoid not being defined when added to integrity monitor*/
  val monitoredBlocks: Seq[Block]

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

  /** Adds the list of given blocks to the list of monitored blocks, if any of this blocks is destroyed, the rune
    * they create is destroyed */
  IntegrityMonitor.addRune(this)
  Runecraft.persistentRunes += center -> this
}