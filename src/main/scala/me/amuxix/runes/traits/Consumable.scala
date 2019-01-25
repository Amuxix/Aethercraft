package me.amuxix.runes.traits

import me.amuxix.pattern.{Key, Signature}
import me.amuxix.runes.Rune

/**
  * Created by Amuxix on 22/11/2016.
  */
/**
  * Defines a trait for runes that can have all their blocks consumed, ie: faith when activated consumes all blocks
  */
trait Consumable extends Rune {
	def consumeRuneBlocks(): Unit = allRuneBlocks.foreach(_.consume(activator))
  def consumeSignatureBlocks(): Unit = specialBlocks(Signature).foreach(_.consume(activator))
  def consumeKeyBlocks(): Unit = specialBlocks(Key).foreach(_.consume(activator))
}
