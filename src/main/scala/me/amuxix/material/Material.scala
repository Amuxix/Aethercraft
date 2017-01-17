package me.amuxix.material

import enumeratum._
import me.amuxix.material.generics._
import org.bukkit.DyeColor._
import org.bukkit.GrassSpecies._
import org.bukkit.Material.{TNT => BTNT, _}
import org.bukkit.SandstoneType._
import org.bukkit.TreeSpecies._
import org.bukkit.block.{Block => BBlock}
import org.bukkit.inventory.ItemStack
import org.bukkit.material._
import org.bukkit.{CoalType, Material => BMaterial}

import scala.collection.immutable.{HashMap, IndexedSeq}
import scala.math.{E, log}

/**
  * Created by Amuxix on 04/01/2017.
  */

sealed abstract case class Material(var energy: Option[Int] = None) extends EnumEntry {
  def this(energy: Int) = this(Some(energy))
  def this(tierString: String) = this(Math.pow(E * 2, tierString.substring(1).toDouble).round.toInt)
  lazy val tier: Option[Int] = {
    energy match {
      case Some(e) => Some((log(e) / log(E * 2)).round.toInt)
      case None => None
    }
  }

  val name: String = getClass.getSimpleName.split("\\$").last

  override def toString: String = s"$name(${energy.getOrElse(-1)})"

  override def hashCode(): Int = name.hashCode()

  override def equals(that: Any): Boolean = {
    that match {
      case s: Material =>
        name.equalsIgnoreCase(s.name)
      case _ =>
        false
    }
  }
}

//Generated from https://docs.google.com/spreadsheets/d/1h1gsL9rj9zVvhGUXnsIEpS1RtxSquCgItGQEmeubiTM/edit#gid=0

//TODO: Add Potions

object Material extends Enum[Material] {
  implicit def bukkitBlock2Material(bBlock: BBlock): Material = {
    getMaterial(bBlock.getState.getType, bBlock.getState.getData)
  }

  implicit def bukkitStack2Material(stack: ItemStack): Material = {
    getMaterial(stack.getType, stack.getData)
  }

