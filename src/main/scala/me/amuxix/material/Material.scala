package me.amuxix.material

import enumeratum._
import me.amuxix.{Energy, Named}
import me.amuxix.material.Generic._
import me.amuxix.material.Properties._

import scala.collection.immutable.IndexedSeq
import scala.math.{E, log}
import scala.reflect.ClassTag

/**
  * Created by Amuxix on 04/01/2017.
  */
sealed abstract class Material(protected[material] var _energy: Option[Energy] = None) extends EnumEntry with Named {
  def this(energy: Energy) = this(Some(energy))
  def this(tier: BaseTier) = this(Some(tier.energy))

  lazy val tier: Option[Int] = {
    energy match {
      case Some(e) => Some((log(e.value) / log(E * 2)).round.toInt)
      case None => None
    }
  }

  def energy: Option[Energy] = _energy
  def energy_=(energy: Energy): Unit = {
    require(hasEnergy, s"Something tried to modify energy of $name which has NoEnergy trait.")
    _energy = Some(energy)
  }

  override def toString: String = s"$name(${if (hasEnergy) energy.fold("None")(_.toString) else "NoEnergy"})"

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
  def isBlock: Boolean = isInstanceOf[BlockProperty]
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

object Material extends CirceEnum[Material] with Enum[Material] {

  override def values: IndexedSeq[Material] = findValues

  def filter[T : ClassTag]: Seq[Material with T] = values collect {
    case t: T => t.asInstanceOf[Material with T]
  }

  private val coralTier: BaseTier = T2

