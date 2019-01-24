package me.amuxix.runes

import me.amuxix.block.Block.Location
import me.amuxix._
import me.amuxix.block.Block
import me.amuxix.inventory.items.PlayerHead
import me.amuxix.inventory.Item
import me.amuxix.logging.Logger.info
import me.amuxix.pattern._

/**
  * Created by Amuxix on 22/11/2016.
  */
abstract class Rune extends Named {
  val blocks: Array[Array[Array[Block]]]
  val center: Location
  val creator: Player
  val direction: Direction
  val pattern: Pattern
  //val rotation: Matrix4

  /**
    * This is where the rune effects when the rune is first activated go.
    * This must always be extended when overriding,
    */
  def activate(activationItem: Option[Item]): Either[String, Boolean] =
  for {
    _ <- validateActivationItem(activationItem).toLeft(())
    cancel <- onActivate(activationItem)
    _ = consumeTrueName()
    _ = logRuneActivation()
    _ = notifyActivator()
  } yield cancel

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
      helmet <- creator.helmet
      if shouldUseTrueName
      playerHead = helmet.asInstanceOf[PlayerHead]
      if playerHead.hasRuneEnchant(TrueName)
      owner <- playerHead.owner
    } yield owner
    owner.getOrElse(creator)
  }

  /**
    * Consumes a true name on the real activator of this rune
    */
  private def consumeTrueName(): Unit =
    creator.helmet.collect {
      case playerHead: PlayerHead if playerHead.hasRuneEnchant(TrueName) =>
        playerHead.amount -= 1
        creator.notify(s"The magic of this rune is activated in ${playerHead.owner}'s name and the true name shatters")
    }

  /**
    * Internal activate method that should contain all code to activate a rune.
    */
  protected def onActivate(activationItem: Option[Item]): Either[String, Boolean]

  /**
    * Checks if rune can be activated with the given item.
    * @param activationItem Item used to activate this rune
    * @return None if activation item is ok, Some with an error if activation should fail
    */
  protected def validateActivationItem(activationItem: Option[Item]): Option[String] = None

  protected var activationMessage: String = name + " activated"

  protected def notifyActivator(): Unit = activator.notify(activationMessage)

  protected def logRuneActivation(): Unit = info(s"${activator.name} activated $name at $center")

  protected def specialBlocks(element: Element): Seq[Block] = pattern.specialBlockVectors(element).map(blockAt)

  protected def nonSpecialBlocks: Seq[Block] = pattern.nonSpecialBlockVectors.map(blockAt)

  protected[runes] def blockAt(position: Vector3[Int]): Block = blocks(position.x)(position.y)(position.z)
}