  def getMaterial(bMaterial: BMaterial, data: MaterialData): Material = {
    val nullMaterialData = new MaterialData(AIR)
    val materials: HashMap[(BMaterial, MaterialData), Material] = HashMap(
      (AIR, nullMaterialData) -> Air,
      (STONE, new MaterialData(STONE, 0.toByte)) -> Stone,
      (STONE, new MaterialData(STONE, 1.toByte)) -> Granite,
      (STONE, new MaterialData(STONE, 2.toByte)) -> PolishedGranite,
      (STONE, new MaterialData(STONE, 3.toByte)) -> Diorite,
      (STONE, new MaterialData(STONE, 4.toByte)) -> PolishedDiorite,
      (STONE, new MaterialData(STONE, 5.toByte)) -> Andesite,
      (STONE, new MaterialData(STONE, 6.toByte)) -> PolishedAndesite,
      (GRASS, nullMaterialData) -> Grass,
      (DIRT, new MaterialData(DIRT, 0.toByte)) -> Dirt,
      (DIRT, new MaterialData(DIRT, 1.toByte)) -> CoarseDirt,
      (DIRT, new MaterialData(DIRT, 2.toByte)) -> Podzol,
      (COBBLESTONE, nullMaterialData) -> Cobblestone,
      (WOOD, new Wood(GENERIC)) -> OakWoodPlanks,
      (WOOD, new Wood(REDWOOD)) -> SpruceWoodPlanks,
      (WOOD, new Wood(BIRCH)) -> BirchWoodPlanks,
      (WOOD, new Wood(JUNGLE)) -> JungleWoodPlanks,
      (WOOD, new Wood(ACACIA)) -> AcaciaWoodPlanks,
      (WOOD, new Wood(DARK_OAK)) -> DarkOakWoodPlanks,
      (SAPLING, new Sapling(GENERIC)) -> OakSapling,
      (SAPLING, new Sapling(REDWOOD)) -> SpruceSapling,
      (SAPLING, new Sapling(BIRCH)) -> BirchSapling,
      (SAPLING, new Sapling(JUNGLE)) -> JungleSapling,
      (SAPLING, new Sapling(ACACIA)) -> AcaciaSapling,
      (SAPLING, new Sapling(DARK_OAK)) -> DarkOakSapling,
      (BEDROCK, nullMaterialData) -> Bedrock,
      (WATER, nullMaterialData) -> Water,
      (STATIONARY_WATER, nullMaterialData) -> StationaryWater,
      (LAVA, nullMaterialData) -> Lava,
      (STATIONARY_LAVA, nullMaterialData) -> StationaryLava,
      (SAND, nullMaterialData) -> Sand,
      (GRAVEL, nullMaterialData) -> Gravel,
      (GOLD_ORE, nullMaterialData) -> GoldOre,
      (IRON_ORE, nullMaterialData) -> IronOre,
      (COAL_ORE, nullMaterialData) -> CoalOre,
      (LOG, new Tree(GENERIC)) -> OakLog,
      (LOG, new Tree(REDWOOD)) -> SpruceLog,
      (LOG, new Tree(BIRCH)) -> BirchLog,
      (LOG, new Tree(JUNGLE)) -> JungleLog,
      (LEAVES, new Leaves(GENERIC)) -> OakLeaves,
      (LEAVES, new Leaves(REDWOOD)) -> SpruceLeaves,
      (LEAVES, new Leaves(BIRCH)) -> BirchLeaves,
      (LEAVES, new Leaves(JUNGLE)) -> JungleLeaves,
      (SPONGE, new MaterialData(SPONGE, 0.toByte)) -> Sponge,
      (SPONGE, new MaterialData(SPONGE, 1.toByte)) -> WetSponge,
      (GLASS, nullMaterialData) -> Glass,
      (LAPIS_ORE, nullMaterialData) -> LapisLazuliOre,
      (LAPIS_BLOCK, nullMaterialData) -> LapisLazuliBlock,
      (DISPENSER, nullMaterialData) -> Dispenser,
      (SANDSTONE, new Sandstone(CRACKED)) -> Sandstone,
      (SANDSTONE, new Sandstone(SMOOTH)) -> SmoothSandstone,
      (SANDSTONE, new Sandstone(GLYPHED)) -> ChiseledSandstone,
      (NOTE_BLOCK, nullMaterialData) -> NoteBlock,
      (BED_BLOCK, nullMaterialData) -> BedBlock,
      (POWERED_RAIL, nullMaterialData) -> PoweredRails,
      (DETECTOR_RAIL, nullMaterialData) -> DetectorRails,
      (PISTON_STICKY_BASE, nullMaterialData) -> StickyPiston,
      (WEB, nullMaterialData) -> Cobweb,
      (LONG_GRASS, new LongGrass(DEAD)) -> Shrub,
      (LONG_GRASS, new LongGrass(NORMAL)) -> LongGrass,
      (LONG_GRASS, new LongGrass(FERN_LIKE)) -> Fern,
      (DEAD_BUSH, nullMaterialData) -> DeadBush,
      (PISTON_BASE, nullMaterialData) -> Piston,
      (PISTON_EXTENSION, nullMaterialData) -> PistonExtension,
      (WOOL, new Wool(WHITE)) -> WhiteWool,
      (WOOL, new Wool(ORANGE)) -> OrangeWool,
      (WOOL, new Wool(MAGENTA)) -> MagentaWool,
      (WOOL, new Wool(LIGHT_BLUE)) -> LightBlueWool,
      (WOOL, new Wool(YELLOW)) -> YellowWool,
      (WOOL, new Wool(LIME)) -> LimeWool,
      (WOOL, new Wool(PINK)) -> PinkWool,
      (WOOL, new Wool(GRAY)) -> GrayWool,
      (WOOL, new Wool(SILVER)) -> LightGrayWool,
      (WOOL, new Wool(CYAN)) -> CyanWool,
      (WOOL, new Wool(PURPLE)) -> PurpleWool,
      (WOOL, new Wool(BLUE)) -> BlueWool,
      (WOOL, new Wool(BROWN)) -> BrownWool,
      (WOOL, new Wool(GREEN)) -> GreenWool,
      (WOOL, new Wool(RED)) -> RedWool,
      (WOOL, new Wool(BLACK)) -> BlackWool,
      (PISTON_MOVING_PIECE, nullMaterialData) -> PistonMovingPiece,
      (YELLOW_FLOWER, nullMaterialData) -> Dandelion,
      (RED_ROSE, new MaterialData(RED_ROSE, 0.toByte)) -> Poppy,
      (RED_ROSE, new MaterialData(RED_ROSE, 1.toByte)) -> BlueOrchid,
      (RED_ROSE, new MaterialData(RED_ROSE, 2.toByte)) -> Allium,
      (RED_ROSE, new MaterialData(RED_ROSE, 3.toByte)) -> AzureBluet,
      (RED_ROSE, new MaterialData(RED_ROSE, 4.toByte)) -> RedTulip,
      (RED_ROSE, new MaterialData(RED_ROSE, 5.toByte)) -> OrangeTulip,
      (RED_ROSE, new MaterialData(RED_ROSE, 6.toByte)) -> WhiteTulip,
      (RED_ROSE, new MaterialData(RED_ROSE, 7.toByte)) -> PinkTulip,
      (RED_ROSE, new MaterialData(RED_ROSE, 8.toByte)) -> OxeyeDaisy,
      (BROWN_MUSHROOM, nullMaterialData) -> BrownMushroom,
      (RED_MUSHROOM, nullMaterialData) -> RedMushroom,
      (GOLD_BLOCK, nullMaterialData) -> GoldBlock,
      (IRON_BLOCK, nullMaterialData) -> IronBlock,
      (DOUBLE_STEP, new MaterialData(DOUBLE_STEP, 0.toByte)) -> StoneDoubleSlab,
      (DOUBLE_STEP, new MaterialData(DOUBLE_STEP, 1.toByte)) -> SandstoneDoubleSlab,
      (DOUBLE_STEP, new MaterialData(DOUBLE_STEP, 2.toByte)) -> OldWoodDoubleSlab,
      (DOUBLE_STEP, new MaterialData(DOUBLE_STEP, 3.toByte)) -> CobblestoneDoubleSlab,
      (DOUBLE_STEP, new MaterialData(DOUBLE_STEP, 4.toByte)) -> BrickDoubleSlab,
      (DOUBLE_STEP, new MaterialData(DOUBLE_STEP, 5.toByte)) -> StoneBrickDoubleSlab,
      (DOUBLE_STEP, new MaterialData(DOUBLE_STEP, 6.toByte)) -> NetherBrickDoubleSlab,
      (DOUBLE_STEP, new MaterialData(DOUBLE_STEP, 7.toByte)) -> QuartzDoubleSlab,
      (STEP, new MaterialData(STEP, 0.toByte)) -> StoneSingleSlab,
      (STEP, new MaterialData(STEP, 1.toByte)) -> SandstoneSingleSlab,
      (STEP, new MaterialData(STEP, 2.toByte)) -> OldWoodSingleSlab,
      (STEP, new MaterialData(STEP, 3.toByte)) -> CobblestoneSingleSlab,
      (STEP, new MaterialData(STEP, 4.toByte)) -> BrickSingleSlab,
      (STEP, new MaterialData(STEP, 5.toByte)) -> StoneBrickSingleSlab,
      (STEP, new MaterialData(STEP, 6.toByte)) -> NetherBrickSingleSlab,
      (STEP, new MaterialData(STEP, 7.toByte)) -> QuartzSingleSlab,
      (BRICK, nullMaterialData) -> BrickBlock,
      (BTNT, nullMaterialData) -> TNT,
      (BOOKSHELF, nullMaterialData) -> Bookshelf,
      (MOSSY_COBBLESTONE, nullMaterialData) -> MossyCobblestone,
      (OBSIDIAN, nullMaterialData) -> Obsidian,
      (TORCH, nullMaterialData) -> Torch,
      (FIRE, nullMaterialData) -> Fire,
      (MOB_SPAWNER, nullMaterialData) -> MobSpawner,
      (WOOD_STAIRS, nullMaterialData) -> OakStairs,
      (CHEST, nullMaterialData) -> Chest,
      (REDSTONE_WIRE, nullMaterialData) -> RedstoneWire,
      (DIAMOND_ORE, nullMaterialData) -> DiamondOre,
      (DIAMOND_BLOCK, nullMaterialData) -> DiamondBlock,
      (WORKBENCH, nullMaterialData) -> CraftingTable,
      (CROPS, nullMaterialData) -> Crops,
      (SOIL, nullMaterialData) -> Soil,
      (FURNACE, nullMaterialData) -> Furnace,
      (BURNING_FURNACE, nullMaterialData) -> BurningFurnace,
      (SIGN_POST, nullMaterialData) -> SignPost,
      (WOODEN_DOOR, nullMaterialData) -> OakDoor,
      (LADDER, nullMaterialData) -> Ladder,
      (RAILS, nullMaterialData) -> Rails,
      (COBBLESTONE_STAIRS, nullMaterialData) -> CobblestoneStairs,
      (WALL_SIGN, nullMaterialData) -> WallSign,
      (LEVER, nullMaterialData) -> Lever,
      (STONE_PLATE, nullMaterialData) -> StonePressurePlate,
      (IRON_DOOR_BLOCK, nullMaterialData) -> IronDoorBlock,
      (WOOD_PLATE, nullMaterialData) -> WoodPressurePlate,
      (REDSTONE_ORE, nullMaterialData) -> RedstoneOre,
      (GLOWING_REDSTONE_ORE, nullMaterialData) -> GlowingRedstoneOre,
      (REDSTONE_TORCH_OFF, nullMaterialData) -> RedstoneTorchOff,
      (REDSTONE_TORCH_ON, nullMaterialData) -> RedstoneTorchOn,
      (STONE_BUTTON, nullMaterialData) -> StoneButton,
      (SNOW, nullMaterialData) -> Snow,
      (ICE, nullMaterialData) -> Ice,
      (SNOW_BLOCK, nullMaterialData) -> SnowBlock,
      (CACTUS, nullMaterialData) -> Cactus,
      (CLAY, nullMaterialData) -> Clay,
      (SUGAR_CANE_BLOCK, nullMaterialData) -> SugarCaneBlock,
      (JUKEBOX, nullMaterialData) -> Jukebox,
      (FENCE, nullMaterialData) -> OakFence,
      (PUMPKIN, nullMaterialData) -> Pumpkin,
      (NETHERRACK, nullMaterialData) -> Netherrack,
      (SOUL_SAND, nullMaterialData) -> SoulSand,
      (GLOWSTONE, nullMaterialData) -> Glowstone,
      (PORTAL, nullMaterialData) -> Portal,
      (JACK_O_LANTERN, nullMaterialData) -> JackOLantern,
      (CAKE_BLOCK, nullMaterialData) -> CakeBlock,
      (DIODE_BLOCK_OFF, nullMaterialData) -> RedstoneRepeaterOff,
      (DIODE_BLOCK_ON, nullMaterialData) -> RedstoneRepeaterOn,
      (STAINED_GLASS, new MaterialData(STAINED_GLASS, 0.toByte)) -> WhiteGlass,
      (STAINED_GLASS, new MaterialData(STAINED_GLASS, 1.toByte)) -> OrangeGlass,
      (STAINED_GLASS, new MaterialData(STAINED_GLASS, 2.toByte)) -> MagentaGlass,
      (STAINED_GLASS, new MaterialData(STAINED_GLASS, 3.toByte)) -> LightBlueGlass,
      (STAINED_GLASS, new MaterialData(STAINED_GLASS, 4.toByte)) -> YellowGlass,
      (STAINED_GLASS, new MaterialData(STAINED_GLASS, 5.toByte)) -> LimeGlass,
      (STAINED_GLASS, new MaterialData(STAINED_GLASS, 6.toByte)) -> PinkGlass,
      (STAINED_GLASS, new MaterialData(STAINED_GLASS, 7.toByte)) -> GrayGlass,
      (STAINED_GLASS, new MaterialData(STAINED_GLASS, 8.toByte)) -> LightGrayGlass,
      (STAINED_GLASS, new MaterialData(STAINED_GLASS, 9.toByte)) -> CyanGlass,
      (STAINED_GLASS, new MaterialData(STAINED_GLASS, 10.toByte)) -> PurpleGlass,
      (STAINED_GLASS, new MaterialData(STAINED_GLASS, 11.toByte)) -> BlueGlass,
      (STAINED_GLASS, new MaterialData(STAINED_GLASS, 12.toByte)) -> BrownGlass,
      (STAINED_GLASS, new MaterialData(STAINED_GLASS, 13.toByte)) -> GreenGlass,
      (STAINED_GLASS, new MaterialData(STAINED_GLASS, 14.toByte)) -> RedGlass,
      (STAINED_GLASS, new MaterialData(STAINED_GLASS, 15.toByte)) -> BlackGlass,
      (TRAP_DOOR, nullMaterialData) -> TrapDoor,
      (MONSTER_EGGS, nullMaterialData) -> MonsterEggs,
      (SMOOTH_BRICK, new MaterialData(SMOOTH_BRICK, 0.toByte)) -> StoneBrick,
      (SMOOTH_BRICK, new MaterialData(SMOOTH_BRICK, 1.toByte)) -> CrackedStoneBrick,
      (SMOOTH_BRICK, new MaterialData(SMOOTH_BRICK, 2.toByte)) -> MossyStoneBrick,
      (SMOOTH_BRICK, new MaterialData(SMOOTH_BRICK, 3.toByte)) -> ChiseledStoneBrick,
      (HUGE_MUSHROOM_1, nullMaterialData) -> HugeMushroom1,
      (HUGE_MUSHROOM_2, nullMaterialData) -> HugeMushroom2,
      (IRON_FENCE, nullMaterialData) -> IronFence,
      (THIN_GLASS, nullMaterialData) -> GlassPane,
      (MELON_BLOCK, nullMaterialData) -> MelonBlock,
      (PUMPKIN_STEM, nullMaterialData) -> PumpkinStem,
      (MELON_STEM, nullMaterialData) -> MelonStem,
      (VINE, nullMaterialData) -> Vine,
      (FENCE_GATE, nullMaterialData) -> OakFenceGate,
      (BRICK_STAIRS, nullMaterialData) -> BrickStairs,
      (SMOOTH_STAIRS, nullMaterialData) -> StoneBrickStairs,
      (MYCEL, nullMaterialData) -> Mycelium,
      (WATER_LILY, nullMaterialData) -> WaterLily,
      (NETHER_BRICK, nullMaterialData) -> NetherBrick,
      (NETHER_FENCE, nullMaterialData) -> NetherBrickFence,
      (NETHER_BRICK_STAIRS, nullMaterialData) -> NetherBrickStairs,
      (NETHER_WARTS, nullMaterialData) -> NetherWarts,
      (ENCHANTMENT_TABLE, nullMaterialData) -> EnchantmentTable,
      (BREWING_STAND, nullMaterialData) -> BrewingStand,
      (CAULDRON, nullMaterialData) -> Cauldron,
      (ENDER_PORTAL, nullMaterialData) -> EnderPortal,
      (ENDER_PORTAL_FRAME, nullMaterialData) -> EnderPortalFrame,
      (ENDER_STONE, nullMaterialData) -> EndStone,
      (DRAGON_EGG, nullMaterialData) -> DragonEgg,
      (REDSTONE_LAMP_OFF, nullMaterialData) -> RedstoneLampOff,
      (REDSTONE_LAMP_ON, nullMaterialData) -> RedstoneLampOn,
      (WOOD_DOUBLE_STEP, new Wood(GENERIC)) -> OakDoubleSlab,
      (WOOD_DOUBLE_STEP, new Wood(REDWOOD)) -> SpruceDoubleSlab,
      (WOOD_DOUBLE_STEP, new Wood(BIRCH)) -> BirchDoubleSlab,
      (WOOD_DOUBLE_STEP, new Wood(JUNGLE)) -> JungleDoubleSlab,
      (WOOD_DOUBLE_STEP, new Wood(ACACIA)) -> AcaciaDoubleSlab,
      (WOOD_DOUBLE_STEP, new Wood(DARK_OAK)) -> DarkOakDoubleSlab,
      (WOOD_STEP, new WoodenStep(GENERIC)) -> OakSingleSlab,
      (WOOD_STEP, new WoodenStep(REDWOOD)) -> SpruceSingleSlab,
      (WOOD_STEP, new WoodenStep(BIRCH)) -> BirchSingleSlab,
      (WOOD_STEP, new WoodenStep(JUNGLE)) -> JungleSingleSlab,
      (WOOD_STEP, new WoodenStep(ACACIA)) -> AcaciaSingleSlab,
      (WOOD_STEP, new WoodenStep(DARK_OAK)) -> DarkOakSingleSlab,
      (COCOA, nullMaterialData) -> Cocoa,
      (SANDSTONE_STAIRS, nullMaterialData) -> SandstoneStairs,
      (EMERALD_ORE, nullMaterialData) -> EmeraldOre,
      (ENDER_CHEST, nullMaterialData) -> EnderChest,
      (TRIPWIRE_HOOK, nullMaterialData) -> TripwireHook,
      (TRIPWIRE, nullMaterialData) -> Tripwire,
      (EMERALD_BLOCK, nullMaterialData) -> EmeraldBlock,
      (SPRUCE_WOOD_STAIRS, nullMaterialData) -> SpruceStairs,
      (BIRCH_WOOD_STAIRS, nullMaterialData) -> BirchStairs,
      (JUNGLE_WOOD_STAIRS, nullMaterialData) -> JungleStairs,
      (COMMAND, nullMaterialData) -> Command,
      (BEACON, nullMaterialData) -> Beacon,
      (COBBLE_WALL, nullMaterialData) -> CobblestoneWall,
      (FLOWER_POT, nullMaterialData) -> FlowerPot,
      (CARROT, nullMaterialData) -> Carrot,
      (POTATO, nullMaterialData) -> Potato,
      (WOOD_BUTTON, nullMaterialData) -> WoodButton,
      (SKULL, nullMaterialData) -> Skull,
      (ANVIL, nullMaterialData) -> Anvil,
      (TRAPPED_CHEST, nullMaterialData) -> TrappedChest,
      (GOLD_PLATE, nullMaterialData) -> GoldPressurePlate,
      (IRON_PLATE, nullMaterialData) -> IronPressurePlate,
      (REDSTONE_COMPARATOR_OFF, nullMaterialData) -> RedstoneComparatorOff,
      (REDSTONE_COMPARATOR_ON, nullMaterialData) -> RedstoneComparatorOn,
      (DAYLIGHT_DETECTOR, nullMaterialData) -> DaylightSensor,
      (REDSTONE_BLOCK, nullMaterialData) -> RedstoneBlock,
      (QUARTZ_ORE, nullMaterialData) -> QuartzOre,
      (HOPPER, nullMaterialData) -> Hopper,
      (QUARTZ_BLOCK, new MaterialData(QUARTZ_BLOCK, 0.toByte)) -> QuartzBlock,
      (QUARTZ_BLOCK, new MaterialData(QUARTZ_BLOCK, 1.toByte)) -> ChiseledQuartzBlock,
      (QUARTZ_BLOCK, new MaterialData(QUARTZ_BLOCK, 2.toByte)) -> PillarQuartzBlock,
      (QUARTZ_STAIRS, nullMaterialData) -> QuartzStairs,
      (ACTIVATOR_RAIL, nullMaterialData) -> ActivatorRails,
      (DROPPER, nullMaterialData) -> Dropper,
      (STAINED_CLAY, new MaterialData(STAINED_CLAY, 0.toByte)) -> WhiteHardenedClay,
      (STAINED_CLAY, new MaterialData(STAINED_CLAY, 1.toByte)) -> OrangeHardenedClay,
      (STAINED_CLAY, new MaterialData(STAINED_CLAY, 2.toByte)) -> MagentaHardenedClay,
      (STAINED_CLAY, new MaterialData(STAINED_CLAY, 3.toByte)) -> LightBlueHardenedClay,
      (STAINED_CLAY, new MaterialData(STAINED_CLAY, 4.toByte)) -> YellowHardenedClay,
      (STAINED_CLAY, new MaterialData(STAINED_CLAY, 5.toByte)) -> LimeHardenedClay,
      (STAINED_CLAY, new MaterialData(STAINED_CLAY, 6.toByte)) -> PinkHardenedClay,
      (STAINED_CLAY, new MaterialData(STAINED_CLAY, 7.toByte)) -> GrayHardenedClay,
      (STAINED_CLAY, new MaterialData(STAINED_CLAY, 8.toByte)) -> LightGrayHardenedClay,
      (STAINED_CLAY, new MaterialData(STAINED_CLAY, 9.toByte)) -> CyanHardenedClay,
      (STAINED_CLAY, new MaterialData(STAINED_CLAY, 10.toByte)) -> PurpleHardenedClay,
      (STAINED_CLAY, new MaterialData(STAINED_CLAY, 11.toByte)) -> BlueHardenedClay,
      (STAINED_CLAY, new MaterialData(STAINED_CLAY, 12.toByte)) -> BrownHardenedClay,
      (STAINED_CLAY, new MaterialData(STAINED_CLAY, 13.toByte)) -> GreenHardenedClay,
      (STAINED_CLAY, new MaterialData(STAINED_CLAY, 14.toByte)) -> RedHardenedClay,
      (STAINED_CLAY, new MaterialData(STAINED_CLAY, 15.toByte)) -> BlackHardenedClay,
      (STAINED_GLASS_PANE, new MaterialData(STAINED_GLASS_PANE, 0.toByte)) -> WhiteGlassPane,
      (STAINED_GLASS_PANE, new MaterialData(STAINED_GLASS_PANE, 1.toByte)) -> OrangeGlassPane,
      (STAINED_GLASS_PANE, new MaterialData(STAINED_GLASS_PANE, 2.toByte)) -> MagentaGlassPane,
      (STAINED_GLASS_PANE, new MaterialData(STAINED_GLASS_PANE, 3.toByte)) -> LightBlueGlassPane,
      (STAINED_GLASS_PANE, new MaterialData(STAINED_GLASS_PANE, 4.toByte)) -> YellowGlassPane,
      (STAINED_GLASS_PANE, new MaterialData(STAINED_GLASS_PANE, 5.toByte)) -> LimeGlassPane,
      (STAINED_GLASS_PANE, new MaterialData(STAINED_GLASS_PANE, 6.toByte)) -> PinkGlassPane,
      (STAINED_GLASS_PANE, new MaterialData(STAINED_GLASS_PANE, 7.toByte)) -> GrayGlassPane,
      (STAINED_GLASS_PANE, new MaterialData(STAINED_GLASS_PANE, 8.toByte)) -> LightGrayGlassPane,
      (STAINED_GLASS_PANE, new MaterialData(STAINED_GLASS_PANE, 9.toByte)) -> CyanGlassPane,
      (STAINED_GLASS_PANE, new MaterialData(STAINED_GLASS_PANE, 10.toByte)) -> PurpleGlassPane,
      (STAINED_GLASS_PANE, new MaterialData(STAINED_GLASS_PANE, 11.toByte)) -> BlueGlassPane,
      (STAINED_GLASS_PANE, new MaterialData(STAINED_GLASS_PANE, 12.toByte)) -> BrownGlassPane,
      (STAINED_GLASS_PANE, new MaterialData(STAINED_GLASS_PANE, 13.toByte)) -> GreenGlassPane,
      (STAINED_GLASS_PANE, new MaterialData(STAINED_GLASS_PANE, 14.toByte)) -> RedGlassPane,
      (STAINED_GLASS_PANE, new MaterialData(STAINED_GLASS_PANE, 15.toByte)) -> BlackGlassPane,
      (LEAVES_2, new Leaves(ACACIA)) -> AcaciaLeaves,
      (LEAVES_2, new Leaves(DARK_OAK)) -> DarkOakLeaves,
      (LOG_2, new Tree(ACACIA)) -> AcaciaLog,
      (LOG_2, new Tree(DARK_OAK)) -> DarkOakLog,
      (ACACIA_STAIRS, nullMaterialData) -> AcaciaStairs,
      (DARK_OAK_STAIRS, nullMaterialData) -> DarkOakStairs,
      (SLIME_BLOCK, nullMaterialData) -> SlimeBlock,
      (BARRIER, nullMaterialData) -> Barrier,
      (IRON_TRAPDOOR, nullMaterialData) -> IronTrapdoor,
      (PRISMARINE, new MaterialData(PRISMARINE, 0.toByte)) -> Prismarine,
      (PRISMARINE, new MaterialData(PRISMARINE, 1.toByte)) -> PrismarineBrick,
      (PRISMARINE, new MaterialData(PRISMARINE, 2.toByte)) -> DarkPrismarine,
      (SEA_LANTERN, nullMaterialData) -> SeaLantern,
      (HAY_BLOCK, nullMaterialData) -> HayBale,
      (CARPET, new MaterialData(CARPET, 0.toByte)) -> WhiteCarpet,
      (CARPET, new MaterialData(CARPET, 1.toByte)) -> OrangeCarpet,
      (CARPET, new MaterialData(CARPET, 2.toByte)) -> MagentaCarpet,
      (CARPET, new MaterialData(CARPET, 3.toByte)) -> LightBlueCarpet,
      (CARPET, new MaterialData(CARPET, 4.toByte)) -> YellowCarpet,
      (CARPET, new MaterialData(CARPET, 5.toByte)) -> LimeCarpet,
      (CARPET, new MaterialData(CARPET, 6.toByte)) -> PinkCarpet,
      (CARPET, new MaterialData(CARPET, 7.toByte)) -> GrayCarpet,
      (CARPET, new MaterialData(CARPET, 8.toByte)) -> LightGrayCarpet,
      (CARPET, new MaterialData(CARPET, 9.toByte)) -> CyanCarpet,
      (CARPET, new MaterialData(CARPET, 10.toByte)) -> PurpleCarpet,
      (CARPET, new MaterialData(CARPET, 11.toByte)) -> BlueCarpet,
      (CARPET, new MaterialData(CARPET, 12.toByte)) -> BrownCarpet,
      (CARPET, new MaterialData(CARPET, 13.toByte)) -> GreenCarpet,
      (CARPET, new MaterialData(CARPET, 14.toByte)) -> RedCarpet,
      (CARPET, new MaterialData(CARPET, 15.toByte)) -> BlackCarpet,
      (HARD_CLAY, nullMaterialData) -> HardenedClay,
      (COAL_BLOCK, nullMaterialData) -> CoalBlock,
      (PACKED_ICE, nullMaterialData) -> PackedIce,
      (DOUBLE_PLANT, new MaterialData(DOUBLE_PLANT, 0.toByte)) -> DoublePlant,
      (DOUBLE_PLANT, new MaterialData(DOUBLE_PLANT, 1.toByte)) -> Sunflower,
      (DOUBLE_PLANT, new MaterialData(DOUBLE_PLANT, 2.toByte)) -> Lilac,
      (DOUBLE_PLANT, new MaterialData(DOUBLE_PLANT, 3.toByte)) -> DoubleTallgrass,
      (DOUBLE_PLANT, new MaterialData(DOUBLE_PLANT, 4.toByte)) -> LargeFern,
      (DOUBLE_PLANT, new MaterialData(DOUBLE_PLANT, 5.toByte)) -> RoseBush,
      (DOUBLE_PLANT, new MaterialData(DOUBLE_PLANT, 6.toByte)) -> Peony,
      (DOUBLE_PLANT, new MaterialData(DOUBLE_PLANT, 7.toByte)) -> TopPlantHalf,
      (STANDING_BANNER, nullMaterialData) -> StandingBanner,
      (WALL_BANNER, nullMaterialData) -> WallBanner,
      (DAYLIGHT_DETECTOR_INVERTED, nullMaterialData) -> InvertedDaylightSensor,
      (RED_SANDSTONE, new MaterialData(RED_SANDSTONE, 0.toByte)) -> RedSandstone,
      (RED_SANDSTONE, new MaterialData(RED_SANDSTONE, 1.toByte)) -> SmoothRedSandstone,
      (RED_SANDSTONE, new MaterialData(RED_SANDSTONE, 2.toByte)) -> ChiseledRedSandstone,
      (RED_SANDSTONE_STAIRS, nullMaterialData) -> RedSandstoneStairs,
      (DOUBLE_STONE_SLAB2, nullMaterialData) -> RedSandstoneDoubleSlab,
      (STONE_SLAB2, nullMaterialData) -> RedSandstoneSingleSlab,
      (SPRUCE_FENCE_GATE, nullMaterialData) -> SpruceFenceGate,
      (BIRCH_FENCE_GATE, nullMaterialData) -> BirchFenceGate,
      (JUNGLE_FENCE_GATE, nullMaterialData) -> JungleFenceGate,
      (DARK_OAK_FENCE_GATE, nullMaterialData) -> DarkOakFenceGate,
      (ACACIA_FENCE_GATE, nullMaterialData) -> AcaciaFenceGate,
      (SPRUCE_FENCE, nullMaterialData) -> SpruceFence,
      (BIRCH_FENCE, nullMaterialData) -> BirchFence,
      (JUNGLE_FENCE, nullMaterialData) -> JungleFence,
      (DARK_OAK_FENCE, nullMaterialData) -> DarkOakFence,
      (ACACIA_FENCE, nullMaterialData) -> AcaciaFence,
      (SPRUCE_DOOR, nullMaterialData) -> SpruceDoor,
      (BIRCH_DOOR, nullMaterialData) -> BirchDoor,
      (JUNGLE_DOOR, nullMaterialData) -> JungleDoor,
      (ACACIA_DOOR, nullMaterialData) -> AcaciaDoor,
      (DARK_OAK_DOOR, nullMaterialData) -> DarkOakDoor,
      (END_ROD, nullMaterialData) -> EndRod,
      (CHORUS_PLANT, nullMaterialData) -> ChorusPlant,
      (CHORUS_FLOWER, nullMaterialData) -> ChorusFlower,
      (PURPUR_BLOCK, nullMaterialData) -> PurpurBlock,
      (PURPUR_PILLAR, nullMaterialData) -> PurpurPillar,
      (PURPUR_STAIRS, nullMaterialData) -> PurpurStairs,
      (PURPUR_DOUBLE_SLAB, nullMaterialData) -> PurpurDoubleSlab,
      (PURPUR_SLAB, nullMaterialData) -> PurpurSingleSlab,
      (END_BRICKS, nullMaterialData) -> EndStoneBricks,
      (BEETROOT_BLOCK, nullMaterialData) -> BeetrootPlantation,
      (GRASS_PATH, nullMaterialData) -> GrassPath,
      (END_GATEWAY, nullMaterialData) -> EndGateway,
      (COMMAND_REPEATING, nullMaterialData) -> CommandRepeating,
      (COMMAND_CHAIN, nullMaterialData) -> CommandChain,
      (FROSTED_ICE, nullMaterialData) -> FrostedIce,
      (MAGMA, nullMaterialData) -> Magma,
      (NETHER_WART_BLOCK, nullMaterialData) -> NetherWartBlock,
      (RED_NETHER_BRICK, nullMaterialData) -> RedNetherBrick,
      (BONE_BLOCK, nullMaterialData) -> BoneBlock,
      (STRUCTURE_VOID, nullMaterialData) -> StructureVoid,
      (OBSERVER, nullMaterialData) -> Observer,
      (WHITE_SHULKER_BOX, nullMaterialData) -> WhiteShulkerBox,
      (ORANGE_SHULKER_BOX, nullMaterialData) -> OrangeShulkerBox,
      (MAGENTA_SHULKER_BOX, nullMaterialData) -> MagentaShulkerBox,
      (LIGHT_BLUE_SHULKER_BOX, nullMaterialData) -> LightBlueShulkerBox,
      (YELLOW_SHULKER_BOX, nullMaterialData) -> YellowShulkerBox,
      (LIME_SHULKER_BOX, nullMaterialData) -> LimeShulkerBox,
      (PINK_SHULKER_BOX, nullMaterialData) -> PinkShulkerBox,
      (GRAY_SHULKER_BOX, nullMaterialData) -> GrayShulkerBox,
      (SILVER_SHULKER_BOX, nullMaterialData) -> LightGrayShulkerBox,
      (CYAN_SHULKER_BOX, nullMaterialData) -> CyanShulkerBox,
      (PURPLE_SHULKER_BOX, nullMaterialData) -> PurpleShulkerBox,
      (BLUE_SHULKER_BOX, nullMaterialData) -> BlueShulkerBox,
      (BROWN_SHULKER_BOX, nullMaterialData) -> BrownShulkerBox,
      (GREEN_SHULKER_BOX, nullMaterialData) -> GreenShulkerBox,
      (RED_SHULKER_BOX, nullMaterialData) -> RedShulkerBox,
      (BLACK_SHULKER_BOX, nullMaterialData) -> BlackShulkerBox,
      (STRUCTURE_BLOCK, new MaterialData(STRUCTURE_BLOCK, 0.toByte)) -> StructureSaveBlock,
      (STRUCTURE_BLOCK, new MaterialData(STRUCTURE_BLOCK, 1.toByte)) -> StructureLoadBlock,
      (STRUCTURE_BLOCK, new MaterialData(STRUCTURE_BLOCK, 2.toByte)) -> StructureCornerBlock,
      (STRUCTURE_BLOCK, new MaterialData(STRUCTURE_BLOCK, 3.toByte)) -> StructureDataBlock,
      (IRON_SPADE, nullMaterialData) -> IronShovel,
      (IRON_PICKAXE, nullMaterialData) -> IronPickaxe,
      (IRON_AXE, nullMaterialData) -> IronAxe,
      (FLINT_AND_STEEL, nullMaterialData) -> FlintAndSteel,
      (APPLE, nullMaterialData) -> Apple,
      (BOW, nullMaterialData) -> Bow,
      (ARROW, nullMaterialData) -> Arrow,
      (COAL, new Coal(CoalType.COAL)) -> Coal,
      (COAL, new Coal(CoalType.CHARCOAL)) -> Charcoal,
      (DIAMOND, nullMaterialData) -> Diamond,
      (IRON_INGOT, nullMaterialData) -> IronIngot,
      (GOLD_INGOT, nullMaterialData) -> GoldIngot,
      (IRON_SWORD, nullMaterialData) -> IronSword,
      (WOOD_SWORD, nullMaterialData) -> WoodSword,
      (WOOD_SPADE, nullMaterialData) -> WoodShovel,
      (WOOD_PICKAXE, nullMaterialData) -> WoodPickaxe,
      (WOOD_AXE, nullMaterialData) -> WoodAxe,
      (STONE_SWORD, nullMaterialData) -> StoneSword,
      (STONE_SPADE, nullMaterialData) -> StoneShovel,
      (STONE_PICKAXE, nullMaterialData) -> StonePickaxe,
      (STONE_AXE, nullMaterialData) -> StoneAxe,
      (DIAMOND_SWORD, nullMaterialData) -> DiamondSword,
      (DIAMOND_SPADE, nullMaterialData) -> DiamondShovel,
      (DIAMOND_PICKAXE, nullMaterialData) -> DiamondPickaxe,
      (DIAMOND_AXE, nullMaterialData) -> DiamondAxe,
      (STICK, nullMaterialData) -> Stick,
      (BOWL, nullMaterialData) -> Bowl,
      (MUSHROOM_SOUP, nullMaterialData) -> MushroomStew,
      (GOLD_SWORD, nullMaterialData) -> GoldSword,
      (GOLD_SPADE, nullMaterialData) -> GoldShovel,
      (GOLD_PICKAXE, nullMaterialData) -> GoldPickaxe,
      (GOLD_AXE, nullMaterialData) -> GoldAxe,
      (STRING, nullMaterialData) -> String,
      (FEATHER, nullMaterialData) -> Feather,
      (SULPHUR, nullMaterialData) -> GunPowder,
      (WOOD_HOE, nullMaterialData) -> WoodHoe,
      (STONE_HOE, nullMaterialData) -> StoneHoe,
      (IRON_HOE, nullMaterialData) -> IronHoe,
      (DIAMOND_HOE, nullMaterialData) -> DiamondHoe,
      (GOLD_HOE, nullMaterialData) -> GoldHoe,
      (SEEDS, nullMaterialData) -> Seeds,
      (WHEAT, nullMaterialData) -> Wheat,
      (BREAD, nullMaterialData) -> Bread,
      (LEATHER_HELMET, nullMaterialData) -> LeatherHelmet,
      (LEATHER_CHESTPLATE, nullMaterialData) -> LeatherChestplate,
      (LEATHER_LEGGINGS, nullMaterialData) -> LeatherLeggings,
      (LEATHER_BOOTS, nullMaterialData) -> LeatherBoots,
      (CHAINMAIL_HELMET, nullMaterialData) -> ChainmailHelmet,
      (CHAINMAIL_CHESTPLATE, nullMaterialData) -> ChainmailChestplate,
      (CHAINMAIL_LEGGINGS, nullMaterialData) -> ChainmailLeggings,
      (CHAINMAIL_BOOTS, nullMaterialData) -> ChainmailBoots,
      (IRON_HELMET, nullMaterialData) -> IronHelmet,
      (IRON_CHESTPLATE, nullMaterialData) -> IronChestplate,
      (IRON_LEGGINGS, nullMaterialData) -> IronLeggings,
      (IRON_BOOTS, nullMaterialData) -> IronBoots,
      (DIAMOND_HELMET, nullMaterialData) -> DiamondHelmet,
      (DIAMOND_CHESTPLATE, nullMaterialData) -> DiamondChestplate,
      (DIAMOND_LEGGINGS, nullMaterialData) -> DiamondLeggings,
      (DIAMOND_BOOTS, nullMaterialData) -> DiamondBoots,
      (GOLD_HELMET, nullMaterialData) -> GoldHelmet,
      (GOLD_CHESTPLATE, nullMaterialData) -> GoldChestplate,
      (GOLD_LEGGINGS, nullMaterialData) -> GoldLeggings,
      (GOLD_BOOTS, nullMaterialData) -> GoldBoots,
      (FLINT, nullMaterialData) -> Flint,
      (PORK, nullMaterialData) -> RawPorkchop,
      (GRILLED_PORK, nullMaterialData) -> CookedPorkchop,
      (PAINTING, nullMaterialData) -> Painting,
      (GOLDEN_APPLE, new MaterialData(GOLDEN_APPLE, 0.toByte)) -> GoldenApple,
      (GOLDEN_APPLE, new MaterialData(GOLDEN_APPLE, 1.toByte)) -> EnchantedGoldenApple,
      (SIGN, nullMaterialData) -> Sign,
      (WOOD_DOOR, nullMaterialData) -> OakDoorItem,
      (BUCKET, nullMaterialData) -> Bucket,
      (WATER_BUCKET, nullMaterialData) -> WaterBucket,
      (LAVA_BUCKET, nullMaterialData) -> LavaBucket,
      (MINECART, nullMaterialData) -> Minecart,
      (SADDLE, nullMaterialData) -> Saddle,
      (IRON_DOOR, nullMaterialData) -> IronDoor,
      (REDSTONE, nullMaterialData) -> Redstone,
      (SNOW_BALL, nullMaterialData) -> SnowBall,
      (BOAT, nullMaterialData) -> OakBoat,
      (LEATHER, nullMaterialData) -> Leather,
      (MILK_BUCKET, nullMaterialData) -> MilkBucket,
      (CLAY_BRICK, nullMaterialData) -> ClayBrick,
      (CLAY_BALL, nullMaterialData) -> ClayBall,
      (SUGAR_CANE, nullMaterialData) -> SugarCane,
      (PAPER, nullMaterialData) -> Paper,
      (BOOK, nullMaterialData) -> Book,
      (SLIME_BALL, nullMaterialData) -> SlimeBall,
      (STORAGE_MINECART, nullMaterialData) -> StorageMinecart,
      (POWERED_MINECART, nullMaterialData) -> PoweredMinecart,
      (EGG, nullMaterialData) -> Egg,
      (COMPASS, nullMaterialData) -> Compass,
      (FISHING_ROD, nullMaterialData) -> FishingRod,
      (WATCH, nullMaterialData) -> Watch,
      (GLOWSTONE_DUST, nullMaterialData) -> GlowstoneDust,
      (RAW_FISH, new MaterialData(RAW_FISH, 0.toByte)) -> RawFish,
      (RAW_FISH, new MaterialData(RAW_FISH, 1.toByte)) -> RawSalmon,
      (RAW_FISH, new MaterialData(RAW_FISH, 2.toByte)) -> Clownfish,
      (RAW_FISH, new MaterialData(RAW_FISH, 3.toByte)) -> Pufferfish,
      (COOKED_FISH, new MaterialData(COOKED_FISH, 0.toByte)) -> CookedFish,
      (COOKED_FISH, new MaterialData(COOKED_FISH, 1.toByte)) -> CookedSalmon,
      (INK_SACK, new Dye(BLACK)) -> InkSack,
      (INK_SACK, new Dye(RED)) -> RoseRed,
      (INK_SACK, new Dye(GREEN)) -> CactusGreen,
      (INK_SACK, new Dye(BROWN)) -> CocoaBeans,
      (INK_SACK, new Dye(BLUE)) -> LapisLazuli,
      (INK_SACK, new Dye(PURPLE)) -> PurpleDye,
      (INK_SACK, new Dye(CYAN)) -> CyanDye,
      (INK_SACK, new Dye(SILVER)) -> LightGrayDye,
      (INK_SACK, new Dye(GRAY)) -> GrayDye,
      (INK_SACK, new Dye(PINK)) -> PinkDye,
      (INK_SACK, new Dye(LIME)) -> LimeDye,
      (INK_SACK, new Dye(YELLOW)) -> DandelionYellow,
      (INK_SACK, new Dye(LIGHT_BLUE)) -> LightBlueDye,
      (INK_SACK, new Dye(MAGENTA)) -> MagentaDye,
      (INK_SACK, new Dye(ORANGE)) -> OrangeDye,
      (INK_SACK, new Dye(WHITE)) -> BoneMeal,
      (BONE, nullMaterialData) -> Bone,
      (SUGAR, nullMaterialData) -> Sugar,
      (CAKE, nullMaterialData) -> Cake,
      (BED, nullMaterialData) -> Bed,
      (DIODE, nullMaterialData) -> RedstoneRepeater,
      (COOKIE, nullMaterialData) -> Cookie,
      (MAP, nullMaterialData) -> DrawnMap,
      (SHEARS, nullMaterialData) -> Shears,
      (MELON, nullMaterialData) -> Melon,
      (PUMPKIN_SEEDS, nullMaterialData) -> PumpkinSeeds,
      (MELON_SEEDS, nullMaterialData) -> MelonSeeds,
      (RAW_BEEF, nullMaterialData) -> RawBeef,
      (COOKED_BEEF, nullMaterialData) -> CookedBeef,
      (RAW_CHICKEN, nullMaterialData) -> RawChicken,
      (COOKED_CHICKEN, nullMaterialData) -> CookedChicken,
      (ROTTEN_FLESH, nullMaterialData) -> RottenFlesh,
      (ENDER_PEARL, nullMaterialData) -> EnderPearl,
      (BLAZE_ROD, nullMaterialData) -> BlazeRod,
      (GHAST_TEAR, nullMaterialData) -> GhastTear,
      (GOLD_NUGGET, nullMaterialData) -> GoldNugget,
      (NETHER_STALK, nullMaterialData) -> NetherWart,
      (POTION, nullMaterialData) -> Potion,
      (GLASS_BOTTLE, nullMaterialData) -> GlassBottle,
      (SPIDER_EYE, nullMaterialData) -> SpiderEye,
      (FERMENTED_SPIDER_EYE, nullMaterialData) -> FermentedSpiderEye,
      (BLAZE_POWDER, nullMaterialData) -> BlazePowder,
      (MAGMA_CREAM, nullMaterialData) -> MagmaCream,
      (BREWING_STAND_ITEM, nullMaterialData) -> BrewingStandItem,
      (CAULDRON_ITEM, nullMaterialData) -> CauldronItem,
      (EYE_OF_ENDER, nullMaterialData) -> EyeOfEnder,
      (SPECKLED_MELON, nullMaterialData) -> GlisteringMelon,
      (MONSTER_EGG, nullMaterialData) -> MonsterEgg,
      (EXP_BOTTLE, nullMaterialData) -> ExpBottle,
      (FIREBALL, nullMaterialData) -> FireCharge,
      (BOOK_AND_QUILL, nullMaterialData) -> BookAndQuill,
      (WRITTEN_BOOK, nullMaterialData) -> WrittenBook,
      (EMERALD, nullMaterialData) -> Emerald,
      (ITEM_FRAME, nullMaterialData) -> ItemFrame,
      (FLOWER_POT_ITEM, nullMaterialData) -> FlowerPotItem,
      (CARROT_ITEM, nullMaterialData) -> CarrotItem,
      (POTATO_ITEM, nullMaterialData) -> PotatoItem,
      (BAKED_POTATO, nullMaterialData) -> BakedPotato,
      (POISONOUS_POTATO, nullMaterialData) -> PoisonousPotato,
      (EMPTY_MAP, nullMaterialData) -> EmptyMap,
      (GOLDEN_CARROT, nullMaterialData) -> GoldenCarrot,
      (SKULL_ITEM, new MaterialData(SKULL_ITEM, 0.toByte)) -> SkeletonHead,
      (SKULL_ITEM, new MaterialData(SKULL_ITEM, 1.toByte)) -> WitherSkeletonHead,
      (SKULL_ITEM, new MaterialData(SKULL_ITEM, 2.toByte)) -> ZombieHead,
      (SKULL_ITEM, new MaterialData(SKULL_ITEM, 3.toByte)) -> PlayerHead,
      (SKULL_ITEM, new MaterialData(SKULL_ITEM, 4.toByte)) -> CreeperHead,
      (SKULL_ITEM, new MaterialData(SKULL_ITEM, 5.toByte)) -> DragonHead,
      (CARROT_STICK, nullMaterialData) -> CarrotStick,
      (NETHER_STAR, nullMaterialData) -> NetherStar,
      (PUMPKIN_PIE, nullMaterialData) -> PumpkinPie,
      (FIREWORK, nullMaterialData) -> FireworkRocket,
      (FIREWORK_CHARGE, nullMaterialData) -> FireworkStar,
      (ENCHANTED_BOOK, nullMaterialData) -> EnchantedBook,
      (REDSTONE_COMPARATOR, nullMaterialData) -> RedstoneComparator,
      (NETHER_BRICK_ITEM, nullMaterialData) -> NetherBrickItem,
      (QUARTZ, nullMaterialData) -> Quartz,
      (EXPLOSIVE_MINECART, nullMaterialData) -> ExplosiveMinecart,
      (HOPPER_MINECART, nullMaterialData) -> HopperMinecart,
      (PRISMARINE_SHARD, nullMaterialData) -> PrismarineShard,
      (PRISMARINE_CRYSTALS, nullMaterialData) -> PrismarineCrystals,
      (RABBIT, nullMaterialData) -> RawRabbit,
      (COOKED_RABBIT, nullMaterialData) -> CookedRabbit,
      (RABBIT_STEW, nullMaterialData) -> RabbitStew,
      (RABBIT_FOOT, nullMaterialData) -> RabbitFoot,
      (RABBIT_HIDE, nullMaterialData) -> RabbitHide,
      (ARMOR_STAND, nullMaterialData) -> ArmorStand,
      (IRON_BARDING, nullMaterialData) -> IronHorseArmor,
      (GOLD_BARDING, nullMaterialData) -> GoldHorseArmor,
      (DIAMOND_BARDING, nullMaterialData) -> DiamondHorseArmor,
      (LEASH, nullMaterialData) -> Leash,
      (NAME_TAG, nullMaterialData) -> NameTag,
      (COMMAND_MINECART, nullMaterialData) -> CommandMinecart,
      (MUTTON, nullMaterialData) -> RawMutton,
      (COOKED_MUTTON, nullMaterialData) -> CookedMutton,
      (BANNER, nullMaterialData) -> Banner,
      (END_CRYSTAL, nullMaterialData) -> EndCrystal,
      (SPRUCE_DOOR_ITEM, nullMaterialData) -> SpruceDoorItem,
      (BIRCH_DOOR_ITEM, nullMaterialData) -> BirchDoorItem,
      (JUNGLE_DOOR_ITEM, nullMaterialData) -> JungleDoorItem,
      (ACACIA_DOOR_ITEM, nullMaterialData) -> AcaciaDoorItem,
      (DARK_OAK_DOOR_ITEM, nullMaterialData) -> DarkOakDoorItem,
      (CHORUS_FRUIT, nullMaterialData) -> ChorusFruit,
      (CHORUS_FRUIT_POPPED, nullMaterialData) -> PoppedChorusFruit,
      (BEETROOT, nullMaterialData) -> Beetroot,
      (BEETROOT_SEEDS, nullMaterialData) -> BeetrootSeeds,
      (BEETROOT_SOUP, nullMaterialData) -> BeetrootSoup,
      (DRAGONS_BREATH, nullMaterialData) -> DragonsBreath,
      (SPLASH_POTION, nullMaterialData) -> SplashPotion,
      (SPECTRAL_ARROW, nullMaterialData) -> SpectralArrow,
      (TIPPED_ARROW, nullMaterialData) -> TippedArrow,
      (LINGERING_POTION, nullMaterialData) -> LingeringPotion,
      (SHIELD, nullMaterialData) -> Shield,
      (ELYTRA, nullMaterialData) -> Elytra,
      (BOAT_SPRUCE, nullMaterialData) -> SpruceBoat,
      (BOAT_BIRCH, nullMaterialData) -> BirchBoat,
      (BOAT_JUNGLE, nullMaterialData) -> JungleBoat,
      (BOAT_ACACIA, nullMaterialData) -> AcaciaBoat,
      (BOAT_DARK_OAK, nullMaterialData) -> DarkOakBoat,
      (TOTEM, nullMaterialData) -> TotemOfUndying,
      (SHULKER_SHELL, nullMaterialData) -> ShulkerShell,
      (IRON_NUGGET, nullMaterialData) -> IronNugget,
      (GOLD_RECORD, nullMaterialData) -> GoldRecord,
      (GREEN_RECORD, nullMaterialData) -> GreenRecord,
      (RECORD_3, nullMaterialData) -> Record3,
      (RECORD_4, nullMaterialData) -> Record4,
      (RECORD_5, nullMaterialData) -> Record5,
      (RECORD_6, nullMaterialData) -> Record6,
      (RECORD_7, nullMaterialData) -> Record7,
      (RECORD_8, nullMaterialData) -> Record8,
      (RECORD_9, nullMaterialData) -> Record9,
      (RECORD_10, nullMaterialData) -> Record10,
      (RECORD_11, nullMaterialData) -> Record11,
      (RECORD_12, nullMaterialData) -> Record12,
      //These Lists are used by recipes that can take any of the types, not all are used but they were generated automaticaly
      (STONE, new MaterialData(STONE, (-1).toByte)) -> Seq(Stone, Granite, PolishedGranite, Diorite, PolishedDiorite, Andesite, PolishedAndesite).minBy(_.energy),
      (DIRT, new MaterialData(DIRT, (-1).toByte)) -> Seq(Dirt, CoarseDirt, Podzol).minBy(_.energy),
      (WOOD, new MaterialData(WOOD, (-1).toByte)) -> Seq(OakWoodPlanks, SpruceWoodPlanks, BirchWoodPlanks, JungleWoodPlanks, AcaciaWoodPlanks, DarkOakWoodPlanks).minBy(_.energy),
      (SAPLING, new MaterialData(SAPLING, (-1).toByte)) -> Seq(OakSapling, SpruceSapling, BirchSapling, JungleSapling, AcaciaSapling, DarkOakSapling).minBy(_.energy),
      (LOG, new MaterialData(LOG, (-1).toByte)) -> Seq(OakLog, SpruceLog, BirchLog, JungleLog).minBy(_.energy),
      (LEAVES, new MaterialData(LEAVES, (-1).toByte)) -> Seq(OakLeaves, SpruceLeaves, BirchLeaves, JungleLeaves).minBy(_.energy),
      (SPONGE, new MaterialData(SPONGE, (-1).toByte)) -> Seq(Sponge, WetSponge).minBy(_.energy),
      (SANDSTONE, new MaterialData(SANDSTONE, (-1).toByte)) -> Seq(Sandstone, SmoothSandstone, ChiseledSandstone).minBy(_.energy),
      (LONG_GRASS, new MaterialData(LONG_GRASS, (-1).toByte)) -> Seq(Shrub, LongGrass, Fern).minBy(_.energy),
      (WOOL, new MaterialData(WOOL, (-1).toByte)) -> Seq(WhiteWool, OrangeWool, MagentaWool, LightBlueWool, YellowWool, LimeWool, PinkWool, GrayWool, LightGrayWool, CyanWool, PurpleWool, BlueWool, BrownWool, GreenWool, RedWool, BlackWool).minBy(_.energy),
      (RED_ROSE, new MaterialData(RED_ROSE, (-1).toByte)) -> Seq(Poppy, BlueOrchid, Allium, AzureBluet, RedTulip, OrangeTulip, WhiteTulip, PinkTulip, OxeyeDaisy).minBy(_.energy),
      (DOUBLE_STEP, new MaterialData(DOUBLE_STEP, (-1).toByte)) -> Seq(StoneDoubleSlab, SandstoneDoubleSlab, OldWoodDoubleSlab, CobblestoneDoubleSlab, BrickDoubleSlab, StoneBrickDoubleSlab, NetherBrickDoubleSlab, QuartzDoubleSlab).minBy(_.energy),
      (STEP, new MaterialData(STEP, (-1).toByte)) -> Seq(StoneSingleSlab, SandstoneSingleSlab, OldWoodSingleSlab, CobblestoneSingleSlab, BrickSingleSlab, StoneBrickSingleSlab, NetherBrickSingleSlab, QuartzSingleSlab).minBy(_.energy),
      (STAINED_GLASS, new MaterialData(STAINED_GLASS, (-1).toByte)) -> Seq(WhiteGlass, OrangeGlass, MagentaGlass, LightBlueGlass, YellowGlass, LimeGlass, PinkGlass, GrayGlass, LightGrayGlass, CyanGlass, PurpleGlass, BlueGlass, BrownGlass, GreenGlass, RedGlass, BlackGlass).minBy(_.energy),
      (SMOOTH_BRICK, new MaterialData(SMOOTH_BRICK, (-1).toByte)) -> Seq(StoneBrick, CrackedStoneBrick, MossyStoneBrick, ChiseledStoneBrick).minBy(_.energy),
      (WOOD_DOUBLE_STEP, new MaterialData(WOOD_DOUBLE_STEP, (-1).toByte)) -> Seq(OakDoubleSlab, SpruceDoubleSlab, BirchDoubleSlab, JungleDoubleSlab, AcaciaDoubleSlab, DarkOakDoubleSlab).minBy(_.energy),
      (WOOD_STEP, new MaterialData(WOOD_STEP, (-1).toByte)) -> Seq(OakSingleSlab, SpruceSingleSlab, BirchSingleSlab, JungleSingleSlab, AcaciaSingleSlab, DarkOakSingleSlab).minBy(_.energy),
      (QUARTZ_BLOCK, new MaterialData(QUARTZ_BLOCK, (-1).toByte)) -> Seq(QuartzBlock, ChiseledQuartzBlock, PillarQuartzBlock).minBy(_.energy),
      (STAINED_CLAY, new MaterialData(STAINED_CLAY, (-1).toByte)) -> Seq(WhiteHardenedClay, OrangeHardenedClay, MagentaHardenedClay, LightBlueHardenedClay, YellowHardenedClay, LimeHardenedClay, PinkHardenedClay, GrayHardenedClay, LightGrayHardenedClay, CyanHardenedClay, PurpleHardenedClay, BlueHardenedClay, BrownHardenedClay, GreenHardenedClay, RedHardenedClay, BlackHardenedClay).minBy(_.energy),
      (STAINED_GLASS_PANE, new MaterialData(STAINED_GLASS_PANE, (-1).toByte)) -> Seq(WhiteGlassPane, OrangeGlassPane, MagentaGlassPane, LightBlueGlassPane, YellowGlassPane, LimeGlassPane, PinkGlassPane, GrayGlassPane, LightGrayGlassPane, CyanGlassPane, PurpleGlassPane, BlueGlassPane, BrownGlassPane, GreenGlassPane, RedGlassPane, BlackGlassPane).minBy(_.energy),
      (LEAVES_2, new MaterialData(LEAVES_2, (-1).toByte)) -> Seq(AcaciaLeaves, DarkOakLeaves).minBy(_.energy),
      (LOG_2, new MaterialData(LOG_2, (-1).toByte)) -> Seq(AcaciaLog, DarkOakLog).minBy(_.energy),
      (PRISMARINE, new MaterialData(PRISMARINE, (-1).toByte)) -> Seq(Prismarine, PrismarineBrick, DarkPrismarine).minBy(_.energy),
      (CARPET, new MaterialData(CARPET, (-1).toByte)) -> Seq(WhiteCarpet, OrangeCarpet, MagentaCarpet, LightBlueCarpet, YellowCarpet, LimeCarpet, PinkCarpet, GrayCarpet, LightGrayCarpet, CyanCarpet, PurpleCarpet, BlueCarpet, BrownCarpet, GreenCarpet, RedCarpet, BlackCarpet).minBy(_.energy),
      (DOUBLE_PLANT, new MaterialData(DOUBLE_PLANT, (-1).toByte)) -> Seq(DoublePlant, Sunflower, Lilac, DoubleTallgrass, LargeFern, RoseBush, Peony, TopPlantHalf).minBy(_.energy),
      (RED_SANDSTONE, new MaterialData(RED_SANDSTONE, (-1).toByte)) -> Seq(RedSandstone, SmoothRedSandstone, ChiseledRedSandstone).minBy(_.energy),
      (STRUCTURE_BLOCK, new MaterialData(STRUCTURE_BLOCK, (-1).toByte)) -> Seq(StructureSaveBlock, StructureLoadBlock, StructureCornerBlock, StructureDataBlock).minBy(_.energy),
      (COAL, new MaterialData(COAL, (-1).toByte)) -> Seq(Coal, Charcoal).minBy(_.energy),
      (GOLDEN_APPLE, new MaterialData(GOLDEN_APPLE, (-1).toByte)) -> Seq(GoldenApple, EnchantedGoldenApple).minBy(_.energy),
      (RAW_FISH, new MaterialData(RAW_FISH, (-1).toByte)) -> Seq(RawFish, RawSalmon, Clownfish, Pufferfish).minBy(_.energy),
      (COOKED_FISH, new MaterialData(COOKED_FISH, (-1).toByte)) -> Seq(CookedFish, CookedSalmon).minBy(_.energy),
      (INK_SACK, new MaterialData(INK_SACK, (-1).toByte)) -> Seq(InkSack, RoseRed, CactusGreen, CocoaBeans, LapisLazuli, PurpleDye, CyanDye, LightGrayDye, GrayDye, PinkDye, LimeDye, DandelionYellow, LightBlueDye, MagentaDye, OrangeDye, BoneMeal).minBy(_.energy),
      (SKULL_ITEM, new MaterialData(SKULL_ITEM, (-1).toByte)) -> Seq(SkeletonHead, WitherSkeletonHead, ZombieHead, PlayerHead, CreeperHead, DragonHead).minBy(_.energy)
    )
    (materials.get((bMaterial, data)) orElse materials.get((bMaterial, nullMaterialData))).get
    /*try {
      bMaterial match {
        case AIR => Air
        case STONE => data.getData match {
          case 0 => Stone
          case 1 => Granite
          case 2 => PolishedGranite
          case 3 => Diorite
          case 4 => PolishedDiorite
          case 5 => Andesite
          case 6 => PolishedAndesite
          case _ => Seq(Stone, Granite, MossyStoneBrick, PolishedGranite, Diorite, PolishedDiorite, Andesite, PolishedAndesite).minBy(_.energy)
        }
        case GRASS => Grass
        case DIRT => data.getData match {
          case 0 => Dirt
          case 1 => CoarseDirt
          case 2 => Podzol
          case _ => Seq(Dirt, CoarseDirt, Podzol).minBy(_.energy)
        }
        case COBBLESTONE => Cobblestone
        case WOOD => data.asInstanceOf[Wood].getSpecies match {
          case GENERIC => OakWoodPlanks
          case REDWOOD => SpruceWoodPlanks
          case BIRCH => BirchWoodPlanks
          case JUNGLE => JungleWoodPlanks
          case ACACIA => AcaciaWoodPlanks
          case DARK_OAK => DarkOakWoodPlanks
          case _ => Seq(OakWoodPlanks, SpruceWoodPlanks, BirchWoodPlanks, JungleWoodPlanks, AcaciaWoodPlanks, DarkOakWoodPlanks).minBy(_.energy)
        }
        case SAPLING => data.asInstanceOf[Sapling].getSpecies match {
          case GENERIC => OakSapling
          case REDWOOD => SpruceSapling
          case BIRCH => BirchSapling
          case JUNGLE => JungleSapling
          case ACACIA => AcaciaSapling
          case DARK_OAK => DarkOakSapling
          case _ => Seq(OakSapling, SpruceSapling, BirchSapling, JungleSapling, AcaciaSapling, DarkOakSapling).minBy(_.energy)
        }
        case BEDROCK => Bedrock
        case WATER => Water
        case STATIONARY_WATER => StationaryWater
        case LAVA => Lava
        case STATIONARY_LAVA => StationaryLava
        case SAND => Sand
        case GRAVEL => Gravel
        case GOLD_ORE => GoldOre
        case IRON_ORE => IronOre
        case COAL_ORE => CoalOre
        case LOG => data.asInstanceOf[Tree].getSpecies match {
          case GENERIC => OakLog
          case REDWOOD => SpruceLog
          case BIRCH => BirchLog
          case JUNGLE => JungleLog
          case _ => Seq(OakLog, SpruceLog, BirchLog, JungleLog).minBy(_.energy)
        }
        case LEAVES => data.asInstanceOf[Leaves].getSpecies match {
          case GENERIC => OakLeaves
          case REDWOOD => SpruceLeaves
          case BIRCH => BirchLeaves
          case JUNGLE => JungleLeaves
          case _ => Seq(OakLeaves, SpruceLeaves, BirchLeaves, JungleLeaves).minBy(_.energy)
        }
        case SPONGE => data.getData match {
          case 0 => Sponge
          case 1 => WetSponge
        }
        case GLASS => Glass
        case LAPIS_ORE => LapisLazuliOre
        case LAPIS_BLOCK => LapisLazuliBlock
        case DISPENSER => Dispenser
        case SANDSTONE => data.asInstanceOf[Sandstone].getType match {
          case CRACKED => Sandstone
          case SMOOTH => SmoothSandstone
          case GLYPHED => ChiseledSandstone
          case _ => Seq(Sandstone, SmoothSandstone, ChiseledSandstone).minBy(_.energy)
        }
        case NOTE_BLOCK => NoteBlock
        case BED_BLOCK => BedBlock
        case POWERED_RAIL => PoweredRails
        case DETECTOR_RAIL => DetectorRails
        case PISTON_STICKY_BASE => StickyPiston
        case WEB => Cobweb
        case LONG_GRASS => data.asInstanceOf[LongGrass].getSpecies match {
          case DEAD => Shrub
          case NORMAL => LongGrass
          case FERN_LIKE => Fern
          case _ => Seq(Shrub, LongGrass, Fern).minBy(_.energy)
        }
        case DEAD_BUSH => DeadBush
        case PISTON_BASE => Piston
        case PISTON_EXTENSION => PistonExtension
        case WOOL => data.asInstanceOf[Wool].getColor match {
          case WHITE => WhiteWool
          case ORANGE => OrangeWool
          case MAGENTA => MagentaWool
          case LIGHT_BLUE => LightBlueWool
          case YELLOW => YellowWool
          case LIME => LimeWool
          case PINK => PinkWool
          case GRAY => GrayWool
          case SILVER => LightGrayWool
          case CYAN => CyanWool
          case PURPLE => PurpleWool
          case BLUE => BlueWool
          case BROWN => BrownWool
          case GREEN => GreenWool
          case RED => RedWool
          case BLACK => BlackWool
          case _ => Seq(WhiteWool, OrangeWool, MagentaWool, LightBlueWool, YellowWool, LimeWool, PinkWool, GrayWool, LightGrayWool, CyanWool, PurpleWool, BlueWool, BrownWool, GreenWool, RedWool, BlackWool).minBy(_.energy)
        }
        case PISTON_MOVING_PIECE => PistonMovingPiece
        case YELLOW_FLOWER => Dandelion
        case RED_ROSE => data.getData match {
          case 0 => Poppy
          case 1 => BlueOrchid
          case 2 => Allium
          case 3 => AzureBluet
          case 4 => RedTulip
          case 5 => OrangeTulip
          case 6 => WhiteTulip
          case 7 => PinkTulip
          case 8 => OxeyeDaisy
          case _ => Seq(Poppy, BlueOrchid, Allium, AzureBluet, RedTulip, OrangeTulip, WhiteTulip, PinkTulip, OxeyeDaisy).minBy(_.energy)
        }
        case BROWN_MUSHROOM => BrownMushroom
        case RED_MUSHROOM => RedMushroom
        case GOLD_BLOCK => GoldBlock
        case IRON_BLOCK => IronBlock
        case DOUBLE_STEP => data.getData match {
          case 0 => StoneDoubleSlab
          case 1 => SandstoneDoubleSlab
          case 2 => OldWoodDoubleSlab
          case 3 => CobblestoneDoubleSlab
          case 4 => BrickDoubleSlab
          case 5 => StoneBrickDoubleSlab
          case 6 => NetherBrickDoubleSlab
          case 7 => QuartzDoubleSlab
          case _ => Seq(StoneDoubleSlab, SandstoneDoubleSlab, CobblestoneDoubleSlab, BrickDoubleSlab, StoneBrickDoubleSlab, NetherBrickDoubleSlab, QuartzDoubleSlab).minBy(_.energy)
        }
        case STEP => data.getData match {
          case 0 | 8 => StoneSingleSlab
          case 1 | 9 => SandstoneSingleSlab
          case 2 | 10 => OldWoodSingleSlab
          case 3 | 11 => CobblestoneSingleSlab
          case 4 | 12 => BrickSingleSlab
          case 5 | 13 => StoneBrickSingleSlab
          case 6 | 14 => NetherBrickSingleSlab
          case 7 | 15 => QuartzSingleSlab
          case _ => Seq(StoneSingleSlab, SandstoneSingleSlab, CobblestoneSingleSlab, BrickSingleSlab, StoneBrickSingleSlab, NetherBrickSingleSlab, QuartzSingleSlab).minBy(_.energy)
        }
        case BRICK => BrickBlock
        case BTNT => TNT
        case BOOKSHELF => Bookshelf
        case MOSSY_COBBLESTONE => MossyCobblestone
        case OBSIDIAN => Obsidian
        case TORCH => Torch
        case FIRE => Fire
        case MOB_SPAWNER => MobSpawner
        case WOOD_STAIRS => OakStairs
        case CHEST => Chest
        case REDSTONE_WIRE => RedstoneWire
        case DIAMOND_ORE => DiamondOre
        case DIAMOND_BLOCK => DiamondBlock
        case WORKBENCH => CraftingTable
        case CROPS => Crops
        case SOIL => Soil
        case FURNACE => Furnace
        case BURNING_FURNACE => BurningFurnace
        case SIGN_POST => SignPost
        case WOODEN_DOOR => OakDoor
        case LADDER => Ladder
        case RAILS => Rails
        case COBBLESTONE_STAIRS => CobblestoneStairs
        case WALL_SIGN => WallSign
        case LEVER => Lever
        case STONE_PLATE => StonePressurePlate
        case IRON_DOOR_BLOCK => IronDoorBlock
        case WOOD_PLATE => WoodPressurePlate
        case REDSTONE_ORE => RedstoneOre
        case GLOWING_REDSTONE_ORE => GlowingRedstoneOre
        case REDSTONE_TORCH_OFF => RedstoneTorchOff
        case REDSTONE_TORCH_ON => RedstoneTorchOn
        case STONE_BUTTON => StoneButton
        case SNOW => Snow
        case ICE => Ice
        case SNOW_BLOCK => SnowBlock
        case CACTUS => Cactus
        case CLAY => Clay
        case SUGAR_CANE_BLOCK => SugarCaneBlock
        case JUKEBOX => Jukebox
        case FENCE => OakFence
        case PUMPKIN => Pumpkin
        case NETHERRACK => Netherrack
        case SOUL_SAND => SoulSand
        case GLOWSTONE => Glowstone
        case PORTAL => Portal
        case JACK_O_LANTERN => JackOLantern
        case CAKE_BLOCK => CakeBlock
        case DIODE_BLOCK_OFF => RedstoneRepeaterOff
        case DIODE_BLOCK_ON => RedstoneRepeaterOn
        case STAINED_GLASS => data.getData match {
          case 0 => WhiteGlass
          case 1 => OrangeGlass
          case 2 => MagentaGlass
          case 3 => LightBlueGlass
          case 4 => YellowGlass
          case 5 => LimeGlass
          case 6 => PinkGlass
          case 7 => GrayGlass
          case 8 => LightGrayGlass
          case 9 => CyanGlass
          case 10 => PurpleGlass
          case 11 => BlueGlass
          case 12 => BrownGlass
          case 13 => GreenGlass
          case 14 => RedGlass
          case 15 => BlackGlass
          case _ => Seq(WhiteGlass, OrangeGlass, MagentaGlass, LightBlueGlass, YellowGlass, LimeGlass, PinkGlass, GrayGlass,
                        LightGrayGlass, CyanGlass, PurpleGlass, BlueGlass, BrownGlass, GreenGlass, RedGlass, BlackGlass).minBy(_.energy)
        }
        case TRAP_DOOR => TrapDoor
        case MONSTER_EGGS => MonsterEggs
        case SMOOTH_BRICK => data.getData match {
          case 0 => StoneBrick
          case 1 => CrackedStoneBrick
          case 2 => MossyStoneBrick
          case 3 => ChiseledStoneBrick
          case _ => Seq(StoneBrick, CrackedStoneBrick, MossyStoneBrick, ChiseledStoneBrick).minBy(_.energy)
        }
        case HUGE_MUSHROOM_1 => HugeMushroom1
        case HUGE_MUSHROOM_2 => HugeMushroom2
        case IRON_FENCE => IronFence
        case THIN_GLASS => GlassPane
        case MELON_BLOCK => MelonBlock
        case PUMPKIN_STEM => PumpkinStem
        case MELON_STEM => MelonStem
        case VINE => Vine
        case FENCE_GATE => OakFenceGate
        case BRICK_STAIRS => BrickStairs
        case SMOOTH_STAIRS => StoneBrickStairs
        case MYCEL => Mycelium
        case WATER_LILY => WaterLily
        case NETHER_BRICK => NetherBrick
        case NETHER_FENCE => NetherBrickFence
        case NETHER_BRICK_STAIRS => NetherBrickStairs
        case NETHER_WARTS => NetherWarts
        case ENCHANTMENT_TABLE => EnchantmentTable
        case BREWING_STAND => BrewingStand
        case CAULDRON => Cauldron
        case ENDER_PORTAL => EnderPortal
        case ENDER_PORTAL_FRAME => EnderPortalFrame
        case ENDER_STONE => EndStone
        case DRAGON_EGG => DragonEgg
        case REDSTONE_LAMP_OFF => RedstoneLampOff
        case REDSTONE_LAMP_ON => RedstoneLampOn
        case WOOD_DOUBLE_STEP => data.asInstanceOf[Wood].getSpecies match {
          case GENERIC => OakDoubleSlab
          case REDWOOD => SpruceDoubleSlab
          case BIRCH => BirchDoubleSlab
          case JUNGLE => JungleDoubleSlab
          case ACACIA => AcaciaDoubleSlab
          case DARK_OAK => DarkOakDoubleSlab
          case _ => Seq(OakDoubleSlab, SpruceDoubleSlab, BirchDoubleSlab, JungleDoubleSlab, AcaciaDoubleSlab, DarkOakDoubleSlab).minBy(_.energy)
        }
        case WOOD_STEP => data.asInstanceOf[WoodenStep].getSpecies match {
          case GENERIC => OakSingleSlab
          case REDWOOD => SpruceSingleSlab
          case BIRCH => BirchSingleSlab
          case JUNGLE => JungleSingleSlab
          case ACACIA => AcaciaSingleSlab
          case DARK_OAK => DarkOakSingleSlab
          case _ => Seq(OakDoubleSlab, SpruceDoubleSlab, BirchDoubleSlab, JungleDoubleSlab, AcaciaDoubleSlab, DarkOakDoubleSlab).minBy(_.energy)
        }
        case COCOA => Cocoa
        case SANDSTONE_STAIRS => SandstoneStairs
        case EMERALD_ORE => EmeraldOre
        case ENDER_CHEST => EnderChest
        case TRIPWIRE_HOOK => TripwireHook
        case TRIPWIRE => Tripwire
        case EMERALD_BLOCK => EmeraldBlock
        case SPRUCE_WOOD_STAIRS => SpruceStairs
        case BIRCH_WOOD_STAIRS => BirchStairs
        case JUNGLE_WOOD_STAIRS => JungleStairs
        case COMMAND => Command
        case BEACON => Beacon
        case COBBLE_WALL => CobblestoneWall
        case FLOWER_POT => FlowerPot
        case CARROT => Carrot
        case POTATO => Potato
        case WOOD_BUTTON => WoodButton
        case SKULL => Skull
        case ANVIL => Anvil
        case TRAPPED_CHEST => TrappedChest
        case GOLD_PLATE => GoldPressurePlate
        case IRON_PLATE => IronPressurePlate
        case REDSTONE_COMPARATOR_OFF => RedstoneComparatorOff
        case REDSTONE_COMPARATOR_ON => RedstoneComparatorOn
        case DAYLIGHT_DETECTOR => DaylightSensor
        case REDSTONE_BLOCK => RedstoneBlock
        case QUARTZ_ORE => QuartzOre
        case HOPPER => Hopper
        case QUARTZ_BLOCK => data.getData match {
          case 0 => QuartzBlock
          case 1 => ChiseledQuartzBlock
          case 2 => PillarQuartzBlock
          case _ => Seq(QuartzBlock, ChiseledQuartzBlock, PillarQuartzBlock).minBy(_.energy)
        }
        case QUARTZ_STAIRS => QuartzStairs
        case ACTIVATOR_RAIL => ActivatorRails
        case DROPPER => Dropper
        case STAINED_CLAY => data.getData match {
          case 0 => WhiteHardenedClay
          case 1 => OrangeHardenedClay
          case 2 => MagentaHardenedClay
          case 3 => LightBlueHardenedClay
          case 4 => YellowHardenedClay
          case 5 => LimeHardenedClay
          case 6 => PinkHardenedClay
          case 7 => GrayHardenedClay
          case 8 => LightGrayHardenedClay
          case 9 => CyanHardenedClay
          case 10 => PurpleHardenedClay
          case 11 => BlueHardenedClay
          case 12 => BrownHardenedClay
          case 13 => GreenHardenedClay
          case 14 => RedHardenedClay
          case 15 => BlackHardenedClay
          case _ => Seq(WhiteHardenedClay, OrangeHardenedClay, MagentaHardenedClay, LightBlueHardenedClay, YellowHardenedClay,
                        LimeHardenedClay, PinkHardenedClay, GrayHardenedClay, LightGrayHardenedClay, CyanHardenedClay,
                        PurpleHardenedClay, BlueHardenedClay, BrownHardenedClay, GreenHardenedClay, RedHardenedClay, BlackHardenedClay).minBy(_.energy)
        }
        case STAINED_GLASS_PANE => data.getData match {
          case 0 => WhiteGlassPane
          case 1 => OrangeGlassPane
          case 2 => MagentaGlassPane
          case 3 => LightBlueGlassPane
          case 4 => YellowGlassPane
          case 5 => LimeGlassPane
          case 6 => PinkGlassPane
          case 7 => GrayGlassPane
          case 8 => LightGrayGlassPane
          case 9 => CyanGlassPane
          case 10 => PurpleGlassPane
          case 11 => BlueGlassPane
          case 12 => BrownGlassPane
          case 13 => GreenGlassPane
          case 14 => RedGlassPane
          case 15 => BlackGlassPane
          case _ => Seq(WhiteGlassPane, OrangeGlassPane, MagentaGlassPane, LightBlueGlassPane, YellowGlassPane,
                    LimeGlassPane, PinkGlassPane, GrayGlassPane, LightGrayGlassPane, CyanGlassPane,
                    PurpleGlassPane, BlueGlassPane, BrownGlassPane, GreenGlassPane, RedGlassPane, BlackGlassPane).minBy(_.energy)
        }
        case LEAVES_2 => data.asInstanceOf[Leaves].getSpecies match {
          case ACACIA => AcaciaLeaves
          case DARK_OAK => DarkOakLeaves
          case _ => Seq(AcaciaLeaves, DarkOakLeaves).minBy(_.energy)
        }
        case LOG_2 => data.asInstanceOf[Tree].getSpecies match {
          case ACACIA => AcaciaLog
          case DARK_OAK => DarkOakLog
          case _ => Seq(AcaciaLog, DarkOakLog).minBy(_.energy)
        }
        case ACACIA_STAIRS => AcaciaStairs
        case DARK_OAK_STAIRS => DarkOakStairs
        case SLIME_BLOCK => SlimeBlock
        case BARRIER => Barrier
        case IRON_TRAPDOOR => IronTrapdoor
        case PRISMARINE => data.getData match {
          case 0 => Prismarine
          case 1 => PrismarineBrick
          case 2 => DarkPrismarine
          case _ => Seq(Prismarine, PrismarineBrick, DarkPrismarine).minBy(_.energy)
        }
        case SEA_LANTERN => SeaLantern
        case HAY_BLOCK => HayBale
        case CARPET => data.getData match {
          case 0 => WhiteCarpet
          case 1 => OrangeCarpet
          case 2 => MagentaCarpet
          case 3 => LightBlueCarpet
          case 4 => YellowCarpet
          case 5 => LimeCarpet
          case 6 => PinkCarpet
          case 7 => GrayCarpet
          case 8 => LightGrayCarpet
          case 9 => CyanCarpet
          case 10 => PurpleCarpet
          case 11 => BlueCarpet
          case 12 => BrownCarpet
          case 13 => GreenCarpet
          case 14 => RedCarpet
          case 15 => BlackCarpet
          case _ => Seq(WhiteCarpet, OrangeCarpet, MagentaCarpet, LightBlueCarpet, YellowCarpet,
            LimeCarpet, PinkCarpet, GrayCarpet, LightGrayCarpet, CyanCarpet,
            PurpleCarpet, BlueCarpet, BrownCarpet, GreenCarpet, RedCarpet, BlackCarpet).minBy(_.energy)
        }
        case HARD_CLAY => HardenedClay
        case COAL_BLOCK => CoalBlock
        case PACKED_ICE => PackedIce
        case DOUBLE_PLANT => data.getData match {
          case 0 => DoublePlant
          case 1 => Sunflower
          case 2 => Lilac
          case 3 => DoubleTallgrass
          case 4 => LargeFern
          case 5 => RoseBush
          case 6 => Peony
          case 7 => TopPlantHalf
          case _ => Seq(DoublePlant, Sunflower, Lilac, DoubleTallgrass, LargeFern,
            LargeFern, RoseBush, Peony, TopPlantHalf).minBy(_.energy)
        }
        case STANDING_BANNER => StandingBanner
        case WALL_BANNER => WallBanner
        case DAYLIGHT_DETECTOR_INVERTED => InvertedDaylightSensor
        case RED_SANDSTONE => data.getData match {
          case 0 => RedSandstone
          case 1 => SmoothRedSandstone
          case 2 => ChiseledRedSandstone
          case _ => Seq(RedSandstone, SmoothRedSandstone, ChiseledRedSandstone).minBy(_.energy)
        }
        case RED_SANDSTONE_STAIRS => RedSandstoneStairs
        case DOUBLE_STONE_SLAB2 => RedSandstoneDoubleSlab
        case STONE_SLAB2 => RedSandstoneSingleSlab
        case SPRUCE_FENCE_GATE => SpruceFenceGate
        case BIRCH_FENCE_GATE => BirchFenceGate
        case JUNGLE_FENCE_GATE => JungleFenceGate
        case DARK_OAK_FENCE_GATE => DarkOakFenceGate
        case ACACIA_FENCE_GATE => AcaciaFenceGate
        case SPRUCE_FENCE => SpruceFence
        case BIRCH_FENCE => BirchFence
        case JUNGLE_FENCE => JungleFence
        case DARK_OAK_FENCE => DarkOakFence
        case ACACIA_FENCE => AcaciaFence
        case SPRUCE_DOOR => SpruceDoor
        case BIRCH_DOOR => BirchDoor
        case JUNGLE_DOOR => JungleDoor
        case ACACIA_DOOR => AcaciaDoor
        case DARK_OAK_DOOR => DarkOakDoor
        case END_ROD => EndRod
        case CHORUS_PLANT => ChorusPlant
        case CHORUS_FLOWER => ChorusFlower
        case PURPUR_BLOCK => PurpurBlock
        case PURPUR_PILLAR => PurpurPillar
        case PURPUR_STAIRS => PurpurStairs
        case PURPUR_DOUBLE_SLAB => PurpurDoubleSlab
        case PURPUR_SLAB => PurpurSingleSlab
        case END_BRICKS => EndStoneBricks
        case BEETROOT_BLOCK => BeetrootPlantation
        case GRASS_PATH => GrassPath
        case END_GATEWAY => EndGateway
        case COMMAND_REPEATING => CommandRepeating
        case COMMAND_CHAIN => CommandChain
        case FROSTED_ICE => FrostedIce
        case MAGMA => Magma
        case NETHER_WART_BLOCK => NetherWartBlock
        case RED_NETHER_BRICK => RedNetherBrick
        case BONE_BLOCK => BoneBlock
        case STRUCTURE_VOID => StructureVoid
        case OBSERVER => Observer
        case WHITE_SHULKER_BOX => WhiteShulkerBox
        case ORANGE_SHULKER_BOX => OrangeShulkerBox
        case MAGENTA_SHULKER_BOX => MagentaShulkerBox
        case LIGHT_BLUE_SHULKER_BOX => LightBlueShulkerBox
        case YELLOW_SHULKER_BOX => YellowShulkerBox
        case LIME_SHULKER_BOX => LimeShulkerBox
        case PINK_SHULKER_BOX => PinkShulkerBox
        case GRAY_SHULKER_BOX => GrayShulkerBox
        case SILVER_SHULKER_BOX => LightGrayShulkerBox
        case CYAN_SHULKER_BOX => CyanShulkerBox
        case PURPLE_SHULKER_BOX => PurpleShulkerBox
        case BLUE_SHULKER_BOX => BlueShulkerBox
        case BROWN_SHULKER_BOX => BrownShulkerBox
        case GREEN_SHULKER_BOX => GreenShulkerBox
        case RED_SHULKER_BOX => RedShulkerBox
        case BLACK_SHULKER_BOX => BlackShulkerBox
        case STRUCTURE_BLOCK => data.getData match {
          case 0 => StructureSaveBlock
          case 1 => StructureLoadBlock
          case 2 => StructureCornerBlock
          case 3 => StructureDataBlock
          case _ => Seq(StructureSaveBlock, StructureLoadBlock, StructureCornerBlock, StructureDataBlock).minBy(_.energy)
        }
        case IRON_SPADE => IronShovel
        case IRON_PICKAXE => IronPickaxe
        case IRON_AXE => IronAxe
        case FLINT_AND_STEEL => FlintAndSteel
        case APPLE => Apple
        case BOW => Bow
        case ARROW => Arrow
        case COAL => data.asInstanceOf[Coal].getType match {
          case CoalType.COAL => Coal
          case CoalType.CHARCOAL => Charcoal
          case _ => Seq(Coal, Charcoal).minBy(_.energy)
        }
        case DIAMOND => Diamond
        case IRON_INGOT => IronIngot
        case GOLD_INGOT => GoldIngot
        case IRON_SWORD => IronSword
        case WOOD_SWORD => WoodSword
        case WOOD_SPADE => WoodShovel
        case WOOD_PICKAXE => WoodPickaxe
        case WOOD_AXE => WoodAxe
        case STONE_SWORD => StoneSword
        case STONE_SPADE => StoneShovel
        case STONE_PICKAXE => StonePickaxe
        case STONE_AXE => StoneAxe
        case DIAMOND_SWORD => DiamondSword
        case DIAMOND_SPADE => DiamondShovel
        case DIAMOND_PICKAXE => DiamondPickaxe
        case DIAMOND_AXE => DiamondAxe
        case STICK => Stick
        case BOWL => Bowl
        case MUSHROOM_SOUP => MushroomStew
        case GOLD_SWORD => GoldSword
        case GOLD_SPADE => GoldShovel
        case GOLD_PICKAXE => GoldPickaxe
        case GOLD_AXE => GoldAxe
        case STRING => String
        case FEATHER => Feather
        case SULPHUR => GunPowder
        case WOOD_HOE => WoodHoe
        case STONE_HOE => StoneHoe
        case IRON_HOE => IronHoe
        case DIAMOND_HOE => DiamondHoe
        case GOLD_HOE => GoldHoe
        case SEEDS => Seeds
        case WHEAT => Wheat
        case BREAD => Bread
        case LEATHER_HELMET => LeatherHelmet
        case LEATHER_CHESTPLATE => LeatherChestplate
        case LEATHER_LEGGINGS => LeatherLeggings
        case LEATHER_BOOTS => LeatherBoots
        case CHAINMAIL_HELMET => ChainmailHelmet
        case CHAINMAIL_CHESTPLATE => ChainmailChestplate
        case CHAINMAIL_LEGGINGS => ChainmailLeggings
        case CHAINMAIL_BOOTS => ChainmailBoots
        case IRON_HELMET => IronHelmet
        case IRON_CHESTPLATE => IronChestplate
        case IRON_LEGGINGS => IronLeggings
        case IRON_BOOTS => IronBoots
        case DIAMOND_HELMET => DiamondHelmet
        case DIAMOND_CHESTPLATE => DiamondChestplate
        case DIAMOND_LEGGINGS => DiamondLeggings
        case DIAMOND_BOOTS => DiamondBoots
        case GOLD_HELMET => GoldHelmet
        case GOLD_CHESTPLATE => GoldChestplate
        case GOLD_LEGGINGS => GoldLeggings
        case GOLD_BOOTS => GoldBoots
        case FLINT => Flint
        case PORK => RawPorkchop
        case GRILLED_PORK => CookedPorkchop
        case PAINTING => Painting
        case GOLDEN_APPLE => data.getData match {
          case 0 => GoldenApple
          case 1 => EnchantedGoldenApple
          case _ => Seq(GoldenApple, EnchantedGoldenApple).minBy(_.energy)
        }
        case SIGN => Sign
        case WOOD_DOOR => OakDoorItem
        case BUCKET => Bucket
        case WATER_BUCKET => WaterBucket
        case LAVA_BUCKET => LavaBucket
        case MINECART => Minecart
        case SADDLE => Saddle
        case IRON_DOOR => IronDoor
        case REDSTONE => Redstone
        case SNOW_BALL => SnowBall
        case BOAT => OakBoat
        case LEATHER => Leather
        case MILK_BUCKET => MilkBucket
        case CLAY_BRICK => ClayBrick
        case CLAY_BALL => ClayBall
        case SUGAR_CANE => SugarCane
        case PAPER => Paper
        case BOOK => Book
        case SLIME_BALL => SlimeBall
        case STORAGE_MINECART => StorageMinecart
        case POWERED_MINECART => PoweredMinecart
        case EGG => Egg
        case COMPASS => Compass
        case FISHING_ROD => FishingRod
        case WATCH => Watch
        case GLOWSTONE_DUST => GlowstoneDust
        case RAW_FISH => data.getData match {
          case 0 => RawFish
          case 1 => RawSalmon
          case 2 => Clownfish
          case 3 => Pufferfish
          case _ => Seq(RawFish, RawSalmon, Clownfish, Pufferfish).minBy(_.energy)
        }
        case COOKED_FISH => data.getData match {
          case 0 => CookedFish
          case 1 => CookedSalmon
          case _ => Seq(CookedFish, CookedSalmon).minBy(_.energy)
        }
        case INK_SACK => data.asInstanceOf[Dye].getColor match {
          case BLACK => InkSack
          case RED => RoseRed
          case GREEN => CactusGreen
          case BROWN => CocoaBeans
          case BLUE => LapisLazuli
          case PURPLE => PurpleDye
          case CYAN => CyanDye
          case SILVER => LightGrayDye
          case GRAY => GrayDye
          case PINK => PinkDye
          case LIME => LimeDye
          case YELLOW => DandelionYellow
          case LIGHT_BLUE => LightBlueDye
          case MAGENTA => MagentaDye
          case ORANGE => OrangeDye
          case WHITE => BoneMeal
          case _ => Seq(InkSack, RoseRed, CactusGreen, CocoaBeans, LapisLazuli, PurpleDye, CyanDye, LightGrayDye, GrayDye, PinkDye, LimeDye, DandelionYellow, LightBlueDye, MagentaDye, OrangeDye, BoneMeal).minBy(_.energy)
        }
        case BONE => Bone
        case SUGAR => Sugar
        case CAKE => Cake
        case BED => Bed
        case DIODE => RedstoneRepeater
        case COOKIE => Cookie
        case MAP => DrawnMap
        case SHEARS => Shears
        case MELON => Melon
        case PUMPKIN_SEEDS => PumpkinSeeds
        case MELON_SEEDS => MelonSeeds
        case RAW_BEEF => RawBeef
        case COOKED_BEEF => CookedBeef
        case RAW_CHICKEN => RawChicken
        case COOKED_CHICKEN => CookedChicken
        case ROTTEN_FLESH => RottenFlesh
        case ENDER_PEARL => EnderPearl
        case BLAZE_ROD => BlazeRod
        case GHAST_TEAR => GhastTear
        case GOLD_NUGGET => GoldNugget
        case NETHER_STALK => NetherWart
        case POTION => Potion
        case GLASS_BOTTLE => GlassBottle
        case SPIDER_EYE => SpiderEye
        case FERMENTED_SPIDER_EYE => FermentedSpiderEye
        case BLAZE_POWDER => BlazePowder
        case MAGMA_CREAM => MagmaCream
        case BREWING_STAND_ITEM => BrewingStandItem
        case CAULDRON_ITEM => CauldronItem
        case EYE_OF_ENDER => EyeOfEnder
        case SPECKLED_MELON => GlisteringMelon
        case MONSTER_EGG => MonsterEgg
        case EXP_BOTTLE => ExpBottle
        case FIREBALL => FireCharge
        case BOOK_AND_QUILL => BookAndQuill
        case WRITTEN_BOOK => WrittenBook
        case EMERALD => Emerald
        case ITEM_FRAME => ItemFrame
        case FLOWER_POT_ITEM => FlowerPotItem
        case CARROT_ITEM => CarrotItem
        case POTATO_ITEM => PotatoItem
        case BAKED_POTATO => BakedPotato
        case POISONOUS_POTATO => PoisonousPotato
        case EMPTY_MAP => EmptyMap
        case GOLDEN_CARROT => GoldenCarrot
        case SKULL_ITEM => data.getData match {
          case 0 => SkeletonHead
          case 1 => WitherSkeletonHead
          case 2 => ZombieHead
          case 3 => PlayerHead
          case 4 => CreeperHead
          case 5 => DragonHead
          case _ => Seq(SkeletonHead, WitherSkeletonHead, ZombieHead, PlayerHead, CreeperHead, DragonHead).minBy(_.energy)
        }
        case CARROT_STICK => CarrotStick
        case NETHER_STAR => NetherStar
        case PUMPKIN_PIE => PumpkinPie
        case FIREWORK => FireworkRocket
        case FIREWORK_CHARGE => FireworkStar
        case ENCHANTED_BOOK => EnchantedBook
        case REDSTONE_COMPARATOR => RedstoneComparator
        case NETHER_BRICK_ITEM => NetherBrickItem
        case QUARTZ => Quartz
        case EXPLOSIVE_MINECART => ExplosiveMinecart
        case HOPPER_MINECART => HopperMinecart
        case PRISMARINE_SHARD => PrismarineShard
        case PRISMARINE_CRYSTALS => PrismarineCrystals
        case RABBIT => RawRabbit
        case COOKED_RABBIT => CookedRabbit
        case RABBIT_STEW => RabbitStew
        case RABBIT_FOOT => RabbitFoot
        case RABBIT_HIDE => RabbitHide
        case ARMOR_STAND => ArmorStand
        case IRON_BARDING => IronHorseArmor
        case GOLD_BARDING => GoldHorseArmor
        case DIAMOND_BARDING => DiamondHorseArmor
        case LEASH => Leash
        case NAME_TAG => NameTag
        case COMMAND_MINECART => CommandMinecart
        case MUTTON => RawMutton
        case COOKED_MUTTON => CookedMutton
        case BANNER => Banner
        case END_CRYSTAL => EndCrystal
        case SPRUCE_DOOR_ITEM => SpruceDoorItem
        case BIRCH_DOOR_ITEM => BirchDoorItem
        case JUNGLE_DOOR_ITEM => JungleDoorItem
        case ACACIA_DOOR_ITEM => AcaciaDoorItem
        case DARK_OAK_DOOR_ITEM => DarkOakDoorItem
        case CHORUS_FRUIT => ChorusFruit
        case CHORUS_FRUIT_POPPED => PoppedChorusFruit
        case BEETROOT => Beetroot
        case BEETROOT_SEEDS => BeetrootSeeds
        case BEETROOT_SOUP => BeetrootSoup
        case DRAGONS_BREATH => DragonsBreath
        case SPLASH_POTION => SplashPotion
        case SPECTRAL_ARROW => SpectralArrow
        case TIPPED_ARROW => TippedArrow
        case LINGERING_POTION => LingeringPotion
        case SHIELD => Shield
        case ELYTRA => Elytra
        case BOAT_SPRUCE => SpruceBoat
        case BOAT_BIRCH => BirchBoat
        case BOAT_JUNGLE => JungleBoat
        case BOAT_ACACIA => AcaciaBoat
        case BOAT_DARK_OAK => DarkOakBoat
        case TOTEM => TotemOfUndying
        case SHULKER_SHELL => ShulkerShell
        case IRON_NUGGET => IronNugget
        case GOLD_RECORD => GoldRecord
        case GREEN_RECORD => GreenRecord
        case RECORD_3 => Record3
        case RECORD_4 => Record4
        case RECORD_5 => Record5
        case RECORD_6 => Record6
        case RECORD_7 => Record7
        case RECORD_8 => Record8
        case RECORD_9 => Record9
        case RECORD_10 => Record10
        case RECORD_11 => Record11
        case RECORD_12 => Record12
      }
    } catch {
      case e: MatchError => throw new Exception(e.getMessage() +  s"Could not find Material for ${bMaterial.name}. Is it new?")
    }*/
  }

