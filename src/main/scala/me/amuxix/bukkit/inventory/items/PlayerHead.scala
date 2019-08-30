package me.amuxix.bukkit.inventory.items

import cats.data.EitherT
import cats.effect.IO
import me.amuxix.Player
import me.amuxix.bukkit.inventory.Item
import me.amuxix.bukkit.inventory.meta.metas.SkullMeta
import me.amuxix.inventory.items
import me.amuxix.runes.TrueName
import me.amuxix.runes.TrueName.trueNameDisplayFor
import org.bukkit.inventory.ItemStack

/**
  * Created by Amuxix on 02/02/2017.
  */
private[bukkit] class PlayerHead(itemStack: ItemStack) extends Item(itemStack) with items.PlayerHead {
  lazy val maybeSkullMeta: Option[SkullMeta] = meta.map(_.asInstanceOf[SkullMeta])

  override def hasOwner: Boolean = maybeSkullMeta.exists(_.hasOwner)

  override def owner: Option[Player] = maybeSkullMeta.flatMap(_.owner)

  override def setOwner(player: Player): EitherT[IO, String, Unit] =
    EitherT.fromEither[IO](maybeSkullMeta.map(_.setOwner(player)).toRight("Failed to set owner"))

  override def isTrueNameOf(player: Player): Boolean =
    hasRuneEnchant(TrueName) && displayName.contains(trueNameDisplayFor(player))
}
