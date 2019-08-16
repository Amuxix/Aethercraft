package me.amuxix.block

import cats.data.OptionT
import cats.effect.IO
import me.amuxix.Player
import me.amuxix.position.{BlockPosition, Vector3}

/**
  * Created by Amuxix on 24/01/2017.
  */
object BlockUtils {
  /**
    * Attempts to move all blocks to the given locations.
    * @param blockToTarget Map that contains each block and their targets
    * @param player Player who triggered the move
    * @return None if all blocks were moved successfully, a Some with a error message otherwise
    */
  def moveSeveralTo(blockToTarget: Map[Block, BlockPosition], player: Player): OptionT[IO, String] =
    OptionT.fromOption[IO](blockToTarget
      .to(LazyList)
      .map { case (block, location) => block.canMoveTo(location, player) }
      .collectFirst { case Some(error) => error})
      .orElse {
        blockToTarget.foldLeft(OptionT.none[IO, String]) {
          case (acc, (block, location)) => acc.orElse(block.moveTo(location, player))
        }
      }

  /**
    * Attempts to move all blocks by summing the move vector to their locations
    * @param blocks Blocks to be moved
    * @param moveVector Vector that defines the movement
    * @param player Player who triggered the move
    * @return None if all blocks were moved successfully, a Some with a error message otherwise
    */
  def moveSeveral(blocks: Seq[Block], moveVector: Vector3[Int], player: Player): OptionT[IO, String] = {
    val blockToTarget = blocks.map { block =>
      (block, block.location + moveVector)
    }.toMap
    moveSeveralTo(blockToTarget, player)
  }

  /**
    * Attempts to move all given blocks, moving each by the corresponding vector
    * @param blockToDirection Map that contains each block and the corresponding move vector
    * @param player Player who triggered the move
    * @return None if all blocks were moved successfully, a Some with a error message otherwise
    */
  def moveSeveralBy(blockToDirection: Map[Block, Vector3[Int]], player: Player): OptionT[IO, String] = {
    val blockToTarget = blockToDirection.map {
      case (block, direction) => (block, block.location + direction)
    }
    moveSeveralTo(blockToTarget, player)
  }
}
