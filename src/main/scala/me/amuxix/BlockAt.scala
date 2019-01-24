package me.amuxix

import me.amuxix.block.Block

trait BlockAt {
  def blockAt(position: Vector3[Int]): Block
}