  override def values: IndexedSeq[Material] = findValues

  //@formatter:off
  object Air extends Material(energy = None) with Transparent with Crushable with Unconsumable
  object Stone extends Material(energy = 1)
  object Granite extends Material(energy = 1)
  object PolishedGranite extends Material
  object Diorite extends Material(energy = 1)
  object PolishedDiorite extends Material
  object Andesite extends Material(energy = 1)
  object PolishedAndesite extends Material
  object Grass extends Material(energy = 1)
  object Dirt extends Material(energy = 1)
  object CoarseDirt extends Material
  object Podzol extends Material(energy = 1)
  object Cobblestone extends Material(energy = 1)
  object OakWoodPlanks extends Material with Solid with GenericPlank
  object SpruceWoodPlanks extends Material with Solid with GenericPlank
  object BirchWoodPlanks extends Material with Solid with GenericPlank
  object JungleWoodPlanks extends Material with Solid with GenericPlank
  object AcaciaWoodPlanks extends Material with Solid with GenericPlank
  object DarkOakWoodPlanks extends Material with Solid with GenericPlank
  object OakSapling extends Material(tierString = "T2") with Transparent with Attaches with Crushable with GenericSapling
  object SpruceSapling extends Material(tierString = "T2") with Transparent with Attaches with Crushable with GenericSapling
  object BirchSapling extends Material(tierString = "T2") with Transparent with Attaches with Crushable with GenericSapling
  object JungleSapling extends Material(tierString = "T2") with Transparent with Attaches with Crushable with GenericSapling
  object AcaciaSapling extends Material(tierString = "T2") with Transparent with Attaches with Crushable with GenericSapling
  object DarkOakSapling extends Material(tierString = "T2") with Transparent with Attaches with Crushable with GenericSapling
  object Bedrock extends Material(energy = None) with Solid with Unconsumable
  object Water extends Material(energy = 14) with Transparent with Liquid
  object StationaryWater extends Material(energy = 14) with Transparent with Liquid
  object Lava extends Material(energy = 68) with Liquid
  object StationaryLava extends Material(energy = 68) with Liquid
  object Sand extends Material(energy = 1) with Solid with Gravity
  object Gravel extends Material(tierString = "T2") with Solid with Gravity
  object GoldOre extends Material(energy = 1168)
  object IronOre extends Material(tierString = "T3")
  object CoalOre extends Material
  object OakLog extends Material(tierString = "T2") with Solid with GenericLog
  object SpruceLog extends Material(tierString = "T2") with Solid with GenericLog
  object BirchLog extends Material(tierString = "T2") with Solid with GenericLog
  object JungleLog extends Material(tierString = "T2") with Solid with GenericLog
  object OakLeaves extends Material(tierString = "T2") with Solid with Crushable with GenericLeaves
  object SpruceLeaves extends Material(tierString = "T2") with Solid with Crushable with GenericLeaves
  object BirchLeaves extends Material(tierString = "T2") with Solid with Crushable with GenericLeaves
  object JungleLeaves extends Material(tierString = "T2") with Solid with Crushable with GenericLeaves
  object Sponge extends Material(energy = 210)
  object WetSponge extends Material(energy = 18538)
  object Glass extends Material with Solid with GenericGlass
  object LapisLazuliOre extends Material
  object LapisLazuliBlock extends Material
  object Dispenser extends Material with Solid with Rotates with Inventory
  object Sandstone extends Material
  object SmoothSandstone extends Material
  object ChiseledSandstone extends Material
  object NoteBlock extends Material
  object BedBlock extends Material with Solid with Rotates
  object PoweredRails extends Material with Transparent with Attaches
  object DetectorRails extends Material with Transparent with Attaches
  object StickyPiston extends Material
  object Cobweb extends Material(energy = 2279) with Transparent with Crushable
  object Shrub extends Material(energy = 1) with Transparent with Crushable
  object LongGrass extends Material(energy = 1) with Transparent with Crushable
  object Fern extends Material(energy = 1) with Transparent with Crushable
  object DeadBush extends Material(tierString = "T3") with Transparent with Crushable // Pretty rare and needs shears
  object Piston extends Material
  object PistonExtension extends Material(energy = None) with Solid with Unconsumable
  object WhiteWool extends Material with Solid with GenericWool
  object OrangeWool extends Material with Solid with GenericWool
  object MagentaWool extends Material with Solid with GenericWool
  object LightBlueWool extends Material with Solid with GenericWool
  object YellowWool extends Material with Solid with GenericWool
  object LimeWool extends Material with Solid with GenericWool
  object PinkWool extends Material with Solid with GenericWool
  object GrayWool extends Material with Solid with GenericWool
  object LightGrayWool extends Material with Solid with GenericWool
  object CyanWool extends Material with Solid with GenericWool
  object PurpleWool extends Material with Solid with GenericWool
  object BlueWool extends Material with Solid with GenericWool
  object BrownWool extends Material with Solid with GenericWool
  object GreenWool extends Material with Solid with GenericWool
  object RedWool extends Material with Solid with GenericWool
  object BlackWool extends Material with Solid with GenericWool
  object PistonMovingPiece extends Material(energy = None) with Solid with Unconsumable
  object Dandelion extends Material(energy = 14) with Transparent with Attaches with Crushable
  object Poppy extends Material(energy = 14) with Transparent with Attaches with Crushable
  object BlueOrchid extends Material(energy = 14) with Transparent with Attaches with Crushable
  object Allium extends Material(energy = 14) with Transparent with Attaches with Crushable
  object AzureBluet extends Material(energy = 14) with Transparent with Attaches with Crushable
  object RedTulip extends Material(energy = 14) with Transparent with Attaches with Crushable
  object OrangeTulip extends Material(energy = 14) with Transparent with Attaches with Crushable
  object WhiteTulip extends Material(energy = 14) with Transparent with Attaches with Crushable
  object PinkTulip extends Material(energy = 14) with Transparent with Attaches with Crushable
  object OxeyeDaisy extends Material(energy = 14) with Transparent with Attaches with Crushable
  object BrownMushroom extends Material(energy = 159) with Transparent with Attaches with Crushable // Same as pumpkin
  object RedMushroom extends Material(energy = 159) with Transparent with Attaches with Crushable // Same as pumpkin
  object GoldBlock extends Material
  object IronBlock extends Material
  object StoneDoubleSlab extends Material with Solid with GenericDoubleSlab
  object SandstoneDoubleSlab extends Material with Solid with GenericDoubleSlab
  object OldWoodDoubleSlab extends Material(energy = None) with Solid with Unconsumable with GenericDoubleSlab
  object CobblestoneDoubleSlab extends Material with Solid with GenericDoubleSlab
  object BrickDoubleSlab extends Material with Solid with GenericDoubleSlab
  object StoneBrickDoubleSlab extends Material with Solid with GenericDoubleSlab
  object NetherBrickDoubleSlab extends Material with Solid with GenericDoubleSlab
  object QuartzDoubleSlab extends Material with Solid with GenericDoubleSlab
  object StoneSingleSlab extends Material with Solid with GenericSingleSlab
  object SandstoneSingleSlab extends Material with Solid with GenericSingleSlab
  object OldWoodSingleSlab extends Material(energy = None) with Solid with Unconsumable with GenericSingleSlab
  object CobblestoneSingleSlab extends Material with Solid with GenericSingleSlab
  object BrickSingleSlab extends Material with Solid with GenericSingleSlab
  object StoneBrickSingleSlab extends Material with Solid with GenericSingleSlab
  object NetherBrickSingleSlab extends Material with Solid with GenericSingleSlab
  object QuartzSingleSlab extends Material with Solid with GenericSingleSlab
  object BrickBlock extends Material
  object TNT extends Material
  object Bookshelf extends Material
  object MossyCobblestone extends Material
  object Obsidian extends Material(energy = 68)
  object Torch extends Material with Transparent with Attaches with Crushable with Rotates
  object Fire extends Material(tierString = "T3") with Transparent with Attaches with Crushable // flint and steel/durability
  object MobSpawner extends Material(tierString = "T6")
  object OakStairs extends Material with Solid with Rotates with GenericStairs
  object Chest extends Material with Solid with Rotates with Inventory
  object RedstoneWire extends Material with Transparent with Attaches // Same as redstone
  object DiamondOre extends Material
  object DiamondBlock extends Material
  object CraftingTable extends Material
  object Crops extends Material(tierString = "T2") with Transparent with Attaches with Crushable // Same as seed
  object Soil extends Material(energy = 1)
  object Furnace extends Material with Solid with Rotates with Inventory
  object BurningFurnace extends Material with Solid with Rotates with Inventory // Same as furnace
  object SignPost extends Material with Solid with Rotates // Same as sign
  object OakDoor extends Material with Solid with Rotates with GenericDoor
  object Ladder extends Material with Transparent with Attaches with Rotates
  object Rails extends Material with Transparent with Attaches
  object CobblestoneStairs extends Material with Solid with Rotates with GenericStairs
  object WallSign extends Material with Transparent with Rotates // Same as sign
  object Lever extends Material with Transparent with Attaches with Rotates
  object StonePressurePlate extends Material with Transparent with Attaches
  object IronDoorBlock extends Material with Solid with Transparent with Attaches with Rotates with GenericDoor // same as iron door
  object WoodPressurePlate extends Material with Transparent with Attaches
  object RedstoneOre extends Material
  object GlowingRedstoneOre extends Material
  object RedstoneTorchOff extends Material with Transparent with Attaches with Rotates // Same as redstone torch on
  object RedstoneTorchOn extends Material with Transparent with Attaches with Rotates // Same as item
  object StoneButton extends Material with Transparent with Attaches with Rotates
  object Snow extends Material(energy = 1) with Transparent with Attaches with Crushable
  object Ice extends Material(energy = 1)
  object SnowBlock extends Material(energy = 1)
  object Cactus extends Material(tierString = "T3") with Solid with Attaches // Half pumpkin, Grows twice as slow
  object Clay extends Material with Solid with GenericClay
  object SugarCaneBlock extends Material(tierString = "T2") with Transparent with Attaches with Crushable // Same as wood, about as hard to farm
  object Jukebox extends Material
  object OakFence extends Material with Solid with Transparent with GenericFence
  object Pumpkin extends Material(tierString = "T3") with Solid with Rotates
  object Netherrack extends Material(energy = 1)
  object SoulSand extends Material
  object Glowstone extends Material
  object Portal extends Material(energy = None) with Transparent with Unconsumable
  object JackOLantern extends Material
  object CakeBlock extends Material with Solid with Transparent with Food // cake is 6 shanks, bread is 2.5 shanks,
  object RedstoneRepeaterOff extends Material
  object RedstoneRepeaterOn extends Material
  object WhiteGlass extends Material with Solid with Transparent with GenericGlass
  object OrangeGlass extends Material with Solid with Transparent with GenericGlass
  object MagentaGlass extends Material with Solid with Transparent with GenericGlass // Hard to locate and use
  object LightBlueGlass extends Material with Solid with Transparent with GenericGlass
  object YellowGlass extends Material with Solid with Transparent with GenericGlass
  object LimeGlass extends Material with Solid with Transparent with GenericGlass
  object PinkGlass extends Material with Solid with Transparent with GenericGlass
  object GrayGlass extends Material with Solid with Transparent with GenericGlass // Same as mushroom
  object LightGrayGlass extends Material with Solid with Transparent with GenericGlass // Same as mushroom
  object CyanGlass extends Material with Solid with Transparent with GenericGlass
  object PurpleGlass extends Material with Solid with Transparent with GenericGlass
  object BlueGlass extends Material with Solid with Transparent with GenericGlass
  object BrownGlass extends Material with Solid with Transparent with GenericGlass
  object GreenGlass extends Material with Solid with Transparent with GenericGlass
  object RedGlass extends Material with Solid with Transparent with GenericGlass
  object BlackGlass extends Material with Solid with Transparent with GenericGlass
  object TrapDoor extends Material with Solid with Transparent with Rotates with GenericDoor
  object MonsterEggs extends Material(tierString = "T6")
  object StoneBrick extends Material(energy = 1)
  object CrackedStoneBrick extends Material(tierString = "T5")
  object MossyStoneBrick extends Material
  object ChiseledStoneBrick extends Material
  object HugeMushroom1 extends Material(tierString = "T3")
  object HugeMushroom2 extends Material(tierString = "T3")
  object IronFence extends Material with Solid with Transparent with GenericFence
  object GlassPane extends Material with Solid with Transparent with GenericGlass with GenericGlassPane
  object MelonBlock extends Material
  object PumpkinStem extends Material(energy = None) with Transparent with Attaches with Crushable with Rotates
  object MelonStem extends Material(energy = None) with Transparent with Attaches with Crushable
  object Vine extends Material(tierString = "T5") with Transparent with Crushable
  object OakFenceGate extends Material with Solid with Transparent with Rotates with GenericFence with GenericFenceGate
  object BrickStairs extends Material with Solid with Rotates with GenericStairs
  object StoneBrickStairs extends Material with Solid with Rotates with GenericStairs
  object Mycelium extends Material(energy = 1)
  object WaterLily extends Material(tierString = "T5") with Transparent with Attaches with Liquid with Crushable
  object NetherBrick extends Material
  object NetherBrickFence extends Material with Solid with GenericFence
  object NetherBrickStairs extends Material with Solid with Rotates with GenericStairs
  object NetherWarts extends Material(tierString = "T4") with Transparent with Attaches with Crushable // Hard to get at first but easy to farm
  object EnchantmentTable extends Material
  object BrewingStand extends Material
  object Cauldron extends Material
  object EnderPortal extends Material(energy = None) with Transparent with Unconsumable
  object EnderPortalFrame extends Material(energy = None) with Solid with Unconsumable
  object EndStone extends Material(energy = 1)
  object DragonEgg extends Material(energy = 140369)
  object RedstoneLampOff extends Material
  object RedstoneLampOn extends Material
  object OakDoubleSlab extends Material with Solid with GenericDoubleSlab
  object SpruceDoubleSlab extends Material with Solid with GenericDoubleSlab
  object BirchDoubleSlab extends Material with Solid with GenericDoubleSlab
  object JungleDoubleSlab extends Material with Solid with GenericDoubleSlab
  object AcaciaDoubleSlab extends Material with Solid with GenericDoubleSlab
  object DarkOakDoubleSlab extends Material with Solid with GenericDoubleSlab
  object OakSingleSlab extends Material with Solid with GenericSingleSlab
  object SpruceSingleSlab extends Material with Solid with GenericSingleSlab
  object BirchSingleSlab extends Material with Solid with GenericSingleSlab
  object JungleSingleSlab extends Material with Solid with GenericSingleSlab
  object AcaciaSingleSlab extends Material with Solid with GenericSingleSlab
  object DarkOakSingleSlab extends Material with Solid with GenericSingleSlab // 3 food
  object Cocoa extends Material(tierString = "T4") with Transparent with Attaches with Crushable // 1 food
  object SandstoneStairs extends Material with Solid with Rotates with GenericStairs // same as item
  object EmeraldOre extends Material
  object EnderChest extends Material with Solid with Rotates
  object TripwireHook extends Material with Transparent with Attaches with Rotates
  object Tripwire extends Material with Transparent with Attaches
  object EmeraldBlock extends Material
  object SpruceStairs extends Material with Solid with Rotates with GenericStairs // same as item
  object BirchStairs extends Material with Solid with Rotates with GenericStairs // same as item
  object JungleStairs extends Material with Solid with Rotates with GenericStairs
  object Command extends Material(energy = None) with Solid with Unconsumable
  object Beacon extends Material with Solid with Inventory // quartz*1.5
  object CobblestoneWall extends Material
  object FlowerPot extends Material with Transparent
  object Carrot extends Material(tierString = "T2") with Transparent with Attaches with Crushable
  object Potato extends Material(tierString = "T2") with Transparent with Attaches with Crushable
  object WoodButton extends Material with Transparent with Rotates
  object Skull extends Material(energy = None) with Transparent with Attaches with Rotates with Unconsumable
  object Anvil extends Material with Solid with Transparent with Gravity with Inventory
  object TrappedChest extends Material with Solid with Rotates
  object GoldPressurePlate extends Material
  object IronPressurePlate extends Material
  object RedstoneComparatorOff extends Material with Transparent with Attaches with Rotates
  object RedstoneComparatorOn extends Material with Transparent with Attaches with Rotates
  object DaylightSensor extends Material
  object RedstoneBlock extends Material
  object QuartzOre extends Material
  object Hopper extends Material with Solid with Inventory
  object QuartzBlock extends Material
  object ChiseledQuartzBlock extends Material
  object PillarQuartzBlock extends Material
  object QuartzStairs extends Material with Solid with GenericStairs
  object ActivatorRails extends Material with Transparent with Attaches
  object Dropper extends Material with Solid with Inventory
  object WhiteHardenedClay extends Material with GenericClay
  object OrangeHardenedClay extends Material with GenericClay
  object MagentaHardenedClay extends Material with GenericClay
  object LightBlueHardenedClay extends Material with GenericClay
  object YellowHardenedClay extends Material with GenericClay // Needs silk touch
  object LimeHardenedClay extends Material with GenericClay
  object PinkHardenedClay extends Material with GenericClay
  object GrayHardenedClay extends Material with GenericClay
  object LightGrayHardenedClay extends Material with GenericClay
  object CyanHardenedClay extends Material with GenericClay
  object PurpleHardenedClay extends Material with GenericClay
  object BlueHardenedClay extends Material with GenericClay
  object BrownHardenedClay extends Material with GenericClay
  object GreenHardenedClay extends Material with GenericClay // same as item
  object RedHardenedClay extends Material with GenericClay // same as item
  object BlackHardenedClay extends Material with GenericClay // same as non inverted
  object WhiteGlassPane extends Material with Solid with Transparent with GenericGlass with GenericGlassPane
  object OrangeGlassPane extends Material with Solid with Transparent with GenericGlass with GenericGlassPane
  object MagentaGlassPane extends Material with Solid with Transparent with GenericGlass with GenericGlassPane
  object LightBlueGlassPane extends Material with Solid with Transparent with GenericGlass with GenericGlassPane
  object YellowGlassPane extends Material with Solid with Transparent with GenericGlass with GenericGlassPane
  object LimeGlassPane extends Material with Solid with Transparent with GenericGlass with GenericGlassPane
  object PinkGlassPane extends Material with Solid with Transparent with GenericGlass with GenericGlassPane
  object GrayGlassPane extends Material with Solid with Transparent with GenericGlass with GenericGlassPane
  object LightGrayGlassPane extends Material with Solid with Transparent with GenericGlass with GenericGlassPane
  object CyanGlassPane extends Material with Solid with Transparent with GenericGlass with GenericGlassPane
  object PurpleGlassPane extends Material with Solid with Transparent with GenericGlass with GenericGlassPane
  object BlueGlassPane extends Material with Solid with Transparent with GenericGlass with GenericGlassPane
  object BrownGlassPane extends Material with Solid with Transparent with GenericGlass with GenericGlassPane
  object GreenGlassPane extends Material with Solid with Transparent with GenericGlass with GenericGlassPane
  object RedGlassPane extends Material with Solid with Transparent with GenericGlass with GenericGlassPane
  object BlackGlassPane extends Material with Solid with Transparent with GenericGlass with GenericGlassPane
  object AcaciaLeaves extends Material(tierString = "T2") with Solid with Transparent with Crushable with GenericLeaves
  object DarkOakLeaves extends Material(tierString = "T2") with Solid with Transparent with Crushable with GenericLeaves
  object AcaciaLog extends Material(tierString = "T2") with Solid with GenericLog
  object DarkOakLog extends Material(tierString = "T2") with Solid with GenericLog
  object AcaciaStairs extends Material with Solid with GenericStairs
  object DarkOakStairs extends Material with Solid with GenericStairs
  object SlimeBlock extends Material
  object Barrier extends Material(energy = None) with Solid with Unconsumable
  object IronTrapdoor extends Material
  object Prismarine extends Material
  object PrismarineBrick extends Material
  object DarkPrismarine extends Material
  object SeaLantern extends Material
  object HayBale extends Material
  object WhiteCarpet extends Material with Transparent with Attaches with GenericCarpet
  object OrangeCarpet extends Material with Transparent with Attaches with GenericCarpet
  object MagentaCarpet extends Material with Transparent with Attaches with GenericCarpet
  object LightBlueCarpet extends Material with Transparent with Attaches with GenericCarpet
  object YellowCarpet extends Material with Transparent with Attaches with GenericCarpet
  object LimeCarpet extends Material with Transparent with Attaches with GenericCarpet
  object PinkCarpet extends Material with Transparent with Attaches with GenericCarpet
  object GrayCarpet extends Material with Transparent with Attaches with GenericCarpet
  object LightGrayCarpet extends Material with Transparent with Attaches with GenericCarpet
  object CyanCarpet extends Material with Transparent with Attaches with GenericCarpet
  object PurpleCarpet extends Material with Transparent with Attaches with GenericCarpet
  object BlueCarpet extends Material with Transparent with Attaches with GenericCarpet
  object BrownCarpet extends Material with Transparent with Attaches with GenericCarpet
  object GreenCarpet extends Material with Transparent with Attaches with GenericCarpet
  object RedCarpet extends Material with Transparent with Attaches with GenericCarpet
  object BlackCarpet extends Material with Transparent with Attaches with GenericCarpet
  object HardenedClay extends Material with Solid with GenericClay
  object CoalBlock extends Material
  object PackedIce extends Material(tierString = "T3")
  object DoublePlant extends Material(tierString = "T2") with Transparent
  object Sunflower extends Material(tierString = "T2") with Transparent
  object Lilac extends Material(tierString = "T2") with Transparent
  object DoubleTallgrass extends Material(tierString = "T2") with Transparent
  object LargeFern extends Material(tierString = "T2") with Transparent
  object RoseBush extends Material(tierString = "T2") with Transparent
  object Peony extends Material(tierString = "T2") with Transparent
  object TopPlantHalf extends Material(energy = 1) with Transparent
  object StandingBanner extends Material with Transparent with Attaches
  object WallBanner extends Material with Transparent with Attaches
  object InvertedDaylightSensor extends Material
  object RedSandstone extends Material
  object SmoothRedSandstone extends Material
  object ChiseledRedSandstone extends Material
  object RedSandstoneStairs extends Material with Solid with GenericStairs
  object RedSandstoneDoubleSlab extends Material with Solid with GenericDoubleSlab
  object RedSandstoneSingleSlab extends Material with Solid with GenericSingleSlab
  object SpruceFenceGate extends Material with Solid with Transparent with GenericFence with GenericFenceGate
  object BirchFenceGate extends Material with Solid with Transparent with GenericFence with GenericFenceGate // 4 food
  object JungleFenceGate extends Material with Solid with Transparent with GenericFence with GenericFenceGate
  object DarkOakFenceGate extends Material with Solid with Transparent with GenericFence with GenericFenceGate
  object AcaciaFenceGate extends Material with Solid with Transparent with GenericFence with GenericFenceGate
  object SpruceFence extends Material with Solid with Transparent with GenericFence
  object BirchFence extends Material with Solid with Transparent with GenericFence
  object JungleFence extends Material with Solid with Transparent with GenericFence
  object DarkOakFence extends Material with Solid with Transparent with GenericFence
  object AcaciaFence extends Material with Solid with Transparent with GenericFence
  object SpruceDoor extends Material with Solid with Transparent with GenericDoor
  object BirchDoor extends Material with Solid with Transparent with GenericDoor
  object JungleDoor extends Material with Solid with Transparent with GenericDoor
  object AcaciaDoor extends Material with Solid with Transparent with GenericDoor
  object DarkOakDoor extends Material with Solid with Transparent with GenericDoor
  object EndRod extends Material(tierString = "T5") with Solid with Transparent
  object ChorusPlant extends Material(tierString = "T2")
  object ChorusFlower extends Material(tierString = "T2") with Solid with Crushable
  object PurpurBlock extends Material(tierString = "T4")
  object PurpurPillar extends Material(tierString = "T4")
  object PurpurStairs extends Material with Solid with GenericStairs
  object PurpurDoubleSlab extends Material with Solid with GenericDoubleSlab
  object PurpurSingleSlab extends Material with Solid with GenericSingleSlab
  object EndStoneBricks extends Material
  object BeetrootPlantation extends Material
  object GrassPath extends Material
  object EndGateway extends Material(energy = None) with Solid with Unconsumable
  object CommandRepeating extends Material(energy = None) with Solid with Unconsumable
  object CommandChain extends Material(energy = None) with Solid with Unconsumable
  object FrostedIce extends Material
  object Magma extends Material
  object NetherWartBlock extends Material
  object RedNetherBrick extends Material
  object BoneBlock extends Material
  object StructureVoid extends Material(energy = None) with Solid with Unconsumable
  object Observer extends Material(energy = None) with Solid with Unconsumable
  object WhiteShulkerBox extends Material with Solid with Inventory
  object OrangeShulkerBox extends Material with Solid with Inventory
  object MagentaShulkerBox extends Material with Solid with Inventory
  object LightBlueShulkerBox extends Material with Solid with Inventory
  object YellowShulkerBox extends Material with Solid with Inventory
  object LimeShulkerBox extends Material with Solid with Inventory
  object PinkShulkerBox extends Material with Solid with Inventory
  object GrayShulkerBox extends Material with Solid with Inventory
  object LightGrayShulkerBox extends Material with Solid with Inventory // Iron part*1.5
  object CyanShulkerBox extends Material with Solid with Inventory // Iron part*1.5
  object PurpleShulkerBox extends Material with Solid with Inventory // Iron part*1.5
  object BlueShulkerBox extends Material with Solid with Inventory // Iron part*1.5
  object BrownShulkerBox extends Material with Solid with Inventory
  object GreenShulkerBox extends Material with Solid with Inventory
  object RedShulkerBox extends Material with Solid with Inventory
  object BlackShulkerBox extends Material with Solid with Inventory
  object StructureSaveBlock extends Material(energy = None) with Solid with Inventory with Unconsumable
  object StructureLoadBlock extends Material(energy = None) with Unconsumable
  object StructureCornerBlock extends Material(energy = None) with Unconsumable
  object StructureDataBlock extends Material(energy = None) with Unconsumable

