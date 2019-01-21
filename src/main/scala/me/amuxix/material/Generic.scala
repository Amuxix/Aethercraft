package me.amuxix.material

private[material] object Generic {
  val tools: Seq[Material with Tool] = Material.filter[Tool]
  val axes: Seq[Material with Axe] = Material.filter[Axe]
  val hoes: Seq[Material with Hoe] = Material.filter[Hoe]
  val pickaxes: Seq[Material with Pickaxe] = Material.filter[Pickaxe]
  val shovels: Seq[Material with Shovel] = Material.filter[Shovel]
  val shears: Seq[Material with Shears] = Material.filter[Shears]

  val armors: Seq[Material with Armor] = Material.filter[Armor]
  val bootss: Seq[Material with Boots] = Material.filter[Boots]
  val chestplates: Seq[Material with Chestplate] = Material.filter[Chestplate]
  val helmets: Seq[Material with Helmet] = Material.filter[Helmet]
  val leggingss: Seq[Material with Leggings] = Material.filter[Leggings]

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
  val rails: Seq[Material with Rail] = Material.filter[Rail]
  val saplings: Seq[Material with Sapling] = Material.filter[Sapling]
  val stairs: Seq[Material with WoodenStairs] = Material.filter[WoodenStairs]
  val torchs: Seq[Material with Torch] = Material.filter[Torch]
  val wools: Seq[Material with Wool] = Material.filter[Wool]
  val banners: Seq[Material with Banner] = Material.filter[Banner]
  val doors: Seq[Material with Door] = Material.filter[Door]
  val swords: Seq[Material with Sword] = Material.filter[Sword]
  val boats: Seq[Material with Boat] = Material.filter[Boat]
  val signs: Seq[Material with Sign] = Material.filter[Sign]
  val dyes: Seq[Material with Dye] = Material.filter[Dye]
  val beds: Seq[Material with Bed] = Material.filter[Bed]
  val concretes: Seq[Material with Concrete] = Material.filter[Concrete]
  val concretePowders: Seq[Material with ConcretePowder] = Material.filter[ConcretePowder]
  val shulkerBoxes: Seq[Material with ShulkerBox] = Material.filter[ShulkerBox]
  val fuels: Seq[Material with Fuel] = Material.filter[Fuel]


  /**
    * Cheapest cost to smelt something, this assumes at least 1 fuel has a energy value when tis is called.
    * @return The lowest energy to smelt something
    */
  def cheapestSmeltEnergy: Int = fuels.flatMap(_.smeltEnergy).min

  sealed trait Generic

  sealed trait Tool extends Generic with Durable
  trait Axe extends Tool
  trait Hoe extends Tool
  trait Pickaxe extends Tool
  trait Shovel extends Tool
  trait Shears extends Tool

  sealed trait Armor extends Generic with Durable
  trait Boots extends Armor
  trait Chestplate extends Armor
  trait Helmet extends Armor
  trait Leggings extends Armor

  sealed trait Weapon extends Generic with Durable
  trait Bow extends Weapon
  trait Sword extends Weapon
  trait Trident extends Weapon

  trait Slab extends Generic with Solid
  trait WoodenSlab extends Slab with Fuel { this: Material =>
    override val burnTicks: Int = 150
  }
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
  trait Wood extends Generic with Solid with Fuel { this: Material =>
    override val burnTicks: Int = 300
  }
  trait Plank extends Generic with Solid with Fuel { this: Material =>
    override val burnTicks: Int = 300
  }
  trait Rail extends Generic with Transparent with Attaches
  trait Sapling extends Generic with Attaches with Transparent with Crushable with Fuel { this: Material =>
    override val burnTicks: Int = 100
  }
  trait WoodenStairs extends Stairs with Fuel { this: Material =>
    override val burnTicks: Int = 300
  }
  trait Torch extends Generic with Transparent with Attaches with Rotates
  trait Wool extends Generic with Solid with Fuel { this: Material =>
    override val burnTicks: Int = 100
  }
  trait Banner extends Generic with Fuel { this: Material =>
    override val burnTicks: Int = 300
  }
  trait WallBanner extends Banner { this: Material => }
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

  //TODO: Add filters for these new generics
  //Todo Add FIsh
  trait Stairs extends Generic with Solid with Rotates
  trait Button extends Generic with Transparent with Rotates with Fuel { this: Material =>
    override val burnTicks: Int = 100
  }
  trait WoodenPressurePlate extends Generic with Transparent with Attaches with Fuel { this: Material =>
    override val burnTicks: Int = 300
  }
  trait WoodenTool extends Generic with Fuel { this: Material =>
    override val burnTicks: Int = 200
  }
  trait WoodenTrapDoor extends Generic with Solid with Transparent with Rotates with Door { this: Material => }
  trait SpawnEgg extends Generic with Consumable with NoEnergy

  trait Coral extends Generic with Transparent with Attaches with Crushable
  trait CoralBlock extends Generic with Solid
  trait CoralFan extends Generic with Transparent with Attaches with Rotates with Crushable
  trait CoralWallFan extends Generic with Transparent with Attaches with Rotates with Crushable
  trait Ore extends Generic with Solid
  trait Air extends Generic with Transparent with Crushable with NoEnergy
}
