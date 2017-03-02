package me.amuxix.inventory.items

import me.amuxix.Player
import me.amuxix.inventory.Item

/**
  * Created by Amuxix on 02/02/2017.
  */
class PlayerHead protected[inventory] (itemStack: ItemStack) extends Item(itemStack) {
  private def skullMeta = meta.asInstanceOf[SkullMeta]

  def hasOwner: Boolean = skullMeta.hasOwner

  def owner: Option[Player] = Player.named(skullMeta.getOwner)

  def owner_=(player: Player): Unit = {
    meta = {
      val newMeta = skullMeta
      newMeta.setOwner(player.name)
      newMeta
    }
  }
}
