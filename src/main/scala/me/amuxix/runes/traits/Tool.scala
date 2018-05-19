package me.amuxix.runes.traits

import me.amuxix.inventory.Item
import me.amuxix.runes.Rune

/**
  * Created by Amuxix on 01/02/2017.
  */
/**
  * Used by runes that require a tool in hand to be activated.
  * Throws an initialization exception if [[Rune.activationItem]] is not a [[Tool]]
  */
trait Tool { this: Rune =>
  def checkActivationItem(activationItem: Item) = {
    /*if (activationItem.isInstanceOf[Tool] == false) {
      throw InitializationException("Item used is not a tool!")
    }*/
  }
}

