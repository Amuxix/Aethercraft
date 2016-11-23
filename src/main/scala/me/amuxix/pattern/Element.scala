package me.amuxix.pattern

import org.bukkit.{Material => BukkitMaterial}

import scala.language.implicitConversions

object Element {
	implicit def bukkitMaterial2Material(material: BukkitMaterial): Material = Material(material)
}

sealed trait Element
case object Tier extends Element
case object Signature extends Element
case object Key extends Element
case object NotInRune extends Element
case class Material(material: BukkitMaterial) extends Element