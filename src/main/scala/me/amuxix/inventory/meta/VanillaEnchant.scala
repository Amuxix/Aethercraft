package me.amuxix.inventory.meta

import io.circe.{Decoder, Encoder, KeyDecoder, KeyEncoder}

import scala.collection.immutable.HashMap
import scala.util.Try

object VanillaEnchant extends Enumeration {
  implicit val decoder: Decoder[Value] = Decoder.decodeEnumeration(VanillaEnchant)
  implicit val encoder: Encoder[Value] = Encoder.encodeEnumeration(VanillaEnchant)

  implicit val keyDecoder: KeyDecoder[Value] = KeyDecoder.instance(input => Try(withName(input)).toOption)
  implicit val keyEncoder: KeyEncoder[Value] = KeyEncoder[String].contramap(_.toString)

  lazy val enchantMap = HashMap(

  )

  type VanillaEnchant = Value
/**
  * Provides protection against environmental damage
  */
  val PROTECTION_ENVIRONMENTAL,

  /**
    * Provides protection against fire damage
    */
  PROTECTION_FIRE,

  /**
    * Provides protection against fall damage
    */
  PROTECTION_FALL,

  /**
    * Provides protection against explosive damage
    */
  PROTECTION_EXPLOSIONS,

  /**
    * Provides protection against projectile damage
    */
  PROTECTION_PROJECTILE,

  /**
    * Decreases the rate of air loss whilst underwater
    */
  OXYGEN,

  /**
    * Increases the speed at which a player may mine underwater
    */
  WATER_WORKER,

  /**
    * Damages the attacker
    */
  THORNS,

  /**
    * Increases walking speed while in water
    */
  DEPTH_STRIDER,

  /**
    * Freezes any still water adjacent to ice / frost which player is walking on
    */
  FROST_WALKER,

  /**
    * Item cannot be removed
    */
  BINDING_CURSE,

  /**
    * Increases damage against all targets
    */
  DAMAGE_ALL,

  /**
    * Increases damage against undead targets
    */
  DAMAGE_UNDEAD,

  /**
    * Increases damage against arthropod targets
    */
  DAMAGE_ARTHROPODS,

  /**
    * All damage to other targets will knock them back when hit
    */
  KNOCKBACK,

  /**
    * When attacking a target, has a chance to set them on fire
    */
  FIRE_ASPECT,

  /**
    * Provides a chance of gaining extra loot when killing monsters
    */
  LOOT_BONUS_MOBS,

  /**
    * Increases damage against targets when using a sweep attack
    */
  SWEEPING_EDGE,

  /**
    * Increases the rate at which you mine/dig
    */
  DIG_SPEED,

  /**
    * Allows blocks to drop themselves instead of fragments (for example,
    * stone instead of cobblestone)
    */
  SILK_TOUCH,

  /**
    * Decreases the rate at which a tool looses durability
    */
  DURABILITY,

  /**
    * Provides a chance of gaining extra loot when destroying blocks
    */
  LOOT_BONUS_BLOCKS,

  /**
    * Provides extra damage when shooting arrows from bows
    */
  ARROW_DAMAGE,

  /**
    * Provides a knockback when an entity is hit by an arrow from a bow
    */
  ARROW_KNOCKBACK,

  /**
    * Sets entities on fire when hit by arrows shot from a bow
    */
  ARROW_FIRE,

  /**
    * Provides infinite arrows when shooting a bow
    */
  ARROW_INFINITE,

  /**
    * Decreases odds of catching worthless junk
    */
  LUCK,

  /**
    * Increases rate of fish biting your hook
    */
  LURE,

  /**
    * Causes a thrown trident to return to the player who threw it
    */
  LOYALTY,

  /**
    * Deals more damage to mobs that live in the ocean
    */
  IMPALING,

  /**
    * When it is rainy, launches the player in the direction their trident is thrown
    */
  RIPTIDE,

  /**
    * Strikes lightning when a mob is hit with a trident if conditions are
    * stormy
    */
  CHANNELING,

  /**
    * Shoot multiple arrows from crossbows
    */
  MULTISHOT,

  /**
    * Charges crossbows quickly
    */
  QUICK_CHARGE,

  /**
    * Crossbow projectiles pierce entities
    */
  PIERCING,

  /**
    * Allows mending the item using experience orbs
    */
  MENDING,

  /**
    * Item disappears instead of dropping
    */
  VANISHING_CURSE =  Value
}
