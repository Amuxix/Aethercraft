package me.amuxix.builder

import java.util.UUID

import cats.data.EitherT
import cats.effect.IO
import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import me.amuxix.Player


object Task {
  private def empty(id: UUID): Task = new Task(None, LazyList.empty, 0, "", id, true)

  def apply(owner: Player, steps: LazyList[Step], actionsPerTick: Int, failureMessage: String): Either[String, Task] = {
    Either.cond(
      steps.forall(_.actions <= actionsPerTick),
      new Task(Some(owner), steps, actionsPerTick, failureMessage),
      "Task must have number of actions per tick greater or equal to the number of actions of smallest task"
    )
  }

  implicit val encoder: Encoder[Task] = deriveEncoder

  implicit val decoder: Decoder[Task] = deriveDecoder
}

private[builder] case class Task(
  owner: Option[Player],
  steps: LazyList[Step],
  actionsPerTick: Int,
  failureMessage: String,
  id: UUID = UUID.randomUUID(),
  hasFinished: Boolean = false,
) {
  /**
    * Combines IOs from the steps until a number of actions equal to the actionsPerTick multiplied by actionMultiplier is satisfied.
    * @param actionLimiterMultiplier Multiplier to the actions per tick, is always less or equal to 1.
    */
  private[builder] def combine(actionLimiterMultiplier: Double): IO[Task] = {
    val actions = actionLimiterMultiplier * actionsPerTick
    var actionsTaken = 0
    val tasksToExecute = steps.takeWhile { task =>
      if (actionsTaken + task.actions <= actions) {
        actionsTaken += task.actions
        true
      } else {
        false
      }
    }
    tasksToExecute
      .foldLeft(EitherT.pure[IO, String](())) {
        case (io, task) => io.flatMap(_ => task.io)
      }
      .map { _ =>
        val remainingSteps = steps.drop(tasksToExecute.size)
        new Task(owner, remainingSteps, actionsPerTick, failureMessage, id, remainingSteps.isEmpty)
      }.recoverWith {
        case error =>
          EitherT.liftF(owner.fold(IO.unit)(_.notifyError(s"$failureMessage $error")).map(_ => Task.empty(id)))
      }
      .fold(
        _ => Task.empty(id),
        identity
      )
  }
}
