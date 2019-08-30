package me

package object amuxix {
  implicit val doubleAsIfIntegral = new DoubleAsIfIntegral {}

  implicit class OptionObjectOps(option: Option.type) {
    def flatWhen[T](condition: Boolean)(t: => Option[T]): Option[T] = if (condition) t else None

    def flatUnless[T](condition: Boolean)(t: => Option[T]): Option[T] = flatWhen(!condition)(t)
  }

  implicit class OptionOps[T](option: Option[T]) {
    def orWhen(condition: Boolean)(t: => T): Option[T] = option.orElse(Option.when(condition)(t))
    def orFlatWhen(condition: Boolean)(t: => Option[T]): Option[T] = option.orElse(Option.flatWhen(condition)(t))

    def orUnless(condition: Boolean)(t: => T): Option[T] = option.orElse(Option.unless(condition)(t))
    def orFlatUnless(condition: Boolean)(t: => Option[T]): Option[T] = option.orElse(Option.flatUnless(condition)(t))

    @inline def getOrDefault(default: T): T = option.getOrElse(default)
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
    implicit val numeric: Numeric[Energy] = new Numeric[Energy] {
      override def plus(x: Energy, y: Energy): Energy = new Energy(x.value + y.value)
      override def minus(x: Energy, y: Energy): Energy = new Energy(x.value - y.value)
      override def times(x: Energy, y: Energy): Energy = new Energy(x.value * y.value)
      override def negate(x: Energy): Energy = new Energy(-x.value)
      override def fromInt(x: Int): Energy = new Energy(x)
      override def parseString(str: String): Option[Energy] = str.toIntOption.map(new Energy(_))
      override def toInt(x: Energy): Int = x.value
      override def toLong(x: Energy): Long = x.value.toLong
      override def toFloat(x: Energy): Float = x.value.toFloat
      override def toDouble(x: Energy): Double = x.value.toDouble
      override def compare(x: Energy, y: Energy): Int = x.value.compare(y.value)
    }

    def unapply(energy: Energy): Option[Int] = Some(energy.value)
  }

  type =|>[-A, +B] = PartialFunction[A, B]

  def indefiniteArticleFor(string: String) = if ("aeiouy".contains(string.head)) "an" else "a"
}
