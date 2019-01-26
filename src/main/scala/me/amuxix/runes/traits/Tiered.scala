package me.amuxix.runes.traits

import cats.data.OptionT
import cats.effect.IO
import cats.implicits._
import me.amuxix.Consumable
import me.amuxix.block.Block
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
  def consumeTierBlocks: OptionT[IO, Int] = Consumable.consume[Stream](tierBlocks)

  lazy val tierBlocks: Stream[Block] = filteredRuneBlocksByElement(Tier)

  lazy val tierMaterial: Material = tierBlocks.head.material

  lazy val tier: Int = tierMaterial.tier.get
}
