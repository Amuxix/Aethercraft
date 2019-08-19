package me.amuxix.runes.traits

import me.amuxix.OptionOps

trait LinkableTiered extends Linkable with Tiered {
  override def validateSignature: Option[String] =
    Option
      .when(signatureIsEmpty)("Signature is empty!")
      .orWhen(signatureContains(tierMaterial))(s"${tierMaterial.name} can't be used as signature because it's already used for tier.")
}
