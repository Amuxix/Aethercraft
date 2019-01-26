package me.amuxix.runes.traits

import cats.data.OptionT
import cats.effect.IO
import cats.implicits._
import me.amuxix.Consumable
import me.amuxix.pattern.{Key, Signature}
import me.amuxix.runes.Rune

/**
  * Created by Amuxix on 22/11/2016.
  */
/**
  * Defines a trait for runes that can have all their blocks consumed, ie: faith when activated consumes all blocks
  */
trait ConsumableBlocks extends Rune with Consumable {
	override def consume: OptionT[IO, Int] = Consumable.consume[Stream](allRuneBlocks)
  def consumeSignatureBlocks: OptionT[IO, Int] = Consumable.consume[Stream](filteredRuneBlocksByElement(Signature))
  def consumeKeyBlocks: OptionT[IO, Int] = Consumable.consume[Stream](filteredRuneBlocksByElement(Key))
}
