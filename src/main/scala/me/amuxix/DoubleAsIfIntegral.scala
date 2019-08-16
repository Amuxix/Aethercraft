package me.amuxix

import scala.math.Ordering

class DoubleAsIfIntegral extends Integral[Double] with Ordering.Double.IeeeOrdering {
  def div(x: Double, y: Double): Double = x / y
  def plus(x: Double, y: Double): Double = x + y
  def minus(x: Double, y: Double): Double = x - y
  def times(x: Double, y: Double): Double = x * y
  def negate(x: Double): Double = -x
  def fromInt(x: Int): Double = x.toDouble
  def parseString(str: String): Option[Double] = str.toDoubleOption
  def toInt(x: Double): Int = x.toInt
  def toLong(x: Double): Long = x.toLong
  def toFloat(x: Double): Float = x.toFloat
  def toDouble(x: Double): Double = x
  override def quot(x: Double, y: Double): Double = (BigDecimal(x) quot BigDecimal(y)).doubleValue
  override def rem(x: Double, y: Double): Double = (BigDecimal(x) remainder BigDecimal(y)).doubleValue
}
