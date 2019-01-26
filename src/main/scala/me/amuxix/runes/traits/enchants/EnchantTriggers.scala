package me.amuxix.runes.traits.enchants

import cats.data.EitherT
import cats.effect.IO
import me.amuxix.block.Block
import me.amuxix.inventory.Item
import me.amuxix.{Direction, Player}

trait BlockBreakTrigger { this: Enchant =>
  Enchant.blockBreakEnchants +:= this
  /** This should run the effect of the enchant and return whether to cancel the event or not */
  def onBlockBreak(player: Player, brokenBlock: Block): EitherT[IO, String, Boolean]
}

trait BlockPlaceTrigger { this: Enchant =>
  Enchant.blockPlaceEnchants +:= this
  /** This should run the effect of the enchant and return whether to cancel the event or not */
  def onBlockPlace(player: Player, placedBlock: Block, placedAgainstBlock: Block, itemPlaced: Option[Item]): EitherT[IO, String, Boolean]
}

trait BlockDamageTrigger { this: Enchant =>
  Enchant.blockDamageEnchants +:= this
  /** This should run the effect of the enchant and return whether to cancel the event or not */
  def onBlockDamage(player: Player, itemInHand: Option[Item], clickedBlock: Block, blockFace: Direction): EitherT[IO, String, Boolean]
}

trait BlockInteractTrigger { this: Enchant =>
  Enchant.blockInteractEnchants +:= this
  /** This should run the effect of the enchant and return whether to cancel the event or not */
  def onBlockInteract(player: Player, itemInHand: Option[Item], clickedBlock: Block, blockFace: Direction): EitherT[IO, String, Boolean]
}

trait AirSwingTrigger { this: Enchant =>
  Enchant.airSwingEnchants +:= this
  /** This should run the effect of the enchant and return whether to cancel the event or not */
  def onAirSwing(player: Player, itemInHand: Option[Item]): EitherT[IO, String, Boolean]
}

trait AirInteractTrigger { this: Enchant =>
  Enchant.airInteractEnchants +:= this
  /** This should run the effect of the enchant and return whether to cancel the event or not */
  def onAirInteract(player: Player, itemInHand: Option[Item]): EitherT[IO, String, Boolean]
}