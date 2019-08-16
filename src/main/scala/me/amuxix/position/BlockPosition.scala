package me.amuxix.position

import io.circe.{Decoder, Encoder}
import me.amuxix.{Direction, Up, World}
import me.amuxix.block.Block
import me.amuxix.doubleAsIfIntegral

object BlockPosition {
  implicit val encodePositionInt: Encoder[BlockPosition] = Encoder.forProduct2("world", "coordinates")(position =>
    (position.world, position.coordinates)
  )

  implicit val decodePositionInt: Decoder[BlockPosition] = Decoder.forProduct2[BlockPosition, World, Vector3[Int]]("world", "coordinates")(BlockPosition.apply)
}

case class BlockPosition(world: World, coordinates: Vector3[Int]) extends Position[Int](world, coordinates) {
  override val x: Int = coordinates.x
  override val y: Int = coordinates.y
  override val z: Int = coordinates.z

  override def +(vector: Vector3[Int]): BlockPosition = BlockPosition(world, coordinates + vector)
  override def -(vector: Vector3[Int]): BlockPosition = BlockPosition(world, coordinates - vector)

  def toEntityPosition: EntityPosition = EntityPosition(world, Vector3(x.toDouble, y.toDouble, z.toDouble))

  def block: Block = world.blockAt(coordinates)

  /**
    * Checks if this position and the position above this have blocks that players can be on, ie: the blocks are lava, or air, or reeds.
    * @return true if a player can fit
    */
  def canFitPlayer: Boolean = block.material.isSolid == false && (this + Up).block.material.isSolid == false

/*  def canEqual(other: Any): Boolean = other.isInstanceOf[BlockPosition]

  override def equals(other: Any): Boolean = other match {
    case position: Position[T] =>
      (position canEqual this) &&
        world.uuid == position.world.uuid && coordinates.equals(position.coordinates)
    case _ => false
  }

  override def hashCode(): Int = {
    val state = Seq(world.uuid, coordinates)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }*/

  lazy val faceNeighbours: List[BlockPosition] = Direction.faceNeighbours.map(this + _)

  lazy val edgeNeighbours: List[BlockPosition] = Direction.edgeNeighbours.map(this + _)

  lazy val vertexNeighbours: List[BlockPosition] = Direction.vertexNeighbours.map(this + _)

  lazy val allNeighbours: List[BlockPosition] = faceNeighbours ++ edgeNeighbours ++ vertexNeighbours
}