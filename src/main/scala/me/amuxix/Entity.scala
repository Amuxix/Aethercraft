package me.amuxix

import me.amuxix.position.EntityPosition

/**
  * Created by Amuxix on 30/12/2016.
  */
object Entity {

}
abstract class Entity {
  def position: Option[EntityPosition]
}
