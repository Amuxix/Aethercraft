package me.amuxix.runes

import me.amuxix.Configuration.{maxBlocksBouncedByTeleporter => maxDistance}
import me.amuxix.Runecraft
import me.amuxix.logging.Logger.info
import me.amuxix.pattern._
import me.amuxix.runes.exceptions.RuneInitializationException
import me.amuxix.runes.teleports.WaypointTrait
import me.amuxix.runes.traits.{Consumable, Linkable, Tiered}
import me.amuxix.util.Block.Location
import me.amuxix.util._
import org.bukkit.Material.{CHORUS_FLOWER, CHORUS_PLANT}
import org.bukkit.event.player.PlayerInteractEvent

import scala.math.Numeric.DoubleAsIfIntegral

/**
  * Created by Amuxix on 03/01/2017.
  */
object Teleporter extends RunePattern {
  val pattern: Pattern = Pattern(Teleporter.apply, width = 5, verticality = true)(
    ActivationLayer(
      NotInRune, Tier, Signature, Tier, NotInRune,
      Tier, Tier, Tier, Tier, Tier,
      Signature, Tier, Key, Tier, Signature,
      Tier, Tier, Tier, Tier, Tier,
      NotInRune, Tier, Signature, Tier, NotInRune
    )
  )
}

case class Teleporter(event: PlayerInteractEvent, blocks: Array[Array[Array[Block]]], rotation: Matrix4, rotationCenter: Vector3[Int], pattern: Pattern)
  extends Rune
          with Tiered
          with Consumable
          with Linkable {
  implicit val doubleAsIfIntegral = DoubleAsIfIntegral

  override def validateSignature(player: Player): Boolean = {
    if (signatureIsEmpty) {
      throw RuneInitializationException("Signature is empty!")
    } else if (signatureContains(tierType)) {
      throw RuneInitializationException(tierType.name() + " can't be used on this rune because it is the same as the tier used in rune.")
    }
    true
  }

  override def logRuneActivation(): Unit = info(activator.name + " teleport from " + center + " to " + finalTarget)

  var finalTarget: Position[Double] = _

  if (validateSignature(activator)) {
    val possibleTargets: Map[Int, Rune with WaypointTrait] = Runecraft.waypoints
    val targetWP: Rune with WaypointTrait = possibleTargets.getOrElse(signature, throw RuneInitializationException("Can't find your destination."))

    /** Location where this teleport will teleport to. Warns rune activator is cannot find a location */
    val target: Location = {
      (1 to maxDistance).find((dist) => {
        val possibleTarget: Location = targetWP.center + targetWP.direction * dist
        possibleTarget.block.material.isSolid == false && possibleTarget.canFitPlayer
      }) match {
        case Some(dist) => targetWP.center + targetWP.direction * dist
        case None => throw RuneInitializationException("The way is barred on the other side!")
      }
    }

    val targetWPTier: Int = {
      var targetTier = targetWP.tier
      if (targetWP.center.world != center.world) targetTier += 1
      targetTier + scala.math.min(0, scala.math.log10(center.distance(target) - 1000).toInt)
    }

    if (targetWPTier > tier && (tierType != CHORUS_PLANT || tierType != CHORUS_FLOWER)) {
      throw RuneInitializationException("This teleporter is not powerful enough to reach the destination.")
    } else {
      val blockCenterOffset: Vector3[Double] = {
        targetWP.direction match {
          case Up | Down => Vector3[Double](0.5, 0, 0.5)
          case East | West => Vector3[Double](0, 0.5, 0.5)
          case North | South => Vector3[Double](0.5, 0.5, 0)
          case _ => Vector3[Double](0, 0, 0)
        }
      }
      finalTarget = Position[Double](target.world, Vector3[Double](target.x, target.y, target.z) + blockCenterOffset)
    }
  }
  activator.teleport(finalTarget, activator.pitch, activator.yaw)
}
