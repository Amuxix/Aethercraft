package me.amuxix.util.events

import me.amuxix.util.Block.Location
import me.amuxix.util.Player
import org.bukkit.event.block.BlockBreakEvent

/**
  * Created by Amuxix on 19/01/2017.
  */
case class RunecraftBreakEvent(target: Location, player: Player) extends BlockBreakEvent(
  target.block.state.getBlock,
  player.getPlayer match {
    case Left(_) => null
    case Right(p) => p
  })
