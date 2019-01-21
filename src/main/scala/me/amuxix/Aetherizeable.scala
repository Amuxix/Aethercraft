package me.amuxix

/**
  * This represetns a class that can turn a native type to its aethercraft counterpart.
  * @tparam T The type returned when aetherized
  */
trait Aetherizeable[T] {
  def aetherize: T
}
