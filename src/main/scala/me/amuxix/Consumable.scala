package me.amuxix

import cats.data.OptionT
import cats.effect.IO


trait Consumable {
  type ConsumeIO = (Energy, OptionT[IO, String])
  /**
    * Returns an Option of tuple from energy to an IO that attempts to fully consume this, the IO returns an Option containing any error if it fails.
    * The outer Option is empty if this can't be consumed.
    * The difference from [[consume]] and this is that this either consumes this completely or nothing at all.
    */
  //def consumeAtomically: Option[(Energy, OptionT[IO, String])]

  /**
    * Returns a ListMap from energy to an IO that attempts to consume this, the IO returns an Option containing any error if it fails.
    * The listMap is empty if no part of this can be consumed.
    * The difference from [[consumeAtomically]] and this is that this will consume the maximum it can, even if this means not all is consumed.
    */
  def consume: List[(List[ConsumeIO], Option[ConsumeIO])]
}

object Consumable {
  /*def consumeAtomically[F[_] : Traverse](consumables: F[Consumable]): Option[(Energy, OptionT[IO, String])] =
    consumables.traverse(_.consumeAtomically).map {
    _.foldLeft((0 Energy, OptionT.none[IO, String])) {
      case ((energyAcc, ioAcc), (energy, io)) => (energyAcc + energy, ioAcc.orElse(io))
    }
  }*/
}
