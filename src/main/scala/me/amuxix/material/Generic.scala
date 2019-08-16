package me.amuxix.material

import me.amuxix.Energy
import me.amuxix.material.Material.{Shears, WoodenPickaxe}
import me.amuxix.material.Properties._

//TODO: Add FIsh
object Generic {
  val fuels: Seq[Material with Fuel] = Material.filter[Fuel]

  /**
    * Cheapest cost to smelt something, this assumes at least 1 fuel has a energy value when tis is called.
    * @return The lowest energy to smelt something
    */
  def cheapestSmeltEnergy: Energy = fuels.flatMap(_.smeltEnergy).min

  private[material] object Compositions extends Enumeration {
    type Composition = Value
    val Wooden, Stony, Ferrous, Golden, Crystalline = Value
  }

  private[material] object Tools extends Enumeration {
    type Tool = Value
    val Axe, Pickaxe, Shovel, Hoe, Shears = Value
  }

  trait Composition {
    private[material] val composition: Compositions.Composition
  }

  sealed trait Generic

  sealed trait GenericBlock extends BlockProperty
  sealed trait GenericBreakableBlock extends BreakableBlockProperty
  sealed trait GenericItem extends ItemProperty

  sealed trait Tool extends GenericItem with Durable { this: Composition =>
    private[material] val toolType: Tools.Tool
  }
  trait Axe extends Tool { this: Composition =>
    override val toolType: Tools.Tool = Tools.Axe
  }
  trait Pickaxe extends Tool { this: Composition =>
    override val toolType: Tools.Tool = Tools.Pickaxe
  }
  trait Hoe extends Tool with Usable { this: Composition =>
    override val toolType: Tools.Tool = Tools.Hoe
  }
  trait Shovel extends Tool { this: Composition =>
    override val toolType: Tools.Tool = Tools.Shovel
  }
  trait Shears extends Tool { this: Composition =>
    override val toolType: Tools.Tool = Tools.Shears
  }

  trait Wooden extends Composition with Fuel { this: Material =>
    override val burnTicks: Int = 200
    override val composition: Compositions.Composition = Compositions.Wooden
  }
  trait Stony extends Composition {
    override val composition: Compositions.Composition = Compositions.Stony
  }
  trait Ferrous extends Composition {
    override val composition: Compositions.Composition = Compositions.Ferrous
  }
  trait Golden extends Composition {
    override val composition: Compositions.Composition = Compositions.Golden
  }
  trait Crystalline extends Composition {
    override val composition: Compositions.Composition = Compositions.Crystalline
  }

  sealed trait Armor extends GenericItem with Durable
  trait Boots extends Armor
  trait Chestplate extends Armor
  trait Helmet extends Armor
  trait Leggings extends Armor

  trait Weapon extends GenericItem with Durable
  trait Sword extends Weapon with Usable

  trait DoorItem extends GenericItem with Fuel { this: Material =>
    override val burnTicks: Int = 200
  }

  sealed trait GenericSlab extends Solid { this: BlockProperty => }
  trait Slab extends GenericBreakableBlock with GenericSlab { override protected val minimumTool = Some(WoodenPickaxe) }
  trait WoodenSlab extends GenericBreakableBlock with GenericSlab with Fuel { this: Material =>
    override protected val minimumTool = Some(WoodenPickaxe)
    override val burnTicks: Int = 150
  }
  trait Plant extends GenericBreakableBlock with Transparent with Attaches with Crushable { override protected val minimumTool = None }
  trait DoublePlant extends Plant

  trait Terracotta extends GenericBreakableBlock with Solid { override protected val minimumTool = Some(WoodenPickaxe) }
  trait DyedTerracotta extends Terracotta
  trait GlazedTerracotta extends GenericBreakableBlock with Solid { override protected val minimumTool = Some(WoodenPickaxe) }

  trait Door extends GenericBreakableBlock with Solid with FourRotations with Transparent with Fuel { this: Material =>
    override protected val minimumTool = Some(WoodenPickaxe)
    override val burnTicks: Int = 200
  }

