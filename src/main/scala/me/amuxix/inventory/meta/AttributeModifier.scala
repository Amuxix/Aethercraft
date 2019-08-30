package me.amuxix.inventory.meta

import java.util.UUID

import me.amuxix.inventory.meta.EquipmentSlot.EquipmentSlot
import me.amuxix.inventory.meta.Operation.Operation

case class AttributeModifier(
  uuid: UUID,
  name: String,
  amount: Double,
  operation: Operation,
  slot: EquipmentSlot,
)
