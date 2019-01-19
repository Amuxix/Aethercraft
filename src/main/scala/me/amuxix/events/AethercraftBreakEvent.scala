package me.amuxix.events

import me.amuxix.Block.Location
import me.amuxix.Player
import org.bukkit.event.block.BlockBreakEvent

/**
  * Created by Amuxix on 19/01/2017.
  */
case class AethercraftBreakEvent(target: Location, player: Player) extends BlockBreakEvent(
  target.block.state.getBlock,
  player.getPlayer match {
    case Left(_) => null
    case Right(p) => p
  })
