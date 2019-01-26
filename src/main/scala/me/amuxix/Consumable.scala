package me.amuxix

import cats.Traverse
import cats.data.OptionT
import cats.effect.IO
import cats.implicits._

trait Consumable {
  /**
    * Removes or replaces this from wherever it may be and returns its energy value
    * @return The energy value
    */
  def consume: OptionT[IO, Int]
}

object Consumable {
  def consume[F[_] : Traverse](consumables: F[Consumable]): OptionT[IO, Int] =
    consumables.map(_.consume).foldLeft(OptionT.pure[IO](0)) {
      case (accumulator, energyItem) =>
        for {
          acc <- accumulator
          energy <- energyItem
        } yield acc + energy
    }
}
