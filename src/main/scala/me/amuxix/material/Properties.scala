package me.amuxix.material
import me.amuxix.Energy
import me.amuxix.material.Generic.{Composition, Tool}

/**
  * Created by Amuxix on 14/02/2017.
  */
object Properties {
  trait NoEnergy

  trait BlockProperty
  trait BreakableBlockProperty extends BlockProperty {
    /** This is the worst tool required to break the block and get drops, None means breaking by any means(Hand or tools) will yield drops */
    protected val minimumTool: Option[Tool with Composition]
    def isAppropriateTool(tool: Tool with Composition): Boolean =
      minimumTool.fold(true)(minimumTool => tool.toolType == minimumTool.toolType && tool.composition >= minimumTool.composition)
  }

  trait Liquid { this: BlockProperty => }
  trait Transparent { this: BlockProperty => }
  trait Solid { this: BlockProperty => }
  trait Inventory extends Solid { this: BlockProperty => }

  sealed trait Rotates { this: BlockProperty => }
  trait FourRotations extends Rotates { this: BlockProperty => }
  trait FiveRotations extends Rotates { this: BlockProperty => } //This is used by items that can be placed on floor and walls but not ceiling
  trait SixRotations extends Rotates { this: BlockProperty => }
  trait SixteenRotations extends Rotates { this: BlockProperty => } //This is exclusive to heads placed on the floor
  /** This represents a block that can only exist attached to another block */
  trait Attaches { this: BlockProperty => }

  /** This represents a block that can be replaced by another block, such as snow layers */
  trait Crushable { this: BlockProperty => }
  /** This represents a block that falls with gravity, such as sand or gravel */
  trait Gravity { this: BlockProperty => }

  trait ItemProperty

  sealed trait Consumable { this: ItemProperty => }
  /** This represents something that is eaten/drunk by animals/monsters */
  trait AnimalConsumable extends Consumable { this: ItemProperty => }
  /** This represents something that is eaten/drunk by players */
  trait PlayerConsumable extends Consumable { this: ItemProperty => }

  /** This represents an item with durability, like tools, weapons and armour */
  trait Durable { this: ItemProperty => }

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

  /** This represents an item that can be used, such as blocking with a sword or shield, using a fire charge or right clicking a map to view it */
  trait Usable
}
