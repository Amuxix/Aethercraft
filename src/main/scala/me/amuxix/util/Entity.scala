package me.amuxix.util

import me.amuxix.util.Player.Location

/**
  * Created by Amuxix on 30/12/2016.
  */
object Entity {

}
abstract class Entity {
  val location: Location
  val pitch: Double
  val yaw: Double
}
