package me.amuxix.pattern.matching

import me.amuxix._
import me.amuxix.block.Block.Location
import me.amuxix.inventory.Item
import me.amuxix.logging.Logger.trace
import me.amuxix.pattern.{Pattern, RunePattern}
import me.amuxix.runes._

/**
  * Created by Amuxix on 21/11/2016.
  * This is the class that knows how to look for runes in the world
  */
object Matcher {
  private var patterns: Stream[Pattern] = Aethercraft.activeRunes.map(_.pattern)

  /**
    * Adds a rune with the given pattern to list of known runes
    * @param runePattern pattern of the rune to be added.
    */
  def addRune(runePattern: RunePattern[_]): Unit = {
    patterns :+= runePattern.pattern
  }

  /**
    * Looks for runes at the given location
    */
  def lookForRunesAt(location: Location, activator: Player, direction: Direction, itemInHand: Option[Item]): Option[Rune] = {
    val possiblePatterns: Stream[Pattern] = patterns.filter(_.canBeActivatedWith(itemInHand))
    matchRunes(location, activator, direction, possiblePatterns)
      .orElse {
        trace("Found no runes, looking for runes with center on the adjacent block of the clicked block face")
        matchRunes(location + direction, activator, direction, possiblePatterns)
      }
  }

  def matchRunes(location: Location, activator: Player, direction: Direction, possiblePatterns: Stream[Pattern]): Option[Rune] = {
    val filteredPatterns: Stream[Pattern] = possiblePatterns.filter(_.centerCanBe(location.block.material)).sorted
    Option.flatUnless(filteredPatterns.isEmpty) {
      trace(s"There are ${filteredPatterns.length} registered patterns with this item and center.")
      val boundingCube = BoundingCube(location, filteredPatterns)
      filteredPatterns.map { pattern =>
        pattern.findMatchingRotation(boundingCube).map { rotation =>
          pattern.createRune(location, activator, direction, rotation)
        }
      } collectFirst { case Some(rune) => rune }
    }
  }
}
