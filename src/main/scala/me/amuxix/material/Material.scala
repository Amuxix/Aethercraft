package me.amuxix.material

import enumeratum._
import me.amuxix.{Energy, Named}
import me.amuxix.material.Generic._
import me.amuxix.material.Properties._
import me.amuxix.pattern.Element

import scala.collection.immutable.IndexedSeq
import scala.math.{E, log}
import scala.reflect.ClassTag

/**
  * Created by Amuxix on 04/01/2017.
  */

sealed abstract case class Material(protected var _energy: Option[Energy] = None) extends EnumEntry with Element with Named {
  def this(energy: Energy) = this(Some(energy))
  def this(tier: BaseTier) = this(Some(tier.energy))

  lazy val tier: Option[Int] = {
    _energy match {
      case Some(e) => Some((log(e.value) / log(E * 2)).round.toInt)
      case None => None
    }
  }

  def energy: Option[Energy] = _energy
  def energy_=(energy: Energy): Unit = {
    require(hasEnergy, s"Something tried to modify energy of $name which has NoEnergy trait.")
    _energy = Some(energy)
  }

  override def toString: String = s"$name(${if (hasEnergy) energy.getOrElse("None") else "NoEnergy"})"

  override def hashCode(): Int = name.hashCode()

  override def equals(that: Any): Boolean = {
    that match {
      case s: Material =>
        name.equalsIgnoreCase(s.name)
      case _ =>
        false
    }
  }

  //Properties
  def isBlock: Boolean = isInstanceOf[Block]
  def isLiquid: Boolean = isInstanceOf[Liquid]
  def isTransparent: Boolean = isInstanceOf[Transparent]
  def isSolid: Boolean = isInstanceOf[Solid]
  def isInventory: Boolean = isInstanceOf[Inventory]
  def isRotates: Boolean = isInstanceOf[Rotates]
  def isFourRotations: Boolean = isInstanceOf[FourRotations]
  def isSixRotations: Boolean = isInstanceOf[SixRotations]
  def isConsumable: Boolean = isInstanceOf[Consumable]
  def isPlayerConsumable: Boolean = isInstanceOf[PlayerConsumable]
  def isDurable: Boolean = isInstanceOf[Durable]
  def isAttachable: Boolean = isInstanceOf[Attaches]
  def isCrushable: Boolean = isInstanceOf[Crushable]
  def isGravity: Boolean = isInstanceOf[Gravity]
  def hasNoEnergy: Boolean = isInstanceOf[NoEnergy]
  def hasEnergy: Boolean = !isInstanceOf[NoEnergy]
  def isUsable: Boolean = isInstanceOf[Usable]

  //Generics
  def isFuel: Boolean = isInstanceOf[Fuel]
  def isAir: Boolean = isInstanceOf[Air]
  def isOre: Boolean = isInstanceOf[Ore]
  def isTool: Boolean = isInstanceOf[Tool]
  def isSword: Boolean = isInstanceOf[Sword]
}

//Generated from https://docs.google.com/spreadsheets/d/1h1gsL9rj9zVvhGUXnsIEpS1RtxSquCgItGQEmeubiTM/edit#gid=0

object Material extends CirceEnum[Material] with Enum[Material] {

  override def values: IndexedSeq[Material] = findValues

  def filter[T: ClassTag]: Seq[Material with T] = values collect {
    case t: T => t.asInstanceOf[Material with T]
  }

  private val coralTier: BaseTier = T2

