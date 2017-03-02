package me.amuxix.runes

import me.amuxix.Block.Location
import me.amuxix.{Block, Direction, Player}

/**
  * Created by Amuxix on 23/01/2017.
  */
/**
  * This is a container for the parameters a rune takes
  */
case class Parameters(blocks: Array[Array[Array[Block]]], center: Location, activator: Player, direction: Direction)
