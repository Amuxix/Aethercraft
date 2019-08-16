package me.amuxix.bukkit.inventory.items

import cats.data.{EitherT, OptionT}
import cats.effect.IO
import cats.implicits.catsStdInstancesForOption
import cats.implicits.toTraverseOps
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
  private lazy val maybeSkullMeta: Option[SkullMeta] = maybeMeta.map(_.asInstanceOf[SkullMeta])

  override def hasOwner: Boolean = maybeSkullMeta.exists(_.hasOwner)

  override def owner: Option[Player] =
    for {
      meta <- maybeSkullMeta
      owner <- Option(meta.getOwningPlayer)
    } yield BPlayer(owner.getUniqueId)

  override def setOwner(player: Player): EitherT[IO, String, Unit] = {
    OptionT(maybeSkullMeta.traverse { skullMeta =>
      val offlinePlayer = Bukkit.server.getOfflinePlayer(player.uuid)
      skullMeta.setOwningPlayer(offlinePlayer)
      setMeta(skullMeta)
    }).toRight("Failed to set owner")
  }

  override def isTrueNameOf(player: Player): Boolean =
    hasRuneEnchant(TrueName) && displayName.contains(trueNameDisplayFor(player))
}
