package me.amuxix.runes

import me.amuxix.exceptions.InitializationException
import me.amuxix.inventory.items.PlayerHead
import me.amuxix.inventory.{Inventory, Item}
import me.amuxix.material.Material
import me.amuxix.material.Material.{Fire, PlayerHead => PlayerHeadMaterial}
import me.amuxix.pattern._
import me.amuxix.runes.traits.Consumable
import me.amuxix.runes.traits.enchants.{BlockPlaceTrigger, Enchant}
import me.amuxix.{Block, Player, Self}
import org.bukkit.ChatColor

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

  override def canEnchant(material: Material): Boolean = material == PlayerHeadMaterial

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
  override def onBlockPlace(player: Player, placedBlock: Block, placedAgainstBlock: Block, itemPlaced: Item): Boolean =
    itemPlaced match {
      case head: PlayerHead if head.isTrueNameOf(player) => false //Trying to place own truename, allow this.
      case head: PlayerHead if head.hasRuneEnchant(this) => //Trying to place someone else's truename, destroy it.
        player.sendNotification(ChatColor.RED + s"As you place ${itemPlaced.displayName.get} it crumbles to dust.")
        itemPlaced.amount = 0 //This will destroy the item.
        true
      case _ => false
    }
}

case class TrueName(parameters: Parameters, pattern: Pattern) extends Rune with Consumable {
  override val shouldUseTrueName: Boolean = false

  /**
    * Internal activate method that should contain all code to activate a rune.
    */
  override protected def onActivate(activationItem: Item): Unit = {
    if (direction == Self) {
      throw InitializationException("This rune cannot be automated.")
    }
    val inventory: Inventory = activator.inventory.getOrElse(throw InitializationException("Cannot access player inventory."))
    consumeRuneBlocks()
    inventory.add(TrueName.createTrueNameOf(activator))
  }
}
