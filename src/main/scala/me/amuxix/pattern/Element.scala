package me.amuxix.pattern

import me.amuxix.material.Material

trait Element

case object Tier extends Element
case object Signature extends Element
case object Key extends Element
case object NotInRune extends Element
//Material also extends Element

case class MaterialChoice(possibilities: Material*) extends Element
