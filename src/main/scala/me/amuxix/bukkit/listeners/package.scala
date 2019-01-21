package me.amuxix.bukkit

import org.bukkit.event.Event.Result
import org.bukkit.event.player.PlayerInteractEvent

package object listeners {
  implicit class PlayerInteractEventOps(event: PlayerInteractEvent) {
    /**
      * This is a workaround to the isCanceled method of PlayerInteractEvent not checking useInteractedBlock
      * @return True if both useItemInHand and useInteractedBlock actions are denied.
      */
    def isDoubleCancelled: Boolean = event.useItemInHand == Result.DENY && event.useInteractedBlock == Result.DENY
  }
}
