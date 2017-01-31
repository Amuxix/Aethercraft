package me.amuxix

import io.circe.generic.auto._
import io.circe.{Decoder, Encoder}
import org.bukkit.block.BlockFace

/**
  * Created by Amuxix on 03/01/2017.
  */
/**
  * These represent a direction or a mix of several directions.
  */
object Direction {
  implicit def BlockFace2Direction(blockFace: BlockFace): Direction = {
    vector3Int2Direction(Vector3[Int](blockFace.getModX, blockFace.getModY, blockFace.getModZ))
  }
  private def vector3Int2Direction(vector: Vector3[Int]): Direction = {
    vector match {
      case Self.vector => Self
      case North.vector => North
      case South.vector => South
      case East.vector => East
      case West.vector => West
      case Up.vector => Up
      case Down.vector => Down
      case NorthWest.vector => NorthWest
      case NorthEast.vector => NorthEast
      case SouthEast.vector => SouthEast
      case SouthWest.vector => SouthWest
      case UpWest.vector => UpWest
      case UpEast.vector => UpEast
      case DownEast.vector => DownEast
      case DownWest.vector => DownWest
      case UpSouth.vector => UpSouth
      case UpNorth.vector => UpNorth
      case DownNorth.vector => DownNorth
      case DownSouth.vector => DownSouth
      case _ => throw new Exception("Cardinal point with this vector does not exist")
    }
  }

  implicit def CardinalPoint2Vector3Int(cardinalPoint: Direction): Vector3[Int] = cardinalPoint.vector
  implicit val cardinalPointEncoder: Encoder[Direction] = Encoder.forProduct1("cardinalPoint")(_.vector)
  implicit val cardinalPointDecoder: Decoder[Direction] = Decoder.forProduct1("cardinalPoint")(vector3Int2Direction)
}

sealed abstract class Direction(val vector: Vector3[Int]) {
  def +(cardinalPoint: Direction): Vector3[Int] = vector + cardinalPoint.vector
}

case object Self extends Direction((0, 0, 0))
case object North extends Direction((0, 0, -1))
case object South extends Direction((0, 0, 1))
case object East extends Direction((1, 0, 0))
case object West extends Direction((-1, 0, 0))
case object Up extends Direction((0, 1, 0))
case object Down extends Direction((0, -1, 0))

case object NorthWest extends Direction(North + West)
case object NorthEast extends Direction(North + East)
case object SouthEast extends Direction(South + East)
case object SouthWest extends Direction(South + West)
case object UpWest extends Direction(Up + West)
case object UpEast extends Direction(Up + East)
case object DownEast extends Direction(Down + East)
case object DownWest extends Direction(Down + West)

case object UpSouth extends Direction(Up + South)
case object UpNorth extends Direction(Up + North)
case object DownNorth extends Direction(Down + North)
case object DownSouth extends Direction(Down + South)

/*
To generate this follow these steps:
  - Fill a line with: "case object DownSouth extends CardinalPoint", replacing DownSouth with the new direction(Capital letters matter)
  - Search for "(case object (\w[a-z]+)(\w[a-z]+).+)"
  - Replace with "$1 \{val vector = $2 \+ $3\}"
*/