  object IronShovel extends Material with GenericShovel
  object IronPickaxe extends Material with GenericPickaxe
  object IronAxe extends Material(energy = 48052) with GenericAxe
  object FlintAndSteel extends Material with Consumable
  object Apple extends Material with Consumable with Food // 3 food
  object Bow extends Material(energy = 118) with Consumable
  object Arrow extends Material(energy = 5)
  object Coal extends Material(energy = 72)
  object Charcoal extends Material
  object Diamond extends Material(energy = 2869)
  object IronIngot extends Material(energy = 459)
  object GoldIngot extends Material(energy = 90551)
  object IronSword extends Material with Usable with GenericSword
  object WoodSword extends Material with Usable with GenericSword
  object WoodShovel extends Material with GenericShovel
  object WoodPickaxe extends Material with GenericPickaxe
  object WoodAxe extends Material with GenericAxe
  object StoneSword extends Material(tierString = "T3") with Usable with GenericSword
  object StoneShovel extends Material with GenericShovel
  object StonePickaxe extends Material with GenericPickaxe
  object StoneAxe extends Material with GenericAxe
  object DiamondSword extends Material with Usable with GenericSword
  object DiamondShovel extends Material with GenericShovel
  object DiamondPickaxe extends Material with GenericPickaxe
  object DiamondAxe extends Material with GenericAxe
  object Stick extends Material(energy = 31)
  object Bowl extends Material
  object MushroomStew extends Material with Consumable with Food
  object GoldSword extends Material(energy = 43307) with Usable with GenericSword
  object GoldShovel extends Material with GenericShovel
  object GoldPickaxe extends Material with GenericPickaxe
  object GoldAxe extends Material with GenericAxe
  object String extends Material(energy = 9)
  object Feather extends Material(energy = 1)
  object GunPowder extends Material(energy = 35)
  object WoodHoe extends Material with Consumable with GenericHoe
  object StoneHoe extends Material with Consumable with GenericHoe
  object IronHoe extends Material with Consumable with GenericHoe
  object DiamondHoe extends Material with Consumable with GenericHoe
  object GoldHoe extends Material with Consumable with GenericHoe
  object Seeds extends Material(tierString = "T2") with Consumable
  object Wheat extends Material(tierString = "T2.5")
  object Bread extends Material with Consumable with Food
  object LeatherHelmet extends Material with GenericHelmet
  object LeatherChestplate extends Material with GenericChestplate
  object LeatherLeggings extends Material with GenericLeggings
  object LeatherBoots extends Material with GenericBoots
  object ChainmailHelmet extends Material with GenericHelmet
  object ChainmailChestplate extends Material with GenericChestplate
  object ChainmailLeggings extends Material with GenericLeggings
  object ChainmailBoots extends Material with GenericBoots
  object IronHelmet extends Material with GenericHelmet
  object IronChestplate extends Material with GenericChestplate
  object IronLeggings extends Material with GenericLeggings
  object IronBoots extends Material with GenericBoots
  object DiamondHelmet extends Material with GenericHelmet
  object DiamondChestplate extends Material with GenericChestplate
  object DiamondLeggings extends Material with GenericLeggings
  object DiamondBoots extends Material with GenericBoots
  object GoldHelmet extends Material with GenericHelmet
  object GoldChestplate extends Material with GenericChestplate
  object GoldLeggings extends Material with GenericLeggings
  object GoldBoots extends Material with GenericBoots
  object Flint extends Material
  object RawPorkchop extends Material(energy = 1) with Consumable with Food
  object CookedPorkchop extends Material with Consumable with Food
  object Painting extends Material
  object GoldenApple extends Material with Consumable with Food
  object EnchantedGoldenApple extends Material with Consumable with Food
  object Sign extends Material
  object OakDoorItem extends Material with GenericDoor
  object Bucket extends Material with Consumable
  object WaterBucket extends Material with Consumable
  object LavaBucket extends Material with Consumable
  object Minecart extends Material
  object Saddle extends Material(tierString = "T3")
  object IronDoor extends Material with GenericDoor
  object Redstone extends Material(energy = 35)
  object SnowBall extends Material(energy = 1) with Consumable
  object OakBoat extends Material
  object Leather extends Material(energy = 1)
  object MilkBucket extends Material with Consumable with Food
  object ClayBrick extends Material with GenericClay
  object ClayBall extends Material(tierString = "T2") with GenericClay
  object SugarCane extends Material(tierString = "T2")
  object Paper extends Material
  object Book extends Material
  object SlimeBall extends Material(energy = 2)
  object StorageMinecart extends Material
  object PoweredMinecart extends Material
  object Egg extends Material(tierString = "T1") with Consumable
  object Compass extends Material
  object FishingRod extends Material with Consumable
  object Watch extends Material
  object GlowstoneDust extends Material(energy = 35)
  object RawFish extends Material(energy = 33) with Consumable with Food
  object RawSalmon extends Material(energy = 43) with Consumable with Food
  object Clownfish extends Material(energy = 3910) with Consumable with Food
  object Pufferfish extends Material(energy = 3903) with Consumable with Food
  object CookedFish extends Material with Consumable with Food
  object CookedSalmon extends Material with Consumable with Food
  object InkSack extends Material(energy = 1)
  object RoseRed extends Material
  object CactusGreen extends Material
  object CocoaBeans extends Material(tierString = "T2")
  object LapisLazuli extends Material(tierString = "T4")
  object PurpleDye extends Material
  object CyanDye extends Material
  object LightGrayDye extends Material
  object GrayDye extends Material
  object PinkDye extends Material
  object LimeDye extends Material
  object DandelionYellow extends Material
  object LightBlueDye extends Material
  object MagentaDye extends Material
  object OrangeDye extends Material
  object BoneMeal extends Material
  object Bone extends Material(energy = 5)
  object Sugar extends Material(energy = 35)
  object Cake extends Material with Consumable with Food
  object Bed extends Material
  object RedstoneRepeater extends Material
  object Cookie extends Material with Consumable
  object DrawnMap extends Material(energy = None) with Unconsumable
  object Shears extends Material
  object Melon extends Material with Food
  object PumpkinSeeds extends Material
  object MelonSeeds extends Material
  object RawBeef extends Material(energy = 1) with Consumable with Food
  object CookedBeef extends Material with Consumable with Food
  object RawChicken extends Material(energy = 1) with Consumable with Food
  object CookedChicken extends Material with Consumable with Food
  object RottenFlesh extends Material(energy = 10) with Consumable with Food
  object EnderPearl extends Material(energy = 1090) with Consumable
  object BlazeRod extends Material(energy = 2322)
  object GhastTear extends Material(energy = 269)
  object GoldNugget extends Material(energy = 2490)
  object NetherWart extends Material(tierString = "T3")
  object Potion extends Material(energy = None) with Consumable with Unconsumable
  object GlassBottle extends Material(energy = 35) with GenericGlass
  object SpiderEye extends Material(energy = 35) with Consumable with Food
  object FermentedSpiderEye extends Material
  object BlazePowder extends Material
  object MagmaCream extends Material(energy = 386)
  object BrewingStandItem extends Material with Inventory
  object CauldronItem extends Material
  object EyeOfEnder extends Material
  object GlisteringMelon extends Material
  object MonsterEgg extends Material(energy = None) with Consumable with Unconsumable
  object ExpBottle extends Material(energy = None) with Consumable with Unconsumable
  object FireCharge extends Material with Consumable
  object BookAndQuill extends Material with Usable
  object WrittenBook extends Material(energy = None) with Usable with Unconsumable
  object Emerald extends Material(energy = 464)
  object ItemFrame extends Material
  object FlowerPotItem extends Material
  object CarrotItem extends Material(energy = 5498) with Consumable with Food
  object PotatoItem extends Material(energy = 5498) with Consumable with Food
  object BakedPotato extends Material with Consumable with Food
  object PoisonousPotato extends Material with Consumable with Food
  object EmptyMap extends Material with Usable
  object GoldenCarrot extends Material with Consumable with Food
  object SkeletonHead extends Material(energy = None) with Unconsumable
  object WitherSkeletonHead extends Material(energy = None) with Unconsumable
  object ZombieHead extends Material(energy = None) with Unconsumable
  object PlayerHead extends Material(energy = None) with Unconsumable
  object CreeperHead extends Material(energy = None) with Unconsumable
  object DragonHead extends Material(energy = None) with Unconsumable
  object CarrotStick extends Material
  object NetherStar extends Material(energy = 62141)
  object PumpkinPie extends Material with Consumable with Food
  object FireworkRocket extends Material with Consumable
  object FireworkStar extends Material
  object EnchantedBook extends Material(energy = None) with Unconsumable
  object RedstoneComparator extends Material
  object NetherBrickItem extends Material
  object Quartz extends Material(tierString = "T3")
  object ExplosiveMinecart extends Material
  object HopperMinecart extends Material with Inventory
  object PrismarineShard extends Material(energy = 939)
  object PrismarineCrystals extends Material(energy = 5332)
  object RawRabbit extends Material(energy = 1) with Consumable with Food
  object CookedRabbit extends Material with Consumable with Food
  object RabbitStew extends Material with Consumable with Food
  object RabbitFoot extends Material(energy = 11)
  object RabbitHide extends Material(energy = 1)
  object ArmorStand extends Material
  object IronHorseArmor extends Material(tierString = "T3")
  object GoldHorseArmor extends Material(tierString = "T4")
  object DiamondHorseArmor extends Material(tierString = "T5")
  object Leash extends Material
  object NameTag extends Material(tierString = "T4")
  object CommandMinecart extends Material(energy = None) with Unconsumable
  object RawMutton extends Material(energy = 1) with Consumable
  object CookedMutton extends Material with Consumable
  object Banner extends Material
  object EndCrystal extends Material
  object SpruceDoorItem extends Material with GenericDoor
  object BirchDoorItem extends Material with GenericDoor
  object JungleDoorItem extends Material with GenericDoor
  object AcaciaDoorItem extends Material(tierString = "T2") with GenericDoor
  object DarkOakDoorItem extends Material with GenericDoor
  object ChorusFruit extends Material(tierString = "T6") with Consumable
  object PoppedChorusFruit extends Material
  object Beetroot extends Material with Consumable with Food
  object BeetrootSeeds extends Material(tierString = "T2") with Consumable
  object BeetrootSoup extends Material with Consumable with Food
  object DragonsBreath extends Material(tierString = "T6")
  object SplashPotion extends Material(energy = None) with Consumable with Unconsumable
  object SpectralArrow extends Material(energy = None) with Unconsumable
  object TippedArrow extends Material(energy = None) with Unconsumable
  object LingeringPotion extends Material(energy = None) with Consumable with Unconsumable
  object Shield extends Material with Consumable
  object Elytra extends Material(tierString = "T6") with Consumable
  object SpruceBoat extends Material
  object BirchBoat extends Material
  object JungleBoat extends Material
  object AcaciaBoat extends Material
  object DarkOakBoat extends Material
  object TotemOfUndying extends Material(energy = 488)
  object ShulkerShell extends Material(energy = 951)
  object IronNugget extends Material
  object GoldRecord extends Material(tierString = "T4")
  object GreenRecord extends Material(tierString = "T4")
  object Record3 extends Material(tierString = "T4")
  object Record4 extends Material(tierString = "T4")
  object Record5 extends Material(tierString = "T4")
  object Record6 extends Material(tierString = "T4")
  object Record7 extends Material(tierString = "T4")
  object Record8 extends Material(tierString = "T4")
  object Record9 extends Material(tierString = "T4")
  object Record10 extends Material(tierString = "T4")
  object Record11 extends Material(tierString = "T4")
  object Record12 extends Material(tierString = "T4")
  //@formatter:on
}
