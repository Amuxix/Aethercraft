package me.amuxix.util

import org.bukkit.entity.{Player => BPlayer}

/**
  * Created by Amuxix on 22/11/2016.
  */
object Player {
  implicit def bukkitPlayer2Player(bukkitPlayer: BPlayer): Player = {
    Player(bukkitPlayer, bukkitPlayer.getLocation.getPitch, bukkitPlayer.getLocation.getYaw)
  }
  type Location = Position[Double]
}
case class Player(location: Player.Location, pitch: Double, yaw: Double) extends Entity {

}
