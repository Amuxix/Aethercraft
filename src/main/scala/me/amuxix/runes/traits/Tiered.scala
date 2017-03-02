package me.amuxix.runes.traits

import me.amuxix.Block
import me.amuxix.material.Material
import me.amuxix.pattern.Tier
import me.amuxix.runes.Rune

/**
  * Created by Amuxix on 22/11/2016.
  */
/**
  * Used by runes that have a tier associated with them
  */
trait Tiered extends Rune {
  def consumeTierBlocks(): Unit = tierBlocks.foreach(_.consume(activator))

  val tierBlocks: Seq[Block] = specialBlocks(Tier)

  val tierType: Material = tierBlocks.head.material

	val tier: Int = tierType.tier.get
}
