package me.amuxix.pattern.matching

import me.amuxix.pattern.{Pattern, RunePattern}
import me.amuxix.runes.{Compass, Rune, Test, Test2}
import me.amuxix.util.Matrix4
import org.bukkit.event.player.PlayerInteractEvent

/**
  * Created by Amuxix on 21/11/2016.
  * This is the class that knows how to look for runes in the world
  */
object Matcher {

  private val patterns: Seq[RunePattern] = Seq(Test, Test2, Compass)

  /**
    * Looks for runes at the given location
    * @param location position to look for runes
    */
  def lookForRunesAt(event: PlayerInteractEvent): Option[Rune] = {
    val possiblePatterns: Set[Pattern] = patterns.map(_.pattern).toSet
    matchRunes(event, possiblePatterns)
  }

  def matchRunes(event: PlayerInteractEvent, possiblePatterns: Set[Pattern]): Option[Rune] = { //The pattern set needs to be sorted to allow some runes to have priority over others
    val boundingCube = BoundingCube(event.getClickedBlock, possiblePatterns)
    /*for {
      pattern <- possiblePatterns if pattern.foundIn(boundingCube)
    } yield pattern.createRune(center, activator, boundingCube, )*/

    for (pattern <- possiblePatterns) {
      val maybeMatrix: Option[Matrix4] = pattern.foundIn(boundingCube)
      if (maybeMatrix.isDefined) {
        return Some(pattern.createRune(event, pattern.getRuneBlocks(boundingCube), maybeMatrix.get, boundingCube.center))
      }
    }
    None
  }
}
