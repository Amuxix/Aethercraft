package me.amuxix.util

import java.util.UUID

/**
  * Created by Amuxix on 17/01/2017.
  */
/**
  * Represents an anonymous player.
  */
object Anonymous extends Player(UUID.randomUUID(), 0, 0)
