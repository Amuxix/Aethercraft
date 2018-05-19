package me.amuxix.inventory.items

import com.github.ghik.silencer.silent
import me.amuxix.{Player, Runecraft}
import me.amuxix.inventory.Item
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

  @silent def owner_=(player: Player): Unit = {
    meta = {
      val newMeta = skullMeta
      newMeta.setOwningPlayer(Runecraft.server.getOfflinePlayer(player.uniqueID))
      newMeta
    }
  }
}
