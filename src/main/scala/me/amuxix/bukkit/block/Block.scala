package me.amuxix.bukkit.block

import cats.data.OptionT
import cats.effect.IO
import me.amuxix.bukkit.Location.BukkitBlockPositionOps
import me.amuxix.bukkit.Material.{BukkitMaterialOps, MaterialOps}
import me.amuxix.bukkit.block.blocks.Chest
import me.amuxix.bukkit.events._
import me.amuxix.bukkit.{Player => _, block => _, _}
import me.amuxix.inventory.{Inventory, Item}
import me.amuxix.material.Material
import me.amuxix.material.Properties.BlockProperty
import me.amuxix.material.Material._
import me.amuxix.position.{BlockPosition, Vector3}
import me.amuxix.{World => _, _}
import org.bukkit.block.BlockState

import scala.collection.immutable.HashMap
import scala.util._


object Block {
  private val blocks: Map[Material with BlockProperty, BlockPosition => Block] = HashMap[Material with BlockProperty, BlockPosition => Block](
    Chest -> (new Chest(_))
  )

  /**
    * Creates a new block depending on the material
    * @param location Location of this block
    * @param material Material at the given location
    * @return A block of a specific type if a type for the given material exists or a generic block otherwise
    */
  def apply(location: BlockPosition, material: Material with BlockProperty): Block =
    //This lookups the constructor of the block at the blocks map, returning that or a default constructor to create a generic block
    blocks.getOrElse(material, new Block(_, material))(location)

  implicit class BukkitBlockOps(state: org.bukkit.block.Block) extends Aetherizeable[Block] {
    def aetherize: Block = Block(state.getLocation.aetherize, state.getType.aetherize.asInstanceOf[Material with BlockProperty])
  }
}

private[bukkit] class Block(val location: BlockPosition, var material: Material with BlockProperty) extends block.Block with BukkitForm[BlockState] {
  protected val state: BlockState = location.world.asInstanceOf[World].world.getBlockAt(location.x, location.y, location.z).getState

  override def setMaterial(material: Material with BlockProperty): OptionT[IO, String] = OptionT.fromOption[IO]{
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
  override def moveTo(target: BlockPosition, player: Player): OptionT[IO, String] =
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

  protected def canBreak(player: Player): Option[String] = {
    val breakEvent = new BlockBreak(this, player)
    Bukkit.callEvent(breakEvent)
    Option.when(breakEvent.isCancelled)(s"Failed to break $this")
  }

  /**
    * Checks if the player can move this block to the target location, it check if the block can be destroyed at
    * the original location and placed at the target.
    * @param target Target of the move
    * @param player Player who triggered the move
    * @return None if the player can move this block, Some with an error otherwise
    */
  override def canMoveTo(target: BlockPosition, player: Player): Option[String] = {
    val targetBlock = target.block.asInstanceOf[Block]
    Option.unless(targetBlock.material.isCrushable)(s"Obstacle is ${targetBlock.material} at $target.")
      .orElse {
        val canBuild = new CanBuild(targetBlock, player, state.getBlockData)
        Bukkit.callEvent(canBuild)

        canBreak(player)
          .orElse(targetBlock.canBreak(player))
          .orElse(Option.unless(canBuild.isBuildable)(s"Failed to build $targetBlock"))
      }
  }

  private def replacementMaterial: Option[Material with BlockProperty] = material match {
    case `Stone` => Some(Air)
    case m if m.hasEnergy && (m.isAttachable || !m.isSolid) => Some(Air)
    case m if m.hasEnergy && m.isSolid => Some(Stone)
    case _ => None
  }

  //TODO check if this block can be broken before consuming
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

  private def breakWith(player: Player, item: Option[Item]): OptionT[IO, String] =
    if (player.inCreativeMode)  {
      setMaterial(Air)
    } else {
      OptionT.fromOption[IO](
        canBreak(player).orElse {
          val blockBroken = item.fold(bukkitForm.getBlock.breakNaturally) { item =>
            bukkitForm.getBlock.breakNaturally(item.asInstanceOf[bukkit.inventory.Item].bukkitForm)
          }
          Option.unless(blockBroken)(Aethercraft.defaultFailureMessage)
        }
      )
    }

  override def breakUsing(player: Player, item: Item): OptionT[IO, String] =
    breakWith(player, Some(item))

  override def break(player: Player): OptionT[IO, String] = breakWith(player, None)

  override def toString: String = s"(${location.toString}, ${material.toString})"

  override def equals(other: Any): Boolean = other match {
    case that: Block => location.equals(that.location)
    case _ => false
  }

  override def hashCode(): Int = location.hashCode()

  override def bukkitForm: BlockState = state
}
