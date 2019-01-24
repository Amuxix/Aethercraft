package me.amuxix.pattern.matching

import me.amuxix.block.Block.Location
import me.amuxix._
import me.amuxix.inventory.Item
import me.amuxix.logging.Logger.trace
import me.amuxix.pattern.{Pattern, RunePattern}
import me.amuxix.runes._
import me.amuxix.runes.test._
import me.amuxix.runes.waypoints.Waypoint

/**
  * Created by Amuxix on 21/11/2016.
  * This is the class that knows how to look for runes in the world
  */
object Matcher {
  private val defaultRunePatterns: Seq[RunePattern] = List(Test, Test2, Waypoint, Teleporter, Compass, TrueName, RunicChest)

  private var patterns: Seq[Pattern] = defaultRunePatterns.map(_.pattern)

  /**
    * Adds a rune with the given pattern to list of known runes
    * @param runePattern pattern of the rune to be added.
    */
  def addRune(runePattern: RunePattern): Unit = {
    patterns :+= runePattern.pattern
  }

  /**
    * Looks for runes at the given location
    */
  def lookForRunesAt(location: Location, activator: Player, direction: Direction, itemInHand: Option[Item]): Option[Rune] = {
    val possiblePatterns: Seq[Pattern] = patterns.filter(_.canBeActivatedWith(itemInHand))
    //val possiblePatterns: Seq[Pattern] = patterns
    matchRunes(location, activator, direction, possiblePatterns)
      .orElse {
        trace("Found no runes, looking for runes with center on the adjacent block of the clicked block face")
        matchRunes(location + direction, activator, direction, possiblePatterns)
      }
  }

  def matchRunes(location: Location, activator: Player, direction: Direction, possiblePatterns: Seq[Pattern]): Option[Rune] = {
    val filteredPatterns: Seq[Pattern] = possiblePatterns.filter(_.centerCanBe(location.block.material)).sorted
    Option.flatUnless(filteredPatterns.isEmpty) {
      trace(s"There are ${filteredPatterns.length} registered patterns with this item and center.")
      val boundingCube = BoundingCube(location, filteredPatterns)
      filteredPatterns.toStream.map { pattern =>
        pattern.findMatchingRotation(boundingCube).map { matrix =>
          pattern.createRune(pattern.runeBlocks(boundingCube, matrix.rotateAbout(boundingCube.center)), location, activator, direction)
        }
      } collectFirst { case Some(rune) => rune }
    }
  }
}
