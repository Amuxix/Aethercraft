package me.amuxix.material

import me.amuxix.Energy

import scala.math.E

sealed case class BaseTier(tier: Int) {
  val energy: Energy = Math.pow(2 * E, tier).round.toInt
}

object T0 extends BaseTier(0)
object T1 extends BaseTier(1)
object T2 extends BaseTier(2)
object T3 extends BaseTier(3)
object T4 extends BaseTier(4)
object T5 extends BaseTier(5)
object T6 extends BaseTier(6)
object T7 extends BaseTier(7)