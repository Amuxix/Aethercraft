package me.amuxix.runes

import me.amuxix.Runecraft
import me.amuxix.pattern._
import me.amuxix.runes.traits.{Linkable, Persistent, Tiered}
import me.amuxix.util.{Block, Matrix4, Vector3}
import org.bukkit.event.player.PlayerInteractEvent

/**
  * Created by Amuxix on 02/01/2017.
  */
object Waypoint extends RunePattern {
  val pattern = Pattern(Waypoint.apply, width = 5)(
    ActivationLayer(
      NotInRune, Tier,      Tier,      Tier,      NotInRune,
      Tier,      Tier,      Signature, Tier,      Tier,
      Tier,      Signature, Tier,      Signature, Tier,
      Tier,      Tier,      Signature, Tier,      Tier,
      NotInRune, Tier,      Tier,      Tier,      NotInRune
    )
  )
}

case class Waypoint(event: PlayerInteractEvent, blocks: Array[Array[Array[Block]]], rotation: Matrix4, rotationCenter: Vector3[Int], pattern: Pattern)
  extends Rune
  with Tiered
  with Linkable
  with Persistent {

  val monitoredBlocks: Seq[Block] = tierBlocks

  Runecraft.mediumWaypoints += signature -> this

  override def destroyRune(): Unit = ???
}
