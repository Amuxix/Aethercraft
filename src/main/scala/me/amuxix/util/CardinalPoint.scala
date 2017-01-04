package me.amuxix.util

import org.bukkit.block.BlockFace

/**
  * Created by Amuxix on 03/01/2017.
  */
object CardinalPoint {
  implicit def BlockFace2CardinalPoint(blockFace: BlockFace): CardinalPoint = {
    new CardinalPoint((blockFace.getModX, blockFace.getModY, blockFace.getModZ)) {}
  }
  implicit  def CardinalPoint2Vector3Int(cardinalPoint: CardinalPoint): Vector3[Int] = cardinalPoint.vector
}

sealed abstract class CardinalPoint(val vector: Vector3[Int]) {
  def +(cardinalPoint: CardinalPoint): Vector3[Int] = vector + cardinalPoint.vector
}

case object Self extends CardinalPoint((0, 0, 0))

case object North extends CardinalPoint((0, 0, -1))
case object South extends CardinalPoint((0, 0, 1))
case object East extends CardinalPoint((1, 0, 0))
case object West extends CardinalPoint((-1, 0, 0))
case object Up extends CardinalPoint((0, 1, 0))
case object Down extends CardinalPoint((0, -1, 0))


case object NorthWest extends CardinalPoint(North + West)
case object NorthEast extends CardinalPoint(North + East)
case object SouthEast extends CardinalPoint(South + East)
case object SouthWest extends CardinalPoint(South + West)
  
case object UpWest extends CardinalPoint(Up + West)
case object UpEast extends CardinalPoint(Up + East)
case object DownEast extends CardinalPoint(Down + East)
case object DownWest extends CardinalPoint(Down + West)
  
case object UpSouth extends CardinalPoint(Up + South)
case object UpNorth extends CardinalPoint(Up + North)
case object DownNorth extends CardinalPoint(Down + North)
case object DownSouth extends CardinalPoint(Down + South)

/*
  To generate this follow these steps:
    - Fill a line with: "case object DownSouth extends CardinalPoint", replacing DownSouth with the new direction(Capital letters matter)
    - Search for "(case object (\w[a-z]+)(\w[a-z]+).+)"
    - Replace with "$1 \{val vector = $2 \+ $3\}"
 */