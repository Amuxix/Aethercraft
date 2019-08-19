package me.amuxix.runes.waypoints

import io.circe.{Decoder, Encoder}
import me.amuxix.{Direction, Matrix4, Player}
import me.amuxix.exceptions.DeserializationException
import me.amuxix.runes.Rune
import me.amuxix.Matrix4.{decoder, encoder}
import me.amuxix.position.BlockPosition
import me.amuxix.runes.traits.{Linkable, Persistent, Tiered}
import me.amuxix.runes.waypoints.Waypoint.pattern
import me.amuxix.runes.waypoints.WaypointSize.Medium

import scala.math.log10

/**
  * Created by Amuxix on 02/01/2017.
  */
object GenericWaypoint {
  implicit val encodeWaypoints: Encoder[GenericWaypoint] =
    Encoder.forProduct6("center", "activator", "direction", "rotation", "signature", "size")(w => (w.center, w.activator, w.direction, w.rotation, w.signature, w.size))

  implicit val decodeWaypoints: Decoder[GenericWaypoint] =
    Decoder.forProduct6[GenericWaypoint, BlockPosition, Player, Direction, Matrix4, Int, WaypointSize]("center", "activator", "direction", "rotation", "signature", "size") {
      case (center, activator, direction, rotation, signature, `Medium`) =>
        Waypoint.deserialize(center, activator, direction, rotation, signature)
        val waypoint = Waypoint(center, activator, direction, rotation, pattern)
        waypoint.signature = signature
        waypoint
      case _ => throw DeserializationException("a Waypoint")
    }
}

/**
  * This represents a point that can be targeted by a waypoint.
  */
trait GenericWaypoint extends Tiered with Linkable with Persistent { this: Rune =>
  val size: WaypointSize

  /**
    * Calculates tier required to travel to this waypoint from a given BlockPosition
    */
  def tierRequiredToTravelHere(from: BlockPosition): Int =
    from.distance(center).fold(5) {
      case distance if distance >= 1000 => log10(distance / 100).toInt
      case _ => 0
    }
}
