package me.amuxix.util

import java.util.UUID

/**
  * Created by Amuxix on 17/01/2017.
  */
/**
  * Represents an anonymous player, the UUID is randomly generated.
  */
object Anonymous extends Player(UUID.fromString("52415616-6703-44c4-af42-3ec6454eadbe"), 0, 0)