  //region Materials
  //@formatter:off
  object AcaciaBoat extends Material with Boat
  object AcaciaButton extends Material with Button
  object AcaciaDoor extends Material(tier = T2) with Solid with Transparent with Door
  object AcaciaFence extends Material with Fence
  object AcaciaFenceGate extends Material with FenceGate
  object AcaciaLeaves extends Material(tier = T2) with Leaves
  object AcaciaLog extends Material(tier = T2) with Log
  object AcaciaPlanks extends Material with Plank
  object AcaciaPressurePlate extends Material with WoodenPressurePlate
  object AcaciaSapling extends Material(tier = T2) with Sapling
  object AcaciaSlab extends Material with WoodenSlab
  object AcaciaStairs extends Material with WoodenStairs
  object AcaciaTrapdoor extends Material with WoodenTrapDoor
  object AcaciaWood extends Material with Wood
  object ActivatorRail extends Material with Rail
  object Air extends Material with Air
  object Allium extends Material(energy = 14) with Plant
  object Andesite extends Material(tier = T0) with Solid
  object Anvil extends Material with Solid with Transparent with Gravity with Inventory
  object Apple extends Material with PlayerConsumable
  object ArmorStand extends Material
  object Arrow extends Material(energy = 5)
  object AttachedMelonStem extends Material(tier = T2) with Plant
  object AttachedPumpkinStem extends Material(tier = T2) with Plant
  object AzureBluet extends Material(energy = 14) with Plant
  object BakedPotato extends Material with PlayerConsumable
  object Barrier extends Material with Solid with NoEnergy
  object BatSpawnEgg extends Material with SpawnEgg
  object Beacon extends Material with Solid
  object Bedrock extends Material with Solid with NoEnergy
  object Beetroot extends Material with PlayerConsumable
  object BeetrootPlantation extends Material with Plant
  object BeetrootSeeds extends Material(tier = T2)
  object BeetrootSoup extends Material with PlayerConsumable
  object BirchBoat extends Material with Boat
  object BirchButton extends Material with Button
  object BirchDoor extends Material with Solid with Transparent with Door
  object BirchFence extends Material with Fence
  object BirchFenceGate extends Material with FenceGate
  object BirchLeaves extends Material(tier = T2) with Leaves
  object BirchLog extends Material(tier = T2) with Log
  object BirchPlanks extends Material with Plank
  object BirchPressurePlate extends Material with WoodenPressurePlate
  object BirchSapling extends Material(tier = T2) with Sapling
  object BirchSlab extends Material with WoodenSlab
  object BirchStairs extends Material with WoodenStairs
  object BirchTrapdoor extends Material with WoodenTrapDoor
  object BirchWood extends Material with Wood
  object BlackBanner extends Material with Banner
  object BlackBed extends Material with Transparent with Bed
  object BlackCarpet extends Material with Carpet
  object BlackConcrete extends Material with Solid with Concrete
  object BlackConcretePowder extends Material with Solid with Gravity with ConcretePowder
  object BlackGlass extends Material with Glass
  object BlackGlassPane extends Material with GlassPane
  object BlackGlazedTerracotta extends Material with GlazedTerracotta
  object BlackShulkerBox extends Material with Solid with Inventory with ShulkerBox
  object BlackTerracotta extends Material with DyedTerracotta
  object BlackWallBanner extends Material with Banner
  object BlackWool extends Material with Wool
  object BlazePowder extends Material
  object BlazeRod extends Material(energy = 2322) with Fuel { override val burnTicks: Int = 2400 }
  object BlazeSpawnEgg extends Material with SpawnEgg
  object BlueBanner extends Material with Banner
  object BlueBed extends Material with Transparent with Bed
  object BlueCarpet extends Material with Carpet
  object BlueConcrete extends Material with Solid with Concrete
  object BlueConcretePowder extends Material with Solid with Gravity with ConcretePowder
  object BlueGlass extends Material with Glass
  object BlueGlassPane extends Material with GlassPane
  object BlueGlazedTerracotta extends Material with GlazedTerracotta
  object BlueIce extends Material with Solid
  object BlueOrchid extends Material(energy = 14) with Plant
  object BlueShulkerBox extends Material with Solid with Inventory with ShulkerBox
  object BlueTerracotta extends Material with DyedTerracotta
  object BlueWallBanner extends Material with Banner
  object BlueWool extends Material with Wool
  object Bone extends Material(energy = 5)
  object BoneBlock extends Material with Solid
  object BoneMeal extends Material with Dye
  object Book extends Material
  object Bookshelf extends Material with Solid with Fuel { override val burnTicks: Int = 300 }
  object Bow extends Material(energy = 118) with Bow with Durable with Fuel { override val burnTicks: Int = 300 }
  object Bowl extends Material with Fuel { override val burnTicks: Int = 100 }
  object BrainCoral extends Material(tier = coralTier) with Coral
  object BrainCoralBlock extends Material(tier = coralTier) with CoralBlock
  object BrainCoralFan extends Material(tier = coralTier) with CoralFan
  object BrainCoralWallFan extends Material(tier = coralTier) with CoralWallFan
  object Bread extends Material with PlayerConsumable
  object BrewingStand extends Material with Solid
  object Brick extends Material
  object BrickBlock extends Material with Solid
  object BrickSingleSlab extends Material with Slab
  object BrickStairs extends Material with Stairs
  object BrownBanner extends Material with Banner
  object BrownBed extends Material with Transparent with Bed
  object BrownCarpet extends Material with Carpet
  object BrownConcrete extends Material with Solid with Concrete
  object BrownConcretePowder extends Material with Solid with Gravity with ConcretePowder
  object BrownGlass extends Material with Glass
  object BrownGlassPane extends Material with GlassPane
  object BrownGlazedTerracotta extends Material with GlazedTerracotta
  object BrownMushroom extends Material(energy = 159) with Plant
  object BrownMushroomBlock extends Material(tier = T3) with Solid
  object BrownShulkerBox extends Material with Solid with Inventory with ShulkerBox
  object BrownTerracotta extends Material with DyedTerracotta
  object BrownWallBanner extends Material with Banner
  object BrownWool extends Material with Wool
  object BubbleColumn extends Material
  object BubbleCoral extends Material(tier = coralTier) with Coral
  object BubbleCoralBlock extends Material(tier = coralTier) with CoralBlock
  object BubbleCoralFan extends Material(tier = coralTier) with CoralFan
  object BubbleCoralWallFan extends Material(tier = coralTier) with CoralWallFan
  object Bucket extends Material
  object Cactus extends Material(tier = T3) with Solid with Attaches
  object CactusGreen extends Material with Dye
  object Cake extends Material with PlayerConsumable
  object Carrot extends Material(energy = 5498) with PlayerConsumable
  object CarrotOnAStick extends Material
  object Carrots extends Material(tier = T2) with Plant
  object CarvedPumpkin extends Material with Solid with Rotates
  object Cauldron extends Material with Solid
  object CaveAir extends Material with Air
  object CaveSpiderSpawnEgg extends Material with SpawnEgg
  object ChainCommand extends Material with Solid with NoEnergy
  object ChainmailBoots extends Material with Boots
  object ChainmailChestplate extends Material with Chestplate
  object ChainmailHelmet extends Material with Helmet
  object ChainmailLeggings extends Material with Leggings
  object Charcoal extends Material with Fuel { override val burnTicks: Int = 1600 }
  object Chest extends Material with Solid with Rotates with Inventory with Fuel { override val burnTicks: Int = 300 }
  object ChestMinecart extends Material with Inventory
  object ChickenSpawnEgg extends Material with SpawnEgg
  object ChippedAnvil extends Material with Solid with Transparent with Gravity with Inventory
  object ChiseledQuartzBlock extends Material with Solid
  object ChiseledRedSandstone extends Material with Solid
  object ChiseledSandstone extends Material with Solid
  object ChiseledStoneBricks extends Material with Solid
  object ChorusFlower extends Material(tier = T2) with Solid with Crushable
  object ChorusFruit extends Material(tier = T6)
  object ChorusPlant extends Material(tier = T2) with Solid
  object Clay extends Material with Solid
  object ClayBall extends Material(tier = T2)
  object Clock extends Material
  object Coal extends Material(energy = 72) with Fuel { override val burnTicks: Int = 1600 }
  object CoalBlock extends Material with Solid with Fuel { override val burnTicks: Int = 16000 }
  object CoalOre extends Material with Ore
  object CoarseDirt extends Material with Solid
  object Cobblestone extends Material(tier = T0) with Solid
  object CobblestoneSlab extends Material with Slab
  object CobblestoneStairs extends Material with Stairs
  object CobblestoneWall extends Material with Solid
  object Cobweb extends Material(energy = 2279) with Plant
  object Cocoa extends Material(tier = T4) with Plant
  object CocoaBeans extends Material(tier = T2) with Dye
  object Cod extends Material(energy = 33) with PlayerConsumable
  object CodBucket extends Material
  object CodSpawnEgg extends Material with SpawnEgg
  object Command extends Material with Solid with NoEnergy
  object CommandBlockMinecart extends Material with NoEnergy
  object CommandRepeating extends Material with Solid with NoEnergy
  object Compass extends Material
  object Conduit extends Material with Solid
  object CookedBeef extends Material with PlayerConsumable
  object CookedChicken extends Material with PlayerConsumable
  object CookedCod extends Material with PlayerConsumable
  object CookedMutton extends Material
  object CookedPorkchop extends Material with PlayerConsumable
  object CookedRabbit extends Material with PlayerConsumable
  object CookedSalmon extends Material with PlayerConsumable
  object Cookie extends Material with PlayerConsumable
  object CowSpawnEgg extends Material with SpawnEgg
  object CrackedStoneBricks extends Material(tier = T5) with Solid
  object CraftingTable extends Material with Solid with Fuel { override val burnTicks: Int = 300 }
  object CreeperHead extends Material with NoEnergy
  object CreeperSpawnEgg extends Material with SpawnEgg
  object CreeperWallHead extends Material with NoEnergy
  object CutRedSandstone extends Material with Solid
  object CutSandstone extends Material with Solid
  object CyanBanner extends Material with Banner
  object CyanBed extends Material with Transparent with Bed
  object CyanCarpet extends Material with Carpet
  object CyanConcrete extends Material with Solid with Concrete
  object CyanConcretePowder extends Material with Solid with Gravity with ConcretePowder
  object CyanDye extends Material with Dye
  object CyanGlass extends Material with Glass
  object CyanGlassPane extends Material with GlassPane
  object CyanGlazedTerracotta extends Material with GlazedTerracotta
  object CyanShulkerBox extends Material with Solid with Inventory with ShulkerBox
  object CyanTerracotta extends Material with DyedTerracotta
  object CyanWallBanner extends Material with Banner
  object CyanWool extends Material with Wool
  object DamagedAnvil extends Material with Solid with Transparent with Gravity with Inventory
  object Dandelion extends Material(energy = 14) with Plant
  object DandelionYellow extends Material with Dye
  object DarkOakBoat extends Material with Boat
  object DarkOakButton extends Material with Button
  object DarkOakDoor extends Material with Solid with Transparent with Door
  object DarkOakFence extends Material with Fence
  object DarkOakFenceGate extends Material with FenceGate
  object DarkOakLeaves extends Material(tier = T2) with Leaves
  object DarkOakLog extends Material(tier = T2) with Log
  object DarkOakPlanks extends Material with Plank
  object DarkOakPressurePlate extends Material with Transparent with Attaches
  object DarkOakSapling extends Material(tier = T2) with Sapling
  object DarkOakSlab extends Material with WoodenSlab
  object DarkOakStairs extends Material with WoodenStairs
  object DarkOakTrapdoor extends Material with Solid with Transparent with Rotates with Door
  object DarkOakWood extends Material with Wood
  object DarkPrismarine extends Material with Solid
  object DarkPrismarineSlab extends Material with Slab
  object DarkPrismarineStairs extends Material with Stairs
  object DaylightDetector extends Material with Solid with Fuel { override val burnTicks: Int = 300 }
  object DeadBrainCoral extends Material(tier = coralTier) with Coral
  object DeadBrainCoralBlock extends Material(tier = coralTier) with CoralBlock
  object DeadBrainCoralFan extends Material(tier = coralTier) with CoralFan
  object DeadBrainCoralWallFan extends Material(tier = coralTier) with CoralWallFan
  object DeadBubbleCoral extends Material(tier = coralTier) with Coral
  object DeadBubbleCoralBlock extends Material(tier = coralTier) with CoralBlock
  object DeadBubbleCoralFan extends Material(tier = coralTier) with CoralFan
  object DeadBubbleCoralWallFan extends Material(tier = coralTier) with CoralWallFan
  object DeadBush extends Material(tier = T3) with Plant
  object DeadFireCoral extends Material(tier = coralTier) with Coral
  object DeadFireCoralBlock extends Material(tier = coralTier) with CoralBlock
  object DeadFireCoralFan extends Material(tier = coralTier) with CoralFan
  object DeadFireCoralWallFan extends Material(tier = coralTier) with CoralWallFan
  object DeadHornCoral extends Material(tier = coralTier) with Coral
  object DeadHornCoralBlock extends Material(tier = coralTier) with CoralBlock
  object DeadHornCoralFan extends Material(tier = coralTier) with CoralFan
  object DeadHornCoralWallFan extends Material(tier = coralTier) with CoralWallFan
  object DeadTubeCoral extends Material(tier = coralTier) with Coral
  object DeadTubeCoralBlock extends Material(tier = coralTier) with CoralBlock
  object DeadTubeCoralFan extends Material(tier = coralTier) with CoralFan
  object DeadTubeCoralWallFan extends Material(tier = coralTier) with CoralWallFan
  object DebugStick extends Material with NoEnergy
  object DetectorRail extends Material with Rail
  object Diamond extends Material(energy = 2869)
  object DiamondAxe extends Material with Axe
  object DiamondBlock extends Material with Solid
  object DiamondBoots extends Material with Boots
  object DiamondChestplate extends Material with Chestplate
  object DiamondHelmet extends Material with Helmet
  object DiamondHoe extends Material with Hoe
  object DiamondHorseArmor extends Material(tier = T5)
  object DiamondLeggings extends Material with Leggings
  object DiamondOre extends Material with Ore
  object DiamondPickaxe extends Material with Pickaxe
  object DiamondShovel extends Material with Shovel
  object DiamondSword extends Material with Usable with Sword
  object Diorite extends Material(tier = T0) with Solid
  object Dirt extends Material(tier = T0) with Solid
  object Dispenser extends Material with Rotates with Inventory
  object DolphinSpawnEgg extends Material with SpawnEgg
  object DonkeySpawnEgg extends Material with SpawnEgg
  object DragonBreath extends Material with NoEnergy
  object DragonEgg extends Material(energy = 140369) with Solid
  object DragonHead extends Material with NoEnergy
  object DragonWallHead extends Material with NoEnergy
  object DriedKelp extends Material with PlayerConsumable with Fuel { override val burnTicks: Int = 4000 }
  object DriedKelpBlock extends Material with Solid
  object Dropper extends Material with Solid with Inventory
  object DrownedSpawnEgg extends Material with SpawnEgg
  object Egg extends Material(tier = T1)
  object ElderGuardianSpawnEgg extends Material with SpawnEgg
  object Elytra extends Material(tier = T6) with Durable
  object Emerald extends Material(energy = 464)
  object EmeraldBlock extends Material with Solid
  object EmeraldOre extends Material with Ore
  object EmptyMap extends Material with Usable
  object EnchantedBook extends Material with NoEnergy
  object EnchantedGoldenApple extends Material with PlayerConsumable
  object EnchantingTable extends Material with Solid
  object EndCrystal extends Material
  object EnderChest extends Material with Solid with Rotates
  object EnderEye extends Material
  object EndermanSpawnEgg extends Material with SpawnEgg
  object EndermiteSpawnEgg extends Material with SpawnEgg
  object EnderPearl extends Material(energy = 1090)
  object EndGateway extends Material with Solid with NoEnergy
  object EndPortal extends Material with Transparent with NoEnergy
  object EndPortalFrame extends Material with Solid with NoEnergy
  object EndRod extends Material(tier = T5) with Solid with Transparent
  object EndStone extends Material(tier = T0) with Solid
  object EndStoneBricks extends Material
  object EvokerSpawnEgg extends Material with SpawnEgg
  object ExperienceBottle extends Material with PlayerConsumable with NoEnergy
  object Farmland extends Material with Solid
  object Feather extends Material(tier = T0)
  object FermentedSpiderEye extends Material
  object Fern extends Material(tier = T0) with Plant
  object FilledMap extends Material with NoEnergy
  object Fire extends Material(tier = T3) with Transparent with Attaches with Crushable
  object FireCharge extends Material with Usable
  object FireCoral extends Material(tier = coralTier) with Coral
  object FireCoralBlock extends Material(tier = coralTier) with CoralBlock
  object FireCoralFan extends Material(tier = coralTier) with CoralFan
  object FireCoralWallFan extends Material(tier = coralTier) with CoralWallFan
  object FireworkRocket extends Material with Usable with NoEnergy
  object FireworkStar extends Material with NoEnergy
  object FishingRod extends Material with Durable with Fuel { override val burnTicks: Int = 300 }
  object Flint extends Material
  object FlintAndSteel extends Material with Durable
  object FlowerPot extends Material with Transparent
  object FrostedIce extends Material with Solid
  object Furnace extends Material with Solid with Rotates with Inventory
  object FurnaceMinecart extends Material with Inventory
  object GhastSpawnEgg extends Material with SpawnEgg
  object GhastTear extends Material(energy = 269)
  object Glass extends Material with Glass
  object GlassBottle extends Material(energy = 35) with Glass
  object GlassPane extends Material with GlassPane
  object GlisteringMelonSlice extends Material
  object Glowstone extends Material with Solid
  object GlowstoneDust extends Material(energy = 35)
  object GoldBlock extends Material with Solid
  object GoldenApple extends Material with PlayerConsumable
  object GoldenAxe extends Material with Axe
  object GoldenBoots extends Material with Boots
  object GoldenCarrot extends Material with PlayerConsumable
  object GoldenChestplate extends Material with Chestplate
  object GoldenHelmet extends Material with Helmet
  object GoldenHoe extends Material with Hoe
  object GoldenHorseArmor extends Material(tier = T4)
  object GoldenLeggings extends Material with Leggings
  object GoldenPickaxe extends Material with Pickaxe
  object GoldenPressurePlate extends Material with Solid
  object GoldenShovel extends Material with Shovel
  object GoldenSword extends Material(energy = 43307) with Usable with Sword
  object GoldIngot extends Material
  object GoldNugget extends Material(energy = 2490)
  object GoldOre extends Material(energy = 1168) with Solid
  object Granite extends Material(tier = T0) with Solid
  object Grass extends Material(tier = T2) with Plant
  object GrassBlock extends Material(tier = T0) with Solid
  object GrassPath extends Material with Solid
  object Gravel extends Material(tier = T2) with Solid with Gravity
  object GrayBanner extends Material with Banner
  object GrayBed extends Material with Transparent with Bed
  object GrayCarpet extends Material with Carpet
  object GrayConcrete extends Material with Solid with Concrete
  object GrayConcretePowder extends Material with Solid with Gravity with ConcretePowder
  object GrayDye extends Material with Dye
  object GrayGlass extends Material with Glass
  object GrayGlassPane extends Material with GlassPane
  object GrayGlazedTerracotta extends Material with GlazedTerracotta
  object GrayShulkerBox extends Material with Solid with Inventory with ShulkerBox
  object GrayTerracotta extends Material with DyedTerracotta
  object GrayWallBanner extends Material with Banner
  object GrayWool extends Material with Wool
  object GreenBanner extends Material with Banner
  object GreenBed extends Material with Transparent with Bed
  object GreenCarpet extends Material with Carpet
  object GreenConcrete extends Material with Solid with Concrete
  object GreenConcretePowder extends Material with Solid with Gravity with ConcretePowder
  object GreenGlass extends Material with Glass
  object GreenGlassPane extends Material with GlassPane
  object GreenGlazedTerracotta extends Material with GlazedTerracotta
  object GreenShulkerBox extends Material with Solid with Inventory with ShulkerBox
  object GreenTerracotta extends Material with DyedTerracotta
  object GreenWallBanner extends Material with Banner
  object GreenWool extends Material with Wool
  object GuardianSpawnEgg extends Material with SpawnEgg
  object Gunpowder extends Material(energy = 35)
  object HayBale extends Material with Solid
  object HeartOfTheSea extends Material(tier = T5)
  object Hopper extends Material with Solid with Inventory
  object HopperMinecart extends Material with Inventory
  object HornCoral extends Material(tier = coralTier) with Coral
  object HornCoralBlock extends Material(tier = coralTier) with CoralBlock
  object HornCoralFan extends Material(tier = coralTier) with CoralFan
  object HornCoralWallFan extends Material(tier = coralTier) with CoralWallFan
  object HorseSpawnEgg extends Material with SpawnEgg
  object HuskSpawnEgg extends Material with SpawnEgg
  object Ice extends Material(tier = T0) with Solid
  object InfestedChiseledStoneBricks extends Material(tier = T6) with Solid
  object InfestedCobblestone extends Material(tier = T6) with Solid
  object InfestedCrackedStoneBricks extends Material(tier = T6) with Solid
  object InfestedMossyStoneBricks extends Material(tier = T6) with Solid
  object InfestedStone extends Material(tier = T6) with Solid
  object InfestedStoneBricks extends Material(tier = T6) with Solid
  object InkSac extends Material(tier = T0) with Dye
  object IronAxe extends Material /*(energy = 48052)*/ with Axe
  object IronBars extends Material with Fence
  object IronBlock extends Material with Solid
  object IronBoots extends Material with Boots
  object IronChestplate extends Material with Chestplate
  object IronDoor extends Material with Solid with Transparent with Attaches with Rotates with Door
  object IronHelmet extends Material with Helmet
  object IronHoe extends Material with Hoe
  object IronHorseArmor extends Material(tier = T3)
  object IronIngot extends Material(energy = 459)
  object IronLeggings extends Material with Leggings
  object IronNugget extends Material
  object IronOre extends Material(tier = T3) with Solid
  object IronPickaxe extends Material with Pickaxe
  object IronPressurePlate extends Material with Solid
  object IronShovel extends Material with Shovel
  object IronSword extends Material with Usable with Sword
  object IronTrapdoor extends Material with Solid
  object ItemFrame extends Material
  object JackOLantern extends Material with Solid
  object Jukebox extends Material with Solid with Fuel { override val burnTicks: Int = 300 }
  object JungleBoat extends Material with Boat
  object JungleButton extends Material with Button
  object JungleDoor extends Material with Solid with Transparent with Door
  object JungleFence extends Material with Fence
  object JungleFenceGate extends Material with FenceGate
  object JungleLeaves extends Material(tier = T2) with Leaves
  object JungleLog extends Material(tier = T2) with Log
  object JunglePlanks extends Material with Plank
  object JunglePressurePlate extends Material with Transparent with Attaches
  object JungleSapling extends Material(tier = T2) with Sapling
  object JungleSlab extends Material with WoodenSlab
  object JungleStairs extends Material with WoodenStairs
  object JungleTrapdoor extends Material with Solid with Transparent with Rotates with Door
  object JungleWood extends Material with Wood
  object Kelp extends Material(14)
  object KelpPlant extends Material(14) with Plant
  object KnowledgeBook extends Material with NoEnergy
  object Ladder extends Material with Transparent with Attaches with Rotates with Fuel { override val burnTicks: Int = 300 }
  object LapisLazuli extends Material(tier = T4) with Dye
  object LapisLazuliBlock extends Material with Solid
  object LapisLazuliOre extends Material with Ore
  object LargeFern extends Material(tier = T2) with DoublePlant
  object Lava extends Material(energy = 68) with Liquid
  object LavaBucket extends Material with Fuel { override val burnTicks: Int = 20000 }
  object Leash extends Material
  object Leather extends Material(tier = T0)
  object LeatherBoots extends Material with Boots
  object LeatherChestplate extends Material with Chestplate
  object LeatherHelmet extends Material with Helmet
  object LeatherLeggings extends Material with Leggings
  object Lever extends Material with Transparent with Attaches with Rotates
  object LightBlueBanner extends Material with Banner
  object LightBlueBed extends Material with Transparent with Bed
  object LightBlueCarpet extends Material with Carpet
  object LightBlueConcrete extends Material with Solid with Concrete
  object LightBlueConcretePowder extends Material with Solid with Gravity with ConcretePowder
  object LightBlueDye extends Material with Dye
  object LightBlueGlass extends Material with Glass
  object LightBlueGlassPane extends Material with GlassPane
  object LightBlueGlazedTerracotta extends Material with GlazedTerracotta
  object LightBlueShulkerBox extends Material with Solid with Inventory with ShulkerBox
  object LightBlueTerracotta extends Material with DyedTerracotta
  object LightBlueWallBanner extends Material with Banner
  object LightBlueWool extends Material with Wool
  object LightGrayBanner extends Material with Banner
  object LightGrayBed extends Material with Transparent with Bed
  object LightGrayCarpet extends Material with Carpet
  object LightGrayConcrete extends Material with Solid with Concrete
  object LightGrayConcretePowder extends Material with Solid with Gravity with ConcretePowder
  object LightGrayDye extends Material with Dye
  object LightGrayGlass extends Material with Glass
  object LightGrayGlassPane extends Material with GlassPane
  object LightGrayGlazedTerracotta extends Material with GlazedTerracotta
  object LightGrayShulkerBox extends Material with Solid with Inventory with ShulkerBox
  object LightGrayTerracotta extends Material with DyedTerracotta
  object LightGrayWallBanner extends Material with Banner
  object LightGrayWool extends Material with Wool
  object Lilac extends Material(tier = T2) with DoublePlant
  object LilyPad extends Material(tier = T5) with Plant
  object LimeBanner extends Material with Banner
  object LimeBed extends Material with Transparent with Bed
  object LimeCarpet extends Material with Carpet
  object LimeConcrete extends Material with Solid with Concrete
  object LimeConcretePowder extends Material with Solid with Gravity with ConcretePowder
  object LimeDye extends Material with Dye
  object LimeGlass extends Material with Glass
  object LimeGlassPane extends Material with GlassPane
  object LimeGlazedTerracotta extends Material with GlazedTerracotta
  object LimeShulkerBox extends Material with Solid with Inventory with ShulkerBox
  object LimeTerracotta extends Material with DyedTerracotta
  object LimeWallBanner extends Material with Banner
  object LimeWool extends Material with Wool
  object LingeringPotion extends Material with Consumable with NoEnergy
  object LlamaSpawnEgg extends Material with SpawnEgg
  object MagentaBanner extends Material with Banner
  object MagentaBed extends Material with Transparent with Bed
  object MagentaCarpet extends Material with Carpet
  object MagentaConcrete extends Material with Solid with Concrete
  object MagentaConcretePowder extends Material with Solid with Gravity with ConcretePowder
  object MagentaDye extends Material with Dye
  object MagentaGlass extends Material with Glass
  object MagentaGlassPane extends Material with GlassPane
  object MagentaGlazedTerracotta extends Material with GlazedTerracotta
  object MagentaShulkerBox extends Material with Solid with Inventory with ShulkerBox
  object MagentaTerracotta extends Material with DyedTerracotta
  object MagentaWallBanner extends Material with Banner
  object MagentaWool extends Material with Wool
  object MagmaBlock extends Material(tier = T3) with Solid
  object MagmaCream extends Material(energy = 386)
  object MagmaCubeSpawnEgg extends Material with SpawnEgg
  object Melon extends Material with Solid
  object MelonSeeds extends Material
  object MelonSlice extends Material with PlayerConsumable
  object MelonStem extends Material(tier = T2) with Plant
  object MilkBucket extends Material with PlayerConsumable
  object Minecart extends Material
  object MooshroomSpawnEgg extends Material with SpawnEgg
  object MossyCobblestone extends Material with Solid
  object MossyCobblestoneWall extends Material with Solid
  object MossyStoneBricks extends Material with Solid
  object MuleSpawnEgg extends Material with SpawnEgg
  object MushroomStem extends Material(tier = T3) with Solid
  object MushroomStew extends Material with PlayerConsumable
  object MusicDisc11 extends Material(tier = T4)
  object MusicDisc13 extends Material(tier = T4)
  object MusicDiscBlocks extends Material(tier = T4)
  object MusicDiscCat extends Material(tier = T4)
  object MusicDiscChirp extends Material(tier = T4)
  object MusicDiscFar extends Material(tier = T4)
  object MusicDiscMall extends Material(tier = T4)
  object MusicDiscMellohi extends Material(tier = T4)
  object MusicDiscStal extends Material(tier = T4)
  object MusicDiscStrad extends Material(tier = T4)
  object MusicDiscWait extends Material(tier = T4)
  object MusicDiscWard extends Material(tier = T4)
  object Mycelium extends Material(tier = T0) with Solid
  object NameTag extends Material(tier = T4)
  object NautilusShell extends Material(tier = T4)
  object NetherBrick extends Material
  object NetherBrickFence extends Material with Fence
  object NetherBricks extends Material with Solid
  object NetherBrickSlab extends Material with Slab
  object NetherBrickStairs extends Material with Stairs
  object NetherPortal extends Material with Transparent with NoEnergy
  object Netherrack extends Material(tier = T0) with Solid
  object NetherStar extends Material(energy = 62141)
  object NetherWart extends Material(tier = T3)
  object NetherWartBlock extends Material with Solid
  object NoteBlock extends Material with Solid with Fuel { override val burnTicks: Int = 300 }
  object OakBoat extends Material with Boat
  object OakButton extends Material with Button
  object OakDoor extends Material with Solid with Rotates with Door
  object OakFence extends Material with Fence
  object OakFenceGate extends Material with FenceGate
  object OakLeaves extends Material(tier = T2) with Leaves
  object OakLog extends Material(tier = T2) with Log
  object OakPlanks extends Material with Plank
  object OakPressurePlate extends Material with Transparent with Attaches
  object OakSapling extends Material(tier = T2) with Sapling
  object OakSlab extends Material with WoodenSlab
  object OakStairs extends Material with WoodenStairs
  object OakTrapdoor extends Material with Solid with Transparent with Rotates with Door
  object OakWood extends Material with Wood
  object Observer extends Material with Solid
  object Obsidian extends Material(energy = 68) with Solid
  object OcelotSpawnEgg extends Material with SpawnEgg
  object OrangeBanner extends Material with Banner
  object OrangeBed extends Material with Transparent with Bed
  object OrangeCarpet extends Material with Carpet
  object OrangeConcrete extends Material with Solid with Concrete
  object OrangeConcretePowder extends Material with Solid with Gravity with ConcretePowder
  object OrangeDye extends Material with Dye
  object OrangeGlass extends Material with Glass
  object OrangeGlassPane extends Material with GlassPane
  object OrangeGlazedTerracotta extends Material with GlazedTerracotta
  object OrangeShulkerBox extends Material with Solid with Inventory with ShulkerBox
  object OrangeTerracotta extends Material with DyedTerracotta
  object OrangeTulip extends Material(energy = 14) with Plant
  object OrangeWallBanner extends Material with Banner
  object OrangeWool extends Material with Wool
  object OxeyeDaisy extends Material(energy = 14) with Plant
  object PackedIce extends Material(tier = T3) with Solid
  object Painting extends Material
  object Paper extends Material
  object ParrotSpawnEgg extends Material with SpawnEgg
  object Peony extends Material(tier = T2) with DoublePlant
  object PetrifiedOakSlab extends Material with Slab with NoEnergy
  object PhantomMembrane extends Material(tier = T2)
  object PhantomSpawnEgg extends Material with SpawnEgg
  object PigSpawnEgg extends Material with SpawnEgg
  object PinkBanner extends Material with Banner
  object PinkBed extends Material with Transparent with Bed
  object PinkCarpet extends Material with Carpet
  object PinkConcrete extends Material with Solid with Concrete
  object PinkConcretePowder extends Material with Solid with Gravity with ConcretePowder
  object PinkDye extends Material with Dye
  object PinkGlass extends Material with Glass
  object PinkGlassPane extends Material with GlassPane
  object PinkGlazedTerracotta extends Material with GlazedTerracotta
  object PinkShulkerBox extends Material with Solid with Inventory with ShulkerBox
  object PinkTerracotta extends Material with DyedTerracotta
  object PinkTulip extends Material(energy = 14) with Plant
  object PinkWallBanner extends Material with Banner
  object PinkWool extends Material with Wool
  object Piston extends Material with Solid
  object PistonHead extends Material with Solid with NoEnergy
  object PistonMovingPiece extends Material with Solid with NoEnergy
  object PlayerHead extends Material with NoEnergy
  object PlayerWallHead extends Material with NoEnergy
  object Podzol extends Material(tier = T0) with Solid
  object PoisonousPotato extends Material with PlayerConsumable
  object PolarBearSpawnEgg extends Material with SpawnEgg
  object PolishedAndesite extends Material with Solid
  object PolishedDiorite extends Material with Solid
  object PolishedGranite extends Material with Solid
  object PoppedChorusFruit extends Material
  object Poppy extends Material(energy = 14) with Plant
  object Potato extends Material(energy = 5498) with PlayerConsumable
  object Potatoes extends Material(tier = T2) with Plant
  object Potion extends Material with PlayerConsumable with NoEnergy
  object PottedAcaciaSapling extends Material with Transparent
  object PottedAllium extends Material with Transparent
  object PottedAzureBluet extends Material with Transparent
  object PottedBirchSapling extends Material with Transparent
  object PottedBlueOrchid extends Material with Transparent
  object PottedBrownMushroom extends Material with Transparent
  object PottedCactus extends Material with Transparent
  object PottedDandelion extends Material with Transparent
  object PottedDarkOakSapling extends Material with Transparent
  object PottedDeadBush extends Material with Transparent
  object PottedFern extends Material with Transparent
  object PottedJungleSapling extends Material with Transparent
  object PottedOakSapling extends Material with Transparent
  object PottedOrangeTulip extends Material with Transparent
  object PottedOxeyeDaisy extends Material with Transparent
  object PottedPinkTulip extends Material with Transparent
  object PottedPoppy extends Material with Transparent
  object PottedRedMushroom extends Material with Transparent
  object PottedRedTulip extends Material with Transparent
  object PottedSpruceSapling extends Material with Transparent
  object PottedWhiteTulip extends Material with Transparent
  object PoweredRail extends Material with Rail
  object Prismarine extends Material with Solid
  object PrismarineBricks extends Material with Solid
  object PrismarineBrickSlab extends Material with Slab
  object PrismarineBrickStairs extends Material with Stairs
  object PrismarineCrystals extends Material(energy =   5332)
  object PrismarineShard extends Material(energy = 469)
  object PrismarineSlab extends Material with Slab
  object PrismarineStairs extends Material with Stairs
  object Pufferfish extends Material(energy = 3903) with PlayerConsumable
  object PufferfishBucket extends Material
  object PufferfishSpawnEgg extends Material with SpawnEgg
  object Pumpkin extends Material(tier = T3) with Solid
  object PumpkinPie extends Material with PlayerConsumable
  object PumpkinSeeds extends Material
  object PumpkinStem extends Material(tier = T2) with Plant with Rotates
  object PurpleBanner extends Material with Banner
  object PurpleBed extends Material with Transparent with Bed
  object PurpleCarpet extends Material with Carpet
  object PurpleConcrete extends Material with Solid with Concrete
  object PurpleConcretePowder extends Material with Solid with Gravity with ConcretePowder
  object PurpleDye extends Material with Dye
  object PurpleGlass extends Material with Glass
  object PurpleGlassPane extends Material with GlassPane
  object PurpleGlazedTerracotta extends Material with GlazedTerracotta
  object PurpleShulkerBox extends Material with Solid with Inventory with ShulkerBox
  object PurpleTerracotta extends Material with DyedTerracotta
  object PurpleWallBanner extends Material with Banner
  object PurpleWool extends Material with Wool
  object PurpurBlock extends Material(tier = T4) with Solid
  object PurpurPillar extends Material(tier = T4) with Solid
  object PurpurSlab extends Material with Slab
  object PurpurStairs extends Material with Stairs
  object Quartz extends Material(tier = T3)
  object QuartzBlock extends Material with Solid
  object QuartzOre extends Material with Ore
  object QuartzPillar extends Material with Solid
  object QuartzSlab extends Material with Slab
  object QuartzStairs extends Material with Stairs
  object RabbitFoot extends Material(energy = 11)
  object RabbitHide extends Material(tier = T0)
  object RabbitSpawnEgg extends Material with SpawnEgg
  object RabbitStew extends Material with PlayerConsumable
  object Rail extends Material with Rail
  object RawBeef extends Material(tier = T0)
  object RawChicken extends Material(tier = T0) with PlayerConsumable
  object RawMutton extends Material(tier = T0)
  object RawPorkchop extends Material(tier = T0) with PlayerConsumable
  object RawRabbit extends Material(tier = T0) with PlayerConsumable
  object RawSalmon extends Material(energy = 43) with PlayerConsumable
  object RedBanner extends Material with Banner
  object RedBed extends Material with Transparent with Bed
  object RedCarpet extends Material with Carpet
  object RedConcrete extends Material with Solid with Concrete
  object RedConcretePowder extends Material with Solid with Gravity with ConcretePowder
  object RedGlass extends Material with Glass
  object RedGlassPane extends Material with GlassPane
  object RedGlazedTerracotta extends Material with GlazedTerracotta
  object RedMushroom extends Material(energy = 159) with Plant
  object RedMushroomBlock extends Material(tier = T3) with Solid
  object RedNetherBricks extends Material with Solid
  object RedSand extends Material(tier = T0) with Solid with Gravity
  object RedSandstone extends Material with Solid
  object RedSandstoneSlab extends Material with Slab
  object RedSandstoneStairs extends Material with Stairs
  object RedShulkerBox extends Material with Solid with Inventory with ShulkerBox
  object Redstone extends Material(energy = 35)
  object RedstoneBlock extends Material with Solid
  object RedstoneComparator extends Material
  object RedstoneLamp extends Material with Solid
  object RedstoneOre extends Material with Ore
  object RedstoneTorch extends Material with Torch
  object RedstoneWallTorch extends Material with Torch
  object RedstoneWire extends Material with Transparent with Attaches
  object RedTerracotta extends Material with DyedTerracotta
  object RedTulip extends Material(energy = 14) with Plant
  object RedWallBanner extends Material with Banner
  object RedWool extends Material with Wool
  object Repeater extends Material
  object RoseBush extends Material(tier = T2) with DoublePlant
  object RoseRed extends Material with Dye
  object RottenFlesh extends Material(energy = 10) with PlayerConsumable
  object Saddle extends Material(tier = T3)
  object SalmonBucket extends Material
  object SalmonSpawnEgg extends Material with SpawnEgg
  object Sand extends Material(tier = T0) with Solid with Gravity
  object Sandstone extends Material with Solid
  object SandstoneSlab extends Material with Slab
  object SandstoneStairs extends Material with Stairs
  object Scute extends Material(tier = T2)
  object Seagrass extends Material(14) with Plant
  object SeaLantern extends Material with Solid
  object SeaPickle extends Material(tier = T2) with Attaches
  object Shears extends Material with Shears
  object SheepSpawnEgg extends Material with SpawnEgg
  object Shield extends Material with Durable
  object ShulkerBox extends Material with Solid with Inventory with ShulkerBox
  object ShulkerShell extends Material(energy = 951)
  object ShulkerSpawnEgg extends Material with SpawnEgg
  object Sign extends Material with Sign
  object SilverfishSpawnEgg extends Material with SpawnEgg
  object SkeletonHorseSpawnEgg extends Material with SpawnEgg
  object SkeletonSkull extends Material with NoEnergy
  object SkeletonSpawnEgg extends Material with SpawnEgg
  object SkeletonWallSkull extends Material with NoEnergy
  object SlimeBall extends Material(energy = 2)
  object SlimeBlock extends Material with Solid
  object SlimeSpawnEgg extends Material with SpawnEgg
  object SmoothQuartz extends Material with Solid
  object SmoothRedSandstone extends Material with Solid
  object SmoothSandstone extends Material with Solid
  object SmoothStone extends Material with Solid
  object Snow extends Material(tier = T0) with Transparent with Attaches with Crushable
  object Snowball extends Material(tier = T0)
  object SnowBlock extends Material(tier = T0) with Solid
  object SoulSand extends Material with Solid
  object Spawner extends Material(tier = T6) with Solid
  object SpectralArrow extends Material with NoEnergy
  object SpiderEye extends Material(energy = 35) with PlayerConsumable
  object SpiderSpawnEgg extends Material with SpawnEgg
  object SplashPotion extends Material with Consumable with NoEnergy
  object Sponge extends Material(energy = 210) with Solid
  object SpruceBoat extends Material with Boat
  object SpruceButton extends Material with Button
  object SpruceDoor extends Material with Solid with Transparent with Door
  object SpruceFence extends Material with Fence
  object SpruceFenceGate extends Material with FenceGate
  object SpruceLeaves extends Material(tier = T2) with Leaves
  object SpruceLog extends Material(tier = T2) with Log
  object SprucePlanks extends Material with Plank
  object SprucePressurePlate extends Material with Transparent with Attaches
  object SpruceSapling extends Material(tier = T2) with Sapling
  object SpruceSlab extends Material with WoodenSlab
  object SpruceStairs extends Material with WoodenStairs
  object SpruceTrapdoor extends Material with Solid with Transparent with Rotates with Door
  object SpruceWood extends Material with Wood
  object SquidSpawnEgg extends Material with SpawnEgg
  object Stick extends Material(energy = 31) with Fuel { override val burnTicks: Int = 100 }
  object StickyPiston extends Material with Solid
  object Stone extends Material(tier = T0) with Solid
  object StoneAxe extends Material with Axe
  object StoneBrick extends Material(tier = T0) with Solid
  object StoneBrickSlab extends Material with Slab
  object StoneBrickStairs extends Material with Stairs
  object StoneButton extends Material with Transparent with Attaches with Rotates
  object StoneHoe extends Material with Hoe
  object StonePickaxe extends Material with Pickaxe
  object StonePressurePlate extends Material with Transparent with Attaches
  object StoneShovel extends Material with Shovel
  object StoneSlab extends Material with Slab
  object StoneSword extends Material(tier = T3) with Usable with Sword
  object StraySpawnEgg extends Material with SpawnEgg
  object String extends Material(energy = 9)
  object StrippedAcaciaLog extends Material with Log
  object StrippedAcaciaWood extends Material with Wood
  object StrippedBirchLog extends Material with Log
  object StrippedBirchWood extends Material with Wood
  object StrippedDarkOakLog extends Material with Log
  object StrippedDarkOakWood extends Material with Wood
  object StrippedJungleLog extends Material with Log
  object StrippedJungleWood extends Material with Wood
  object StrippedOakLog extends Material with Log
  object StrippedOakWood extends Material with Wood
  object StrippedSpruceLog extends Material with Log
  object StrippedSpruceWood extends Material with Wood
  object StructureBlock extends Material with Solid with Inventory with NoEnergy
  object StructureVoid extends Material with Solid with NoEnergy
  object Sugar extends Material(energy = 35)
  object SugarCane extends Material(tier = T2)
  object Sunflower extends Material(tier = T2) with DoublePlant
  object TallGrass extends Material(tier = T0) with DoublePlant
  object TallSeagrass extends Material(tier = T0) with DoublePlant
  object Terracotta extends Material with Terracotta
  object TippedArrow extends Material with NoEnergy
  object TNT extends Material with Solid
  object TNTMinecart extends Material
  object Torch extends Material with Torch with Crushable
  object TotemOfUndying extends Material(energy = 488)
  object TrappedChest extends Material with Inventory with Solid with Rotates with Fuel { override val burnTicks: Int = 300 }
  object Trident extends Material(tier = T2) with Trident
  object Tripwire extends Material with Transparent with Attaches
  object TripwireHook extends Material with Transparent with Attaches with Rotates
  object TropicalFish extends Material(3910) with PlayerConsumable
  object TropicalFishBucket extends Material
  object TropicalFishSpawnEgg extends Material with SpawnEgg
  object TubeCoral extends Material(tier = coralTier) with Coral
  object TubeCoralBlock extends Material(tier = coralTier) with CoralBlock
  object TubeCoralFan extends Material(tier = coralTier) with CoralFan
  object TubeCoralWallFan extends Material(tier = coralTier) with CoralWallFan
  object TurtleEgg extends Material with SpawnEgg
  object TurtleHelmet extends Material with Helmet
  object TurtleSpawnEgg extends Material with SpawnEgg
  object VexSpawnEgg extends Material with SpawnEgg
  object VillagerSpawnEgg extends Material with SpawnEgg
  object VindicatorSpawnEgg extends Material with SpawnEgg
  object Vine extends Material(tier = T5) with Transparent with Crushable
  object VoidAir extends Material with Air
  object WallSign extends Material with Transparent with Rotates with Sign
  object WallTorch extends Material with Torch
  object Water extends Material(energy = 14) with Transparent with Liquid
  object WaterBucket extends Material
  object WetSponge extends Material(energy = 18538) with Solid
  object Wheat extends Material(energy = 69) with Consumable
  object WheatSeeds extends Material(tier = T2) with Consumable
  object WhiteBanner extends Material with Banner
  object WhiteBed extends Material with Transparent with Bed
  object WhiteCarpet extends Material with Carpet
  object WhiteConcrete extends Material with Solid with Concrete
  object WhiteConcretePowder extends Material with Solid with Gravity with ConcretePowder
  object WhiteGlass extends Material with Glass
  object WhiteGlassPane extends Material with GlassPane
  object WhiteGlazedTerracotta extends Material with GlazedTerracotta
  object WhiteShulkerBox extends Material with Solid with Inventory with ShulkerBox
  object WhiteTerracotta extends Material with DyedTerracotta
  object WhiteTulip extends Material(energy = 14) with Plant
  object WhiteWallBanner extends Material with Banner
  object WhiteWool extends Material with Wool
  object WitchSpawnEgg extends Material with SpawnEgg
  object WitherSkeletonSkull extends Material with NoEnergy
  object WitherSkeletonSpawnEgg extends Material with SpawnEgg
  object WitherSkeletonWallSkull extends Material with NoEnergy
  object WolfSpawnEgg extends Material with SpawnEgg
  object WoodenAxe extends Material with Axe with WoodenTool
  object WoodenHoe extends Material with Hoe with WoodenTool
  object WoodenPickaxe extends Material with Pickaxe with WoodenTool
  object WoodenShovel extends Material with Shovel with WoodenTool
  object WoodenSword extends Material with Usable with Sword with WoodenTool
  object WritableBook extends Material with Usable
  object WrittenBook extends Material with Usable with NoEnergy
  object YellowBanner extends Material with Banner
  object YellowBed extends Material with Transparent with Bed
  object YellowCarpet extends Material with Carpet
  object YellowConcrete extends Material with Solid with Concrete
  object YellowConcretePowder extends Material with Solid with Gravity with ConcretePowder
  object YellowGlass extends Material with Glass
  object YellowGlassPane extends Material with GlassPane
  object YellowGlazedTerracotta extends Material with GlazedTerracotta
  object YellowShulkerBox extends Material with Solid with Inventory with ShulkerBox
  object YellowTerracotta extends Material with DyedTerracotta
  object YellowWallBanner extends Material with Banner
  object YellowWool extends Material with Wool
  object ZombieHead extends Material with NoEnergy
  object ZombieHorseSpawnEgg extends Material with SpawnEgg
  object ZombiePigmanSpawnEgg extends Material with SpawnEgg
  object ZombieSpawnEgg extends Material with SpawnEgg
  object ZombieVillagerSpawnEgg extends Material with SpawnEgg
  object ZombieWallHead extends Material with NoEnergy
  //Special Recipe Materials
  object Milk extends Material(energy = (Recipe.foodQuality * 10).toInt) //This energy is the food value of a milk bucket
  //@formatter:on
  //endregion
}