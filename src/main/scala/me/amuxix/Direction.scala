package me.amuxix

import io.circe.generic.semiauto.{deriveEncoder, deriveDecoder}
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
  private def vector3Int2Direction(vector: Vector3[Int]): Direction = vector match {
    case Self => Self
    case North => North
    case South => South
    case East => East
    case West => West
    case Up => Up
    case Down => Down
    case NorthWest => NorthWest
    case NorthEast => NorthEast
    case SouthEast => SouthEast
    case SouthWest => SouthWest
    case UpWest => UpWest
    case UpEast => UpEast
    case DownEast => DownEast
    case DownWest => DownWest
    case UpSouth => UpSouth
    case UpNorth => UpNorth
    case DownNorth => DownNorth
    case DownSouth => DownSouth
    case _ => throw new Exception("Cardinal point with this vector does not exist")
  }

  val directNeighbours = List(
    North,
    South,
    East,
    West,
    Up,
    Down,
  )
  
  val indirectNeighbours = List(
    NorthWest,
    NorthEast,
    SouthEast,
    SouthWest,
    UpWest,
    UpEast,
    DownEast,
    DownWest,
  )

  val allNeighbours: List[Direction] = directNeighbours ++ indirectNeighbours

  implicit val encoder: Encoder[Direction] = deriveEncoder
  implicit val decoder: Decoder[Direction] = deriveDecoder
}

sealed abstract class Direction(x: Int, y: Int, z: Int) extends Vector3[Int](x, y, z) {
  def this(vector: Vector3[Int]) = this(vector.x, vector.y, vector.z)
}

object Self extends Direction(0, 0, 0)
object North extends Direction(0, 0, -1)
object South extends Direction(0, 0, 1)
object East extends Direction(1, 0, 0)
object West extends Direction(-1, 0, 0)
object Up extends Direction(0, 1, 0)
object Down extends Direction(0, -1, 0)

object NorthWest extends Direction(North + West)
object NorthEast extends Direction(North + East)
object SouthEast extends Direction(South + East)
object SouthWest extends Direction(South + West)
object UpWest extends Direction(Up + West)
object UpEast extends Direction(Up + East)
object DownEast extends Direction(Down + East)
object DownWest extends Direction(Down + West)

object UpSouth extends Direction(Up + South)
object UpNorth extends Direction(Up + North)
object DownNorth extends Direction(Down + North)
object DownSouth extends Direction(Down + South)

/*
To generate this follow these steps:
  - Fill a line with: "case object DownSouth extends CardinalPoint", replacing DownSouth with the new direction(Capital letters matter)
  - Search for "(case object (\w[a-z]+)(\w[a-z]+).+)"
  - Replace with "$1 \{val vector = $2 \+ $3\}"
*/