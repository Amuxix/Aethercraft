package me.amuxix.builder

import java.util.UUID

import better.files.File
import cats.effect.IO
import cats.implicits.{catsStdInstancesForLazyList, catsStdInstancesForList, toTraverseOps}
import io.circe.Decoder.decodeList
import io.circe.Encoder.encodeList
import io.circe.{Decoder, Encoder, HCursor}
import me.amuxix.logging.Logger
import me.amuxix.serialization.Persistable
import me.amuxix.{Aethercraft, Player}

import scala.util.Try

object Builder extends Persistable[List[Task]] {
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
            .map { playerTasks =>
              (player, playerTasks.filter(_.nonEmpty))
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

  override implicit lazy val encoder: Encoder[List[Task]] = (list: List[Task]) => encodeList[Task].apply(list)

  override implicit lazy val decoder: Decoder[List[Task]] = (c: HCursor) => decodeList[Task].apply(c)

  override protected val persistablesName: String = "Tasks"

  override protected val extension: String = ".task"

  override protected val folder: File = Aethercraft.dataFolder.createChild(persistablesName, asDirectory = true)

  override protected def persistables: Map[String, List[Task]] = taskMap.map {
    case (player, tasks) => player.uuid.toString -> tasks.toList
  }

  override protected def updateWithLoaded(fileName: String, thing: List[Task]): Unit = {
    Try(UUID.fromString(fileName)).toOption
      .map { uuid =>
        taskMap += (Player(uuid) -> thing.to(LazyList))
      }
      .fold(Logger.severe(s"Invalid player uuid: $fileName"))(IO.pure)
  }
}
