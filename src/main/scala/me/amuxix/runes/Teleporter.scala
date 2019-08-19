package me.amuxix.runes

import cats.data.EitherT
import cats.effect.IO
import me.amuxix._
import me.amuxix.bukkit.Configuration.maxBlocksBouncedByTeleporter
import me.amuxix.inventory.Item
import me.amuxix.material.Material.{ChorusFlower, ChorusPlant}
import me.amuxix.pattern._
import me.amuxix.position.{BlockPosition, EntityPosition, Vector3}
import me.amuxix.runes.traits._
import me.amuxix.runes.waypoints.WaypointSize.Medium
import me.amuxix.runes.waypoints.{GenericWaypoint, Waypoint}

/**
  * Created by Amuxix on 03/01/2017.
  */
object Teleporter extends RunePattern[Teleporter] {
  override val castableVertically: Boolean = true
  // format: off
  override val layers: List[BaseLayer] = List(
    ActivationLayer(
      NotInRune, Tier, Signature, Tier, NotInRune,
      Tier, Tier, Tier, Tier, Tier,
      Signature, Tier, Key, Tier, Signature,
      Tier, Tier, Tier, Tier, Tier,
      NotInRune, Tier, Signature, Tier, NotInRune
    )
  )
  // format: on

  /** Teleports the player to the center of the block instead of the corner */
  private def centerTeleport(
    bouncedTarget: BlockPosition,
    targetWaypoint: GenericWaypoint
  ): EntityPosition = {
    val blockCenterOffset: Vector3[Double] =
      targetWaypoint.direction match {
        case Up | Down     => Vector3[Double](0.5, 0, 0.5)
        case East | West   => Vector3[Double](0, 0.5, 0.5)
        case North | South => Vector3[Double](0.5, 0.5, 0)
        case _             => Vector3[Double](0, 0, 0)
      }
    bouncedTarget.toEntityPosition + blockCenterOffset
  }

  /** Location where this teleport will teleport to. Warns rune activator is cannot find a location */
  def bounceAndCenterTarget(target: GenericWaypoint): Either[String, EntityPosition] =
    (1 to maxBlocksBouncedByTeleporter).to(LazyList)
      .map(target.center + target.direction * _)
      .collectFirst {
        case possibleTarget if possibleTarget.block.material.isSolid == false && possibleTarget.canFitPlayer =>
          Right(centerTeleport(possibleTarget, target))
      }
      .getOrElse(Left("The way is barred on the other side!"))

  /**
    * Checks if the difference in tiers is high enough to support travel
    *
    * This increases required tier by the 1 for each factor of 10 of the distance / 1000
    * This means if distance is in [1000, 9999], the increase will be 1
    * if distance is in [10000, 99999], the increase will be 2
    * if distance is in [100000, 999999], the increase will be 3
    * etc
    */
  def validateTier(source: Tiered, tierRequired: Int): Either[String, Unit] =
    Either.cond(
      tierRequired < source.tier && (source.tierMaterial != ChorusPlant || source.tierMaterial != ChorusFlower),
      (),
      s"This ${source.name} is not powerful enough to reach the destination."
    )

  def validateAndTeleport(player: Player, from: BlockPosition, targetSignature: Int, tier: Int): EitherT[IO, String, EntityPosition] =
    for {
      targetWaypoint <- EitherT.fromEither[IO](Waypoint.waypoints.get(targetSignature).toRight("Can't find your destination."))
      _ <- EitherT.cond[IO](targetWaypoint.size == Medium, (), "Target waypoint denied your request.")
      tierRequired = targetWaypoint.tierRequiredToTravelHere(from)
      _ <- EitherT.cond[IO](tierRequired < tier,
        (),
        s"This ${name} is not powerful enough to reach the destination."
      )
      _ <- EitherT.cond[IO](targetWaypoint.tier >= tierRequired, (), "Target waypoint cannot accept connections from this far.")
      target <- EitherT.fromEither[IO](Teleporter.bounceAndCenterTarget(targetWaypoint))
      _ <- player.teleportTo(target, player.pitch, player.yaw)
    } yield target
}

case class Teleporter(
  center: BlockPosition,
  creator: Player,
  direction: Direction,
  rotation: Matrix4,
  pattern: Pattern
) extends Rune
    with LinkableTiered
    with ConsumableBlocks {

  override protected def onActivate(activationItem: Option[Item]): EitherT[IO, String, Boolean] =
    Teleporter.validateAndTeleport(activator, center, signature, tier).map { target =>
      activationLogMessage = s"${activator.name} teleported from $center to $target"
      true
    }

  /**
    * Should this rune use a true name if the activator is wearing one?
    */
  override val shouldUseTrueName: Boolean = true
}
