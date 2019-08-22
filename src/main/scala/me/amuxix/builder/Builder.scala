package me.amuxix.builder

import cats.implicits.catsStdInstancesForLazyList
import cats.implicits.catsStdInstancesForList
import cats.implicits.toTraverseOps
import cats.effect.IO
import me.amuxix.Player
import me.amuxix.logging.Logger

object Builder {
  var taskMap: Map[Player, LazyList[Task]] = Map.empty
  val totalMax: Int = 5000
  val playerMax: Int = 500

  def addTask(player: Player, newTask: Task): Unit = {
    val playerTasks = taskMap.getOrElse(player, LazyList.empty)
    Logger.info(s"adding ${newTask}.").unsafeRunSync()
    taskMap += (player -> (playerTasks :+ newTask))
    Logger.info(s"map is now ${taskMap}.").unsafeRunSync()
  }



  val handleTasks: IO[Unit] = for {
    globalTotalActions <- IO(taskMap.values.foldLeft(0D) {
      case (total, playerTasks) => total + playerTasks.foldLeft(0D)(_ + _.actionsPerTick) min playerMax
    })

    globalActionMultiplier <- IO((totalMax / globalTotalActions) min 1)

    _ <- taskMap.toList.traverse {
      case (player, tasks) =>
        handlePlayerTasks(tasks, globalActionMultiplier)
          .map { playerTasks =>
            (player, playerTasks.filter(_.nonEmpty))
          }
    }
      .map { list =>
        taskMap = list.toMap
      }
  } yield ()

  private def handlePlayerTasks(tasks: LazyList[Task], globalActionMultiplier: Double): IO[LazyList[Task]] = {
    val totalActions = tasks.foldLeft(0)(_ + _.actionsPerTick)
    val actionMultiplier = (playerMax / totalActions.toDouble) min 1
    tasks.traverse(_.combine(actionMultiplier * globalActionMultiplier))
  }
}
