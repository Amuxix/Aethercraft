package me.amuxix.pattern.matching

import me.amuxix.logging.Logger
import me.amuxix.pattern.{Pattern, RunePattern}
import me.amuxix.runes.{Rune, Test2}
import me.amuxix.util.Block.Location
import me.amuxix.util.{Matrix4, Player}

/**
  * Created by Amuxix on 21/11/2016.
  * This is the class that knows how to look for runes in the world
  */
object Matcher {

  private val patterns: Seq[RunePattern] = Seq(Test2)

  /**
    * Looks for runes at the given location
    * @param location position to look for runes
    */
  def lookForRunesAt(location: Location, activator: Player): Option[Rune] = {
    Logger.log("Click Location:" + location)
    val possiblePatterns: Set[Pattern] = patterns.map((obj) => obj.pattern).toSet
    matchRunes(location, activator, possiblePatterns)
  }

  def matchRunes(center: Location, activator: Player, possiblePatterns: Set[Pattern]): Option[Rune] = { //The pattern set needs to be sorted to allow some runes to have priority over others
    val boundingCube = BoundingCube(center, possiblePatterns)
    /*for {
      pattern <- possiblePatterns if pattern.foundIn(boundingCube)
    } yield pattern.createRune(center, activator, boundingCube, )*/

    for (pattern <- possiblePatterns) {
      val maybeMatrix: Option[Matrix4] = pattern.foundIn(boundingCube)
      if (maybeMatrix.isDefined) {
        Logger.log("Rune found")
        return Some(pattern.createRune(center, activator, pattern.getRuneBlocks(boundingCube), maybeMatrix.get))
      }
    }
    None
  }
}
