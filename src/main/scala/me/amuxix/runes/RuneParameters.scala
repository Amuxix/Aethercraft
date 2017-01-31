package me.amuxix.runes

import me.amuxix.util.Block.Location
import me.amuxix.util._

/**
  * Created by Amuxix on 23/01/2017.
  */
/**
  * This is a container for the parameters a rune takes
  */
case class RuneParameters(blocks: Array[Array[Array[Block]]], center: Location, activator: Player, direction: CardinalPoint)