  //region Materials
  //@formatter:off
  object AcaciaBoat extends Material with Boat
  object AcaciaButton extends Material with WoodenButton
  object AcaciaDoor extends Material(tier = T2) with Door
  object AcaciaFence extends Material with Fence
  object AcaciaFenceGate extends Material with FenceGate
  object AcaciaLeaves extends Material with Leaves
  object AcaciaLog extends Material with Log
  object AcaciaPlanks extends Material with Plank
  object AcaciaPressurePlate extends Material with WoodenPressurePlate
  object AcaciaSapling extends Material with Sapling
  object AcaciaSign extends Material with Sign
  object AcaciaSlab extends Material with WoodenSlab
  object AcaciaStairs extends Material with WoodenStairs
  object AcaciaTrapdoor extends Material with WoodenTrapDoor
  object AcaciaWallSign extends Material with WallSign
  object AcaciaWood extends Material with Wood
  object ActivatorRail extends Material with Rail
  object Air extends Material with Air
  object Allium extends Material(energy = 14) with Plant
  object Andesite extends Material(tier = T0) with BreakableBlockProperty with Solid { override protected val minimumTool = Some(WoodenPickaxe) }
  object AndesiteSlab extends Material with Slab
  object AndesiteStairs extends Material with Stairs
  object AndesiteWall extends Material with Wall
  object Anvil extends Material with BreakableBlockProperty with Transparent with Gravity with Inventory { override protected val minimumTool = Some(WoodenPickaxe) }
  object Apple extends Material with ItemProperty with PlayerConsumable
  object ArmorStand extends Material with ItemProperty
  object Arrow extends Material(energy = 5) with ItemProperty
  object AttachedMelonStem extends Material(tier = T2) with Plant
  object AttachedPumpkinStem extends Material(tier = T2) with Plant
  object AzureBluet extends Material(energy = 14) with Plant
  object BakedPotato extends Material with ItemProperty with PlayerConsumable
  object Bamboo extends Material with Log with Fuel { override val burnTicks: Int = 50 }
  object BambooSapling extends Material with Sapling
  object Barrel extends Material with GenericChest
  object Barrier extends Material with BlockProperty with Solid with NoEnergy
  object BatSpawnEgg extends Material with SpawnEgg
  object Beacon extends Material with BreakableBlockProperty with Solid { override protected val minimumTool = None }
  object Bedrock extends Material with BreakableBlockProperty with Solid with NoEnergy { override protected val minimumTool = None }
  object Beetroot extends Material with ItemProperty with PlayerConsumable
  object BeetrootPlantation extends Material with Plant
  object BeetrootSeeds extends Material(tier = T2) with ItemProperty
  object BeetrootSoup extends Material with ItemProperty with PlayerConsumable
  object Bell extends Material(tier = T3) with BreakableBlockProperty with Transparent with FourRotations { override protected val minimumTool = Some(WoodenPickaxe) }
  object BirchBoat extends Material with Boat
  object BirchButton extends Material with WoodenButton
  object BirchDoor extends Material with Door
  object BirchFence extends Material with Fence
  object BirchFenceGate extends Material with FenceGate
  object BirchLeaves extends Material with Leaves
  object BirchLog extends Material with Log
  object BirchPlanks extends Material with Plank
  object BirchPressurePlate extends Material with WoodenPressurePlate
  object BirchSapling extends Material with Sapling
  object BirchSign extends Material with Sign
  object BirchSlab extends Material with WoodenSlab
  object BirchStairs extends Material with WoodenStairs
  object BirchTrapdoor extends Material with WoodenTrapDoor
  object BirchWallSign extends Material with WallSign
  object BirchWood extends Material with Wood
  object BlackBanner extends Material with Banner
  object BlackBed extends Material with Bed
  object BlackCarpet extends Material with Carpet
  object BlackConcrete extends Material with Concrete
  object BlackConcretePowder extends Material with ConcretePowder
  object BlackDye extends Material with Dye
  object BlackGlass extends Material with Glass
  object BlackGlassPane extends Material with GlassPane
  object BlackGlazedTerracotta extends Material with GlazedTerracotta
  object BlackShulkerBox extends Material with ShulkerBox
  object BlackTerracotta extends Material with DyedTerracotta
  object BlackWallBanner extends Material with WallBanner
  object BlackWool extends Material with Wool
  object BlastFurnace extends Material with BreakableBlockProperty with Solid with FourRotations with Inventory { override protected val minimumTool = Some(WoodenPickaxe) }
  object BlazePowder extends Material with ItemProperty
  object BlazeRod extends Material(energy = 2322) with ItemProperty with Fuel { override val burnTicks: Int = 2400 }
  object BlazeSpawnEgg extends Material with SpawnEgg
  object BlueBanner extends Material with Banner
  object BlueBed extends Material with Bed
  object BlueCarpet extends Material with Carpet
  object BlueConcrete extends Material with Concrete
  object BlueConcretePowder extends Material with ConcretePowder
  object BlueDye extends Material with Dye
  object BlueGlass extends Material with Glass
  object BlueGlassPane extends Material with GlassPane
  object BlueGlazedTerracotta extends Material with GlazedTerracotta
  object BlueIce extends Material with BreakableBlockProperty with Solid { override protected val minimumTool = Some(WoodenPickaxe) }
  object BlueOrchid extends Material(energy = 14) with Plant
  object BlueShulkerBox extends Material with ShulkerBox
  object BlueTerracotta extends Material with DyedTerracotta
  object BlueWallBanner extends Material with WallBanner
  object BlueWool extends Material with Wool
  object Bone extends Material(energy = 5) with ItemProperty
  object BoneBlock extends Material with BreakableBlockProperty with Solid { override protected val minimumTool = Some(WoodenPickaxe) }
  object BoneMeal extends Material with ItemProperty
  object Book extends Material with ItemProperty
  object Bookshelf extends Material with BreakableBlockProperty with Solid with Fuel {
    override protected val minimumTool = Some(WoodenPickaxe)
    override val burnTicks: Int = 300
  }
  object Bow extends Material(energy = 118) with Weapon with Fuel { override val burnTicks: Int = 300 }
  object Bowl extends Material with ItemProperty with Fuel { override val burnTicks: Int = 100 }
  object BrainCoral extends Material(tier = coralTier) with Coral
  object BrainCoralBlock extends Material(tier = coralTier) with CoralBlock
  object BrainCoralFan extends Material(tier = coralTier) with CoralFan
  object BrainCoralWallFan extends Material(tier = coralTier) with CoralWallFan
  object Bread extends Material with ItemProperty with PlayerConsumable
  object BrewingStand extends Material with BreakableBlockProperty with Solid { override protected val minimumTool = Some(WoodenPickaxe) }
  object Brick extends Material with ItemProperty
  object BrickBlock extends Material with BreakableBlockProperty with Solid { override protected val minimumTool = Some(WoodenPickaxe) }
  object BrickSlab extends Material with Slab
  object BrickStairs extends Material with Stairs
  object BrickWall extends Material with Wall
  object BrownBanner extends Material with Banner
  object BrownBed extends Material with Bed
  object BrownCarpet extends Material with Carpet
  object BrownConcrete extends Material with Concrete
  object BrownConcretePowder extends Material with ConcretePowder
  object BrownDye extends Material with Dye
  object BrownGlass extends Material with Glass
  object BrownGlassPane extends Material with GlassPane
  object BrownGlazedTerracotta extends Material with GlazedTerracotta
  object BrownMushroom extends Material(energy = 159) with Plant
  object BrownMushroomBlock extends Material(tier = T3) with BreakableBlockProperty with Solid { override protected val minimumTool = Some(WoodenAxe) }
  object BrownShulkerBox extends Material with ShulkerBox
  object BrownTerracotta extends Material with DyedTerracotta
  object BrownWallBanner extends Material with WallBanner
  object BrownWool extends Material with Wool
  object BubbleColumn extends Material with ItemProperty
  object BubbleCoral extends Material(tier = coralTier) with Coral
  object BubbleCoralBlock extends Material(tier = coralTier) with CoralBlock
  object BubbleCoralFan extends Material(tier = coralTier) with CoralFan
  object BubbleCoralWallFan extends Material(tier = coralTier) with CoralWallFan
  object Bucket extends Material with ItemProperty
  object Cactus extends Material(tier = T3) with BreakableBlockProperty with Solid with Attaches { override protected val minimumTool = None }
  object Cake extends Material with ItemProperty with PlayerConsumable
  object Campfire extends Material with BreakableBlockProperty with Transparent with FourRotations { override protected val minimumTool = Some(WoodenAxe) }
  object Carrot extends Material(energy = 5498) with ItemProperty with PlayerConsumable
  object CarrotOnAStick extends Material with ItemProperty
  object Carrots extends Material(tier = T2) with Plant
  object CartographyTable extends Material with BreakableBlockProperty with Solid with FourRotations { override protected val minimumTool = Some(WoodenAxe) }
  object CarvedPumpkin extends Material with BreakableBlockProperty with Solid with FourRotations { override protected val minimumTool = Some(WoodenAxe) }
  object CatSpawnEgg extends Material with SpawnEgg
  object Cauldron extends Material with BreakableBlockProperty with Solid { override protected val minimumTool = Some(WoodenPickaxe) }
  object CaveAir extends Material with Air
  object CaveSpiderSpawnEgg extends Material with SpawnEgg
  object ChainCommand extends Material with BlockProperty with Solid with NoEnergy
  object ChainmailBoots extends Material with Boots
  object ChainmailChestplate extends Material with Chestplate
  object ChainmailHelmet extends Material with Helmet
  object ChainmailLeggings extends Material with Leggings
  object Charcoal extends Material with ItemProperty with Fuel { override val burnTicks: Int = 1600 }
  object Chest extends Material with GenericChest
  object ChestMinecart extends Material with ItemProperty// with Inventory
  object ChickenSpawnEgg extends Material with SpawnEgg
  object ChippedAnvil extends Material with BreakableBlockProperty with Transparent with Gravity with Inventory { override protected val minimumTool = Some(WoodenPickaxe) }
  object ChiseledQuartzBlock extends Material with BreakableBlockProperty with Solid { override protected val minimumTool = Some(WoodenPickaxe) }
  object ChiseledRedSandstone extends Material with BreakableBlockProperty with Solid { override protected val minimumTool = Some(WoodenPickaxe) }
  object ChiseledSandstone extends Material with BreakableBlockProperty with Solid { override protected val minimumTool = Some(WoodenPickaxe) }
  object ChiseledStoneBricks extends Material with BreakableBlockProperty with Solid { override protected val minimumTool = Some(WoodenPickaxe) }
  object ChorusFlower extends Material(tier = T2) with BreakableBlockProperty with Solid with Crushable { override protected val minimumTool = Some(WoodenAxe) }
  object ChorusFruit extends Material(tier = T6) with ItemProperty
  object ChorusPlant extends Material(tier = T2) with BreakableBlockProperty with Solid { override protected val minimumTool = Some(WoodenAxe) }
  object Clay extends Material with BreakableBlockProperty with Solid { override protected val minimumTool = Some(WoodenShovel) }
  object ClayBall extends Material(tier = T2) with ItemProperty
  object Clock extends Material with ItemProperty
  object Coal extends Material(energy = 72) with ItemProperty with Fuel { override val burnTicks: Int = 1600 }
  object CoalBlock extends Material with BreakableBlockProperty with Solid with Fuel {
    override protected val minimumTool = Some(WoodenPickaxe)
    override val burnTicks: Int = 16000
  }
  object CoalOre extends Material with Ore
  object CoarseDirt extends Material with BreakableBlockProperty with Solid { override protected val minimumTool = Some(WoodenShovel) }
  object Cobblestone extends Material(tier = T0) with BreakableBlockProperty with Solid { override protected val minimumTool = Some(WoodenPickaxe) }
  object CobblestoneSlab extends Material with Slab
  object CobblestoneStairs extends Material with Stairs
  object CobblestoneWall extends Material with Wall
  object Cobweb extends Material(energy = 2279) with Plant
  object Cocoa extends Material(tier = T4) with Plant
  object CocoaBeans extends Material(tier = T2) with ItemProperty
  object Cod extends Material(energy = 33) with ItemProperty with PlayerConsumable
  object CodBucket extends Material with ItemProperty
  object CodSpawnEgg extends Material with SpawnEgg
  object Command extends Material with BlockProperty with Solid with NoEnergy
  object CommandBlockMinecart extends Material with ItemProperty with NoEnergy
  object CommandRepeating extends Material with BlockProperty with Solid with NoEnergy
  object Compass extends Material with ItemProperty
  object Composter extends Material with BreakableBlockProperty with Solid { override protected val minimumTool = Some(WoodenAxe) }
  object Conduit extends Material with BreakableBlockProperty with Solid { override protected val minimumTool = Some(WoodenPickaxe) }
  object CookedBeef extends Material with ItemProperty with PlayerConsumable
  object CookedChicken extends Material with ItemProperty with PlayerConsumable
  object CookedCod extends Material with ItemProperty with PlayerConsumable
  object CookedMutton extends Material with ItemProperty
  object CookedPorkchop extends Material with ItemProperty with PlayerConsumable
  object CookedRabbit extends Material with ItemProperty with PlayerConsumable
  object CookedSalmon extends Material with ItemProperty with PlayerConsumable
  object Cookie extends Material with ItemProperty with PlayerConsumable
  object Cornflower extends Material(tier = T2) with Plant
  object CowSpawnEgg extends Material with SpawnEgg
  object CrackedStoneBricks extends Material(tier = T5) with BreakableBlockProperty with Solid { override protected val minimumTool = Some(WoodenPickaxe) }
  object CraftingTable extends Material with BreakableBlockProperty with Solid with Fuel {
    override protected val minimumTool = Some(WoodenAxe)
    override val burnTicks: Int = 300
  }
  object CreeperBannerPattern extends Material with BannerPattern
  object CreeperHead extends Material(tier = T5) with FloorHead
  object CreeperSpawnEgg extends Material with SpawnEgg
  object CreeperWallHead extends Material with WallHead
  object Crossbow extends Material with Weapon
  object CutRedSandstone extends Material with BreakableBlockProperty with Solid { override protected val minimumTool = Some(WoodenPickaxe) }
  object CutRedSandstoneSlab extends Material with Slab
  object CutSandstone extends Material with BreakableBlockProperty with Solid { override protected val minimumTool = Some(WoodenPickaxe) }
  object CutSandstoneSlab extends Material with Slab
  object CyanBanner extends Material with Banner
  object CyanBed extends Material with Bed
  object CyanCarpet extends Material with Carpet
  object CyanConcrete extends Material with Concrete
  object CyanConcretePowder extends Material with ConcretePowder
  object CyanDye extends Material with Dye
  object CyanGlass extends Material with Glass
  object CyanGlassPane extends Material with GlassPane
  object CyanGlazedTerracotta extends Material with GlazedTerracotta
  object CyanShulkerBox extends Material with ShulkerBox
  object CyanTerracotta extends Material with DyedTerracotta
  object CyanWallBanner extends Material with WallBanner
  object CyanWool extends Material with Wool
  object DamagedAnvil extends Material with BreakableBlockProperty with Transparent with Gravity with Inventory { override protected val minimumTool = Some(WoodenPickaxe) }
  object Dandelion extends Material(energy = 14) with Plant
  object DarkOakBoat extends Material with Boat
  object DarkOakButton extends Material with WoodenButton
  object DarkOakDoor extends Material with Door
  object DarkOakFence extends Material with Fence
  object DarkOakFenceGate extends Material with FenceGate
  object DarkOakLeaves extends Material with Leaves
  object DarkOakLog extends Material with Log
  object DarkOakPlanks extends Material with Plank
  object DarkOakPressurePlate extends Material with WoodenPressurePlate
  object DarkOakSapling extends Material with Sapling
  object DarkOakSign extends Material with Sign
  object DarkOakSlab extends Material with WoodenSlab
  object DarkOakStairs extends Material with WoodenStairs
  object DarkOakTrapdoor extends Material with Door
  object DarkOakWallSign extends Material with WallSign
  object DarkOakWood extends Material with Wood
  object DarkPrismarine extends Material with BreakableBlockProperty with Solid { override protected val minimumTool = Some(WoodenPickaxe) }
  object DarkPrismarineSlab extends Material with Slab
  object DarkPrismarineStairs extends Material with Stairs
  object DaylightDetector extends Material with BreakableBlockProperty with Solid with Fuel {
    override protected val minimumTool = Some(WoodenAxe)
    override val burnTicks: Int = 300
  }
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
  object DebugStick extends Material with ItemProperty with NoEnergy
  object DetectorRail extends Material with Rail
  object Diamond extends Material(energy = 2869) with ItemProperty
  object DiamondAxe extends Material with Axe with Crystalline
  object DiamondBlock extends Material with BreakableBlockProperty with Solid { override protected val minimumTool = Some(WoodenPickaxe) }
  object DiamondBoots extends Material with Boots with Crystalline
  object DiamondChestplate extends Material with Chestplate with Crystalline
  object DiamondHelmet extends Material with Helmet with Crystalline
  object DiamondHoe extends Material with Hoe with Crystalline
  object DiamondHorseArmor extends Material(tier = T5) with ItemProperty
  object DiamondLeggings extends Material with Leggings with Crystalline
  object DiamondOre extends Material with Ore
  object DiamondPickaxe extends Material with Pickaxe with Crystalline
  object DiamondShovel extends Material with Shovel with Crystalline
  object DiamondSword extends Material with Sword with Crystalline
  object Diorite extends Material(tier = T0) with BreakableBlockProperty with Solid { override protected val minimumTool = Some(WoodenPickaxe) }
  object DioriteSlab extends Material with Slab
  object DioriteStairs extends Material with Stairs
  object DioriteWall extends Material with Wall
  object Dirt extends Material(tier = T0) with BreakableBlockProperty with Solid { override protected val minimumTool = Some(WoodenShovel) }
  object Dispenser extends Material with BreakableBlockProperty with Inventory { override protected val minimumTool = Some(WoodenPickaxe) }
  object DolphinSpawnEgg extends Material with SpawnEgg
  object DonkeySpawnEgg extends Material with SpawnEgg
  object DragonBreath extends Material with ItemProperty with NoEnergy
  object DragonEgg extends Material(energy = 140369) with BreakableBlockProperty with Solid { override protected val minimumTool = Some(WoodenAxe) }
  object DragonHead extends Material(tier = T5) with FloorHead
  object DragonWallHead extends Material with WallHead
  object DriedKelp extends Material with ItemProperty with PlayerConsumable with Fuel { override val burnTicks: Int = 4000 }
  object DriedKelpBlock extends Material with BreakableBlockProperty with Solid { override protected val minimumTool = None }
  object Dropper extends Material with BreakableBlockProperty with Inventory { override protected val minimumTool = Some(WoodenPickaxe) }
  object DrownedSpawnEgg extends Material with SpawnEgg
  object Egg extends Material(tier = T1) with ItemProperty
  object ElderGuardianSpawnEgg extends Material with SpawnEgg
  object Elytra extends Material(tier = T6) with ItemProperty with Durable
  object Emerald extends Material(energy = 464) with ItemProperty
  object EmeraldBlock extends Material with BreakableBlockProperty with Solid { override protected val minimumTool = Some(WoodenPickaxe) }
  object EmeraldOre extends Material with Ore
  object EmptyMap extends Material with ItemProperty with Usable
  object EnchantedBook extends Material with ItemProperty with NoEnergy
  object EnchantedGoldenApple extends Material with ItemProperty with PlayerConsumable
  object EnchantingTable extends Material with BreakableBlockProperty with Solid { override protected val minimumTool = Some(WoodenPickaxe) }
  object EndCrystal extends Material with ItemProperty
  object EnderChest extends Material with BreakableBlockProperty with Solid with FourRotations { override protected val minimumTool = Some(WoodenPickaxe) }
  object EnderEye extends Material with ItemProperty
  object EndermanSpawnEgg extends Material with SpawnEgg
  object EndermiteSpawnEgg extends Material with SpawnEgg
  object EnderPearl extends Material(energy = 1090) with ItemProperty
  object EndGateway extends Material with BlockProperty with Solid with NoEnergy
  object EndPortal extends Material with BlockProperty with Transparent with NoEnergy
  object EndPortalFrame extends Material with BreakableBlockProperty with Solid with NoEnergy { override protected val minimumTool = None }
  object EndRod extends Material(tier = T5) with BreakableBlockProperty with Transparent { override protected val minimumTool = None }
  object EndStone extends Material(tier = T0) with BreakableBlockProperty with Solid { override protected val minimumTool = Some(WoodenPickaxe) }
  object EndStoneBricks extends Material with ItemProperty
  object EndStoneBrickSlab extends Material with Slab
  object EndStoneBrickStairs extends Material with Stairs
  object EndStoneBrickWall extends Material with Wall
  object EvokerSpawnEgg extends Material with SpawnEgg
  object ExperienceBottle extends Material with ItemProperty with PlayerConsumable with NoEnergy
  object Farmland extends Material with BreakableBlockProperty with Solid { override protected val minimumTool = Some(WoodenShovel) }
  object Feather extends Material(tier = T0) with ItemProperty
  object FermentedSpiderEye extends Material with ItemProperty
  object Fern extends Material(tier = T0) with Plant
  object FilledMap extends Material with ItemProperty with NoEnergy
  object Fire extends Material(tier = T3) with BreakableBlockProperty with Transparent with Attaches with Crushable { override protected val minimumTool = None }
  object FireCharge extends Material with ItemProperty with Usable
  object FireCoral extends Material(tier = coralTier) with Coral
  object FireCoralBlock extends Material(tier = coralTier) with CoralBlock
  object FireCoralFan extends Material(tier = coralTier) with CoralFan
  object FireCoralWallFan extends Material(tier = coralTier) with CoralWallFan
  object FireworkRocket extends Material with ItemProperty with Usable with NoEnergy
  object FireworkStar extends Material with ItemProperty with NoEnergy
  object FishingRod extends Material with ItemProperty with Durable with Fuel { override val burnTicks: Int = 300 }
  object FletchingTable extends Material with BreakableBlockProperty with Solid with Fuel {
    override protected val minimumTool = Some(WoodenAxe)
    override val burnTicks: Int = 300
  }
  object Flint extends Material with ItemProperty
  object FlintAndSteel extends Material with ItemProperty with Durable
  object FlowerBannerPattern extends Material with BannerPattern
  object FlowerPot extends Material with BreakableBlockProperty with Transparent { override protected val minimumTool = None }
  object FoxSpawnEgg extends Material with SpawnEgg
  object FrostedIce extends Material with BreakableBlockProperty with Solid { override protected val minimumTool = None }
  object Furnace extends Material with BreakableBlockProperty with Solid with FourRotations with Inventory { override protected val minimumTool = Some(WoodenPickaxe) }
  object FurnaceMinecart extends Material with ItemProperty// with Inventory
  object GhastSpawnEgg extends Material with SpawnEgg
  object GhastTear extends Material(energy = 269) with ItemProperty
  object Glass extends Material with Glass
  object GlassBottle extends Material(energy = 35) with Glass
  object GlassPane extends Material with GlassPane
  object GlisteringMelonSlice extends Material with ItemProperty
  object GlobeBannerPattern extends Material with BannerPattern
  object Glowstone extends Material with BreakableBlockProperty with Solid { override protected val minimumTool = None }
  object GlowstoneDust extends Material(energy = 35) with ItemProperty
  object GoldBlock extends Material with BreakableBlockProperty with Solid { override protected val minimumTool = Some(WoodenPickaxe) }
  object GoldenApple extends Material with ItemProperty with PlayerConsumable
  object GoldenAxe extends Material with Axe with Golden
  object GoldenBoots extends Material with Boots with Golden
  object GoldenCarrot extends Material with ItemProperty with PlayerConsumable
  object GoldenChestplate extends Material with Chestplate with Golden
  object GoldenHelmet extends Material with Helmet with Golden
  object GoldenHoe extends Material with Hoe with Golden
  object GoldenHorseArmor extends Material(tier = T4) with HorseArmor with Golden
  object GoldenLeggings extends Material with Leggings with Golden
  object GoldenPickaxe extends Material with Pickaxe with Golden
  object GoldenPressurePlate extends Material with PressurePlate
  object GoldenShovel extends Material with Shovel with Golden
  object GoldenSword extends Material(energy = 43307) with Sword with Golden
  object GoldIngot extends Material with ItemProperty
  object GoldNugget extends Material(energy = 2490) with ItemProperty
  object GoldOre extends Material(energy = 1168) with Ore
  object Granite extends Material(tier = T0) with BreakableBlockProperty with Solid { override protected val minimumTool = Some(WoodenPickaxe) }
  object GraniteSlab extends Material with Slab
  object GraniteStairs extends Material with Stairs
  object GraniteWall extends Material with Wall
  object Grass extends Material(tier = T2) with Plant
  object GrassBlock extends Material(tier = T0) with BreakableBlockProperty with Solid { override protected val minimumTool = Some(WoodenShovel) }
  object GrassPath extends Material with BreakableBlockProperty with Solid { override protected val minimumTool = Some(WoodenShovel) }
  object Gravel extends Material(tier = T2) with BreakableBlockProperty with Solid with Gravity { override protected val minimumTool = Some(WoodenShovel) }
  object GrayBanner extends Material with Banner
  object GrayBed extends Material with Bed
  object GrayCarpet extends Material with Carpet
  object GrayConcrete extends Material with Concrete
  object GrayConcretePowder extends Material with ConcretePowder
  object GrayDye extends Material with Dye
  object GrayGlass extends Material with Glass
  object GrayGlassPane extends Material with GlassPane
  object GrayGlazedTerracotta extends Material with GlazedTerracotta
  object GrayShulkerBox extends Material with ShulkerBox
  object GrayTerracotta extends Material with DyedTerracotta
  object GrayWallBanner extends Material with WallBanner
  object GrayWool extends Material with Wool
  object GreenBanner extends Material with Banner
  object GreenBed extends Material with Bed
  object GreenCarpet extends Material with Carpet
  object GreenConcrete extends Material with Concrete
  object GreenConcretePowder extends Material with ConcretePowder
  object GreenDye extends Material with Dye
  object GreenGlass extends Material with Glass
  object GreenGlassPane extends Material with GlassPane
  object GreenGlazedTerracotta extends Material with GlazedTerracotta
  object GreenShulkerBox extends Material with ShulkerBox
  object GreenTerracotta extends Material with DyedTerracotta
  object GreenWallBanner extends Material with WallBanner
  object GreenWool extends Material with Wool
  object Grindstone extends Material with BreakableBlockProperty with Transparent with Attaches with SixRotations { override protected val minimumTool = Some(WoodenPickaxe) }
  object GuardianSpawnEgg extends Material with SpawnEgg
  object Gunpowder extends Material(energy = 35) with ItemProperty
  object HayBale extends Material with BreakableBlockProperty with Solid { override protected val minimumTool = None }
  object HeartOfTheSea extends Material(tier = T5) with ItemProperty
  object Hopper extends Material with BreakableBlockProperty with Inventory { override protected val minimumTool = Some(WoodenPickaxe) }
  object HopperMinecart extends Material with ItemProperty// with Inventory
  object HornCoral extends Material(tier = coralTier) with Coral
  object HornCoralBlock extends Material(tier = coralTier) with CoralBlock
  object HornCoralFan extends Material(tier = coralTier) with CoralFan
  object HornCoralWallFan extends Material(tier = coralTier) with CoralWallFan
  object HorseSpawnEgg extends Material with SpawnEgg
  object HuskSpawnEgg extends Material with SpawnEgg
  object Ice extends Material(tier = T0) with BreakableBlockProperty with Solid { override protected val minimumTool = Some(WoodenPickaxe) }
  object InfestedChiseledStoneBricks extends Material with InfestedStone
  object InfestedCobblestone extends Material with InfestedStone
  object InfestedCrackedStoneBricks extends Material with InfestedStone
  object InfestedMossyStoneBricks extends Material with InfestedStone
  object InfestedStone extends Material with InfestedStone
  object InfestedStoneBricks extends Material with InfestedStone
  object InkSac extends Material(tier = T0) with ItemProperty
  object IronAxe extends Material with ItemProperty /*(energy = 48052)*/ with Axe with Ferrous
  object IronBars extends Material with Fence
  object IronBlock extends Material with BreakableBlockProperty with Solid { override protected val minimumTool = Some(WoodenPickaxe) }
  object IronBoots extends Material with Boots with Ferrous
  object IronChestplate extends Material with Chestplate with Ferrous
  object IronDoor extends Material with Door
  object IronHelmet extends Material with Helmet with Ferrous
  object IronHoe extends Material with Hoe with Ferrous
  object IronHorseArmor extends Material(tier = T3) with HorseArmor with Ferrous
  object IronIngot extends Material(energy = 459) with ItemProperty
  object IronLeggings extends Material with Leggings with Ferrous
  object IronNugget extends Material with ItemProperty
  object IronOre extends Material(tier = T3) with Ore
  object IronPickaxe extends Material with Pickaxe with Ferrous
  object IronPressurePlate extends Material with PressurePlate
  object IronShovel extends Material with Shovel with Ferrous
  object IronSword extends Material with Sword with Ferrous
  object IronTrapdoor extends Material with BreakableBlockProperty with Solid { override protected val minimumTool = Some(WoodenPickaxe) }
  object ItemFrame extends Material with ItemProperty
  object JackOLantern extends Material with BreakableBlockProperty with Solid { override protected val minimumTool = Some(WoodenAxe) }
  object JigsawBlock extends Material with BreakableBlockProperty with Solid with NoEnergy { override protected val minimumTool = None }
  object Jukebox extends Material with BreakableBlockProperty with Solid with Fuel {
    override protected val minimumTool = Some(WoodenAxe)
    override val burnTicks: Int = 300
  }
  object JungleBoat extends Material with Boat
  object JungleButton extends Material with WoodenButton
  object JungleDoor extends Material with Door
  object JungleFence extends Material with Fence
  object JungleFenceGate extends Material with FenceGate
  object JungleLeaves extends Material with Leaves
  object JungleLog extends Material with Log
  object JunglePlanks extends Material with Plank
  object JunglePressurePlate extends Material with WoodenPressurePlate
  object JungleSapling extends Material with Sapling
  object JungleSign extends Material with Sign
  object JungleSlab extends Material with WoodenSlab
  object JungleStairs extends Material with WoodenStairs
  object JungleTrapdoor extends Material with Door
  object JungleWallSign extends Material with WallSign
  object JungleWood extends Material with Wood
  object Kelp extends Material(14) with ItemProperty
  object KelpPlant extends Material(14) with Plant
  object KnowledgeBook extends Material with ItemProperty with NoEnergy
  object Ladder extends Material with BreakableBlockProperty with Transparent with Attaches with FourRotations with Fuel {
    override protected val minimumTool = Some(WoodenAxe)
    override val burnTicks: Int = 300
  }
  object Lantern extends Material with BreakableBlockProperty with Solid { override protected val minimumTool = Some(WoodenPickaxe) }
  object LapisLazuli extends Material(tier = T4) with ItemProperty
  object LapisLazuliBlock extends Material with BreakableBlockProperty with Solid { override protected val minimumTool = Some(WoodenPickaxe) }
  object LapisLazuliOre extends Material with Ore
  object LargeFern extends Material(tier = T2) with DoublePlant
  object Lava extends Material(energy = 68) with BlockProperty with Liquid
  object LavaBucket extends Material with ItemProperty with Fuel { override val burnTicks: Int = 20000 }
  object Leash extends Material with ItemProperty
  object Leather extends Material(tier = T0) with ItemProperty
  object LeatherBoots extends Material with Boots
  object LeatherChestplate extends Material with Chestplate
  object LeatherHelmet extends Material with Helmet
  object LeatherHorseArmor extends Material with HorseArmor
  object LeatherLeggings extends Material with Leggings
  object Lectern extends Material with BreakableBlockProperty with Solid with FourRotations { override protected val minimumTool = Some(WoodenAxe) }
  object Lever extends Material with BreakableBlockProperty with Transparent with Attaches with SixRotations { override protected val minimumTool = None }
  object LightBlueBanner extends Material with Banner
  object LightBlueBed extends Material with Bed
  object LightBlueCarpet extends Material with Carpet
  object LightBlueConcrete extends Material with Concrete
  object LightBlueConcretePowder extends Material with ConcretePowder
  object LightBlueDye extends Material with Dye
  object LightBlueGlass extends Material with Glass
  object LightBlueGlassPane extends Material with GlassPane
  object LightBlueGlazedTerracotta extends Material with GlazedTerracotta
  object LightBlueShulkerBox extends Material with ShulkerBox
  object LightBlueTerracotta extends Material with DyedTerracotta
  object LightBlueWallBanner extends Material with WallBanner
  object LightBlueWool extends Material with Wool
  object LightGrayBanner extends Material with Banner
  object LightGrayBed extends Material with Bed
  object LightGrayCarpet extends Material with Carpet
  object LightGrayConcrete extends Material with Concrete
  object LightGrayConcretePowder extends Material with ConcretePowder
  object LightGrayDye extends Material with Dye
  object LightGrayGlass extends Material with Glass
  object LightGrayGlassPane extends Material with GlassPane
  object LightGrayGlazedTerracotta extends Material with GlazedTerracotta
  object LightGrayShulkerBox extends Material with ShulkerBox
  object LightGrayTerracotta extends Material with DyedTerracotta
  object LightGrayWallBanner extends Material with WallBanner
  object LightGrayWool extends Material with Wool
  object Lilac extends Material(tier = T2) with DoublePlant
  object LilyOfTheValley extends Material(tier = T2) with Plant
  object LilyPad extends Material(tier = T5) with Plant
  object LimeBanner extends Material with Banner
  object LimeBed extends Material with Bed
  object LimeCarpet extends Material with Carpet
  object LimeConcrete extends Material with Concrete
  object LimeConcretePowder extends Material with ConcretePowder
  object LimeDye extends Material with Dye
  object LimeGlass extends Material with Glass
  object LimeGlassPane extends Material with GlassPane
  object LimeGlazedTerracotta extends Material with GlazedTerracotta
  object LimeShulkerBox extends Material with ShulkerBox
  object LimeTerracotta extends Material with DyedTerracotta
  object LimeWallBanner extends Material with WallBanner
  object LimeWool extends Material with Wool
  object LingeringPotion extends Material with ItemProperty with PlayerConsumable with NoEnergy
  object LlamaSpawnEgg extends Material with SpawnEgg
  object Loom extends Material with BreakableBlockProperty with Solid { override protected val minimumTool = Some(WoodenAxe) }
  object MagentaBanner extends Material with Banner
  object MagentaBed extends Material with Bed
  object MagentaCarpet extends Material with Carpet
  object MagentaConcrete extends Material with Concrete
  object MagentaConcretePowder extends Material with ConcretePowder
  object MagentaDye extends Material with Dye
  object MagentaGlass extends Material with Glass
  object MagentaGlassPane extends Material with GlassPane
  object MagentaGlazedTerracotta extends Material with GlazedTerracotta
  object MagentaShulkerBox extends Material with ShulkerBox
  object MagentaTerracotta extends Material with DyedTerracotta
  object MagentaWallBanner extends Material with WallBanner
  object MagentaWool extends Material with Wool
  object MagmaBlock extends Material(tier = T3) with BreakableBlockProperty with Solid { override protected val minimumTool = Some(WoodenAxe) }
  object MagmaCream extends Material(energy = 386) with ItemProperty
  object MagmaCubeSpawnEgg extends Material with SpawnEgg
  object Melon extends Material with BreakableBlockProperty with Solid { override protected val minimumTool = Some(WoodenAxe) }
  object MelonSeeds extends Material with ItemProperty
  object MelonSlice extends Material with ItemProperty with PlayerConsumable
  object MelonStem extends Material(tier = T2) with Plant
  object MilkBucket extends Material with ItemProperty with PlayerConsumable
  object Minecart extends Material with ItemProperty
  object MojangBannerPattern extends Material with BannerPattern
  object MooshroomSpawnEgg extends Material with SpawnEgg
  object MossyCobblestone extends Material with BreakableBlockProperty with Solid { override protected val minimumTool = Some(WoodenPickaxe) }
  object MossyCobblestoneSlab extends Material with Slab
  object MossyCobblestoneStairs extends Material with Stairs
  object MossyCobblestoneWall extends Material with Wall
  object MossyStoneBricks extends Material with BreakableBlockProperty with Solid { override protected val minimumTool = Some(WoodenPickaxe) }
  object MossyStoneBrickSlab extends Material with Slab
  object MossyStoneBrickStairs extends Material with Stairs
  object MossyStoneBrickWall extends Material with Wall
  object MuleSpawnEgg extends Material with SpawnEgg
  object MushroomStem extends Material(tier = T3) with BreakableBlockProperty with Solid { override protected val minimumTool = Some(WoodenAxe) }
  object MushroomStew extends Material with ItemProperty with PlayerConsumable
  object MusicDisc11 extends Material(tier = T4) with ItemProperty
  object MusicDisc13 extends Material(tier = T4) with ItemProperty
  object MusicDiscBlocks extends Material(tier = T4) with ItemProperty
  object MusicDiscCat extends Material(tier = T4) with ItemProperty
  object MusicDiscChirp extends Material(tier = T4) with ItemProperty
  object MusicDiscFar extends Material(tier = T4) with ItemProperty
  object MusicDiscMall extends Material(tier = T4) with ItemProperty
  object MusicDiscMellohi extends Material(tier = T4) with ItemProperty
  object MusicDiscStal extends Material(tier = T4) with ItemProperty
  object MusicDiscStrad extends Material(tier = T4) with ItemProperty
  object MusicDiscWait extends Material(tier = T4) with ItemProperty
  object MusicDiscWard extends Material(tier = T4) with ItemProperty
  object Mycelium extends Material(tier = T0) with BreakableBlockProperty with Solid { override protected val minimumTool = Some(WoodenShovel) }
  object NameTag extends Material(tier = T4) with ItemProperty
  object NautilusShell extends Material(tier = T4) with ItemProperty
  object NetherBrick extends Material with ItemProperty
  object NetherBrickFence extends Material with Fence
  object NetherBricks extends Material with BreakableBlockProperty with Solid { override protected val minimumTool = Some(WoodenPickaxe) }
  object NetherBrickSlab extends Material with Slab
  object NetherBrickStairs extends Material with Stairs
  object NetherBrickWall extends Material with Wall
  object NetherPortal extends Material with BlockProperty with Transparent with NoEnergy
  object Netherrack extends Material(tier = T0) with BreakableBlockProperty with Solid { override protected val minimumTool = Some(WoodenPickaxe) }
  object NetherStar extends Material(energy = 62141) with ItemProperty
  object NetherWart extends Material(tier = T3) with ItemProperty
  object NetherWartBlock extends Material with BreakableBlockProperty with Solid { override protected val minimumTool = None }
  object NoteBlock extends Material with BreakableBlockProperty with Solid with Fuel {
    override protected val minimumTool = Some(WoodenAxe)
    override val burnTicks: Int = 300
  }
  object OakBoat extends Material with Boat
  object OakButton extends Material with WoodenButton
  object OakDoor extends Material with Door
  object OakFence extends Material with Fence
  object OakFenceGate extends Material with FenceGate
  object OakLeaves extends Material with Leaves
  object OakLog extends Material with Log
  object OakPlanks extends Material with Plank
  object OakPressurePlate extends Material with WoodenPressurePlate
  object OakSapling extends Material with Sapling
  object OakSign extends Material with Sign
  object OakSlab extends Material with WoodenSlab
  object OakStairs extends Material with WoodenStairs
  object OakTrapdoor extends Material with Door
  object OakWallSign extends Material with WallSign
  object OakWood extends Material with Wood
  object Observer extends Material with BreakableBlockProperty with Solid { override protected val minimumTool = Some(WoodenPickaxe) }
  object Obsidian extends Material(energy = 68) with BreakableBlockProperty with Solid { override protected val minimumTool = Some(WoodenPickaxe) }
  object OcelotSpawnEgg extends Material with SpawnEgg
  object OrangeBanner extends Material with Banner
  object OrangeBed extends Material with Bed
  object OrangeCarpet extends Material with Carpet
  object OrangeConcrete extends Material with Concrete
  object OrangeConcretePowder extends Material with ConcretePowder
  object OrangeDye extends Material with Dye
  object OrangeGlass extends Material with Glass
  object OrangeGlassPane extends Material with GlassPane
  object OrangeGlazedTerracotta extends Material with GlazedTerracotta
  object OrangeShulkerBox extends Material with ShulkerBox
  object OrangeTerracotta extends Material with DyedTerracotta
  object OrangeTulip extends Material(energy = 14) with Plant
  object OrangeWallBanner extends Material with WallBanner
  object OrangeWool extends Material with Wool
  object OxeyeDaisy extends Material(energy = 14) with Plant
  object PackedIce extends Material(tier = T3) with BreakableBlockProperty with Solid { override protected val minimumTool = Some(WoodenPickaxe) }
  object Painting extends Material with ItemProperty
  object PandaSpawnEgg extends Material with SpawnEgg
  object Paper extends Material with ItemProperty
  object ParrotSpawnEgg extends Material with SpawnEgg
  object Peony extends Material(tier = T2) with DoublePlant
  object PetrifiedOakSlab extends Material with Slab with NoEnergy
  object PhantomMembrane extends Material(tier = T2) with ItemProperty
  object PhantomSpawnEgg extends Material with SpawnEgg
  object PigSpawnEgg extends Material with SpawnEgg
  object PillagerSpawnEgg extends Material with SpawnEgg
  object PinkBanner extends Material with Banner
  object PinkBed extends Material with Bed
  object PinkCarpet extends Material with Carpet
  object PinkConcrete extends Material with Concrete
  object PinkConcretePowder extends Material with ConcretePowder
  object PinkDye extends Material with Dye
  object PinkGlass extends Material with Glass
  object PinkGlassPane extends Material with GlassPane
  object PinkGlazedTerracotta extends Material with GlazedTerracotta
  object PinkShulkerBox extends Material with ShulkerBox
  object PinkTerracotta extends Material with DyedTerracotta
  object PinkTulip extends Material(energy = 14) with Plant
  object PinkWallBanner extends Material with WallBanner
  object PinkWool extends Material with Wool
  object Piston extends Material with BreakableBlockProperty with Solid { override protected val minimumTool = None }
  object PistonHead extends Material with BlockProperty with Solid with NoEnergy
  object PistonMovingPiece extends Material with BlockProperty with Solid with NoEnergy
  object PlayerHead extends Material with FloorHead with NoEnergy
  object PlayerWallHead extends Material with WallHead with NoEnergy
  object Podzol extends Material(tier = T0) with BreakableBlockProperty with Solid { override protected val minimumTool = Some(WoodenShovel) }
  object PoisonousPotato extends Material with ItemProperty with PlayerConsumable
  object PolarBearSpawnEgg extends Material with SpawnEgg
  object PolishedAndesite extends Material with BreakableBlockProperty with Solid { override protected val minimumTool = Some(WoodenPickaxe) }
  object PolishedAndesiteSlab extends Material with Slab
  object PolishedAndesiteStairs extends Material with Stairs
  object PolishedDiorite extends Material with BreakableBlockProperty with Solid { override protected val minimumTool = Some(WoodenPickaxe) }
  object PolishedDioriteSlab extends Material with Slab
  object PolishedDioriteStairs extends Material with Stairs
  object PolishedGranite extends Material with BreakableBlockProperty with Solid { override protected val minimumTool = Some(WoodenPickaxe) }
  object PolishedGraniteSlab extends Material with Slab
  object PolishedGraniteStairs extends Material with Stairs
  object PoppedChorusFruit extends Material with ItemProperty
  object Poppy extends Material(energy = 14) with Plant
  object Potato extends Material(energy = 5498) with ItemProperty with PlayerConsumable
  object Potatoes extends Material(tier = T2) with Plant
  object Potion extends Material with ItemProperty with PlayerConsumable with NoEnergy
  object PottedAcaciaSapling extends Material with PottedPlant
  object PottedAllium extends Material with PottedPlant
  object PottedAzureBluet extends Material with PottedPlant
  object PottedBamboo extends Material with PottedPlant
  object PottedBirchSapling extends Material with PottedPlant
  object PottedBlueOrchid extends Material with PottedPlant
  object PottedBrownMushroom extends Material with PottedPlant
  object PottedCactus extends Material with PottedPlant
  object PottedCornflower extends Material with PottedPlant
  object PottedDandelion extends Material with PottedPlant
  object PottedDarkOakSapling extends Material with PottedPlant
  object PottedDeadBush extends Material with PottedPlant
  object PottedFern extends Material with PottedPlant
  object PottedJungleSapling extends Material with PottedPlant
  object PottedLilyOfTheValley extends Material with PottedPlant
  object PottedOakSapling extends Material with PottedPlant
  object PottedOrangeTulip extends Material with PottedPlant
  object PottedOxeyeDaisy extends Material with PottedPlant
  object PottedPinkTulip extends Material with PottedPlant
  object PottedPoppy extends Material with PottedPlant
  object PottedRedMushroom extends Material with PottedPlant
  object PottedRedTulip extends Material with PottedPlant
  object PottedSpruceSapling extends Material with PottedPlant
  object PottedWhiteTulip extends Material with PottedPlant
  object PottedWitherRose extends Material with PottedPlant
  object PoweredRail extends Material with Rail
  object Prismarine extends Material with BreakableBlockProperty with Solid { override protected val minimumTool = Some(WoodenPickaxe) }
  object PrismarineBricks extends Material with BreakableBlockProperty with Solid { override protected val minimumTool = Some(WoodenPickaxe) }
  object PrismarineBrickSlab extends Material with Slab
  object PrismarineBrickStairs extends Material with Stairs
  object PrismarineCrystals extends Material(energy =   5332) with ItemProperty
  object PrismarineShard extends Material(energy = 469) with ItemProperty
  object PrismarineSlab extends Material with Slab
  object PrismarineStairs extends Material with Stairs
  object PrismarineWall extends Material with Wall
  object Pufferfish extends Material(energy = 3903) with ItemProperty with PlayerConsumable
  object PufferfishBucket extends Material with ItemProperty
  object PufferfishSpawnEgg extends Material with SpawnEgg
  object Pumpkin extends Material(tier = T3) with BreakableBlockProperty with Solid { override protected val minimumTool = Some(WoodenAxe) }
  object PumpkinPie extends Material with ItemProperty with PlayerConsumable
  object PumpkinSeeds extends Material with ItemProperty
  object PumpkinStem extends Material(tier = T2) with Plant with FiveRotations
  object PurpleBanner extends Material with Banner
  object PurpleBed extends Material with Bed
  object PurpleCarpet extends Material with Carpet
  object PurpleConcrete extends Material with Concrete
  object PurpleConcretePowder extends Material with ConcretePowder
  object PurpleDye extends Material with Dye
  object PurpleGlass extends Material with Glass
  object PurpleGlassPane extends Material with GlassPane
  object PurpleGlazedTerracotta extends Material with GlazedTerracotta
  object PurpleShulkerBox extends Material with ShulkerBox
  object PurpleTerracotta extends Material with DyedTerracotta
  object PurpleWallBanner extends Material with WallBanner
  object PurpleWool extends Material with Wool
  object PurpurBlock extends Material(tier = T4) with BreakableBlockProperty with Solid { override protected val minimumTool = Some(WoodenPickaxe) }
  object PurpurPillar extends Material(tier = T4) with BreakableBlockProperty with Solid { override protected val minimumTool = Some(WoodenPickaxe) }
  object PurpurSlab extends Material with Slab
  object PurpurStairs extends Material with Stairs
  object Quartz extends Material(tier = T3) with ItemProperty
  object QuartzBlock extends Material with BreakableBlockProperty with Solid { override protected val minimumTool = Some(WoodenPickaxe) }
  object QuartzOre extends Material with Ore
  object QuartzPillar extends Material with BreakableBlockProperty with Solid { override protected val minimumTool = Some(WoodenPickaxe) }
  object QuartzSlab extends Material with Slab
  object QuartzStairs extends Material with Stairs
  object RabbitFoot extends Material(energy = 11) with ItemProperty
  object RabbitHide extends Material(tier = T0) with ItemProperty
  object RabbitSpawnEgg extends Material with SpawnEgg
  object RabbitStew extends Material with ItemProperty with PlayerConsumable
  object Rail extends Material with Rail
  object RavagerSpawnEgg extends Material with SpawnEgg
  object RawBeef extends Material(tier = T0) with ItemProperty
  object RawChicken extends Material(tier = T0) with ItemProperty with PlayerConsumable
  object RawMutton extends Material(tier = T0) with ItemProperty
  object RawPorkchop extends Material(tier = T0) with ItemProperty with PlayerConsumable
  object RawRabbit extends Material(tier = T0) with ItemProperty with PlayerConsumable
  object RawSalmon extends Material(energy = 43) with ItemProperty with PlayerConsumable
  object RedBanner extends Material with Banner
  object RedBed extends Material with Bed
  object RedCarpet extends Material with Carpet
  object RedConcrete extends Material with Concrete
  object RedConcretePowder extends Material with ConcretePowder
  object RedDye extends Material with Dye
  object RedGlass extends Material with Glass
  object RedGlassPane extends Material with GlassPane
  object RedGlazedTerracotta extends Material with GlazedTerracotta
  object RedMushroom extends Material(energy = 159) with Plant
  object RedMushroomBlock extends Material(tier = T3) with BreakableBlockProperty with Solid { override protected val minimumTool = Some(WoodenAxe) }
  object RedNetherBricks extends Material with BreakableBlockProperty with Solid { override protected val minimumTool = Some(WoodenPickaxe) }
  object RedNetherBrickSlab extends Material with Slab
  object RedNetherBrickStairs extends Material with Stairs
  object RedNetherBrickWall extends Material with Wall
  object RedSand extends Material(tier = T0) with BreakableBlockProperty with Solid with Gravity { override protected val minimumTool = Some(WoodenShovel) }
  object RedSandstone extends Material with BreakableBlockProperty with Solid { override protected val minimumTool = Some(WoodenPickaxe) }
  object RedSandstoneSlab extends Material with Slab
  object RedSandstoneStairs extends Material with Stairs
  object RedSandstoneWall extends Material with Wall
  object RedShulkerBox extends Material with ShulkerBox
  object Redstone extends Material(energy = 35) with ItemProperty
  object RedstoneBlock extends Material with BreakableBlockProperty with Solid { override protected val minimumTool = Some(WoodenPickaxe) }
  object RedstoneComparator extends Material with ItemProperty
  object RedstoneLamp extends Material with BreakableBlockProperty with Solid { override protected val minimumTool = None }
  object RedstoneOre extends Material with Ore
  object RedstoneTorch extends Material with Torch with FiveRotations
  object RedstoneWallTorch extends Material with Torch
  object RedstoneWire extends Material with BreakableBlockProperty with Transparent with Attaches { override protected val minimumTool = None }
  object RedTerracotta extends Material with DyedTerracotta
  object RedTulip extends Material(energy = 14) with Plant
  object RedWallBanner extends Material with WallBanner
  object RedWool extends Material with Wool
  object Repeater extends Material with ItemProperty
  object RoseBush extends Material(tier = T2) with DoublePlant
  object RottenFlesh extends Material(energy = 10) with ItemProperty with PlayerConsumable
  object Saddle extends Material(tier = T3) with ItemProperty
  object SalmonBucket extends Material with ItemProperty
  object SalmonSpawnEgg extends Material with SpawnEgg
  object Sand extends Material(tier = T0) with BreakableBlockProperty with Solid with Gravity { override protected val minimumTool = None }
  object Sandstone extends Material with BreakableBlockProperty with Solid { override protected val minimumTool = Some(WoodenPickaxe) }
  object SandstoneSlab extends Material with Slab
  object SandstoneStairs extends Material with Stairs
  object SandstoneWall extends Material with Wall
  object Scaffolding extends Material with BreakableBlockProperty with Transparent { override protected val minimumTool = None }
  object Scute extends Material(tier = T2) with ItemProperty
  object Seagrass extends Material(14) with Plant
  object SeaLantern extends Material with BreakableBlockProperty with Solid { override protected val minimumTool = None }
  object SeaPickle extends Material(tier = T2) with BreakableBlockProperty with Attaches { override protected val minimumTool = None }
  object Shears extends Material with Shears with Ferrous
  object SheepSpawnEgg extends Material with SpawnEgg
  object Shield extends Material with ItemProperty with Durable with Usable
  object ShulkerBox extends Material with ShulkerBox
  object ShulkerShell extends Material(energy = 951) with ItemProperty
  object ShulkerSpawnEgg extends Material with SpawnEgg
  object SilverfishSpawnEgg extends Material with SpawnEgg
  object SkeletonHorseSpawnEgg extends Material with SpawnEgg
  object SkeletonSkull extends Material(tier = T5) with FloorHead
  object SkeletonSpawnEgg extends Material with SpawnEgg
  object SkeletonWallSkull extends Material with WallHead
  object SkullBannerPattern extends Material with BannerPattern
  object SlimeBall extends Material(energy = 2) with ItemProperty
  object SlimeBlock extends Material with BreakableBlockProperty with Solid { override protected val minimumTool = None }
  object SlimeSpawnEgg extends Material with SpawnEgg
  object SmithingTable extends Material with BreakableBlockProperty with Solid { override protected val minimumTool = Some(WoodenAxe) }
  object Smoker extends Material with BreakableBlockProperty with Solid { override protected val minimumTool = Some(WoodenPickaxe) }
  object SmoothQuartz extends Material with BreakableBlockProperty with Solid { override protected val minimumTool = Some(WoodenPickaxe) }
  object SmoothQuartzSlab extends Material with Slab
  object SmoothQuartzStairs extends Material with Stairs
  object SmoothRedSandstone extends Material with BreakableBlockProperty with Solid { override protected val minimumTool = Some(WoodenPickaxe) }
  object SmoothRedSandstoneSlab extends Material with Slab
  object SmoothRedSandstoneStairs extends Material with Stairs
  object SmoothSandstone extends Material with BreakableBlockProperty with Solid { override protected val minimumTool = Some(WoodenPickaxe) }
  object SmoothSandstoneSlab extends Material with Slab
  object SmoothSandstoneStairs extends Material with Stairs
  object SmoothStone extends Material with BreakableBlockProperty with Solid { override protected val minimumTool = Some(WoodenPickaxe) }
  object SmoothStoneSlab extends Material with Slab
  object Snow extends Material(tier = T0) with BreakableBlockProperty with Transparent with Attaches with Crushable { override protected val minimumTool = Some(WoodenShovel) }
  object Snowball extends Material(tier = T0) with ItemProperty
  object SnowBlock extends Material(tier = T0) with BreakableBlockProperty with Solid { override protected val minimumTool = Some(WoodenShovel) }
  object SoulSand extends Material with BreakableBlockProperty with Solid { override protected val minimumTool = Some(WoodenShovel) }
  object Spawner extends Material(tier = T6) with BreakableBlockProperty with Solid { override protected val minimumTool = Some(WoodenPickaxe) }
  object SpectralArrow extends Material with ItemProperty with NoEnergy
  object SpiderEye extends Material(energy = 35) with ItemProperty with PlayerConsumable
  object SpiderSpawnEgg extends Material with SpawnEgg
  object SplashPotion extends Material with ItemProperty with NoEnergy
  object Sponge extends Material(energy = 210) with BreakableBlockProperty with Solid { override protected val minimumTool = None }
  object SpruceBoat extends Material with Boat
  object SpruceButton extends Material with WoodenButton
  object SpruceDoor extends Material with Door
  object SpruceFence extends Material with Fence
  object SpruceFenceGate extends Material with FenceGate
  object SpruceLeaves extends Material with Leaves
  object SpruceLog extends Material with Log
  object SprucePlanks extends Material with Plank
  object SprucePressurePlate extends Material with WoodenPressurePlate
  object SpruceSapling extends Material with Sapling
  object SpruceSign extends Material with Sign
  object SpruceSlab extends Material with WoodenSlab
  object SpruceStairs extends Material with WoodenStairs
  object SpruceTrapdoor extends Material with Door
  object SpruceWallSign extends Material with WallSign
  object SpruceWood extends Material with Wood
  object SquidSpawnEgg extends Material with SpawnEgg
  object Stick extends Material(energy = 31) with ItemProperty with Fuel { override val burnTicks: Int = 100 }
  object StickyPiston extends Material with BreakableBlockProperty with Solid { override protected val minimumTool = None }
  object Stone extends Material(tier = T0) with BreakableBlockProperty with Solid { override protected val minimumTool = Some(WoodenPickaxe) }
  object StoneAxe extends Material with Axe with Stony
  object StoneBrick extends Material(tier = T0) with BreakableBlockProperty with Solid { override protected val minimumTool = Some(WoodenPickaxe) }
  object StoneBrickSlab extends Material with Slab
  object StoneBrickStairs extends Material with Stairs
  object StoneBrickWall extends Material with Wall
  object StoneButton extends Material with Button
  object Stonecutter extends Material with BreakableBlockProperty with Solid { override protected val minimumTool = Some(WoodenPickaxe) }
  object StoneHoe extends Material with Hoe with Stony
  object StonePickaxe extends Material with Pickaxe with Stony
  object StonePressurePlate extends Material with PressurePlate
  object StoneShovel extends Material with Shovel with Stony
  object StoneSlab extends Material with Slab
  object StoneStairs extends Material with Stairs
  object StoneSword extends Material(tier = T3) with Sword with Stony
  object StraySpawnEgg extends Material with SpawnEgg
  object String extends Material(energy = 9) with ItemProperty
  object StrippedAcaciaLog extends Material with StrippedLog
  object StrippedAcaciaWood extends Material with Wood
  object StrippedBirchLog extends Material with StrippedLog
  object StrippedBirchWood extends Material with Wood
  object StrippedDarkOakLog extends Material with StrippedLog
  object StrippedDarkOakWood extends Material with Wood
  object StrippedJungleLog extends Material with StrippedLog
  object StrippedJungleWood extends Material with Wood
  object StrippedOakLog extends Material with StrippedLog
  object StrippedOakWood extends Material with Wood
  object StrippedSpruceLog extends Material with StrippedLog
  object StrippedSpruceWood extends Material with Wood
  object StructureBlock extends Material with BlockProperty with Inventory with NoEnergy
  object StructureVoid extends Material with BlockProperty with Solid with NoEnergy
  object Sugar extends Material(energy = 35) with ItemProperty
  object SugarCane extends Material(tier = T2) with ItemProperty
  object Sunflower extends Material(tier = T2) with DoublePlant
  object SuspiciousStew extends Material with ItemProperty with PlayerConsumable
  object SweetBerries extends Material with ItemProperty with PlayerConsumable
  object SweetBerryBush extends Material(tier = T2) with Plant
  object TallGrass extends Material(tier = T0) with DoublePlant
  object TallSeagrass extends Material(tier = T0) with DoublePlant
  object Terracotta extends Material with Terracotta
  object TippedArrow extends Material with ItemProperty with NoEnergy
  object TNT extends Material with BreakableBlockProperty with Solid { override protected val minimumTool = None }
  object TNTMinecart extends Material with ItemProperty
  object Torch extends Material with Torch with Crushable with FiveRotations
  object TotemOfUndying extends Material(energy = 488) with ItemProperty
  object TraderLlamaSpawnEgg extends Material with SpawnEgg
  object TrappedChest extends Material with GenericChest
  object Trident extends Material(tier = T2) with Weapon
  object Tripwire extends Material with BreakableBlockProperty with Transparent with Attaches { override protected val minimumTool = None }
  object TripwireHook extends Material with BreakableBlockProperty with Transparent with Attaches with FourRotations { override protected val minimumTool = None }
  object TropicalFish extends Material(3910) with ItemProperty with PlayerConsumable
  object TropicalFishBucket extends Material with ItemProperty
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
  object Vine extends Material(tier = T5) with BreakableBlockProperty with Transparent with Crushable { override protected val minimumTool = Some(Shears) }
  object VoidAir extends Material with Air
  object WallTorch extends Material with Torch
  object WanderingTraderSpawnEgg extends Material with SpawnEgg
  object Water extends Material(energy = 14) with BlockProperty with Liquid
  object WaterBucket extends Material with ItemProperty
  object WetSponge extends Material(energy = 18538) with BreakableBlockProperty with Solid { override protected val minimumTool = None }
  object Wheat extends Material(energy = 69) with ItemProperty with AnimalConsumable
  object WheatSeeds extends Material(tier = T2) with ItemProperty with AnimalConsumable
  object WhiteBanner extends Material with Banner
  object WhiteBed extends Material with Bed
  object WhiteCarpet extends Material with Carpet
  object WhiteConcrete extends Material with Concrete
  object WhiteConcretePowder extends Material with ConcretePowder
  object WhiteDye extends Material with Dye
  object WhiteGlass extends Material with Glass
  object WhiteGlassPane extends Material with GlassPane
  object WhiteGlazedTerracotta extends Material with GlazedTerracotta
  object WhiteShulkerBox extends Material with ShulkerBox
  object WhiteTerracotta extends Material with DyedTerracotta
  object WhiteTulip extends Material(energy = 14) with Plant
  object WhiteWallBanner extends Material with WallBanner
  object WhiteWool extends Material with Wool
  object WitchSpawnEgg extends Material with SpawnEgg
  object WitherRose extends Material(tier = T5) with Plant
  object WitherSkeletonSkull extends Material(tier = T3) with FloorHead
  object WitherSkeletonSpawnEgg extends Material with SpawnEgg
  object WitherSkeletonWallSkull extends Material with WallHead
  object WolfSpawnEgg extends Material with SpawnEgg
  object WoodenAxe extends Material with Axe with Wooden
  object WoodenHoe extends Material with Hoe with Wooden
  object WoodenPickaxe extends Material with Pickaxe with Wooden
  object WoodenShovel extends Material with Shovel with Wooden
  object WoodenSword extends Material with Sword with Wooden
  object WritableBook extends Material with ItemProperty with Usable
  object WrittenBook extends Material with ItemProperty with Usable with NoEnergy
  object YellowBanner extends Material with Banner
  object YellowBed extends Material with Bed
  object YellowCarpet extends Material with Carpet
  object YellowConcrete extends Material with Concrete
  object YellowConcretePowder extends Material with ConcretePowder
  object YellowDye extends Material with Dye
  object YellowGlass extends Material with Glass
  object YellowGlassPane extends Material with GlassPane
  object YellowGlazedTerracotta extends Material with GlazedTerracotta
  object YellowShulkerBox extends Material with ShulkerBox
  object YellowTerracotta extends Material with DyedTerracotta
  object YellowWallBanner extends Material with WallBanner
  object YellowWool extends Material with Wool
  object ZombieHead extends Material(tier = T5) with FloorHead
  object ZombieHorseSpawnEgg extends Material with SpawnEgg
  object ZombiePigmanSpawnEgg extends Material with SpawnEgg
  object ZombieSpawnEgg extends Material with SpawnEgg
  object ZombieVillagerSpawnEgg extends Material with SpawnEgg
  object ZombieWallHead extends Material with WallHead
  //Special Recipe Materials
  object Milk extends Material(energy = (Recipe.foodQuality * 10).toInt) with ItemProperty //This energy is the food value of a milk bucket
  //@formatter:on
  //endregion
}