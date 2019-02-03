package me.amuxix.bukkit.block

import cats.data.OptionT
import cats.effect.IO
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
import scala.util._


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

  override def setMaterial(material: Material): OptionT[IO, String] = OptionT.fromOption[IO]{
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
  override def move(displacementVector: Vector3[Int], player: Player): OptionT[IO, String] = moveTo(location + displacementVector, player)

  /**
    * Attempts to move this block to the target location.
    * @param target Location where the block should be moved to.
    * @return true if the move was successful, false otherwise.
    */
  override def moveTo(target: Location, player: Player): OptionT[IO, String] =
      OptionT.fromOption[IO](canMoveTo(target, player)).orElse {
      val moveInventory: OptionT[IO, String] = this match {
        case inv: Inventory =>
          Try(Block(target, material).asInstanceOf[Chest]).fold(
            _ => OptionT.pure[IO]("Failed to move inventory."),
            chest => OptionT(inv.replaceContentsOf(chest.inventory).map(_ => Option.empty[String]))
          )
        case _ => OptionT.none
      }

      val targetBlock = target.block.asInstanceOf[Block]
      if (targetBlock.material.isCrushable) {
        Bukkit.callEvent(new BlockBreak(target.block, player))
      }
      val replacedBlock = Block(target, targetBlock.material)
      Bukkit.callEvent(new BlockPlace(target.block, replacedBlock, player))

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
        Bukkit.callEvent(breakEvent)
        Option.when(breakEvent.isCancelled)( s"Failed to break $target")
      }
      val thisBreak = new BlockBreak(this, player)
      Bukkit.callEvent(thisBreak)

      val canBuild = new CanBuild(targetBlock, player, state.getBlockData)
      Bukkit.callEvent(canBuild)

      Option.when(thisBreak.isCancelled)(s"Failed to break $this")
        .orElse(targetDestruction)
        .orElse(Option.unless(canBuild.isBuildable)(s"Failed to build $targetBlock"))
    }
  }

  private def replacementMaterial: Option[Material] = material match {
    case `Stone` => Some(Air)
    case m if m.hasEnergy && m.isAttachable => Some(Air)
    case m if m.hasEnergy && m.isSolid => Some(Stone)
    case m if m.hasEnergy && m.isSolid == false => Some(Air)
    case _ => None
  }


  private def consumeBlockMaterial: Option[ConsumeIO] =
    replacementMaterial.flatMap { replacementMaterial =>
      for {
        replacementEnergy <- replacementMaterial.energy
        materialEnergy <- material.energy
      } yield (materialEnergy - replacementEnergy) -> setMaterial(replacementMaterial)
    }

  /**
    * Changes this material to air or stone depending on if the material is solid or not and returns the worth of this block.
    * If this block has an inventory and one of the items has no energy it will do nothing
    * @return An IO that when ran tries to consume this block
    */
/*  override def consumeAtomically: Option[(Energy, OptionT[IO, String])] = {
    val consumeInventory: Option[(Energy, OptionT[IO, String])] = this match {
      case inv: Inventory => inv.consumeAtomically
      case _ => Some((0 Energy, OptionT.none))
    }
    consumeBlockMaterial.flatMap {
      case (energy, io) => consumeInventory.map {
        (invEnergy, invIO) => (energy + invEnergy, io)
      }
    }
    for {
      inventoryEnergy <- consumeInventory
      blockEnergy <- consumeBlockMaterial
    } yield inventoryEnergy + blockEnergy
  }*/

  /**
    * Returns a List of IO to consume each part individually, this is consumed from left to right
    */
  override def consume: List[(List[ConsumeIO], Option[ConsumeIO])] = {
    val consumeInventory: List[ConsumeIO] = this match {
      case inv: Inventory => inv.consume.head._1
      case _ => List.empty
    }
    List((consumeInventory, consumeBlockMaterial))
  }

  override def toString: String = s"(${location.toString}, ${material.toString})"

  override def equals(other: Any): Boolean = other match {
    case that: Block => location.equals(that.location)
    case _ => false
  }

  override def hashCode(): Int = location.hashCode()

  override def bukkitForm: BlockState = state
}
