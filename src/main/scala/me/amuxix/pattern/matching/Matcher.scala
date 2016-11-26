package me.amuxix.pattern.matching

import me.amuxix.pattern.Pattern
import me.amuxix.util.{Location, Matrix4}
import org.bukkit.World

import scala.collection.immutable.SortedSet

/**
  * Created by Amuxix on 21/11/2016.
  * This is the class that knows how to look for runes in the world
  */
class Matcher {

  def matchRunes(world: World, possiblePatterns: SortedSet[Pattern], center: Location) = {
    val boundingCube = BoundingCube(world, possiblePatterns, center)
    for (pattern <- possiblePatterns) {
      if (pattern.foundIn(boundingCube)) {
        true //Return rune here
      }
    }
  }
}
