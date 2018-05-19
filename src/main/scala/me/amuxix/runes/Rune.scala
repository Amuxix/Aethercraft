package me.amuxix.runes

import me.amuxix.Block.Location
import me.amuxix._
import me.amuxix.inventory.Item
import me.amuxix.inventory.items.PlayerHead
import me.amuxix.logging.Logger.info
import me.amuxix.pattern._

/**
  * Created by Amuxix on 22/11/2016.
  */
abstract class Rune(parameters: Parameters) extends Named {
  val blocks: Array[Array[Array[Block]]] = parameters.blocks
  val center: Location = parameters.center
  /**
    * The player that triggered the rune activation.
    */
  private val realActivator: Player = parameters.activator
  val direction: Direction = parameters.direction
  val pattern: Pattern

  /**
    * This is where the rune effects when the rune is first activated go.
    * This must always be extended when overriding,
    */
  def activate(activationItem: Item): Unit = {
    innerActivate(activationItem)
    consumeTrueName()
    logRuneActivation()
    notifyActivator()
  }

  /**
    * Should this rune use a true name if the activator is wearing one?
    */
  protected[runes] val shouldUseTrueName: Boolean
  /**
    * This is the player that will be effected by the rune, normally it is the activator
    * but it can be someone else if a truename is being used.
    */
  val activator: Player = {
    val owner = for {
      possibleHelmet <- realActivator.helmet()
      playerHead = possibleHelmet.asInstanceOf[PlayerHead]
      owner <- playerHead.owner if shouldUseTrueName && playerHead.hasRuneEnchant(TrueName)
    } yield owner
    owner.getOrElse(realActivator)
  }

  /**
    * Consumes a true name on the real activator of this rune
    */
  private def consumeTrueName(): Unit = {
    val playerHead = for {
      possibleHelmet <- realActivator.helmet()
      playerHead = possibleHelmet.asInstanceOf[PlayerHead]
    } yield playerHead
    if (playerHead.isDefined && playerHead.get.hasOwner && playerHead.get.hasRuneEnchant(TrueName)) {
      playerHead.get.amount -= 1
      realActivator.sendNotification(s"The magic of this rune is activated in ${playerHead.get.owner}'s name and the true name shatters")
    }
  }

  /**
    * Internal activate method that should contain all code to activate a rune.
    */
  protected def innerActivate(activationItem: Item): Unit

  protected def notifyActivator(): Unit = {
    activator.sendNotification(name + " activated")
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

  protected[runes] def blockAt(position: Vector3[Int]): Block = {
    blocks(position.x)(position.y)(position.z)
  }
}
