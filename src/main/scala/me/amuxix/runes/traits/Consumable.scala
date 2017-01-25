package me.amuxix.runes.traits

import me.amuxix.pattern.{Key, Signature}
import me.amuxix.runes.Rune

/**
  * Created by Amuxix on 22/11/2016.
  */
/**
  * Defines a trait for runes that can have all their blocks consumed, ie: faith when activated consumes all blocks
  */
trait Consumable extends Tiered { this: Rune =>
	def consumeRuneBlocks(): Unit = pattern.allRuneBlockVectors.map(blockAt).foreach(_.consume(activator))
	def consumeTierBlocks(): Unit = tierBlocks.foreach(_.consume(activator))
  def consumeSignatureBlocks(): Unit = pattern.specialBlockVectors(Signature).map(blockAt).foreach(_.consume(activator))
  def consumeKeyBlocks(): Unit = pattern.specialBlockVectors(Key).map(blockAt).foreach(_.consume(activator))
}
