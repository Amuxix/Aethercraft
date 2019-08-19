package me.amuxix.runes

import cats.data.EitherT
import cats.effect.IO
import me.amuxix.block.Block
import me.amuxix.inventory.Item
import me.amuxix.material.Material.{RedstoneTorch, RedstoneWire}
import me.amuxix.pattern._
import me.amuxix.position.BlockPosition
import me.amuxix.runes.traits.LinkableTiered
import me.amuxix.runes.traits.enchants._
import me.amuxix.{Direction, Matrix4, Player}

object Portkey extends RunePattern[Portkey] with Enchant with BlockInteractTrigger with AirInteractTrigger {
  // format: off
  override val layers: List[ActivationLayer] = List(
    ActivationLayer(
      Tier,          RedstoneWire, RedstoneWire,  RedstoneWire, Tier,
      RedstoneWire,  Signature,    RedstoneWire,  Signature,    RedstoneWire,
      RedstoneWire,  RedstoneWire, RedstoneTorch, RedstoneWire, RedstoneWire,
      RedstoneWire,  Signature,    RedstoneWire,  Signature,    RedstoneWire,
      Tier,          RedstoneWire, RedstoneWire,  RedstoneWire, Tier,
    )
  )
  // format: on
  /** The validation to check if the item can be enchanted. */
  override def itemValidation(item: Item): Boolean = !item.material.isBlock && !item.material.isUsable

  /** The description of possible items that can be enchanted by this rune to be given as error if it fails. */
  override val itemDescription: String = "items without a right click to use"

  /** This should run the effect of the enchant and return whether to cancel the event or not */
  override def onAirInteract(
    player: Player,
    itemInHand: Item
  ): EitherT[IO, String, Boolean] =
    for {
      tierAndSignature <- EitherT.fromEither[IO](itemInHand.findEnchantState[String](this)).map(_.split(",").toList.map(_.toInt))
      List(tier, signature) = tierAndSignature
      playerPosition <- EitherT.fromOption[IO](player.position, "Unable to find player position")
      _ <- Teleporter.validateAndTeleport(player, playerPosition.toBlockPosition, signature, tier)
      _ <- EitherT.liftF(itemInHand.destroy(1))
      _ <- EitherT.liftF(player.notify(s"Your $name crumbles as you move through the Aether."))
    } yield false

  /** This should run the effect of the enchant and return whether to cancel the event or not */
  override def onBlockInteract(
    player: Player,
    itemInHand: Item,
    clickedBlock: Block,
    blockFace: Direction
  ): EitherT[IO, String, Boolean] = onAirInteract(player, itemInHand)
}

case class Portkey(
  center: BlockPosition,
  creator: Player,
  direction: Direction,
  rotation: Matrix4,
  pattern: Pattern
) extends Rune
    with RuneEnchant
    with LinkableTiered {

  override def addRuneEnchant(item: Item): EitherT[IO, String, Unit] = item.addRuneEnchantWithState(enchant, s"$tier,$signature")

  override val enchant: Enchant = Portkey
}
