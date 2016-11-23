package me.amuxix.util

/**
  * Created by Amuxix on 22/11/2016.
  */
implicit class TuppleAlgebra(t: (Int, Int, Int)) {
	def +(p: (Int, Int, Int)) = (p._1 + t._1, p._2 + t._2, p._3 + t._3)
}
