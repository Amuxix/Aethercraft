package me.amuxix.material
import me.amuxix.Energy

/**
  * Created by Amuxix on 14/02/2017.
  */
private[material] object Properties {
  trait Block
  trait Liquid extends Block
  trait Transparent extends Block
  trait Solid extends Block
  trait Inventory extends Solid

  sealed trait Rotates
  trait FourRotations extends Rotates
  trait FiveRotations extends Rotates //This is used by items that can be placed on floor and walls but not ceiling
  trait SixRotations extends Rotates

  /**
    * This represents something that is eaten/drunk either by player or animals/monsters
    */
  trait Consumable

  /**
    * This represents something that is eaten/drunk by player, it may also be consumed by animals/monsters
    */
  trait PlayerConsumable extends Consumable

  /**
    * This represents an item with durability, like tools, weapons and armour
    */
  trait Durable

  trait Attaches
  trait Crushable

  trait Gravity
  trait NoEnergy/* { this: Material =>
  this._energy = None
}*/
  trait Usable
  trait Fuel { this: Material =>

    /**
      * Energy required to smelt 1 item using this fuel
      */
    def smeltEnergy: Option[Energy] = energy.map(_ * 200 / this.burnTicks)

    /**
      * Number of ticks this fuel lasts for.
      */
    val burnTicks: Int
  }
}
