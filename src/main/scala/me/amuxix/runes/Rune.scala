package me.amuxix.runes

import cats.data.{EitherT, OptionT}
import cats.effect.IO
import me.amuxix._
import me.amuxix.block.Block
import me.amuxix.block.Block.Location
import me.amuxix.inventory.Item
import me.amuxix.inventory.items.PlayerHead
import me.amuxix.logging.Logger.info
import me.amuxix.pattern._

/**
  * Created by Amuxix on 22/11/2016.
  */
abstract class Rune extends Named {
  val center: Location
  val creator: Player
  val direction: Direction
  val rotation: Matrix4
  val pattern: Pattern

  /**
    * This is where the rune effects when the rune is first activated go.
    * This must always be extended when overriding,
    */
  def activate(activationItem: Option[Item]): EitherT[IO, String, Boolean] =
  for {
    _ <- EitherT.fromEither[IO](validateActivationItem(activationItem).toLeft(()))
    cancel <- onActivate(activationItem)
    _ <- EitherT.liftF(consumeTrueName)
    _ <- EitherT.liftF(logRuneActivation)
    _ <- EitherT.liftF(notifyActivator)
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
  private def consumeTrueName: IO[Unit] = {
    IO(creator.helmet.collect {
      case playerHead: PlayerHead if playerHead.hasRuneEnchant(TrueName) =>
        playerHead.amount -= 1
        creator.notify(s"The magic of this rune is activated in ${playerHead.owner}'s name and the true name shatters")
    })
  }

  /**
    * Internal activate method that should contain all code to activate a rune.
    */
  protected def onActivate(activationItem: Option[Item]): EitherT[IO, String, Boolean]

  /**
    * Checks if rune can be activated with the given item.
    * @param activationItem Item used to activate this rune
    * @return None if activation item is ok, Some with an error if activation should fail
    */
  protected def validateActivationItem(activationItem: Option[Item]): Option[String] = None

  protected var activationMessage: String = name + " activated"

  protected def notifyActivator: IO[Unit] = IO(activator.notify(activationMessage))

  protected def logRuneActivation: IO[Unit] = info(s"${activator.name} activated ${if ("aeiouy".contains(name.head)) "an" else "a"} $name at $center")

  protected def allRuneBlocks: Stream[Block] = pattern.allRuneBlocks(rotation, center)

  protected def filteredRuneBlocksByElement(element: Element): Stream[Block] = pattern.specialBlocks(rotation, center, element)

  protected def nonSpecialBlocks: Stream[Block] = pattern.nonSpecialBlocks(rotation, center)
}
