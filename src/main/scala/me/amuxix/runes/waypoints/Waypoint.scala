package me.amuxix.runes.waypoints

import cats.data.EitherT
import cats.effect.IO
import me.amuxix._
import me.amuxix.block.Block.Location
import me.amuxix.bukkit.Aethercraft
import me.amuxix.inventory.Item
import me.amuxix.pattern._
import me.amuxix.runes.Rune
import me.amuxix.runes.traits.{Linkable, Persistent}
import me.amuxix.runes.waypoints.WaypointSize.Medium

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
    * @param center Center of the rune
    * @param creator Who owns this rune
    * @param direction Activation this teleport will teleport to
    * @param rotation The rotation this waypoint was created in
    * @param signature Signature of the waypoint
    * @return A waypoint instance with the given parameters.
    */
  def deserialize(center: Location, creator: Player, direction: Direction, rotation: Matrix4, signature: Int): Waypoint = {
    val waypoint = Waypoint(center, creator, direction, rotation, pattern)
    waypoint.signature = signature
    waypoint
  }
}

case class Waypoint(center: Location, creator: Player, direction: Direction, rotation: Matrix4, pattern: Pattern)
  extends Rune
          with GenericWaypoint
          with Linkable
          with Persistent {
  override val size: WaypointSize = Medium

  override def validateSignature: Option[String] =
    Option.when(signatureIsEmpty)("Signature is empty!")
    .orWhen(signatureContains(tierMaterial))(s"${tierMaterial.name} can't be used on this Waypoint because it is the same as the tier used in rune.")
    .orWhen(Serialization.waypoints.contains(signature))("Signature already in use.")

  /**
    * Checks whether this rune can be activated, should warn activator about the error that occurred
    *
    * @return true if rune can be activated, false if an error occurred
    */
  override def notifyActivator: IO[Unit] = {
    super.notifyActivator
    activator.notify("Signature is no longer needed here.")
  }

  /**
    * Updates the rune with the new changes and notifies the player who triggered the update
    *
    * @param player Player who triggered the update
    */
  override def update(player: Player): EitherT[IO, String, Boolean] = {
    if (signature == calculateSignature) {
      EitherT.leftT(s"This $name is already active.")
    } else {
      EitherT.fromEither {
        validateSignature
          .orElse(Option.when(player.uuid != activator.uuid)(s"The signature of your $name in $center was changed!"))
          .toLeft{
            signature = calculateSignature
            this.activationMessage = "Signature updated."
            true
          }
      }
    }
  }

  /**
    * Destroys the rune effect. This should undo all lasting effects this rune introduced.
    */
  override def destroyRune(): Unit = Serialization.waypoints -= signature

  override protected def onActivate(activationItem: Option[Item]): EitherT[IO, String, Boolean] = {
    EitherT.rightT {
      Serialization.waypoints += signature -> this
      true
    }
  }

  /**
    * Should this rune use a true name if the activator is wearing one?
    */
  override val shouldUseTrueName: Boolean = false
}
