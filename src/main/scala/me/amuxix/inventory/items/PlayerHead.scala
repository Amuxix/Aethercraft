package me.amuxix.inventory.items

import me.amuxix.{Player, Aethercraft}
import me.amuxix.inventory.Item
import me.amuxix.runes.TrueName
import me.amuxix.runes.TrueName.trueNameDisplayFor
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta

/**
  * Created by Amuxix on 02/02/2017.
  */
class PlayerHead protected[inventory] (itemStack: ItemStack) extends Item(itemStack) {
  private def skullMeta = meta.asInstanceOf[SkullMeta]

  def hasOwner: Boolean = skullMeta.hasOwner

  def owner: Option[Player] = skullMeta.getOwningPlayer match {
    case null => None
    case owner => Some(Player(owner.getUniqueId))
  }

  def owner_=(player: Player): Unit = {
    meta = {
      val newMeta = skullMeta
      newMeta.setOwningPlayer(Aethercraft.server.getOfflinePlayer(player.uniqueID))
      newMeta
    }
  }

  def isTrueNameOf(player: Player): Boolean =
    hasRuneEnchant(TrueName) && displayName.contains(trueNameDisplayFor(player))
}
