package me.amuxix.pattern.matching

import me.amuxix.pattern.{RunePattern, Pattern}
import me.amuxix.runes.{Test, Test2}
import me.amuxix.util.{Location, Player}

/**
  * Created by Amuxix on 21/11/2016.
  * This is the class that knows how to look for runes in the world
  */
object Matcher {

  private val patterns: Seq[RunePattern] = Seq(Test, Test2)

  /**
    * Looks for runes at the given location
    * @param location position to look for runes
    */
  def lookForRunesAt(location: Location, activator: Player): Unit = {
    val possiblePatterns: Set[Pattern] = patterns.map((obj) => obj.pattern).toSet
      matchRunes(location, activator, possiblePatterns)
  }

  def matchRunes(center: Location, activator: Player, possiblePatterns: Set[Pattern]) = { //The pattern set needs to be sorted to allow some runes to have priority over others
    val boundingCube = BoundingCube(center, possiblePatterns)
    /*for {
      pattern <- possiblePatterns if pattern.foundIn(boundingCube)
    } yield pattern.createRune(center, activator, boundingCube, )*/

    for (pattern <- possiblePatterns) {
      if (pattern.foundIn(boundingCube)) {
        true //pattern.createRune(center, activator) //Return rune here
      }
    }
  }
}
