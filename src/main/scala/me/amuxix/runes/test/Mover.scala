package me.amuxix.runes.test

import cats.data.EitherT
import cats.effect.IO
import me.amuxix.builder.{Builder, DisplaceStep, NotificationStep, Task}
import me.amuxix.inventory.Item
import me.amuxix.material.Material.{DiamondBlock, GoldBlock}
import me.amuxix.pattern.{ActivationLayer, Pattern, RunePattern}
import me.amuxix.position.BlockPosition
import me.amuxix.runes.Rune
import me.amuxix.{Direction, East, Matrix4, North, Player, Up}

object Mover extends RunePattern[Mover] {
  // format: off
  override val layers = List(
    ActivationLayer(
      DiamondBlock, GoldBlock, DiamondBlock,
      GoldBlock, DiamondBlock, GoldBlock,
      DiamondBlock, GoldBlock, DiamondBlock
    ),
  )
  // format: on
}

case class Mover(
  center: BlockPosition,
  creator: Player,
  direction: Direction,
  rotation: Matrix4,
  pattern: Pattern
) extends Rune {

  override protected def onActivate(activationItem: Option[Item]): EitherT[IO, String, Boolean] = {
    val cubeSize = 10
    val moveSteps = for {
      x <- LazyList.range(-cubeSize, cubeSize)
      z <- LazyList.range(-cubeSize, cubeSize)
      y <- LazyList.range(1, cubeSize * 2 + 1)
      location = center + East * x + North * z + Up * y
      block = location.block
    } yield DisplaceStep(activator, block, Up * (cubeSize * 2 + 5))

    val startNotificationStep = NotificationStep(activator, s"$name started.")
    val endNotificationStep = NotificationStep(activator, s"$name ended.")

    EitherT.fromEither[IO](Task(activator, startNotificationStep +: moveSteps :+ endNotificationStep, 2, s"$name task failed!"))
      .map { task =>
        Builder.addTask(activator, task)
        true
      }
  }

  /**
    * Should this rune use a true name if the activator is wearing one?
    */
  override val shouldUseTrueName: Boolean = false
}
