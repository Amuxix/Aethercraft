package me.amuxix.util

import me.amuxix.util.Block.Location

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
  def moveSeveralTo(blockToTarget: Map[Block, Location], player: Player): Boolean = {
    if (blockToTarget.forall(e => e._1.canMoveTo(e._2, player))) {
      blockToTarget.forall(e => e._1.moveTo(e._2, player))
    } else {
      false
    }
  }

  /**
    * Attempts to move all blocks by summing the move vector to their locations
    * @param blocks Blocks to be moved
    * @param moveVector Vector that defines the movement
    * @param player Player who triggered the move
    * @return true if all blocks were moved successfully, else otherwise
    */
  def moveSeveral(blocks: Seq[Block], moveVector: Vector3[Int], player: Player): Boolean = {
    if (blocks.forall(block => block.canMoveTo(block.location + moveVector, player))) {
      blocks.forall(_.move(moveVector, player))
    } else {
      false
    }
  }

  /**
    * Attempts to move all given blocks, moving each by the corresponding vector
    * @param blockToTarget Map that contains each block and the corresponding move vector
    * @param player Player who triggered the move
    * @return true if all blocks were moved successfully, else otherwise
    */
  def moveSeveralBy(blockToTarget: Map[Block, Vector3[Int]], player: Player): Boolean = {
    if (blockToTarget.forall(e => e._1.canMoveTo(e._1.location + e._2, player))) {
      blockToTarget.forall(e => e._1.moveTo(e._1.location + e._2, player))
    } else {
      false
    }
  }
}
