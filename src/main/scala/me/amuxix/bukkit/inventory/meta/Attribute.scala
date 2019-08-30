package me.amuxix.bukkit.inventory.meta

import io.circe.{Decoder, Encoder}

object Attribute extends Enumeration {
  implicit val decoder: Decoder[Value] = Decoder.decodeEnumeration(Attribute)
  implicit val encoder: Encoder[Value] = Encoder.encodeEnumeration(Attribute)

  type Attribute = Value

  /**
    * Maximum health of an Entity.
    */
  val GENERIC_MAX_HEALTH,
  /**
    * Range at which an Entity will follow others.
    */
  GENERIC_FOLLOW_RANGE,
  /**
    * Resistance of an Entity to knockback.
    */
  GENERIC_KNOCKBACK_RESISTANCE,
  /**
    * Movement speed of an Entity.
    */
  GENERIC_MOVEMENT_SPEED,
  /**
    * Flying speed of an Entity.
    */
  GENERIC_FLYING_SPEED,
  /**
    * Attack damage of an Entity.
    */
  GENERIC_ATTACK_DAMAGE,
  /**
    * Attack speed of an Entity.
    */
  GENERIC_ATTACK_SPEED,
  /**
    * Armor bonus of an Entity.
    */
  GENERIC_ARMOR,
  /**
    * Armor durability bonus of an Entity.
    */
  GENERIC_ARMOR_TOUGHNESS,
  /**
    * Luck bonus of an Entity.
    */
  GENERIC_LUCK,
  /**
    * Strength with which a horse will jump.
    */
  HORSE_JUMP_STRENGTH,
  /**
    * Chance of a zombie to spawn reinforcements.
    */
  ZOMBIE_SPAWN_REINFORCEMENTS = Value
}
