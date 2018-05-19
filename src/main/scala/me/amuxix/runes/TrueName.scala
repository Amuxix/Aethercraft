package me.amuxix.runes

import me.amuxix.exceptions.InitializationException
import me.amuxix.inventory.items.PlayerHead
import me.amuxix.inventory.{Inventory, Item}
import me.amuxix.material.Material.{Fire, PlayerHead => PlayerHeadMaterial}
import me.amuxix.pattern._
import me.amuxix.runes.traits.{Consumable, Enchant}
import me.amuxix.{Player, Self}

/**
  * Created by Amuxix on 01/02/2017.
  */
object TrueName extends RunePattern with Enchant {
  val pattern: Pattern = Pattern(TrueName.apply)(
    ActivationLayer(
      NotInRune, Tier, Tier, Tier, NotInRune,
      Tier, Fire, Tier, Fire, Tier,
      Tier, Tier, NotInRune, Tier, Tier,
      NotInRune, Tier, Tier, Tier, NotInRune,
      NotInRune, Tier, NotInRune, Tier, NotInRune
    )
  )
}

case class TrueName(parameters: Parameters, pattern: Pattern)
  extends Rune(parameters) with Consumable {
  override val shouldUseTrueName: Boolean = false

  def getTrueNameOf(player: Player): PlayerHead = {
    val trueName: PlayerHead = Item(PlayerHeadMaterial).asInstanceOf[PlayerHead]
    trueName.addRuneEnchant(TrueName)
    trueName.owner = player
    trueName.displayName = s"${player.name}'s ${TrueName.name}"
    trueName
  }

  /**
    * Internal activate method that should contain all code to activate a rune.
    */
  override protected def innerActivate(activationItem: Item): Unit = {
    if (direction == Self) {
      throw InitializationException("This rune cannot be automated.")
    }
    val inventory: Inventory = activator.inventory.getOrElse(throw InitializationException("Cannot access player inventory."))
    consumeRuneBlocks()
    inventory.add(getTrueNameOf(activator))
  }
}
