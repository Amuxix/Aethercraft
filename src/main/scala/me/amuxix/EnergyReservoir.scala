package me.amuxix

import java.util.UUID

import better.files.File
import cats.data.{EitherT, OptionT}
import cats.effect.IO
import io.circe.{Decoder, Encoder}
import me.amuxix.logging.Logger
import me.amuxix.logging.Logger.info
import me.amuxix.serialization.Persistable

import scala.util.Try

object EnergyReservoir extends Persistable[EnergyReservoir] {
  override implicit val encoder: Encoder[EnergyReservoir] = Encoder.forProduct3("player", "energy", "cap")(reservoir =>
    (reservoir.player, reservoir.energy.value, reservoir.cap.value)
  )
  override implicit val decoder: Decoder[EnergyReservoir] = Decoder.forProduct3[EnergyReservoir, Player, Int, Int]("player", "energy", "cap"){new EnergyReservoir(_, _, _)}

  val defaultCap: Energy = 100000

  var energyReservoirs: Map[Player, EnergyReservoir] = Map.empty

  def apply(player: Player): EnergyReservoir = energyReservoirs.get(player).fold {
    val reservoir = new EnergyReservoir(player)
    energyReservoirs += (player -> reservoir)
    reservoir
  }(identity)

  override protected val persistablesName = "Reservoirs"

  private def persistables: Map[String, EnergyReservoir] = energyReservoirs.map {
    case (player, reservoir) => player.uuid.toString -> reservoir
  }

  private val folder: File = Aethercraft.dataFolder.createChild(persistablesName, asDirectory = true)

  override protected val extension: String = ".reservoir"

  override protected def updateWithLoaded(fileName: String, thing: EnergyReservoir): Unit =
    Try(UUID.fromString(fileName))
      .toOption.flatMap(Aethercraft.players.get)
      .map { player =>
        energyReservoirs += (player -> thing)
      }
      .fold(Logger.severe(s"Invalid player uuid: $fileName"))(IO.pure)

  val saveReservoirs: IO[Unit] =
    for {
      _ <- info(s"Saving all $persistablesName")
      _ <- saveAll(persistables, folder)
    } yield ()

  val loadReservoirs: IO[Unit] =
    for {
      amount <- loadAll(folder, updateWithLoaded)
      _ <- info(s"Loaded $amount $persistablesName")
    } yield ()
}

class EnergyReservoir private(private val player: Player, private var energy: Energy = 0, private val cap: Energy = EnergyReservoir.defaultCap) {

  /**
    * Adds the most energy possible starting from the left of the input list
    */
  def addMaximumEnergyFrom(consumables: List[(List[Consumable#ConsumeIO], Option[Consumable#ConsumeIO])]): EitherT[IO, String, Energy] = {
    def combine(c1: Consumable#ConsumeIO, c2: Consumable#ConsumeIO): Consumable#ConsumeIO = {
      val (c1Energy, c1OptionT) = c1
      val (c2Energy, c2OptionT) = c2
      val combinedIO = OptionT(c1OptionT.value.flatMap {
        case Some(error) => IO(Some(error))
        case None => c2OptionT.value
      })
      (c1Energy + c2Energy, combinedIO)
    }

    val maxEnergy = cap - this.energy
    val (energy, io) = consumables.foldLeft((0 Energy, OptionT.none[IO, String])) { //Accumulate consumables until maximum is reached or is very close
      case (current @ (currentEnergy, _), _) if currentEnergy == maxEnergy => current //Skip if we are at maximum.
      case (current, (List(), None)) => current
      case (current @ (currentEnergy, _), (List(), Some(block @ (energy, _)))) if currentEnergy + energy <= maxEnergy => combine(current, block)
      case (current, (items, _)) => items.foldLeft(current) { //Accumulate from item stacks.
        case (current @ (currentEnergy, _), item @ (energy, _)) if currentEnergy + energy <= maxEnergy => combine(current, item)
      }
    }
    for {
    _ <- io.toLeft(energy)
    _ <- add(energy)
    } yield energy
  }

  /**
    * Attempts to add energy to a player's reservoir, notifies the player of the amount added
    * @param energy Amount of energy to add
    * @return The energy added to this reservoir
    */
  def add(energy: Energy): EitherT[IO, String, Energy] =
    if (energy < 0) {
      EitherT.leftT("Cannot add negative energy.")
    } else if (energy == 0.Energy) {
      EitherT.rightT(this.energy)
    } else if (this.energy + energy > cap) {
      add(cap - energy).flatMap { _ =>
        EitherT.leftT(s"Adding $energy would surpasses your energy cap of $cap by ${this.energy + energy - cap}.")
      }
    } else {
      this.energy += energy
      EitherT.liftF {
        for {
          _ <- Logger.info(s"${player.name} gained $energy energy.")
          _ <- player.notify(s"Added $energy energy.")
          _ <- EnergyReservoir.saveOneAsync(EnergyReservoir.folder, player.uuid.toString, this)
        } yield this.energy
      }
    }

  /**
    * Attempts to remove energy from a player's reservoir
    * @param energy Amount of energy to add
    * @return The energy removed from this reservoir
    */
  def remove(energy: Energy): EitherT[IO, String, Energy] =
    if (energy < 0) {
      EitherT.leftT("Cannot remove negative energy.")
    } else if (energy == 0.Energy) {
      EitherT.rightT(this.energy)
    } else if (this.energy < energy) {
      EitherT.leftT(s"You don't have enough energy, you need at least $energy")
    } else {
      this.energy -= energy
      EitherT.liftF {
        for {
          _ <- Logger.info(s"${player.name} used $energy energy.")
          _ <- EnergyReservoir.saveOneAsync(EnergyReservoir.folder, player.uuid.toString, this)
        } yield this.energy
      }
    }


  def hasAtLeast(energy: Energy): Boolean = this.energy >= energy
}
