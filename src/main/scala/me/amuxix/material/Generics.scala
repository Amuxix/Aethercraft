package me.amuxix.material

import me.amuxix.logging.Logger

/**
  * Created by Amuxix on 14/02/2017.
  */
object Generic {
  implicit class SpreadableMaterial(material: Material) {
    def spread: Seq[Material] = material match {
      case _: Material with Axe => axes
      case _: Material with Hoe => hoes
      case _: Material with Pickaxe => pickaxes
      case _: Material with Shovel => shovels
      case _: Material with Tool => tools
      case _: Material with Boots => bootss
      case _: Material with Chestplate => chestplates
      case _: Material with Helmet => helmets
      case _: Material with Leggings => leggingss
      case _: Material with Armor => armors
      case _: Material with WoodenSingleSlab => woodenSingleSlabs
      case _: Material with SingleSlab => singleSlabs
      case _: Material with WoodenDoubleSlab => woodenDoubleSlabs
      case _: Material with DoubleSlab => doubleSlabs
      case _: Material with WoodenSlab => woodenSlabs
      case _: Material with Slab => slabs
      case _: Material with DoublePlant => doublePlants
      case _: Material with Plant => plants
      case _: Material with DyedTerracotta => dyedTerracottas
      case _: Material with Terracotta => terracottas
      case _: Material with GlazedTerracotta => glazedTerracottas
      case _: Material with Carpet => carpets
      case _: Material with FenceGate => fenceGates
      case _: Material with Fence => fences
      case _: Material with GlassPane => glassPanes
      case _: Material with Glass => glass
      case _: Material with Leaves => leaves
      case _: Material with Log => logs
      case _: Material with Plank => planks
      case _: Material with Rails => rails
      case _: Material with Sapling => saplings
      case _: Material with Stairs => stairs
      case _: Material with Torch => torchs
      case _: Material with Wool => wools
      case _: Material with Banner => banners
      case _: Material with Door => doors
      case _: Material with Sword => swords
      case _: Material with Boat => boats
      case _: Material with Sign => signs
      case _: Material with Dye => dyes
      case _: Material with Bed => beds
      case _: Material with Concrete => concretes
      case _: Material with ConcretePowder => concretePowders
      case _: Material with ShulkerBox => shulkerBoxes
      case _: Material with Generic =>
        Logger.info(s"Trying to spread generic material($material) that has no attached spreading list.")
        Seq.empty
      case _ =>
        Logger.trace(s"Trying to spread non generic material $material")
        Seq(material)
    }
  }

  val tools: Seq[Material with Tool] = Material.filter[Tool]
  val axes: Seq[Material with Axe] = Material.filter[Axe]
  val hoes: Seq[Material with Hoe] = Material.filter[Hoe]
  val pickaxes: Seq[Material with Pickaxe] = Material.filter[Pickaxe]
  val shovels: Seq[Material with Shovel] = Material.filter[Shovel]

  val armors: Seq[Material with Armor] = Material.filter[Armor]
  val bootss: Seq[Material with Boots] = Material.filter[Boots]
  val chestplates: Seq[Material with Chestplate] = Material.filter[Chestplate]
  val helmets: Seq[Material with Helmet] = Material.filter[Helmet]
  val leggingss: Seq[Material with Leggings] = Material.filter[Leggings]

  val woodenSingleSlabs: Seq[Material with WoodenSingleSlab] = Material.filter[WoodenSingleSlab]
  val woodenDoubleSlabs: Seq[Material with WoodenDoubleSlab] = Material.filter[WoodenDoubleSlab]
  val singleSlabs: Seq[Material with SingleSlab] = Material.filter[SingleSlab]
  val doubleSlabs: Seq[Material with DoubleSlab] = Material.filter[DoubleSlab]
  val woodenSlabs: Seq[Material with WoodenSlab] = Material.filter[WoodenSlab]
  val slabs: Seq[Material with Slab] = Material.filter[Slab]

  val plants: Seq[Material with Plant] = Material.filter[Plant]
  val doublePlants: Seq[Material with DoublePlant] = Material.filter[DoublePlant]

  val dyedTerracottas: Seq[Material with DyedTerracotta] = Material.filter[DyedTerracotta]
  val terracottas: Seq[Material with Terracotta] = Material.filter[Terracotta]
  val glazedTerracottas: Seq[Material with GlazedTerracotta] = Material.filter[GlazedTerracotta]

