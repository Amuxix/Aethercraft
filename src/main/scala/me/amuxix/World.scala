package me.amuxix

import java.io.File
import java.util.UUID

import io.circe.{Decoder, Encoder}
import me.amuxix.block.Block
import me.amuxix.block.Block.Location
import me.amuxix.bukkit.Aethercraft

object World {
  implicit val encoder: Encoder[World] = Encoder.forProduct1("uuid")(_.uuid)
  implicit val decoder: Decoder[World] = Decoder.forProduct1("uuid")(Aethercraft.getWorld)
}

trait World {
  def uuid: UUID

  def blockAt(location: Location): Block

  def name: String

  def worldFolder: File
}
