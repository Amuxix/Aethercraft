package me.amuxix.inventory.meta.metas

import me.amuxix.Player
import me.amuxix.inventory.meta.ItemMeta

trait SkullMeta extends ItemMeta {
  def hasOwner: Boolean

  def owner: Option[Player]

  def setOwner(player: Player): Unit
}
