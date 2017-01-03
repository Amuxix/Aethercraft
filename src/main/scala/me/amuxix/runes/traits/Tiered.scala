package me.amuxix.runes.traits

import me.amuxix.pattern.Tier
import me.amuxix.runes.Rune
import me.amuxix.util.Block
import org.bukkit.Material

/**
  * Created by Amuxix on 22/11/2016.
  */
/**
  * Used by runes that have a tier associated with them
  */
trait Tiered { this: Rune =>
  val tierBlocks: Seq[Block] = specialBlocks(Tier)

  val tierType: Material = tierBlocks.head.getType

	def tier: Integer = 4 // See xkcd about randomness
}
