package me.amuxix

import io.circe.{Encoder, _}
import me.amuxix.Block.Location
import me.amuxix.Position._
import me.amuxix.events.{RunecraftBreakEvent, RunecraftPlaceEvent}
import me.amuxix.material.Material.{Air, Stone}
import me.amuxix.material.{Crushable, Material}
import org.bukkit.block.{BlockState, Block => BukkitBlock}

/**
  * Created by Amuxix on 22/11/2016.
  */
object Block {
  implicit def BukkitBlock2Block(bukkitBlock: BukkitBlock): Block = Block(bukkitLocation2Position(bukkitBlock.getLocation), bukkitBlock.getState.getData)
  type Location = Position[Int]

  implicit val encodeBlock: Encoder[Block] = Encoder.forProduct2("location", "material")(b =>
    (b.location, b.material) //This works, intelliJ just doesn't know it.
  )
  implicit val decodeBlock: Decoder[Block] = Decoder.forProduct2("location", "material")(Block.apply)
}

case class Block(location: Location, material: Material) {
  val state: BlockState = location.world.getBlockAt(location.x, location.y, location.z).getState

  def setMaterial(material: Material): Unit = {
    state.setType(material.getItemType)
    state.setData(material)
    state.update(true)
  }

  /**
    * Attempts to move this block by the displacement vector.
    * @param displacementVector Vector that defines the move.
    * @return true if the move was successful, false otherwise.
    */
  def move(displacementVector: Vector3[Int], player: Player): Boolean = {
    val target: Location = location + displacementVector
    moveTo(target, player)
  }

  /**
    * Attempts to move this block to the target location.
    * @param target Location where the block should be moved to.
    * @return true if the move was successful, false otherwise.
    */
  def moveTo(target: Location, player: Player): Boolean = {
    if (canMoveTo(target, player)) {
      target.block.setMaterial(this.material)
      setMaterial(Air)
      true
    } else {
      false
    }
  }

  /**
    * Checks if the player can move this block to the target location, it check if the block can be destroyed at
    * the original location and placed at the target.
    * @param target Target of the move
    * @param player Player who triggered the move
    * @return true if the player can move this block, false otherwise
    */
  def canMoveTo(target: Location, player: Player): Boolean = {
    if (target.block.material.isInstanceOf[Crushable]) {
      val placeEvent = RunecraftPlaceEvent(target, material, player)
      val breakEvent = RunecraftBreakEvent(location, player)
      Runecraft.server.getPluginManager.callEvent(placeEvent)
      Runecraft.server.getPluginManager.callEvent(breakEvent)
      if (breakEvent.isCancelled == false && placeEvent.isCancelled == false && placeEvent.canBuild) {
        return true
      }
    }
    false
  }

  /**
    * Consumes this block and gives energy to the player
    *
    * @param player Player who receives the energy for consuming this block
    */
  def consume(player: Player): Unit = setMaterial(Stone)

  override def toString: String = s"(${location.toString}, ${material.toString})"

  def canEqual(other: Any): Boolean = other.isInstanceOf[Block]

  override def equals(other: Any): Boolean = other match {
    case that: Block =>
      (that canEqual this) &&
        location.equals(that.location)
    case _ => false
  }

  override def hashCode(): Int = {
    val state = Seq(location)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }
}
