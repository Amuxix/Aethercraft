package me.amuxix.position

import me.amuxix.World

case class EntityPosition(world: World, coordinates: Vector3[Double]) extends Position[Double](world, coordinates) {
  override val x: Double = coordinates.x
  override val y: Double = coordinates.y
  override val z: Double = coordinates.z

  override def +(vector: Vector3[Double]): EntityPosition = EntityPosition(world, coordinates + vector)
  override def -(vector: Vector3[Double]): EntityPosition = EntityPosition(world, coordinates - vector)

  def toBlockPosition: BlockPosition = BlockPosition(world, Vector3(x.toInt, y.toInt, z.toInt))

  /*def canEqual(other: Any): Boolean = other.isInstanceOf[Position[T]]

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
}