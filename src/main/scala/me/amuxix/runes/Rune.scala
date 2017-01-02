package me.amuxix.runes

import me.amuxix.logging.Logger
import me.amuxix.pattern.Pattern
import me.amuxix.util.Block.Location
import me.amuxix.util.{Block, Matrix4, Player, Vector3}
import org.bukkit.ChatColor

/**
  * Created by Amuxix on 22/11/2016.
  */
abstract class Rune {
  val location: Location
  val activator: Player
  val blocks: Array[Array[Array[Block]]]
  val rotation: Matrix4
  val rotationCenter: Vector3[Int]
  val pattern: Pattern

  activator.sendMessage(ChatColor.GREEN + getClass.getSimpleName + " activated")
  Logger.info(activator.name + " activated " + getClass.getSimpleName + " in " + location)

  def getBlockAt(position: Vector3[Int]): Block = {
    val rotatedPosition: Vector3[Int] = position.rotateAbout(rotation, rotationCenter)
    blocks(rotatedPosition.x)(rotatedPosition.y)(rotatedPosition.z)
  }
}
