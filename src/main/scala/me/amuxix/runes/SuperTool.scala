package me.amuxix.runes

import me.amuxix.inventory.Item
import me.amuxix.material.Consumable
import me.amuxix.material.Material.{Redstone, RedstoneTorchOn}
import me.amuxix.pattern._
import me.amuxix.runes.traits.{Enchant, Tool}

/**
  * Created by Amuxix on 01/02/2017.
  */
object SuperTool extends RunePattern with Enchant {
  val pattern: Pattern = Pattern(SuperTool.apply)(
    ActivationLayer(
      Redstone,        NotInRune, RedstoneTorchOn, NotInRune, Redstone,
      NotInRune,       Tier,      Redstone,        Tier,      NotInRune,
      RedstoneTorchOn, Redstone,  NotInRune,       Redstone,  RedstoneTorchOn,
      NotInRune,       Tier,      Redstone,        Tier,      NotInRune,
      Redstone,        NotInRune, RedstoneTorchOn, NotInRune, Redstone
    )
  )
}

case class SuperTool(parameters: Parameters, pattern: Pattern)
  extends Rune(parameters)
          with Consumable
          with Tool {

  /**
    * Internal activate method that should contain all code to activate a rune.
    */
  override protected def innerActivate(activationItem: Item): Unit = {
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