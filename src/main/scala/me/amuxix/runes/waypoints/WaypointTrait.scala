package me.amuxix.runes.waypoints

import me.amuxix.runes.Rune
import me.amuxix.runes.traits.{Linkable, Tiered}

/**
  * Created by Amuxix on 02/01/2017.
  */
/**
  * This represents a point that can be targeted by a waypoint.
  */
trait WaypointTrait extends Tiered with Linkable { this: Rune =>
  val size: WaypointSize
}
