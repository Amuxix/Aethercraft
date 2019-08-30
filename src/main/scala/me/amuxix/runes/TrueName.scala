package me.amuxix.runes

import cats.data.EitherT
import cats.effect.IO
import me.amuxix._
import me.amuxix.block.Block
import me.amuxix.bukkit.inventory.Item
import me.amuxix.inventory.Item
import me.amuxix.inventory.items.PlayerHead
import me.amuxix.material.Material.{Fire, PlayerHead => PlayerHeadMaterial}
import me.amuxix.pattern._
import me.amuxix.position.BlockPosition
import me.amuxix.runes.traits.ConsumableBlocks
import me.amuxix.runes.traits.enchants.{BlockPlaceTrigger, Enchant}

/**
  * Created by Amuxix on 01/02/2017.
  */
object TrueName extends RunePattern[TrueName] with Enchant with BlockPlaceTrigger {

  // format: off
  override val layers: List[BaseLayer] = List(
    ActivationLayer(
      NotInRune, Tier, Tier,      Tier, NotInRune,
      Tier,      Fire, Tier,      Fire, Tier,
      Tier,      Tier, NotInRune, Tier, Tier,
      NotInRune, Tier, Tier,      Tier, NotInRune,
      NotInRune, Tier, NotInRune, Tier, NotInRune,
    )
  )
  // format: on
  /** The validation to check if the item can be enchanted. */
  override def itemValidation(item: Item): Boolean = item.material == PlayerHeadMaterial

  /** The description of possible items that can be enchanted by this rune to be given as error if it fails. */
  override val itemDescription: String = "player's heads"

  def createTrueNameOf(player: Player): EitherT[IO, String, PlayerHead] = {
    val trueName: PlayerHead = Item(PlayerHeadMaterial).asInstanceOf[PlayerHead]
    for {
      _ <- trueName.setOwner(player)
      _ <- trueName.setDisplayName(trueNameDisplayFor(player))
      _ <- trueName.addVanillaCurses()
      _ <- trueName.addRuneEnchant(TrueName)
    } yield trueName
  }

  def trueNameDisplayFor(player: Player): String =
    s"${player.name.get}'s ${TrueName.name}"

  //This will cancel the block place if the item being placed is a true name of another player
  /** This should run the effect of the enchant and return whether to cancel the event or not */
  override def onBlockPlace(
    player: Player,
    placedBlock: Block,
    placedAgainstBlock: Block,
    itemPlaced: Option[Item]
  ): EitherT[IO, String, Boolean] =
    itemPlaced match {
      //Trying to place own true name, allow this.
      case Some(head: PlayerHead) if head.isTrueNameOf(player) => EitherT.rightT(false)

      //Trying to place someone else's true name, destroy it.
      case Some(head: PlayerHead) if head.hasRuneEnchant(this) =>
        player.notifyError(s"As you place ${head.displayName} it crumbles to dust.")
        EitherT(head.destroyAll.map[Either[String, Boolean]](_ => Right(true)))

      case _ => EitherT.rightT(false)
    }
}

case class TrueName(
  center: BlockPosition,
  creator: Player,
  direction: Direction,
  rotation: Matrix4,
  pattern: Pattern
) extends Rune
    with ConsumableBlocks {
  override val shouldUseTrueName: Boolean = false

  /**
    * Internal activate method that should contain all code to activate a rune.
    */
  override protected def onActivate(activationItem: Option[Item]): EitherT[IO, String, Boolean] =
    if (direction == Self) {
      EitherT.leftT("This rune cannot be automated.")
    } else {
      for {
        _ <- activator.addMaximumEnergyFrom(consume)
        trueName <- TrueName.createTrueNameOf(activator)
        _ <- activator.add(trueName).toLeft(())
      } yield true
    }
}
