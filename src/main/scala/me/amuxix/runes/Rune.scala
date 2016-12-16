package me.amuxix.runes

import me.amuxix.pattern.Pattern
import me.amuxix.util.{Block, Location, Matrix4, Player}

/**
  * Created by Amuxix on 22/11/2016.
  */
abstract class Rune {
  val location: Location
  val activator: Player
  val blocks: Array[Array[Array[Block]]]
  val rotation: Matrix4
  val pattern: Pattern
}
