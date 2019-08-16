package me.amuxix.runes.traits

import me.amuxix.Consumable
import me.amuxix.block.Block
import me.amuxix.material.Material
import me.amuxix.material.Properties.BlockProperty
import me.amuxix.pattern.Tier
import me.amuxix.runes.Rune

/**
  * Created by Amuxix on 22/11/2016.
  */
/**
  * Used by runes that have a tier associated with them
  */
trait Tiered extends Rune {
  def consumeTierBlocks: List[(List[Consumable#ConsumeIO], Option[Consumable#ConsumeIO])] = tierBlocks.toList.flatMap(_.consume)

  lazy val tierBlocks: LazyList[Block] = filteredRuneBlocksByElement(Tier)

  lazy val tierMaterial: Material with BlockProperty = tierBlocks.head.material

  lazy val tier: Int = tierMaterial.tier.get
}
