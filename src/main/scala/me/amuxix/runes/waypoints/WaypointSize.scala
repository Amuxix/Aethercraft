package me.amuxix.runes.waypoints

/**
  * Created by Amuxix on 03/01/2017.
  */
sealed trait WaypointSize {
  val size: Int
}

/**
  * This supports items.
  */
object Small extends WaypointSize {override val size: Int = 0}

/**
  * This supports items and entities(Player, Monsters)
  */
object Medium extends WaypointSize {override val size: Int = 1}

/**
  * This supports Blocks (useful for faiths)
  */
object Large extends WaypointSize {override val size: Int = 2}
