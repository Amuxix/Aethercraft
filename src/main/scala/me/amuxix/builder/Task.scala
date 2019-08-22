package me.amuxix.builder

import cats.data.EitherT
import cats.effect.IO
//import io.circe.{Decoder, Encoder}
//import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import me.amuxix.Player


object Task {
  def empty: Task = new Task(None, LazyList.empty, 0, "")

  def apply(owner: Player, steps: LazyList[Step], actionsPerTick: Int, failureMessage: String): Either[String, Task] = {
    Either.cond(
      steps.forall(_.actions <= actionsPerTick),
      new Task(Some(owner), steps, actionsPerTick, failureMessage),
      "Task must have number of actions per tick greater or equal to the number of actions of smallest task"
    )
  }

  //implicit val taskEncoder: Encoder[Task] = deriveEncoder
  //implicit val taskDecoder: Decoder[Task] = deriveDecoder
}

private[builder] class Task(owner: Option[Player], val steps: LazyList[Step], val actionsPerTick: Int, failureMessage: String) {
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
    tasksToExecute.foldLeft(EitherT.pure[IO, String](())) {
      case (io, task) => io.flatMap(_ => task.io)
    }
      .map(_ => new Task(owner, steps.drop(tasksToExecute.size), actionsPerTick, failureMessage))
      .recoverWith {
        case error =>
          EitherT.liftF(owner.fold(IO.unit)(_.notifyError(s"$failureMessage $error")).map(_ => Task.empty))
      }
      .fold(
        _ => Task.empty,
        identity
      )
  }

  def isEmpty: Boolean = steps.isEmpty

  def nonEmpty: Boolean = steps.nonEmpty
}
