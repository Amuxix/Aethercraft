package me.amuxix.runes.waypoints

/**
  * Created by Amuxix on 03/01/2017.
  */
sealed trait WaypointSize

/**
  * This supports items.
  */
object Small extends WaypointSize

/**
  * This supports items and entities(Player, Monsters)
  */
object Medium extends WaypointSize

/**
  * This supports Blocks (useful for faiths)
  */
object Large extends WaypointSize
