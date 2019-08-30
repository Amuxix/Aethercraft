package me.amuxix.pattern.matching

import me.amuxix._
import me.amuxix.logging.Logger.{trace, info}
import me.amuxix.pattern.{Pattern, RunePattern}
import me.amuxix.position.BlockPosition
import me.amuxix.runes._

/**
  * Created by Amuxix on 21/11/2016.
  * This is the class that knows how to look for runes in the world
  */
object Matcher {
  private var patterns: LazyList[Pattern] = Aethercraft.activeRunes.map(_.pattern)

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
  def lookForRunesAt(location: BlockPosition, activator: Player, direction: Direction): Option[Rune] = {
    lazy val testPatterns =
      if (activator.inCreativeMode && Aethercraft.approvedTesters.contains(activator.uuid)) {
        Aethercraft.testRunes.map(_.pattern)
      } else {
        LazyList.empty
      }
    val possiblePatterns = patterns ++ testPatterns
    matchRunes(location, activator, direction, possiblePatterns)
      .orElse {
        info("Found no runes, looking for runes with center on the adjacent block of the clicked block face")
        matchRunes(location + direction, activator, direction, possiblePatterns)
      }
  }

  def matchRunes(location: BlockPosition, activator: Player, direction: Direction, possiblePatterns: LazyList[Pattern]): Option[Rune] = {
    val filteredPatterns: LazyList[Pattern] = possiblePatterns.filter(_.centerCanBe(location.block.material)).sorted
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