  val carpets: Seq[Material with Carpet] = Material.filter[Carpet]
  val fences: Seq[Material with Fence] = Material.filter[Fence]
  val fenceGates: Seq[Material with FenceGate] = Material.filter[FenceGate]
  val glass: Seq[Material with Glass] = Material.filter[Glass]
  val glassPanes: Seq[Material with GlassPane] = Material.filter[GlassPane]
  val leaves: Seq[Material with Leaves] = Material.filter[Leaves]
  val logs: Seq[Material with Log] = Material.filter[Log]
  val planks: Seq[Material with Plank] = Material.filter[Plank]
  val rails: Seq[Material with Rails] = Material.filter[Rails]
  val saplings: Seq[Material with Sapling] = Material.filter[Sapling]
  val stairs: Seq[Material with Stairs] = Material.filter[Stairs]
  val torchs: Seq[Material with Torch] = Material.filter[Torch]
  val wools: Seq[Material with Wool] = Material.filter[Wool]
  val banners: Seq[Material with Banner] = Material.filter[Banner]
  //TODO: Separate door items from door blocks
  val doors: Seq[Material with Door] = Material.filter[Door]
  val swords: Seq[Material with Sword] = Material.filter[Sword]
  val boats: Seq[Material with Boat] = Material.filter[Boat]
  val signs: Seq[Material with Sign] = Material.filter[Sign]
  val dyes: Seq[Material with Dye] = Material.filter[Dye]
  val beds: Seq[Material with Bed] = Material.filter[Bed]
  val concretes: Seq[Material with Concrete] = Material.filter[Concrete]
  val concretePowders: Seq[Material with ConcretePowder] = Material.filter[ConcretePowder]
  val shulkerBoxes: Seq[Material with ShulkerBox] = Material.filter[ShulkerBox]

  def cheapestFuel: Material with Fuel = {
    val allFuels: Seq[Material with Fuel] = Material.filter[Fuel]
    allFuels.filter(_.energy.isDefined).minBy(m => m.energy.get / m.burnTicks)
  }

}

sealed trait Generic

sealed trait Tool extends Generic with Durable
trait Axe extends Tool
trait Hoe extends Tool
trait Pickaxe extends Tool
trait Shovel extends Tool

sealed trait Armor extends Generic with Durable
trait Boots extends Armor
trait Chestplate extends Armor
trait Helmet extends Armor
trait Leggings extends Armor

sealed trait Weapon extends Generic with Durable
trait Bow
trait Sword

sealed trait Slab extends Generic with Solid
sealed trait WoodenSlab extends Slab with Fuel { this: Material =>
  override val burnTicks: Int = 150
}
trait SingleSlab extends Slab
trait WoodenSingleSlab extends SingleSlab with WoodenSlab { this: Material => }

trait DoubleSlab extends Slab
trait WoodenDoubleSlab extends DoubleSlab with WoodenSlab { this: Material => }

trait Plant extends Generic with Transparent with Attaches with Crushable
trait DoublePlant extends Plant

trait Terracotta extends Generic with Solid
trait DyedTerracotta extends Terracotta
trait GlazedTerracotta extends Generic with Solid

trait Door extends Generic with Fuel { this: Material =>
  override val burnTicks: Int = 200
}
trait DoorItem extends Generic with Fuel { this: Material =>
  override val burnTicks: Int = 200
}

trait Carpet extends Generic with Transparent with Attaches with Fuel { this: Material =>
  override val burnTicks: Int = 67
}
trait Fence extends Generic with Solid with Transparent with Fuel { this: Material =>
  override val burnTicks: Int = 300
}
trait FenceGate extends Generic with Fence with Rotates { this: Material => }
trait Glass extends Generic with Solid with Transparent
trait GlassPane extends Glass
trait Leaves extends Generic with Solid with Transparent with Crushable
trait Log extends Generic with Solid with Fuel { this: Material =>
  override val burnTicks: Int = 300
}
trait Plank extends Generic with Solid with Fuel { this: Material =>
  override val burnTicks: Int = 300
}
trait Rails extends Generic with Transparent with Attaches
trait Sapling extends Generic with Attaches with Transparent with Crushable with Fuel { this: Material =>
  override val burnTicks: Int = 100
}
trait Stairs extends Generic with Solid with Rotates with Fuel { this: Material =>
  override val burnTicks: Int = 300
}
trait Torch extends Generic with Transparent with Attaches with Rotates
trait Wool extends Generic with Solid with Fuel { this: Material =>
  override val burnTicks: Int = 100
}
trait Banner extends Generic with Fuel { this: Material =>
  override val burnTicks: Int = 300
}
trait Boat extends Generic with Fuel { this: Material =>
  override val burnTicks: Int = 400
}
trait Sign extends Generic with Fuel { this: Material =>
  override val burnTicks: Int = 200
}
trait Dye extends Generic
trait Bed extends Generic

trait ConcretePowder extends Generic
trait Concrete extends Generic
trait ShulkerBox extends Generic
