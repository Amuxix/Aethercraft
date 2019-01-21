package me.amuxix.block

import io.circe.{Decoder, Encoder}
import me.amuxix._
import me.amuxix.block.Block.Location
import me.amuxix.bukkit.block.{Block => BBlock}
import me.amuxix.material.Material

object Block {
  type Location = Position[Int]
  implicit val encodeBlock: Encoder[Block] = Encoder.forProduct2("location", "material")(b =>
    (b.location, b.material)
  )
  implicit val decodeBlock: Decoder[Block] = Decoder.forProduct2("location", "material"){BBlock.apply}
}

trait Block {
  val location: Location
  var material: Material

  def setMaterial(material: Material): Unit

  /**
    * Attempts to move this block by the displacement vector.
    *
    * @param displacementVector Vector that defines the move.
    * @return true if the move was successful, false otherwise.
    */
  def move(displacementVector: Vector3[Int], player: Player): Boolean

  /**
    * Attempts to move this block to the target location.
    *
    * @param target Location where the block should be moved to.
    * @return true if the move was successful, false otherwise.
    */
  def moveTo(target: Location, player: Player): Boolean

  /**
    * Checks if the player can move this block to the target location, it check if the block can be destroyed at
    * the original location and placed at the target.
    * @param target Target of the move
    * @param player Player who triggered the move
    * @return true if the player can move this block, false otherwise
    */
  def canMoveTo(target: Location, player: Player): Boolean

  /**
    * Consumes this block and gives energy to the player
    *
    * @param player Player who receives the energy for consuming this block
    */
  def consume(player: Player): Unit
}
