package me.amuxix.runes.traits

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
	//override def consumeAtomically: Option[(Energy, OptionT[IO, String])] = Consumable.consumeAtomically[Stream](allRuneBlocks)
	override def consume: List[(List[ConsumeIO], Option[ConsumeIO])] = allRuneBlocks.toList.flatMap(_.consume)
  def consumeSignatureBlocks: List[(List[ConsumeIO], Option[ConsumeIO])] = filteredRuneBlocksByElement(Signature).toList.flatMap(_.consume)
  def consumeKeyBlocks: List[(List[ConsumeIO], Option[ConsumeIO])] = filteredRuneBlocksByElement(Key).toList.flatMap(_.consume)
}
