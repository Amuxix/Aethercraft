package me.amuxix.util

/**
  * Created by Amuxix on 22/11/2016.
  */
sealed trait Axis
case object X extends Axis
case object Y extends Axis
case object Z extends Axis
