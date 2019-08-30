package me.amuxix.inventory.meta

import io.circe.{Decoder, Encoder}

object Operation extends Enumeration {
  implicit val decoder: Decoder[Value] = Decoder.decodeEnumeration(Operation)
  implicit val encoder: Encoder[Value] = Encoder.encodeEnumeration(Operation)

  type Operation = Value

  /**
    * Adds (or subtracts) the specified amount to the base value.
    */
  val ADD_NUMBER,
  /**
    * Adds this scalar of amount to the base value.
    */
  ADD_SCALAR,
  /**
    * Multiply amount by this value, after adding 1 to it.
    */
  MULTIPLY_SCALAR_1 = Value
}
