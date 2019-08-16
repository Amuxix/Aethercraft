package me.amuxix

import me.amuxix.block.Block
import me.amuxix.position.Vector3

trait BlockAt {
  def blockAt(position: Vector3[Int]): Block
}
