package me.amuxix.runes

import me.amuxix.Block.Location
import me.amuxix.logging.Logger.info
import me.amuxix.pattern._
import me.amuxix.{Block, Direction, Player, Vector3}

/**
  * Created by Amuxix on 22/11/2016.
  */
abstract class Rune(parameters: RuneParameters) {
  val blocks: Array[Array[Array[Block]]] = parameters.blocks
  val center: Location = parameters.center
  val activator: Player = parameters.activator
  val direction: Direction = parameters.direction
  val pattern: Pattern

  /**
    * This is where the rune effects when the rune is first activated go.
    * This must always be extended when overriding,
    */
  def activate(): Unit = {
    innerActivate()
    logRuneActivation()
    notifyActivator()
  }

  /**
    * Internal activate method that should contain all code to activate a rune.
    */
  protected def innerActivate(): Unit

  protected def notifyActivator(): Unit = {
    activator.sendMessage(name + " activated")
  }

  protected def logRuneActivation(): Unit = {
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
