package me.amuxix.bukkit.block

import me.amuxix.block.Block.Location
import me.amuxix.bukkit.Location.BukkitIntPositionOps
import me.amuxix.bukkit.Material.{BukkitMaterialOps, MaterialOps}
import me.amuxix.bukkit.block.blocks.Chest
import me.amuxix.bukkit.events._
import me.amuxix.bukkit.{Player => _, block => _, _}
import me.amuxix.inventory.Inventory
import me.amuxix.material.Material
import me.amuxix.material.Material._
import me.amuxix.{World => _, _}
import org.bukkit.block.BlockState

import scala.collection.immutable.HashMap
import scala.util.Try


object Block {
  private val blocks: Map[Material, Location => Block] = HashMap[Material, Location => Block](
    Chest -> (new Chest(_))
  )

  /**
    * Creates a new block depending on the material
    * @param location Location of this block
    * @param material Material at the given location
    * @return A block of a specific type if a type for the given material exists or a generic block otherwise
    */
  def apply(location: Location, material: Material): Block =
    //This lookups the constructor of the block at the blocks map, returning that or a default constructor to create a generic block
    blocks.getOrElse(material, new Block(_, material))(location)

  implicit class BukkitBlockOps(state: org.bukkit.block.Block) extends Aetherizeable[Block] {
    def aetherize: Block = Block(state.getLocation.aetherize, state.getType.aetherize)
  }
}

private[bukkit] class Block(val location: Location, var material: Material) extends block.Block with BukkitForm[BlockState] {
  protected val state: BlockState = location.world.asInstanceOf[World].world.getBlockAt(location.x, location.y, location.z).getState

  override def setMaterial(material: Material): Option[String] = {
    state.setType(material.bukkitForm)
    this.material = material
    Option.unless(state.update(true))(Aethercraft.defaultFailureMessage)
  }

  /**
    * Attempts to move this block by the displacement vector.
 *
    * @param displacementVector Vector that defines the move.
 *
    * @return true if the move was successful, false otherwise.
    */
  override def move(displacementVector: Vector3[Int], player: Player): Option[String] = moveTo(location + displacementVector, player)

  /**
    * Attempts to move this block to the target location.
    * @param target Location where the block should be moved to.
    * @return true if the move was successful, false otherwise.
    */
  override def moveTo(target: Location, player: Player): Option[String] =
    canMoveTo(target, player).orElse {
      def moveInventory: Option[String] = this match {
        case inv: Inventory =>
          Try(Block(target, material).asInstanceOf[Chest]).toOption
            .map { chest =>
              inv.replaceContentsOf(chest.inventory)
            } match {
            case None => Some("Failed to move inventory.")
            case Some(_) => None
          }
        case _ => None
      }

      val targetBlock = target.block.asInstanceOf[Block]
      if (targetBlock.material.isCrushable) {
        Aethercraft.callEvent(new BlockBreak(target.block, player))
      }
      val replacedBlock = Block(target, targetBlock.material)
      Aethercraft.callEvent(new BlockPlace(target.block, replacedBlock, player))

      targetBlock.setMaterial(material)
        .orElse(moveInventory)
        .orElse(setMaterial(Air))
    }

  /**
    * Checks if the player can move this block to the target location, it check if the block can be destroyed at
    * the original location and placed at the target.
    * @param target Target of the move
    * @param player Player who triggered the move
    * @return true if the player can move this block, false otherwise
    */
  override def canMoveTo(target: Location, player: Player): Option[String] = {
    val targetBlock = target.block.asInstanceOf[Block]
    Option.flatWhen(targetBlock.material.isCrushable) {
      val targetDestruction = Option.flatWhen(targetBlock.material.isAir) {
        val breakEvent = new BlockBreak(targetBlock, player)
        Aethercraft.callEvent(breakEvent)
        Option.unless(breakEvent.isCancelled)( s"Failed to break $target")
      }
      val thisBreak = new BlockBreak(this, player)
      Aethercraft.callEvent(thisBreak)

      val canBuild = new CanBuild(targetBlock, player, state.getBlockData)
      Aethercraft.callEvent(canBuild)

      Option.unless(thisBreak.isCancelled)(s"Failed to break $this")
        .orElse(targetDestruction)
        .orElse(Option.when(canBuild.isBuildable)(s"Failed to build $targetBlock"))
    }
  }

  /**
    * Consumes this block and gives energy to the player
    *
    * @param player Player who receives the energy for consuming this block
    * @return Energy received by the player, 0 if material cannot be consumed
    */
  override def consume(player: Player): Int = (material match {
    case m if m.hasEnergy && m.isAttachable => Some(Air)
    case m if m.hasEnergy && m.isSolid => Some(Stone)
    case m if m.hasEnergy && m.isSolid == false => Some(Air)
    case _ => None
  }).fold(0) { mat =>
    this match {
      case inv: Inventory => inv.consumeContents(player)
      case _ => //Do nothing
    }
    setMaterial(mat)
    player.addEnergy(material.energy.getOrElse(0))
  }

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

  override def bukkitForm: BlockState = state
}
