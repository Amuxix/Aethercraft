package me.amuxix.bukkit.inventory.meta.metas

import me.amuxix.Player
import me.amuxix.bukkit.Player.BukkitPlayerOps
import me.amuxix.bukkit.inventory.meta.ItemMeta
import me.amuxix.bukkit.{Player => BukkitPlayer}
import me.amuxix.inventory.meta.metas
import me.amuxix.material.Material.PlayerHead
import org.bukkit.inventory.meta.{SkullMeta => BukkitSkullMeta}

class SkullMeta(itemMeta: BukkitSkullMeta) extends ItemMeta(itemMeta, PlayerHead) with metas.SkullMeta {

  override def hasOwner: Boolean = itemMeta.hasOwner

  override def owner: Option[Player] = Option(itemMeta.getOwningPlayer).map(_.aetherize)

  override def setOwner(player: Player): Unit =
    itemMeta.setOwningPlayer(player.asInstanceOf[BukkitPlayer].bukkitForm)
}
