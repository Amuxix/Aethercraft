package me.amuxix.bukkit.inventory.items

import cats.effect.IO
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
  private lazy val skullMeta: SkullMeta = meta.asInstanceOf[SkullMeta]

  override def hasOwner: Boolean = skullMeta.hasOwner

  override def owner: Option[Player] = Option(skullMeta.getOwningPlayer).map(owner => BPlayer(owner.getUniqueId))

  override def setOwner(player: Player): IO[Unit] = {
    val offlinePlayer = Bukkit.server.getOfflinePlayer(player.uuid)
    skullMeta.setOwningPlayer(offlinePlayer)
    setMeta(skullMeta)
  }

  override def isTrueNameOf(player: Player): Boolean =
    hasRuneEnchant(TrueName) && displayName.contains(trueNameDisplayFor(player))
}
