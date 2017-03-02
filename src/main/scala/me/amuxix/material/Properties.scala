package me.amuxix.material

/**
  * Created by Amuxix on 14/02/2017.
  */

trait Block
trait Liquid extends Block
trait Transparent extends Block
trait Solid extends Block
trait Inventory extends Solid

trait Rotates
trait FourRotations extends Rotates
trait SixRotations extends Rotates

trait Consumable
trait SpawningEgg extends Consumable

trait Attaches
trait Crushable
trait Food
trait Gravity
trait Unconsumable
trait Usable