  trait Carpet extends GenericBreakableBlock with Transparent with Attaches with Fuel { this: Material =>
    override protected val minimumTool = None
    override val burnTicks: Int = 67
  }
  trait Fence extends GenericBreakableBlock with Solid with Transparent with Fuel { this: Material =>
    override protected val minimumTool = Some(WoodenPickaxe)
    override val burnTicks: Int = 300
  }
  trait FenceGate extends GenericBreakableBlock with Fence with FourRotations { this: Material =>
    override protected val minimumTool = Some(WoodenPickaxe)
  }
  trait Glass extends GenericBreakableBlock with Solid with Transparent { override protected val minimumTool = None }
  trait GlassPane extends Glass
  trait Leaves extends GenericBreakableBlock with Solid with Transparent with Crushable { this: Material =>
    override protected val minimumTool = Some(Shears)
    _energy = Some(T2.energy)
  }
  trait StrippedLog extends GenericBreakableBlock with Solid with Fuel { this: Material =>
    override protected val minimumTool = Some(WoodenPickaxe)
    override val burnTicks: Int = 300
  }
  trait Log extends StrippedLog { this: Material =>
    _energy = Some(T2.energy)
  }
  trait Wood extends GenericBreakableBlock with Solid with Fuel { this: Material =>
    override protected val minimumTool = Some(WoodenPickaxe)
    override val burnTicks: Int = 300
  }
  trait Plank extends GenericBreakableBlock with Solid with Fuel { this: Material =>
    override protected val minimumTool = Some(WoodenPickaxe)
    override val burnTicks: Int = 300
  }
  trait Rail extends GenericBreakableBlock with Transparent with Attaches { override protected val minimumTool = Some(WoodenPickaxe) }
  trait Sapling extends GenericBreakableBlock with Attaches with Transparent with Crushable with Fuel { this: Material =>
    override protected val minimumTool = None
    _energy = Some(T2.energy)
    override val burnTicks: Int = 100
  }
  trait Stairs extends GenericBreakableBlock with Solid with SixRotations { override protected val minimumTool = Some(WoodenPickaxe) }
  trait WoodenStairs extends GenericBreakableBlock with Fuel { this: Material =>
    override protected val minimumTool = Some(WoodenPickaxe)
    override val burnTicks: Int = 300
  }
  trait Torch extends GenericBreakableBlock with Transparent with Attaches { override protected val minimumTool = None }
  trait Wool extends GenericBreakableBlock with Solid with Fuel { this: Material =>
    override protected val minimumTool = Some(Shears)
    override val burnTicks: Int = 100
  }
  trait Banner extends GenericBreakableBlock with Fuel { this: Material =>
    override protected val minimumTool = None
    override val burnTicks: Int = 300
  }
  trait WallBanner extends Banner { this: Material => }

  trait Boat extends GenericItem with Fuel { this: Material =>
    override val burnTicks: Int = 400
  }
  trait Sign extends GenericBreakableBlock with Fuel { this: Material =>
    override protected val minimumTool = None
    override val burnTicks: Int = 200
  }
  trait WallSign extends Sign with Transparent with FourRotations { this: Material => }
  trait Dye extends GenericItem
  trait Bed extends GenericItem with GenericBreakableBlock with Transparent { override protected val minimumTool = None }

  trait ConcretePowder extends GenericBreakableBlock with Solid with Gravity { override protected val minimumTool = Some(WoodenPickaxe) }
  trait Concrete extends GenericBreakableBlock with Solid { override protected val minimumTool = Some(WoodenPickaxe) }
  trait ShulkerBox extends GenericBreakableBlock with Inventory { override protected val minimumTool = Some(WoodenPickaxe) }


  trait Button extends GenericBreakableBlock with Transparent with SixRotations { override protected val minimumTool = None }
  trait WoodenButton extends Button with Fuel { this: Material =>
    override val burnTicks: Int = 100
  }

  sealed trait GenericPressurePlate extends Transparent with Attaches { this: BlockProperty => }
  trait PressurePlate extends GenericBreakableBlock with GenericPressurePlate { override protected val minimumTool = Some(WoodenPickaxe) }
  trait WoodenPressurePlate extends GenericBreakableBlock with GenericPressurePlate with Fuel { this: Material =>
    override protected val minimumTool = Some(WoodenPickaxe)
    override val burnTicks: Int = 300
  }

  trait WoodenTrapDoor extends GenericBreakableBlock with Solid with Transparent with FourRotations with Door { this: Material =>
    override protected val minimumTool = Some(WoodenPickaxe)
  }
  trait SpawnEgg extends GenericItem with Usable with NoEnergy

  trait Coral extends GenericBreakableBlock with Transparent with Attaches with Crushable { override protected val minimumTool = None }
  trait CoralBlock extends GenericBreakableBlock with Solid { override protected val minimumTool = Some(WoodenPickaxe) }
  trait CoralFan extends GenericBreakableBlock with Transparent with Attaches with FourRotations with Crushable { override protected val minimumTool = None }
  trait CoralWallFan extends GenericBreakableBlock with Transparent with Attaches with FourRotations with Crushable { override protected val minimumTool = None }
  trait Ore extends GenericBreakableBlock { override protected val minimumTool = Some(WoodenPickaxe) }
  trait Air extends GenericBlock with Transparent with Crushable with NoEnergy
  trait Wall extends GenericBreakableBlock with Transparent { override protected val minimumTool = Some(WoodenPickaxe) }
  trait HorseArmor extends GenericItem
  trait BannerPattern extends GenericItem
  trait PottedPlant extends GenericBreakableBlock with Transparent { override protected val minimumTool = None }
  sealed trait Head extends GenericBreakableBlock with Transparent { override protected val minimumTool = None }
  trait FloorHead extends GenericItem with Head with SixteenRotations
  trait WallHead extends Head with FourRotations
  trait InfestedStone extends GenericBreakableBlock with Solid { this: Material =>
    override protected val minimumTool = Some(WoodenPickaxe)
    _energy = Some(T6.energy)
  }
  trait GenericChest extends GenericBreakableBlock with Solid with FourRotations with Inventory with Fuel { this: Material =>
    override protected val minimumTool = Some(WoodenPickaxe)
    override val burnTicks: Int = 300
  }
}
