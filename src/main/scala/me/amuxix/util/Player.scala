package me.amuxix.util

/**
  * Created by Amuxix on 22/11/2016.
  */
object Player {
  implicit def bukkitPlayer2Player(bukkitPlayer: org.bukkit.entity.Player): Player = {
    Player(bukkitPlayer.getLocation)
  }
}
case class Player(location: Location) {

}
