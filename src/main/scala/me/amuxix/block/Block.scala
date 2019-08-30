package me.amuxix.block

import cats.data.OptionT
import cats.effect.IO
import io.circe.Decoder.Result
import io.circe.{Decoder, Encoder, HCursor}
import io.circe.syntax.EncoderOps
import me.amuxix._
import me.amuxix.bukkit.block.{Block => BBlock}
import me.amuxix.inventory.Item
import me.amuxix.material.Material
import me.amuxix.material.Properties.BlockProperty
import me.amuxix.position.{BlockPosition, Vector3}

object Block {
  implicit val materialEncoder: Encoder[Material with BlockProperty] = a => (a: Material).asJson(implicitly[Encoder[Material]])
  implicit val materialDecoder: Decoder[Material with BlockProperty] = (c: HCursor) => c.as[Material].asInstanceOf[Result[Material with BlockProperty]]
  implicit val encodeBlock: Encoder[Block] = Encoder.forProduct2("location", "material")(b =>
    (b.location, b.material: Material)
  )
  implicit val decodeBlock: Decoder[Block] = Decoder.forProduct2("location", "material"){BBlock.apply}
}

trait Block extends Consumable {
  val location: BlockPosition
  var material: Material with BlockProperty

  def setMaterial(material: Material with BlockProperty): OptionT[IO, String]

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
    * @param target BlockPosition where the block should be moved to.
    * @return true if the move was successful, false otherwise.
    */
  def moveTo(target: BlockPosition, player: Player): OptionT[IO, String]

  /**
    * Checks if the player can move this block to the target location, it check if the block can be destroyed at
    * the original location and placed at the target.
    * @param target Target of the move
    * @param player Player who triggered the move
    * @return true if the player can move this block, false otherwise
    */
  def canMoveTo(target: BlockPosition, player: Player): Option[String]

  lazy val faceNeighbours: List[Block] = location.faceNeighbours.map(_.block)

  lazy val edgeNeighbours: List[Block] = location.edgeNeighbours.map(_.block)

  lazy val vertexNeighbours: List[Block] = location.vertexNeighbours.map(_.block)

  lazy val allNeighbours: List[Block] = faceNeighbours ++ edgeNeighbours ++ vertexNeighbours

  def breakUsing(player: Player, item: Item): OptionT[IO, String]

  def break(player: Player): OptionT[IO, String]
}
