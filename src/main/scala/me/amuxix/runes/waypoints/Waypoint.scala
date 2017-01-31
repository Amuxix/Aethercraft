package me.amuxix.runes.waypoints

import me.amuxix.Runecraft
import me.amuxix.pattern._
import me.amuxix.runes.exceptions.RuneInitializationException
import me.amuxix.runes.traits.{Linkable, Persistent}
import me.amuxix.runes.{Rune, RuneParameters}
import me.amuxix.util.Block.Location
import me.amuxix.util._
import org.bukkit.ChatColor

/**
  * Created by Amuxix on 03/01/2017.
  */
object Waypoint extends RunePattern {
  val pattern: Pattern = Pattern(Waypoint.apply, width = 5, verticality = true)(
    ActivationLayer(
      NotInRune, Tier,      Tier,      Tier,      NotInRune,
      Tier,      Tier,      Signature, Tier,      Tier,
      Tier,      Signature, Tier,      Signature, Tier,
      Tier,      Tier,      Signature, Tier,      Tier,
      NotInRune, Tier,      Tier,      Tier,      NotInRune
    )
  )

  /**
    * This knows how to load a waypoint with the given parameters.
    * @param blocks Blocks that make up the rune
    * @param center Center of the rune
    * @param activator Who owns this rune
    * @param direction Activation this teleport will teleport to
    * @param signature Signature of the waypoint
    * @return A waypoint instance with the given parameters.
    */
  def deserialize(blocks: Array[Array[Array[Block]]], center: Location, activator: Player, direction: CardinalPoint, signature: Int): Waypoint = {
    val parameters: RuneParameters = RuneParameters(blocks, center, activator, direction)
    val waypoint = Waypoint(parameters, pattern)
    waypoint.signature = signature
    waypoint
  }
}

case class Waypoint(parameters: RuneParameters, pattern: Pattern)
  extends Rune(parameters)
          with WaypointTrait
          with Linkable
          with Persistent {
  override val size: WaypointSize = Medium

  override def validateSignature(): Boolean = {
    if (signatureIsEmpty) {
      throw RuneInitializationException("Signature is empty!")
    } else if (signatureContains(tierType)) {
      throw RuneInitializationException(tierType.name + " can't be used on this rune because it is the same as the tier used in rune.")
    } else if (Runecraft.waypoints.contains(signature)) {
      throw RuneInitializationException("Signature already in use.")
    }
    true
  }

  /**
    * Checks whether this rune can be activated, should warn activator about the error that occurred
    *
    * @return true if rune can be activated, false if an error occurred
    */
  override def notifyActivator(): Unit = {
    super.notifyActivator()
    activator.sendMessage("Signature is no longer needed here.")
  }

  /**
    * Updates the rune with the new changes and notifies the player who triggered the update
    *
    * @param player Player who triggered the update
    */
  override def update(player: Player): Unit = {
    if (signature == calculateSignature()) {
      throw RuneInitializationException("This " + getClass.getSimpleName + " is already active.")
    } else {
      if (validateSignature()) {
        signature = calculateSignature()
        player.sendMessage("Signature updated.")
        if (player.uniqueID != activator.uniqueID) {
          activator.sendMessage(ChatColor.RED + "The signature of your " + getClass.getSimpleName + " in " + center + " was changed!")
        }
      }
    }
  }

  /**
    * Destroys the rune effect. This should undo all lasting effects this rune introduced.
    */
  override def destroyRune(): Unit = Runecraft.waypoints -= signature

  override protected def innerActivate(): Unit = {
    Runecraft.waypoints += signature -> this
  }
}
