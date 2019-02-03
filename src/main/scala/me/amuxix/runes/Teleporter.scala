package me.amuxix.runes

import cats.data.EitherT
import cats.effect.IO
import me.amuxix._
import me.amuxix.block.Block.Location
import me.amuxix.bukkit.Configuration.{maxBlocksBouncedByTeleporter => maxDistance}
import me.amuxix.inventory.Item
import me.amuxix.logging.Logger.info
import me.amuxix.material.Material.{ChorusFlower, ChorusPlant}
import me.amuxix.pattern._
import me.amuxix.runes.traits._
import me.amuxix.runes.waypoints.{GenericWaypoint, Waypoint}

import scala.math.log10


/**
  * Created by Amuxix on 03/01/2017.
  */
object Teleporter extends RunePattern {
  val pattern: Pattern = Pattern(Teleporter.apply, verticality = true)(
    ActivationLayer(
      NotInRune, Tier, Signature, Tier, NotInRune,
      Tier, Tier, Tier, Tier, Tier,
      Signature, Tier, Key, Tier, Signature,
      Tier, Tier, Tier, Tier, Tier,
      NotInRune, Tier, Signature, Tier, NotInRune
    )
  )
}

case class Teleporter(center: Location, creator: Player, direction: Direction, rotation: Matrix4, pattern: Pattern) extends Rune with Tiered with ConsumableBlocks with Linkable {
  override def validateSignature: Option[String] = {
    Option.when(signatureIsEmpty)("Signature is empty!")
      .orWhen(signatureContains(tierMaterial))(s"${tierMaterial.name} can't be used on this Teleporter because it is the same as the tier used in rune.")
  }

  var finalTarget: Position[Double] = _

  override def logRuneActivation: IO[Unit] = info(s"${activator.name} teleported from $center to $finalTarget")

  override protected def onActivate(activationItem: Option[Item]): EitherT[IO, String, Boolean] = {
    /** Location where this teleport will teleport to. Warns rune activator is cannot find a location */
    def bounceTarget(target: GenericWaypoint): Either[String, Location] =
      (1 to maxDistance).toStream
        .map(target.center + target.direction * _)
        .collectFirst {
          case possibleTarget if possibleTarget.block.material.isSolid == false && possibleTarget.canFitPlayer =>
            Right(possibleTarget)
        }
        .getOrElse(Left("The way is barred on the other side!"))

    def calculateFinalTarget(bouncedTarget: Location, targetWaypoint: GenericWaypoint): Position[Double] = {
      val blockCenterOffset: Vector3[Double] =
        targetWaypoint.direction match {
          case Up | Down => Vector3[Double](0.5, 0, 0.5)
          case East | West => Vector3[Double](0, 0.5, 0.5)
          case North | South => Vector3[Double](0.5, 0.5, 0)
          case _ => Vector3[Double](0, 0, 0)
        }
      bouncedTarget.toDoublePosition + blockCenterOffset
    }

    /**
      * This increases required tier by the 1 for each factor of 10 of the distance / 1000
      * This means if distance is in [1000, 9999], the increase will be 1
      * if distance is in [10000, 99999], the increase will be 2
      * if distance is in [100000, 999999], the increase will be 3
      * etc
      */
    def checkTier(target: GenericWaypoint): Option[String] = {
      val distance = center.distance(target.center)
      val distanceModifier = if (distance >= 1000) log10(distance / 100).toInt else 0
      val worldModifier = if (target.center.world != center.world) 1 else 0
      val requiredTier = target.tier + distanceModifier + worldModifier
      Option.when(requiredTier > tier && (tierMaterial != ChorusPlant || tierMaterial != ChorusFlower)) {
        "This teleporter is not powerful enough to reach the destination."
      }
    }

    for {
      targetWaypoint <- EitherT.fromEither[IO](Waypoint.waypoints.get(signature).toRight("Can't find your destination."))
      _ <- EitherT.fromEither[IO](checkTier(targetWaypoint).toLeft(()))
      bouncedTarget <- EitherT.fromEither[IO](bounceTarget(targetWaypoint))
      finalTarget = calculateFinalTarget(bouncedTarget, targetWaypoint)
      _ <- activator.teleportTo(finalTarget, activator.pitch, activator.yaw).toLeft(())
    } yield {
      this.finalTarget = finalTarget
      true
    }
  }

  /**
    * Should this rune use a true name if the activator is wearing one?
    */
  override val shouldUseTrueName: Boolean = true
}
