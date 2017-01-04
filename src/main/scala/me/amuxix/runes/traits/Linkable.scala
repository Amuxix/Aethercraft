package me.amuxix.runes.traits

import me.amuxix.pattern.Signature
import me.amuxix.runes.Rune
import me.amuxix.util.{Block, Player}
import org.bukkit.Material
import org.bukkit.Material.AIR

/**
  * Created by Amuxix on 22/11/2016.
  */
/**
  * Defines a trait for runes that use signature blocks to link to other stuff: ie: Teleports, Waypoints
  */
trait Linkable { this: Rune =>
  val signatureBlocks: Seq[Block] = specialBlocks(Signature)

  //This is true if the set only contains true, meaning all blocks are air
  val signatureIsEmpty: Boolean = signatureBlocks.forall(_.material == AIR)

  /**
    * Checks if the signature contains the material
    * @param material Material to be checked
    * @return true if signature contains a block with the given material
    */
  def signatureContains(material: Material): Boolean = {
    signatureBlocks.map(_.material).contains(material)
  }

  /**
    * Calculates the signature for this rune
    * @return The signature
    */
  def calculateSignature(): Int = specialBlocks(Signature).map(_.material.name).sorted.hashCode
  var signature: Int = calculateSignature()

  /**
    * Checks whether the signature is valid for this rune and notifies player if it is not and why
    * @param player player to be notified in case of signature being invalid
    * @return true if signature is valid, false otherwise
    */
  def validateSignature(player: Player): Boolean

  validateSignature(activator)
}
