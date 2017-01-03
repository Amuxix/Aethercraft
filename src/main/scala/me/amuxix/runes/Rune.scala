package me.amuxix.runes

import me.amuxix.logging.Logger
import me.amuxix.pattern._
import me.amuxix.util.Block.Location
import me.amuxix.util.{Block, Matrix4, Player, Vector3}
import org.bukkit.ChatColor
import org.bukkit.event.player.PlayerInteractEvent

/**
  * Created by Amuxix on 22/11/2016.
  */
abstract class Rune {
  val blocks: Array[Array[Array[Block]]]
  val rotation: Matrix4
  val rotationCenter: Vector3[Int]
  val pattern: Pattern
  val event: PlayerInteractEvent
  val center: Location = event.getClickedBlock
  val activator: Player = event.getPlayer

  activator.sendMessage(ChatColor.GREEN + getClass.getSimpleName + " activated")
  Logger.info(activator.name + " activated " + getClass.getSimpleName + " in " + center)

  def blockAt(position: Vector3[Int]): Block = {
    val rotatedPosition: Vector3[Int] = position.rotateAbout(rotation, rotationCenter)
    blocks(rotatedPosition.x)(rotatedPosition.y)(rotatedPosition.z)
  }

  protected def specialBlocks(element: Element): Seq[Block] = {
    pattern.specialBlocksVectors(element).map(blockAt)
  }

  def getKeyBlocks: Seq[Block] = specialBlocks(Key)
}
