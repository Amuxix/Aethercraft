package me.amuxix.bukkit.events

import me.amuxix.Player
import me.amuxix.block.Block
import me.amuxix.bukkit.block.{Block => BukkitBlock}
import me.amuxix.bukkit.{Player => BPlayer}
import org.bukkit.event.block.{BlockBreakEvent => BukkitBlockBreakEvent}

class BlockBreak(block: Block, player: Player) extends BukkitBlockBreakEvent(block.asInstanceOf[BukkitBlock].bukkitForm.getBlock, player.asInstanceOf[BPlayer].bukkitForm)
