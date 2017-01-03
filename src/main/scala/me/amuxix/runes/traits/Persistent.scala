package me.amuxix.runes.traits

import me.amuxix.IntegrityMonitor
import me.amuxix.runes.Rune
import me.amuxix.util.Block

/**
  * Created by Amuxix on 26/11/2016.
  */
/**
  * Rune that has some persistent world effect that should be serialized
  */
trait Persistent { this: Rune =>
  /** Blocks to be monitored in case of destruction */
  val monitoredBlocks: Seq[Block]

  /** Adds the list of given blocks to the list of monitored blocks, if any of this blocks is destroyed, the rune they create is destroyed*/
  IntegrityMonitor.addRune(this)

  def destroyRune(): Unit
}