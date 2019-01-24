package me.amuxix.runes.traits

import me.amuxix.inventory.Item
import me.amuxix.runes.Rune

/**
  * Created by Amuxix on 01/02/2017.
  */
/**
  * Used by runes that require a tool in hand to be activated.
  */
trait Tool { this: Rune =>

  override def validateActivationItem(activationItem: Option[Item]): Option[String] =
    activationItem match {
      case Some(item) if item.material.isTool => None
      case _ => Some("This rune must be activated with a tool")
    }
}

