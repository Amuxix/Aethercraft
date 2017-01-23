package me.amuxix.runes.waypoints

import me.amuxix.runes.Rune
import me.amuxix.runes.traits.Tiered

/**
  * Created by Amuxix on 02/01/2017.
  */
/**
  * This represents a point that can be targeted by a waypoint.
  */
trait WaypointTrait extends Tiered { this: Rune =>
  val size: WaypointSize
}
