package me.amuxix.runes.traits

import me.amuxix.pattern.Signature
import me.amuxix.runes.Rune
import me.amuxix.util.Block

/**
  * Created by Amuxix on 22/11/2016.
  */
/**
  * Defines a trait for runes that use signature blocks that are consumed to link to other stuff: ie: Teleports, Waypoints
  */
trait Linkable { this: Rune =>
  val signatureBlocks: Seq[Block] = specialBlocks(Signature)

  val signature: Int = signatureBlocks.sortWith(_.material.name() < _.material.name()).hashCode()
}
