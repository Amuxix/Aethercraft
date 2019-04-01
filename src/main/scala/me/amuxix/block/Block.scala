package me.amuxix.block

import cats.data.OptionT
import cats.effect.IO
import io.circe.{Decoder, Encoder}
import me.amuxix._
import me.amuxix.block.Block.Location
import me.amuxix.bukkit.block.{Block => BBlock}
import me.amuxix.inventory.Item
import me.amuxix.material.Material

object Block {
  type Location = Position[Int]
  implicit val encodeBlock: Encoder[Block] = Encoder.forProduct2("location", "material")(b =>
    (b.location, b.material)
  )
  implicit val decodeBlock: Decoder[Block] = Decoder.forProduct2("location", "material"){BBlock.apply}
}

trait Block extends Consumable {
  val location: Location
  var material: Material

  def setMaterial(material: Material): OptionT[IO, String]

  /**
    * Attempts to move this block by the displacement vector.
    *
    * @param displacementVector Vector that defines the move.
    * @return true if the move was successful, false otherwise.
    */
  def move(displacementVector: Vector3[Int], player: Player): OptionT[IO, String]

  /**
    * Attempts to move this block to the target location.
    *
    * @param target Location where the block should be moved to.
    * @return true if the move was successful, false otherwise.
    */
  def moveTo(target: Location, player: Player): OptionT[IO, String]

  /**
    * Checks if the player can move this block to the target location, it check if the block can be destroyed at
    * the original location and placed at the target.
    * @param target Target of the move
    * @param player Player who triggered the move
    * @return true if the player can move this block, false otherwise
    */
  def canMoveTo(target: Location, player: Player): Option[String]

  def directNeighbours: List[Block] = location.directNeighbours.map(_.block)

  def indirectNeighbours: List[Block] = location.indirectNeighbours.map(_.block)

  def allNeighbours: List[Block] = directNeighbours ++ indirectNeighbours

  def breakUsing(player: Player, item: Item): OptionT[IO, String]
}
