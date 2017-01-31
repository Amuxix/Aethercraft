package me.amuxix.events

import com.github.ghik.silencer.silent
import me.amuxix.Block.Location
import me.amuxix.Player
import me.amuxix.events.RunecraftPlaceEvent._
import me.amuxix.material.Material
import org.bukkit.block.Block
import org.bukkit.entity.{Player => BPlayer}
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.inventory.ItemStack

/**
  * Created by Amuxix on 19/01/2017.
  */
object RunecraftPlaceEvent {
  def getPlacedBlock(target: Location, material: Material): Block = {
    target.block.state.setData(material)
    target.block.state.update(true)
    target.block.state.getBlock
  }

  def getPlayer(runePlayer: Player): BPlayer = {
    runePlayer.getPlayer match {
      case Left(_) => null
      case Right(p) => p
    }
  }

  def getItemInHand(runePlayer: Player): ItemStack = {
    runePlayer.getPlayer match {
      case Left(_) => null
      case Right(p) => p.getInventory.getItemInMainHand
    }
  }
}
@silent case class RunecraftPlaceEvent(target: Location, material: Material, runePlayer: Player)
  extends BlockPlaceEvent(getPlacedBlock(target, material), target.block.state, target.block.state.getBlock, getItemInHand(runePlayer), getPlayer(runePlayer), true)
