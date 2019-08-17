package me.amuxix.pattern

import me.amuxix.material.Material
import me.amuxix.material.Properties.BlockProperty

sealed trait Element

case object Tier extends Element
case object Signature extends Element
case object Key extends Element
case object NotInRune extends Element
case class BlockElement(block: Material with BlockProperty) extends Element

case class MaterialChoice(possibilities: Material with BlockProperty*) extends Element
