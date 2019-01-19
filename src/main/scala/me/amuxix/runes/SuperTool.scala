package me.amuxix.runes

import me.amuxix.{Block, Player}
import me.amuxix.inventory.Item
import me.amuxix.material.{Consumable, Material, Tool => GenericTool}
import me.amuxix.material.Material.{Redstone, RedstoneTorch}
import me.amuxix.pattern._
import me.amuxix.runes.traits.Tool
import me.amuxix.runes.traits.enchants.{BlockBreakTrigger, Enchant}

/**
  * Created by Amuxix on 01/02/2017.
  */
object SuperTool extends RunePattern with Enchant with BlockBreakTrigger {
  val pattern: Pattern = Pattern(SuperTool.apply)(
    ActivationLayer(
      Redstone,        NotInRune, RedstoneTorch,   NotInRune, Redstone,
      NotInRune,       Tier,      Redstone,        Tier,      NotInRune,
      RedstoneTorch,   Redstone,  NotInRune,       Redstone,  RedstoneTorch,
      NotInRune,       Tier,      Redstone,        Tier,      NotInRune,
      Redstone,        NotInRune, RedstoneTorch,   NotInRune, Redstone
    )
  )

  override def canEnchant(material: Material): Boolean = material.isInstanceOf[GenericTool]

  /** This should run the effect of the enchant and return whether to cancel the event or not */
  override def onBlockBreak(player: Player, brokenBlock: Block): Boolean = ???
}

case class SuperTool(parameters: Parameters, pattern: Pattern) extends Rune with Consumable with Tool {

  /**
    * Internal activate method that should contain all code to activate a rune.
    */
  override protected def onActivate(activationItem: Item): Unit = {
    /*checkActivationItem(activationItem)
    val meta = activationItem.meta
    val lore: Seq[String] = if (meta.hasLore) {
      meta.getLore.asScala :+ name
    } else {
      Seq(name)
    }
    meta.setLore(lore.toList.asJava)*/
  }

  /**
    * Should this rune use a true name if the activator is wearing one?
    */
  override val shouldUseTrueName: Boolean = true
}