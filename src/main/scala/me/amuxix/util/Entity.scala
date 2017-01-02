package me.amuxix.util

import me.amuxix.util.Player.Location

/**
  * Created by Amuxix on 30/12/2016.
  */
object Entity {

}
abstract class Entity {
  def location: Option[Location]
  def pitch: Double
  def yaw: Double
}
