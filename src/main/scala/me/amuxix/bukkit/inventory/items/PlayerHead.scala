package me.amuxix.bukkit.inventory.items

import me.amuxix.Player
import me.amuxix.bukkit.inventory.Item
import me.amuxix.bukkit.{Player => BPlayer, _}
import me.amuxix.inventory.items
import me.amuxix.runes.TrueName
import me.amuxix.runes.TrueName.trueNameDisplayFor
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta

/**
  * Created by Amuxix on 02/02/2017.
  */
private[bukkit] class PlayerHead(itemStack: ItemStack) extends Item(itemStack) with items.PlayerHead {
  private def skullMeta: SkullMeta = meta.get.asInstanceOf[SkullMeta]

  override def hasOwner: Boolean = skullMeta.hasOwner

  override def owner: Option[Player] = Option(skullMeta.getOwningPlayer).map(owner => BPlayer(owner.getUniqueId))

  override def owner_=(player: Player): Unit = {
    meta = {
      val newMeta = skullMeta
      newMeta.setOwningPlayer(Aethercraft.server.getOfflinePlayer(player.uuid))
      newMeta
    }
  }

  override def isTrueNameOf(player: Player): Boolean =
    hasRuneEnchant(TrueName) && displayName.contains(trueNameDisplayFor(player))
}
