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

  implicit class Energy(val value: Int) extends AnyVal with Ordered[Energy] {
    def Energy: Energy = new Energy(value)

    def *(other: Energy): Energy = new Energy(value * other.value)
    def /(other: Energy): Energy = new Energy(value / other.value)
    def +(other: Energy): Energy = new Energy(value + other.value)
    def -(other: Energy): Energy = new Energy(value - other.value)

    override def toString: String = f"$value%,d".replaceAll("[\\.,]", " ")

    override def compare(that: Energy): Int = value compare that.value
  }

  object Energy {
    //implicit val ordering = Ordering.ordered[Energy]
    implicit val numeric: Numeric[Energy] = new Numeric[Energy] {
      override def plus(x: Energy, y: Energy): Energy = new Energy(x.value + y.value)
      override def minus(x: Energy, y: Energy): Energy = new Energy(x.value - y.value)
      override def times(x: Energy, y: Energy): Energy = new Energy(x.value * y.value)
      override def negate(x: Energy): Energy = new Energy(-x.value)
      override def fromInt(x: Int): Energy = new Energy(x)
      override def toInt(x: Energy): Int = x.value
      override def toLong(x: Energy): Long = x.value.toLong
      override def toFloat(x: Energy): Float = x.value.toFloat
      override def toDouble(x: Energy): Double = x.value.toDouble
      override def compare(x: Energy, y: Energy): Int = x.value.compare(y.value)
    }

    def unapply(energy: Energy): Option[Int] = Some(energy.value)
  }
}
