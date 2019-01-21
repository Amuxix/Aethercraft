package me.amuxix.runes.waypoints

import me.amuxix.block.Block.Location
import me.amuxix.block.Block
import me.amuxix._
import me.amuxix.inventory.Item
import me.amuxix.exceptions.InitializationException
import me.amuxix.pattern._
import me.amuxix.runes.traits.{Linkable, Persistent}
import me.amuxix.runes.Rune
import me.amuxix.runes.waypoints.WaypointSize.Medium
import org.bukkit.ChatColor

/**
  * Created by Amuxix on 03/01/2017.
  */
object Waypoint extends RunePattern {
  val pattern: Pattern = Pattern(Waypoint.apply, verticality = true)(
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
    * @param creator Who owns this rune
    * @param direction Activation this teleport will teleport to
    * @param signature Signature of the waypoint
    * @return A waypoint instance with the given parameters.
    */
  def deserialize(blocks: Array[Array[Array[Block]]], center: Location, creator: Player, direction: Direction, signature: Int): Waypoint = {
    val waypoint = Waypoint(blocks, center, creator, direction, pattern)
    waypoint.signature = signature
    waypoint
  }
}

case class Waypoint(blocks: Array[Array[Array[Block]]], center: Location, creator: Player, direction: Direction, pattern: Pattern)
  extends Rune
          with GenericWaypoint
          with Linkable
          with Persistent {
  override val size: WaypointSize = Medium

  override def validateSignature(): Boolean = {
    if (signatureIsEmpty) {
      throw InitializationException("Signature is empty!")
    } else if (signatureContains(tierMaterial)) {
      throw InitializationException(tierMaterial.name + " can't be used on this rune because it is the same as the tier used in rune.")
    } else if (Serialization.waypoints.contains(signature)) {
      throw InitializationException("Signature already in use.")
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
    activator.notify("Signature is no longer needed here.")
  }

  /**
    * Updates the rune with the new changes and notifies the player who triggered the update
    *
    * @param player Player who triggered the update
    */
  override def update(player: Player): Boolean = {
    if (signature == calculateSignature()) {
      throw InitializationException("This " + getClass.getSimpleName + " is already active.")
    } else {
      if (validateSignature()) {
        signature = calculateSignature()
        player.notify("Signature updated.")
        if (player.uuid != activator.uuid) {
          activator.notify(ChatColor.RED + "The signature of your " + getClass.getSimpleName + " in " + center + " was changed!")
        }
        true
      } else {
        false
      }
    }
  }

  /**
    * Destroys the rune effect. This should undo all lasting effects this rune introduced.
    */
  override def destroyRune(): Unit = Serialization.waypoints -= signature

  override protected def onActivate(activationItem: Item): Boolean = {
    Serialization.waypoints += signature -> this
    true
  }

  /**
    * Should this rune use a true name if the activator is wearing one?
    */
  override val shouldUseTrueName: Boolean = false
}
