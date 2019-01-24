package me.amuxix.runes

import me.amuxix._
import me.amuxix.block.Block
import me.amuxix.block.Block.Location
import me.amuxix.bukkit.inventory.Item
import me.amuxix.inventory.Item
import me.amuxix.inventory.items.PlayerHead
import me.amuxix.material.Material.{Fire, PlayerHead => PlayerHeadMaterial}
import me.amuxix.pattern._
import me.amuxix.runes.traits.Consumable
import me.amuxix.runes.traits.enchants.{BlockPlaceTrigger, Enchant}
import me.amuxix.OptionObjectOps

/**
  * Created by Amuxix on 01/02/2017.
  */
object TrueName extends RunePattern with Enchant with BlockPlaceTrigger {
  val pattern: Pattern = Pattern(TrueName.apply)(
    ActivationLayer(
      NotInRune, Tier, Tier,      Tier, NotInRune,
      Tier,      Fire, Tier,      Fire, Tier,
      Tier,      Tier, NotInRune, Tier, Tier,
      NotInRune, Tier, Tier,      Tier, NotInRune,
      NotInRune, Tier, NotInRune, Tier, NotInRune,
    )
  )

  override def canEnchant(item: Item): Option[String] = Option.unless(item.material == PlayerHeadMaterial)("Truename can only be applied to player's heads")

  def createTrueNameOf(player: Player): PlayerHead = {

    val trueName: PlayerHead = Item(PlayerHeadMaterial).asInstanceOf[PlayerHead]
    trueName.addRuneEnchant(TrueName)
    trueName.owner = player
    trueName.displayName = trueNameDisplayFor(player)
    trueName
  }

  def trueNameDisplayFor(player: Player): String = {
    s"${player.name}'s ${TrueName.name}"
  }

  //This will cancel the block place if the item being placed is a true name of another player
  /** This should run the effect of the enchant and return whether to cancel the event or not */
  override def onBlockPlace(player: Player, placedBlock: Block, placedAgainstBlock: Block, itemPlaced: Option[Item]): Boolean =
    itemPlaced match {
      case Some(head: PlayerHead) if head.isTrueNameOf(player) => false //Trying to place own truename, allow this.
      case Some(head: PlayerHead) if head.hasRuneEnchant(this) => //Trying to place someone else's truename, destroy it.
        player.notifyError(s"As you place ${head.displayName.get} it crumbles to dust.")
        head.destroy()
        true
      case _ => false
    }
}

case class TrueName(blocks: Array[Array[Array[Block]]], center: Location, creator: Player, direction: Direction, pattern: Pattern) extends Rune with Consumable {
  override val shouldUseTrueName: Boolean = false

  /**
    * Internal activate method that should contain all code to activate a rune.
    */
  override protected def onActivate(activationItem: Option[Item]): Either[String, Boolean] =
    if (direction == Self) {
      Left("This rune cannot be automated.")
    } else {
      activator.inventory.toRight("Cannot access player inventory.").flatMap { inventory =>
        consumeRuneBlocks()
        inventory
          .add(TrueName.createTrueNameOf(activator))
          .toLeft(true)
      }
    }
}
