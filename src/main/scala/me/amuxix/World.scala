package me.amuxix

import java.io.File
import java.util.UUID

import cats.effect.IO
import io.circe.{Decoder, Encoder}
import me.amuxix.position.EntityPosition

object World {
  implicit val encoder: Encoder[World] = Encoder.forProduct1("uuid")(_.uuid)
  implicit val decoder: Decoder[World] = Decoder.forProduct1("uuid")(Aethercraft.getWorld)
}

trait World extends BlockAt {
  def uuid: UUID

  def name: String

  def worldFolder: File

  def strikeLightning(position: EntityPosition): IO[Unit]

  def strikeLightningEffect(position: EntityPosition): IO[Unit]
}
