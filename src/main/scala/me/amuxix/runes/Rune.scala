package me.amuxix.runes

import me.amuxix.logging.Logger.info
import me.amuxix.pattern._
import me.amuxix.util.Block.Location
import me.amuxix.util._

/**
  * Created by Amuxix on 22/11/2016.
  */
abstract class Rune(parameters: RuneParameters) {
  val blocks: Array[Array[Array[Block]]] = parameters.blocks
  val center: Location = parameters.center
  val activator: Player = parameters.activator
  val direction: CardinalPoint = parameters.direction
  val pattern: Pattern

  def notifyActivator(): Unit = {
    activator.sendMessage(name + " activated")
  }

  def logRuneActivation(): Unit = {
    info(s"${activator.name} activated $name in $center")
  }

  protected def specialBlocks(element: Element): Seq[Block] = {
    pattern.specialBlockVectors(element).map(blockAt)
  }

  protected def nonSpecialBlocks: Seq[Block] = {
    pattern.nonSpecialBlockVectors.map(blockAt)
  }

  def blockAt(position: Vector3[Int]): Block = {
    blocks(position.x)(position.y)(position.z)
  }

  /**
    * @return Default name of the rune is its class name, may be overridden.
    */
  val name: String = getClass.getSimpleName
}
