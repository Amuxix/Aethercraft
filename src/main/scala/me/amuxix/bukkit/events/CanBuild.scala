package me.amuxix.bukkit.events

import me.amuxix.bukkit.{Player => BPlayer}
import me.amuxix.Player
import me.amuxix.bukkit.block.Block
import org.bukkit.block.data.BlockData
import org.bukkit.event.block.BlockCanBuildEvent

class CanBuild(block: Block, player: Player, blockData: BlockData) extends BlockCanBuildEvent(block.bukkitForm.getBlock, player.asInstanceOf[BPlayer].bukkitForm, blockData, true)
