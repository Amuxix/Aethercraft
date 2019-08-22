package me.amuxix.runes.waypoints

import cats.effect.IO
import io.circe.{Decoder, Encoder}
import me.amuxix.Matrix4.{decoder => matrixDecoder, encoder => matrixEncoder}
import me.amuxix.exceptions.DeserializationException
import me.amuxix.position.BlockPosition
import me.amuxix.runes.Rune
import me.amuxix.runes.traits.{Linkable, Persistent, Tiered}
import me.amuxix.runes.waypoints.WaypointSize.Medium
import me.amuxix.serialization.PersistableRune
import me.amuxix.{Direction, Matrix4, Player, World}

import scala.math.log10

/**
  * Created by Amuxix on 02/01/2017.
  */
object GenericWaypoint extends PersistableRune[GenericWaypoint] {

  /** The map key is the [[me.amuxix.runes.traits.Linkable.signature]] of the waypoint */
  var waypoints = Map.empty[Int, GenericWaypoint]

  implicit val encoder: Encoder[GenericWaypoint] =
    Encoder.forProduct6("center", "activator", "direction", "rotation", "signature", "size")(w => (w.center, w.activator, w.direction, w.rotation, w.signature, w.size))

  implicit val decoder: Decoder[GenericWaypoint] =
    Decoder.forProduct6[GenericWaypoint, BlockPosition, Player, Direction, Matrix4, Int, WaypointSize]("center", "activator", "direction", "rotation", "signature", "size") {
      case (center, activator, direction, rotation, signature, `Medium`) =>
        Waypoint(center, activator, direction, rotation, signature)
      case _ => throw DeserializationException("a Waypoint")
    }

  override protected def runes: Map[World, Map[String, GenericWaypoint]] = waypoints.groupBy(_._2.center.world).map {
    case (world, map) => world -> map.map {
      case (signature, waypoint) => signature.toString -> waypoint
    }
  }

  override protected val persistablesName: String = "Waypoints"

  override protected def updateWithLoaded(fileName: String, thing: GenericWaypoint): Unit = this.waypoints += fileName.toInt -> thing

  def saveOneAsync(waypoint: GenericWaypoint): IO[Unit] = {
    val folder = runeMagicFolder(waypoint.center.world)
    saveOneAsync(folder, waypoint.signature.toString, waypoint)
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
