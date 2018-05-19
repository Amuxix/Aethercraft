package me.amuxix.inventory.items

import com.github.ghik.silencer.silent
import me.amuxix.Player
import me.amuxix.inventory.Item
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta

/**
  * Created by Amuxix on 02/02/2017.
  */
class PlayerHead protected[inventory] (itemStack: ItemStack) extends Item(itemStack) {
  private def skullMeta = meta.asInstanceOf[SkullMeta]

  def hasOwner: Boolean = skullMeta.hasOwner

  @silent def owner: Option[Player] = Player.named(skullMeta.getOwner)

  @silent def owner_=(player: Player): Unit = {
    meta = {
      val newMeta = skullMeta
      newMeta.setOwner(player.name)
      newMeta
    }
  }
}
