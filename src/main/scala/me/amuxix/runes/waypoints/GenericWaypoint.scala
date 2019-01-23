package me.amuxix.runes.waypoints

import io.circe.{Decoder, Encoder}
import me.amuxix.{Direction, Player}
import me.amuxix.block.Block
import me.amuxix.block.Block.Location
import me.amuxix.exceptions.DeserializationException
import me.amuxix.runes.Rune
import me.amuxix.runes.traits.{Linkable, Tiered}
import me.amuxix.runes.waypoints.Waypoint.pattern
import me.amuxix.runes.waypoints.WaypointSize.Medium

/**
  * Created by Amuxix on 02/01/2017.
  */
object GenericWaypoint {
  implicit val encodeWaypoints: Encoder[GenericWaypoint] =
    Encoder.forProduct6("blocks", "center", "activator", "direction", "signature", "size")(w => (w.blocks, w.center, w.activator, w.direction, w.signature, w.size))

  implicit val decodeWaypoints: Decoder[GenericWaypoint] =
    Decoder.forProduct6[GenericWaypoint, Array[Array[Array[Block]]], Location, Player, Direction, Int, WaypointSize]("blocks", "center", "activator", "direction", "signature", "size") {
      case (blocks, center, activator, direction, signature, `Medium`) =>
        Waypoint.deserialize(blocks, center, activator, direction, signature)
        val waypoint = Waypoint(blocks, center, activator, direction, pattern)
        waypoint.signature = signature
        waypoint
      case _ => throw DeserializationException("a Waypoint")
    }
}

/**
  * This represents a point that can be targeted by a waypoint.
  */
trait GenericWaypoint extends Tiered with Linkable { this: Rune =>
  val size: WaypointSize
}
