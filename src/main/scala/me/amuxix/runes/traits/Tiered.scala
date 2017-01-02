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
  def getTierBlocks: Seq[Block] = {
    pattern.getSpecialBlocksVectors(Tier).map(getBlockAt)
  }

  val getTierType: Material = getTierBlocks.head.getType

	def getTier: Integer = ???
}
