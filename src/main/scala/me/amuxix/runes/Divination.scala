package me.amuxix.runes

import cats.data.EitherT
import cats.effect.IO
import me.amuxix.block.Block
import me.amuxix.inventory.Item
import me.amuxix.material.Material.{Bookshelf, RedstoneTorch, RedstoneWire, Stick}
import me.amuxix.pattern.{ActivationLayer, NotInRune, Pattern, RunePattern}
import me.amuxix.position.BlockPosition
import me.amuxix.runes.traits.enchants.{BlockInteractTrigger, Enchant, RuneEnchant}
import me.amuxix.{Direction, Matrix4, Player}

object Divination extends RunePattern[Divination] with Enchant with BlockInteractTrigger {
  // format: off
  override val layers: List[ActivationLayer] = List(
    ActivationLayer(
      NotInRune,     RedstoneWire, RedstoneTorch,
      RedstoneWire,  Bookshelf,    RedstoneWire,
      RedstoneTorch, RedstoneWire, RedstoneWire,
    )
  )
  // format: on
  /** The validation to check if the item can be enchanted. */
  override def itemValidation(item: Item): Boolean = item.material == Stick

  /** The description of possible items that can be enchanted by this rune to be given as error if it fails.*/
  override val itemDescription: String = "sticks"

  /** This should run the effect of the enchant and return whether to cancel the event or not */
  override def onBlockInteract(
    player: Player,
    itemInHand: Item,
    clickedBlock: Block,
    blockFace: Direction
  ): EitherT[IO, String, Boolean] = {
    val material = clickedBlock.material
    val message = material.tier.flatMap(t => material.energy.map((t, _)))
      .fold(s"${material.name} is inert.") {
        case (tier, energy) => s"${material.name} is tier $tier and is worth $energy energy."
      }
    EitherT.liftF(player.notify(message)).map(_ => true)
  }
}

case class Divination(
  center: BlockPosition,
  creator: Player,
  direction: Direction,
  rotation: Matrix4,
  pattern: Pattern
) extends Rune with RuneEnchant {
  override val enchant: Enchant = Divination
}
