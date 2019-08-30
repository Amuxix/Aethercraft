package me.amuxix.inventory.meta

import io.circe.{Decoder, Encoder}

object EquipmentSlot extends Enumeration {
  implicit val decoder: Decoder[Value] = Decoder.decodeEnumeration(EquipmentSlot)
  implicit val encoder: Encoder[Value] = Encoder.encodeEnumeration(EquipmentSlot)

  type EquipmentSlot = Value

  val HAND,
  OFF_HAND,
  FEET,
  LEGS,
  CHEST,
  HEAD = Value
}
