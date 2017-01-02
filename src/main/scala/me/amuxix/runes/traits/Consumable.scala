package me.amuxix.runes.traits

import me.amuxix.runes.Rune

/**
  * Created by Amuxix on 22/11/2016.
  */
/**
  * Defines a trait for runes that can have all their blocks consumed, ie: faith when activated consumes all blocks
  */
trait Consumable extends Tiered { this: Rune =>
	def consumeRuneBlocks() = pattern.getAllRuneBlocksVectors.map(getBlockAt).foreach(_.consume(activator))
	def consumeTierBlocks() = getTierBlocks.foreach(_.consume(activator))
}
