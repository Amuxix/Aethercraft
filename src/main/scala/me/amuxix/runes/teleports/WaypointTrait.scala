package me.amuxix.runes.teleports

import me.amuxix.runes.Rune
import me.amuxix.runes.traits.Tiered
import me.amuxix.util.CardinalPoint

/**
  * Created by Amuxix on 02/01/2017.
  */
/**
  * This represents a point that can be targeted by a waypoint.
  */
trait WaypointTrait extends Tiered { this: Rune =>
  val direction: CardinalPoint = event.getBlockFace
  val size: WaypointSize
}
