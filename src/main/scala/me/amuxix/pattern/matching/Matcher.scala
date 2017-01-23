package me.amuxix.pattern.matching

import me.amuxix.logging.Logger.trace
import me.amuxix.pattern.{Pattern, RunePattern}
import me.amuxix.runes._
import me.amuxix.runes.waypoints.Waypoint
import me.amuxix.util.Block.Location
import me.amuxix.util.{CardinalPoint, Matrix4, Player, Rotation}

import scala.collection.SortedSet

/**
  * Created by Amuxix on 21/11/2016.
  * This is the class that knows how to look for runes in the world
  */
object Matcher {

  private val patterns: Seq[RunePattern] = Seq(Test, Test2, Waypoint, Teleporter, Compass)

  /**
    * Looks for runes at the given location
    * @param event Event that contains the location where to look for runes as well as some other useful parameters when activating the rune
    */
  def lookForRunesAt(location: Location, activator: Player, direction: CardinalPoint): Option[Rune] = {
    val possiblePatterns: SortedSet[Pattern] = SortedSet(patterns.map(_.pattern):_*)
    matchRunes(location, activator, direction, possiblePatterns)
  }

  def matchRunes(location: Location, activator: Player, direction: CardinalPoint, possiblePatterns: SortedSet[Pattern]): Option[Rune] = {
    trace(s"There are ${patterns.length} registered patterns.")
    val boundingCube = BoundingCube(location, possiblePatterns)
    for (pattern <- possiblePatterns) {
      val maybeMatrix: Option[Matrix4] = pattern.foundIn(boundingCube)
      if (maybeMatrix.isDefined) {
        val rotation = Rotation(maybeMatrix.get, boundingCube.center)
        return Some(pattern.createRune(RuneParameters(pattern.getRuneBlocks(boundingCube, rotation), location, activator, direction)))
      }
    }
    None
  }
}
