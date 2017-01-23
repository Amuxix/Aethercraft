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
    activator.sendMessage(getClass.getSimpleName + " activated")
  }

  def logRuneActivation(): Unit = {
    info(activator.name + " activated " + getClass.getSimpleName + " in " + center)
  }

  def getKeyBlocks: Seq[Block] = specialBlocks(Key)

  protected def specialBlocks(element: Element): Seq[Block] = {
    val vectors = pattern.specialBlocksVectors(element)
    vectors.map(blockAt)
  }

  def blockAt(position: Vector3[Int]): Block = {
    blocks(position.x)(position.y)(position.z)
  }
}
