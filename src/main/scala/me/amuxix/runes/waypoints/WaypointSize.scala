package me.amuxix.runes.waypoints

import enumeratum.{CirceEnum, Enum, EnumEntry}

import scala.collection.immutable.IndexedSeq

/**
  * Created by Amuxix on 03/01/2017.
  */
sealed trait WaypointSize extends EnumEntry

object WaypointSize extends CirceEnum[WaypointSize] with Enum[WaypointSize] {
  override def values: IndexedSeq[WaypointSize] = findValues

  case object Small extends WaypointSize //This supports items.
  case object Medium extends WaypointSize //This supports items and entities(Player, Monsters)
  case object Large extends WaypointSize //This supports Blocks (useful for faiths)
}

