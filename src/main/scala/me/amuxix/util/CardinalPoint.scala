package me.amuxix.util

/**
  * Created by Amuxix on 03/01/2017.
  */
object CardinalPoint {
  implicit def CardinalPoint2Vector3Int(cardinalPoint: CardinalPoint): Vector3[Int] = cardinalPoint.vector
}

sealed trait CardinalPoint {
  val vector: Vector3[Int]
  def +(cardinalPoint: CardinalPoint): Vector3[Int] = vector + cardinalPoint.vector
}

case object North extends CardinalPoint {val vector  = Vector3[Int](0, 0, -1)}
case object South extends CardinalPoint {val vector = Vector3[Int](0, 0, 1)}
case object East extends CardinalPoint {val vector = Vector3[Int](1, 0, 0)}
case object West extends CardinalPoint {val vector = Vector3[Int](-1, 0, 0)}
case object Up extends CardinalPoint {val vector = Vector3[Int](0, 1, 0)}
case object Down extends CardinalPoint {val vector = Vector3[Int](0, -1, 0)}


case object NorthWest extends CardinalPoint {val vector = North + West}
case object NorthEast extends CardinalPoint {val vector = North + East}
case object SouthEast extends CardinalPoint {val vector = South + East}
case object SouthWest extends CardinalPoint {val vector = South + West}
  
case object UpWest extends CardinalPoint {val vector = Up + West}
case object UpEast extends CardinalPoint {val vector = Up + East}
case object DownEast extends CardinalPoint {val vector = Down + East}
case object DownWest extends CardinalPoint {val vector = Down + West}
  
case object UpSouth extends CardinalPoint {val vector = Up + South}
case object UpNorth extends CardinalPoint {val vector = Up + North}
case object DownNorth extends CardinalPoint {val vector = Down + North}
case object DownSouth extends CardinalPoint {val vector = Down + South}

/*
  To generate this follow these steps:
    - Fill a line with: "case object DownSouth extends CardinalPoint", replacing DownSouth with the new direction(Capital letters matter)
    - Search for "(case object (\w[a-z]+)(\w[a-z]+).+)"
    - Replace with "$1 \{val vector = $2 \+ $3\}"
 */