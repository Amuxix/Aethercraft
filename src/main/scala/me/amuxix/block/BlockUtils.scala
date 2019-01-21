package me.amuxix.block

import me.amuxix.{Player, Vector3}
import me.amuxix.block.Block.Location

/**
  * Created by Amuxix on 24/01/2017.
  */
object BlockUtils {
  /**
    * Attempts to move all blocks to the given locations.
    * @param blockToTarget Map that contains each block and their targets
    * @param player Player who triggered the move
    * @return true if all blocks were moved successfully, else otherwise
    */
  def moveSeveralTo(blockToTarget: Map[Block, Location], player: Player): Boolean =
    if (blockToTarget.forall { case (block, location) => block.canMoveTo(location, player) }) {
      blockToTarget.forall { case (block, location) => block.moveTo(location, player) }
    } else {
      false
    }


  /**
    * Attempts to move all blocks by summing the move vector to their locations
    * @param blocks Blocks to be moved
    * @param moveVector Vector that defines the movement
    * @param player Player who triggered the move
    * @return true if all blocks were moved successfully, else otherwise
    */
  def moveSeveral(blocks: Seq[Block], moveVector: Vector3[Int], player: Player): Boolean = {
    val blockToTarget = blocks.map { block =>
      (block, block.location + moveVector)
    }.toMap
    moveSeveralTo(blockToTarget, player)
  }

  /**
    * Attempts to move all given blocks, moving each by the corresponding vector
    * @param blockToDirection Map that contains each block and the corresponding move vector
    * @param player Player who triggered the move
    * @return true if all blocks were moved successfully, else otherwise
    */
  def moveSeveralBy(blockToDirection: Map[Block, Vector3[Int]], player: Player): Boolean = {
    val blockToTarget = blockToDirection.map {
      case (block, direction) => (block, block.location + direction)
    }
    moveSeveralTo(blockToTarget, player)
  }
}
