package me.amuxix.runes

import cats.data.EitherT
import cats.effect.IO
import me.amuxix.inventory.Item
import me.amuxix.material.Material.{BlackWool, WhiteWool}
import me.amuxix.pattern.{ActivationLayer, BaseLayer, Pattern, RunePattern}
import me.amuxix.position.BlockPosition
import me.amuxix.{Direction, Matrix4, Player}

import scala.util.Random.nextInt

object MagicEightBall extends RunePattern[MagicEightBall] {
  // format: off
  override val layers: List[BaseLayer] = List(
    ActivationLayer(
      BlackWool, BlackWool,  BlackWool,
      BlackWool, WhiteWool, BlackWool,
      BlackWool, BlackWool,  BlackWool,
    ),
  )
  // format: on
}

case class MagicEightBall(
  center: BlockPosition,
  creator: Player,
  direction: Direction,
  rotation: Matrix4,
  pattern: Pattern
) extends Rune {

  /**
    * Internal activate method that should contain all code to activate a rune.
    */
  override protected def onActivate(activationItem: Option[Item]): EitherT[IO, String, Boolean] = EitherT.pure(true)

  activationMessage = {
    val possibleResponses = List(
      "It is certain.",
      "It is decidedly so.",
      "Without a doubt.",
      "Yes - definitely.",
      "You may rely on it.",
      "As I see it, yes.",
      "Most likely.",
      "Outlook good.",
      "Yes.",
      "Signs point to yes.",
      "Reply hazy, try again.",
      "Ask again later.",
      "Better not tell you now.",
      "Cannot predict now.",
      "Concentrate and ask again.",
      "Don't count on it.",
      "My reply is no.",
      "My sources say no.",
      "Outlook not so good.",
      "Very doubtful.",
    )
    possibleResponses(nextInt(possibleResponses.size))
  }

  /**
    * Should this rune use a true name if the activator is wearing one?
    */
  override val shouldUseTrueName: Boolean = false
}
