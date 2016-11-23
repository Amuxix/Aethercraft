package me.amuxix.runes

import me.amuxix.pattern.Pattern
import me.amuxix.util.{Block, Location, Player}

/**
  * Created by Amuxix on 22/11/2016.
  */
abstract case class Rune(center: Location, activator: Player, blocks: Array[Array[Array[Block]]])(pattern: Pattern) {

}
