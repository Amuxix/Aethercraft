package me.amuxix.runes.waypoints

import cats.data.EitherT
import cats.effect.IO
import cats.implicits.{catsStdInstancesForEither, toTraverseOps}
import me.amuxix._
import me.amuxix.inventory.Item
import me.amuxix.pattern._
import me.amuxix.position.BlockPosition
import me.amuxix.runes.Rune
import me.amuxix.runes.waypoints.WaypointSize.Medium

/**
  * Created by Amuxix on 03/01/2017.
  */
object Waypoint extends RunePattern[Waypoint] {
  override val castableVertically: Boolean = true
  // format: off
  override val layers: List[BaseLayer] = List(
    ActivationLayer(
      NotInRune, Tier,      Tier,      Tier,      NotInRune,
      Tier,      Tier,      Signature, Tier,      Tier,
      Tier,      Signature, Tier,      Signature, Tier,
      Tier,      Tier,      Signature, Tier,      Tier,
      NotInRune, Tier,      Tier,      Tier,      NotInRune
    )
  )
  // format: on

  /**
    * This knows how to load a waypoint with the given parameters.
    * @param center Center of the rune
    * @param creator Who owns this rune
    * @param direction Activation this teleport will teleport to
    * @param rotation The rotation this waypoint was created in
    * @param signature Signature of the waypoint
    * @return A waypoint instance with the given parameters.
    */
  def apply(
    center: BlockPosition,
    creator: Player,
    direction: Direction,
    rotation: Matrix4,
    signature: Int
  ): Waypoint = {
    val waypoint = Waypoint(center, creator, direction, rotation, pattern)
    waypoint.signature = signature
    waypoint
  }
}

case class Waypoint(
  center: BlockPosition,
  creator: Player,
  direction: Direction,
  rotation: Matrix4,
  pattern: Pattern
) extends Rune
    with GenericWaypoint {
  override val size: WaypointSize = Medium

  override def validateSignature: Option[String] =
    Option
      .when(signatureIsEmpty)("Signature is empty!")
      .orWhen(signatureContains(tierMaterial))(
        s"${tierMaterial.name} can't be used on this Waypoint because it is the same as the tier used in rune."
      )
      .orWhen(GenericWaypoint.waypoints.contains(calculateSignature))("Signature already in use.")

  /**
    * Checks whether this rune can be activated, should warn activator about the error that occurred
    *
    * @return true if rune can be activated, false if an error occurred
    */
  override def notifyActivator: IO[Unit] =
    super.notifyActivator.flatMap(_ => activator.notify("Signature is no longer needed here."))

  /**
    * Updates the rune with the new changes and notifies the player who triggered the update
    *
    * @param player Player who triggered the update
    */
  override def update(player: Player): EitherT[IO, String, Boolean] =
    if (signature == calculateSignature) {
      EitherT.leftT(s"This $name is already active.")
    } else {
      EitherT(validateSignature
        .orElse(Option.when(player.uuid != activator.uuid)(s"The signature of your $name in $center was changed!"))
        .toLeft(())
        .traverse { _ =>
          val currentFile = GenericWaypoint.getFile(this)
          GenericWaypoint.waypoints -= signature
          signature = calculateSignature
          GenericWaypoint.waypoints += signature -> this
          for {
            _ <- activator.notify("Signature updated.")
            _ <- GenericWaypoint.updateFileName(this, currentFile)
          } yield ()
        }
      )
        .map(_ => true)
    }

  /**
    * Destroys the rune effect. This should undo all lasting effects this rune introduced.
    */
  override def destroyRune(): Unit = GenericWaypoint.waypoints -= signature

  override protected def onActivate(activationItem: Option[Item]): EitherT[IO, String, Boolean] =
    EitherT.liftF {
      GenericWaypoint.waypoints += signature -> this
      GenericWaypoint.saveAsync(this)
        .map(_ => true)
    }

  /**
    * Should this rune use a true name if the activator is wearing one?
    */
  override val shouldUseTrueName: Boolean = false
}
