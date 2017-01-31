package me.amuxix.runes.traits

import me.amuxix.material.Material
import me.amuxix.pattern.Tier
import me.amuxix.runes.Rune
import me.amuxix.util.Block

/**
  * Created by Amuxix on 22/11/2016.
  */
/**
  * Used by runes that have a tier associated with them
  */
trait Tiered extends Rune {
  val tierBlocks: Seq[Block] = specialBlocks(Tier)

  val tierType: Material = tierBlocks.head.material

	val tier: Int = tierType.tier.get
}
