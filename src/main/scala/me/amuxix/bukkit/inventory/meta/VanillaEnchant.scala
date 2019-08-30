package me.amuxix.bukkit.inventory.meta

import me.amuxix.Aetherizeable
import me.amuxix.bukkit.BukkitForm
import me.amuxix.inventory.meta.VanillaEnchant.{VanillaEnchant, _}
import org.bukkit.enchantments.{Enchantment => BukkitEnchantment}

import scala.collection.immutable.HashMap

object VanillaEnchant {
  private lazy val vanillaEnchantToBukkitEnchant: Map[VanillaEnchant, BukkitEnchantment] = HashMap(
    PROTECTION_ENVIRONMENTAL -> BukkitEnchantment.PROTECTION_ENVIRONMENTAL,
    PROTECTION_FIRE -> BukkitEnchantment.PROTECTION_FIRE,
    PROTECTION_FALL -> BukkitEnchantment.PROTECTION_FALL,
    PROTECTION_EXPLOSIONS -> BukkitEnchantment.PROTECTION_EXPLOSIONS,
    PROTECTION_PROJECTILE -> BukkitEnchantment.PROTECTION_PROJECTILE,
    OXYGEN -> BukkitEnchantment.OXYGEN,
    WATER_WORKER -> BukkitEnchantment.WATER_WORKER,
    THORNS -> BukkitEnchantment.THORNS,
    DEPTH_STRIDER -> BukkitEnchantment.DEPTH_STRIDER,
    FROST_WALKER -> BukkitEnchantment.FROST_WALKER,
    BINDING_CURSE -> BukkitEnchantment.BINDING_CURSE,
    DAMAGE_ALL -> BukkitEnchantment.DAMAGE_ALL,
    DAMAGE_UNDEAD -> BukkitEnchantment.DAMAGE_UNDEAD,
    DAMAGE_ARTHROPODS -> BukkitEnchantment.DAMAGE_ARTHROPODS,
    KNOCKBACK -> BukkitEnchantment.KNOCKBACK,
    FIRE_ASPECT -> BukkitEnchantment.FIRE_ASPECT,
    LOOT_BONUS_MOBS -> BukkitEnchantment.LOOT_BONUS_MOBS,
    SWEEPING_EDGE -> BukkitEnchantment.SWEEPING_EDGE,
    DIG_SPEED -> BukkitEnchantment.DIG_SPEED,
    SILK_TOUCH -> BukkitEnchantment.SILK_TOUCH,
    DURABILITY -> BukkitEnchantment.DURABILITY,
    LOOT_BONUS_BLOCKS -> BukkitEnchantment.LOOT_BONUS_BLOCKS,
    ARROW_DAMAGE -> BukkitEnchantment.ARROW_DAMAGE,
    ARROW_KNOCKBACK -> BukkitEnchantment.ARROW_KNOCKBACK,
    ARROW_FIRE -> BukkitEnchantment.ARROW_FIRE,
    ARROW_INFINITE -> BukkitEnchantment.ARROW_INFINITE,
    LUCK -> BukkitEnchantment.LUCK,
    LURE -> BukkitEnchantment.LURE,
    LOYALTY -> BukkitEnchantment.LOYALTY,
    IMPALING -> BukkitEnchantment.IMPALING,
    RIPTIDE -> BukkitEnchantment.RIPTIDE,
    CHANNELING -> BukkitEnchantment.CHANNELING,
    MULTISHOT -> BukkitEnchantment.MULTISHOT,
    QUICK_CHARGE -> BukkitEnchantment.QUICK_CHARGE,
    PIERCING -> BukkitEnchantment.PIERCING,
    MENDING -> BukkitEnchantment.MENDING,
    VANISHING_CURSE -> BukkitEnchantment.VANISHING_CURSE,
  )

  private lazy val bukkitEnchantToVanillaEnchant: Map[BukkitEnchantment, VanillaEnchant] = vanillaEnchantToBukkitEnchant.map(_.swap)

  implicit class BukkitEnchantOps(enchant: BukkitEnchantment) extends Aetherizeable[VanillaEnchant] {
    override def aetherize: VanillaEnchant = bukkitEnchantToVanillaEnchant.getOrElse(enchant, throw new Exception(s"$enchant is missing its aether form"))
  }

  implicit class VanillaEnchantOps(enchant: VanillaEnchant) extends BukkitForm[BukkitEnchantment] {
    override def bukkitForm: BukkitEnchantment = vanillaEnchantToBukkitEnchant.getOrElse(enchant, throw new Exception(s"$enchant has no matching bukkit form"))
  }

}
