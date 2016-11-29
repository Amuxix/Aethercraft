package me.amuxix.runes.traits

import me.amuxix.runes.Rune
import me.amuxix.util.{Block, Matrix4}

/**
  * Created by Amuxix on 22/11/2016.
  */
/**
  * Used by runes that have a tier associated with them
  */
trait Tiered { this: Rune =>
  def getTierBlocks: List[Block] = ???
	def getTier: Integer = ???
}
