package me.amuxix.builder

import better.files.File
import cats.effect.IO
import cats.implicits.{catsStdInstancesForLazyList, catsStdInstancesForList, toTraverseOps}
import io.circe.{Decoder, Encoder}
import me.amuxix.serialization.Persistable
import me.amuxix.{Aethercraft, Player}

object Builder extends Persistable[Task] {
  var taskMap: Map[Player, LazyList[Task]] = Map.empty
  val totalMax: Int = 5000
  val playerMax: Int = 500

  def addTask(player: Player, newTask: Task): Unit = {
    val playerTasks = taskMap.getOrElse(player, LazyList.empty)
    taskMap += (player -> (playerTasks :+ newTask))
  }

  val handleTasks: IO[Unit] = for {

    maps <- IO(taskMap.partition(_._1.isOnline))

    (onlinePlayerTasks, offlinePlayerTasks) = maps

    globalTotalActions <- IO(onlinePlayerTasks.values.foldLeft(0D) {
      case (total, playerTasks) => total + playerTasks.foldLeft(0D)(_ + _.actionsPerTick) min playerMax
    })

    globalActionMultiplier <- IO((totalMax / globalTotalActions) min 1)

    _ <- onlinePlayerTasks.view
      .filterKeys(_.isOnline)
      .toList
      .traverse {
        case (player, tasks) =>
          handlePlayerTasks(tasks, globalActionMultiplier)
            .flatMap { playerTasks =>
              playerTasks
                .flatTraverse {
                  case task if task.hasFinished =>
                    deleteAsync(task).map(_ => LazyList.empty)
                  case task =>
                    IO(LazyList(task))
                }
                .map(player -> _)
            }
      }
      .map { list =>
        taskMap = list.toMap ++ offlinePlayerTasks
      }
  } yield ()

  private def handlePlayerTasks(tasks: LazyList[Task], globalActionMultiplier: Double): IO[LazyList[Task]] = {
    val totalActions = tasks.foldLeft(0)(_ + _.actionsPerTick)
    val actionMultiplier = (playerMax / totalActions.toDouble) min 1
    tasks.traverse(_.combine(actionMultiplier * globalActionMultiplier))
  }

  override implicit val encoder: Encoder[Task] = Task.encoder

  override implicit val decoder: Decoder[Task] = Task.decoder

  override protected val persistablesName: String = "Tasks"

  override protected val extension: String = ".task"

  override protected val folder: File = Aethercraft.dataFolder.createChild(persistablesName, asDirectory = true)

  override protected def persistables: List[Task] = taskMap.values.toList.flatten

  override protected def getFileName(task: Task): String = task.id.toString

  override protected def updateWithLoaded(fileName: String, task: Task): Unit = {
    val player = task.owner.getOrElse(throw new Exception("Trying to load task without owner!"))
    addTask(player, task)
  }
}
