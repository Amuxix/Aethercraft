package me

package object amuxix {
  import scala.math.Numeric.DoubleAsIfIntegral
  implicit val doubleAsIfIntegral = DoubleAsIfIntegral //Allows this method to be implicitly used by Tuple32Vector

  implicit class OptionObjectOps(option: Option.type) {
    def when[T](condition: Boolean)(t: => T): Option[T] = if (condition) Some(t) else None
    def flatWhen[T](condition: Boolean)(t: => Option[T]): Option[T] = if (condition) t else None

    def unless[T](condition: Boolean)(t: => T): Option[T] = when(!condition)(t)
    def flatUnless[T](condition: Boolean)(t: => Option[T]): Option[T] = flatWhen(!condition)(t)
  }

  implicit class OptionOps[T](option: Option[T]) {
    def orWhen(condition: Boolean)(t: => T): Option[T] = option.orElse(Option.when(condition)(t))
    def orFlatWhen(condition: Boolean)(t: => Option[T]): Option[T] = option.orElse(Option.flatWhen(condition)(t))

    def orUnless(condition: Boolean)(t: => T): Option[T] = option.orElse(Option.unless(condition)(t))
    def orFlatUnless(condition: Boolean)(t: => Option[T]): Option[T] = option.orElse(Option.flatUnless(condition)(t))
  }
}
