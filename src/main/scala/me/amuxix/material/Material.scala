package me.amuxix.material

import com.github.ghik.silencer.silent
import enumeratum._
import me.amuxix.Named
import me.amuxix.pattern.Element
import org.bukkit.CoalType
import org.bukkit.DyeColor._
import org.bukkit.GrassSpecies._
import org.bukkit.Material.{TNT => BTNT, _}
import org.bukkit.SandstoneType._
import org.bukkit.TreeSpecies._
import org.bukkit.material.{MaterialData, _}

import scala.collection.immutable.{HashMap, IndexedSeq}
import scala.math.{E, log}
import scala.reflect.ClassTag

/**
  * Created by Amuxix on 04/01/2017.
  */

sealed abstract case class Material(private var _energy: Option[Int] = None) extends EnumEntry with Element with Named {
  def this(energy: Int) = this(Some(energy))
  def this(tier: BaseTier) = this(Some(tier.energy))

  lazy val tier: Option[Int] = {
    _energy match {
      case Some(e) => Some((log(e) / log(E * 2)).round.toInt)
      case None => None
    }
  }

  def energy: Option[Int] = _energy
  def energy_=(energy: Int): Unit = {
    require(this.isInstanceOf[NoEnergy] == false, s"Something tried to modify energy of $name which has NoEnergy trait.")
    _energy = Some(energy)
  }

  override def toString: String = s"$name(${if (this.isInstanceOf[NoEnergy]) "NoEnergy" else energy.getOrElse("None")})"

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

object Material extends CirceEnum[Material] with Enum[Material] {

  type ServerMaterial <: Material.type

  @silent protected[material] val materialDataToMaterial: Map[MaterialData, Material] = HashMap(
    new MaterialData(AIR) -> Air,
    new MaterialData(STONE) -> Stone,
    new MaterialData(STONE, 1.toByte) -> Granite,
    new MaterialData(STONE, 2.toByte) -> PolishedGranite,
    new MaterialData(STONE, 3.toByte) -> Diorite,
    new MaterialData(STONE, 4.toByte) -> PolishedDiorite,
    new MaterialData(STONE, 5.toByte) -> Andesite,
    new MaterialData(STONE, 6.toByte) -> PolishedAndesite,
    new MaterialData(GRASS) -> Grass,
    new MaterialData(DIRT) -> Dirt,
    new MaterialData(DIRT, 1.toByte) -> CoarseDirt,
    new MaterialData(DIRT, 2.toByte) -> Podzol,
    new MaterialData(COBBLESTONE) -> Cobblestone,
    new Wood(GENERIC) -> OakWoodPlanks,
    new Wood(REDWOOD) -> SpruceWoodPlanks,
    new Wood(BIRCH) -> BirchWoodPlanks,
    new Wood(JUNGLE) -> JungleWoodPlanks,
    new Wood(ACACIA) -> AcaciaWoodPlanks,
    new Wood(DARK_OAK) -> DarkOakWoodPlanks,
    new org.bukkit.material.Sapling(GENERIC) -> OakSapling,
    new org.bukkit.material.Sapling(REDWOOD) -> SpruceSapling,
    new org.bukkit.material.Sapling(BIRCH) -> BirchSapling,
    new org.bukkit.material.Sapling(JUNGLE) -> JungleSapling,
    new org.bukkit.material.Sapling(ACACIA) -> AcaciaSapling,
    new org.bukkit.material.Sapling(DARK_OAK) -> DarkOakSapling,
    new MaterialData(BEDROCK) -> Bedrock,
    new MaterialData(WATER) -> Water,
    new MaterialData(STATIONARY_WATER) -> StationaryWater,
    new MaterialData(LAVA) -> Lava,
    new MaterialData(STATIONARY_LAVA) -> StationaryLava,
    new MaterialData(SAND) -> Sand,
    new MaterialData(SAND, 1.toByte) -> RedSand,
    new MaterialData(GRAVEL) -> Gravel,
    new MaterialData(GOLD_ORE) -> GoldOre,
    new MaterialData(IRON_ORE) -> IronOre,
    new MaterialData(COAL_ORE) -> CoalOre,
    new Tree(GENERIC) -> OakLog,
    new Tree(REDWOOD) -> SpruceLog,
    new Tree(BIRCH) -> BirchLog,
    new Tree(JUNGLE) -> JungleLog,
    new org.bukkit.material.Leaves(GENERIC) -> OakLeaves,
    new org.bukkit.material.Leaves(REDWOOD) -> SpruceLeaves,
    new org.bukkit.material.Leaves(BIRCH) -> BirchLeaves,
    new org.bukkit.material.Leaves(JUNGLE) -> JungleLeaves,
    new MaterialData(SPONGE) -> Sponge,
    new MaterialData(SPONGE, 1.toByte) -> WetSponge,
    new MaterialData(GLASS) -> Glass,
    new MaterialData(LAPIS_ORE) -> LapisLazuliOre,
    new MaterialData(LAPIS_BLOCK) -> LapisLazuliBlock,
    new MaterialData(DISPENSER) -> Dispenser,
    new Sandstone(CRACKED) -> Sandstone,
    new Sandstone(SMOOTH) -> SmoothSandstone,
    new Sandstone(GLYPHED) -> ChiseledSandstone,
    new MaterialData(NOTE_BLOCK) -> NoteBlock,
    new MaterialData(BED_BLOCK) -> BedBlock,
    new MaterialData(POWERED_RAIL) -> PoweredRails,
    new MaterialData(DETECTOR_RAIL) -> DetectorRails,
    new MaterialData(PISTON_STICKY_BASE) -> StickyPiston,
    new MaterialData(WEB) -> Cobweb,
    new LongGrass(DEAD) -> Shrub,
    new LongGrass(NORMAL) -> LongGrass,
    new LongGrass(FERN_LIKE) -> Fern,
    new MaterialData(DEAD_BUSH) -> DeadBush,
    new MaterialData(PISTON_BASE) -> Piston,
    new MaterialData(PISTON_EXTENSION) -> PistonExtension,
    new org.bukkit.material.Wool(WHITE) -> WhiteWool,
    new org.bukkit.material.Wool(ORANGE) -> OrangeWool,
    new org.bukkit.material.Wool(MAGENTA) -> MagentaWool,
    new org.bukkit.material.Wool(LIGHT_BLUE) -> LightBlueWool,
    new org.bukkit.material.Wool(YELLOW) -> YellowWool,
    new org.bukkit.material.Wool(LIME) -> LimeWool,
    new org.bukkit.material.Wool(PINK) -> PinkWool,
    new org.bukkit.material.Wool(GRAY) -> GrayWool,
    new org.bukkit.material.Wool(SILVER) -> LightGrayWool,
    new org.bukkit.material.Wool(CYAN) -> CyanWool,
    new org.bukkit.material.Wool(PURPLE) -> PurpleWool,
    new org.bukkit.material.Wool(BLUE) -> BlueWool,
    new org.bukkit.material.Wool(BROWN) -> BrownWool,
    new org.bukkit.material.Wool(GREEN) -> GreenWool,
    new org.bukkit.material.Wool(RED) -> RedWool,
    new org.bukkit.material.Wool(BLACK) -> BlackWool,
    new MaterialData(PISTON_MOVING_PIECE) -> PistonMovingPiece,
    new MaterialData(YELLOW_FLOWER) -> Dandelion,
    new MaterialData(RED_ROSE) -> Poppy,
    new MaterialData(RED_ROSE, 1.toByte) -> BlueOrchid,
    new MaterialData(RED_ROSE, 2.toByte) -> Allium,
    new MaterialData(RED_ROSE, 3.toByte) -> AzureBluet,
    new MaterialData(RED_ROSE, 4.toByte) -> RedTulip,
    new MaterialData(RED_ROSE, 5.toByte) -> OrangeTulip,
    new MaterialData(RED_ROSE, 6.toByte) -> WhiteTulip,
    new MaterialData(RED_ROSE, 7.toByte) -> PinkTulip,
    new MaterialData(RED_ROSE, 8.toByte) -> OxeyeDaisy,
    new MaterialData(BROWN_MUSHROOM) -> BrownMushroom,
    new MaterialData(RED_MUSHROOM) -> RedMushroom,
    new MaterialData(GOLD_BLOCK) -> GoldBlock,
    new MaterialData(IRON_BLOCK) -> IronBlock,
    new MaterialData(DOUBLE_STEP) -> StoneDoubleSlab,
    new MaterialData(DOUBLE_STEP, 1.toByte) -> SandstoneDoubleSlab,
    new MaterialData(DOUBLE_STEP, 2.toByte) -> OldWoodDoubleSlab,
    new MaterialData(DOUBLE_STEP, 3.toByte) -> CobblestoneDoubleSlab,
    new MaterialData(DOUBLE_STEP, 4.toByte) -> BrickDoubleSlab,
    new MaterialData(DOUBLE_STEP, 5.toByte) -> StoneBrickDoubleSlab,
    new MaterialData(DOUBLE_STEP, 6.toByte) -> NetherBrickDoubleSlab,
    new MaterialData(DOUBLE_STEP, 7.toByte) -> QuartzDoubleSlab,
    new MaterialData(STEP) -> StoneSingleSlab,
    new MaterialData(STEP, 1.toByte) -> SandstoneSingleSlab,
    new MaterialData(STEP, 2.toByte) -> OldWoodSingleSlab,
    new MaterialData(STEP, 3.toByte) -> CobblestoneSingleSlab,
    new MaterialData(STEP, 4.toByte) -> BrickSingleSlab,
    new MaterialData(STEP, 5.toByte) -> StoneBrickSingleSlab,
    new MaterialData(STEP, 6.toByte) -> NetherBrickSingleSlab,
    new MaterialData(STEP, 7.toByte) -> QuartzSingleSlab,
    new MaterialData(BRICK) -> BrickBlock,
    new MaterialData(BTNT) -> TNT,
    new MaterialData(BOOKSHELF) -> Bookshelf,
    new MaterialData(MOSSY_COBBLESTONE) -> MossyCobblestone,
    new MaterialData(OBSIDIAN) -> Obsidian,
    new MaterialData(TORCH) -> Torch,
    new MaterialData(FIRE) -> Fire,
    new MaterialData(MOB_SPAWNER) -> MobSpawner,
    new MaterialData(WOOD_STAIRS) -> OakStairs,
    new MaterialData(CHEST) -> Chest,
    new MaterialData(REDSTONE_WIRE) -> RedstoneWire,
    new MaterialData(DIAMOND_ORE) -> DiamondOre,
    new MaterialData(DIAMOND_BLOCK) -> DiamondBlock,
    new MaterialData(WORKBENCH) -> CraftingTable,
    new MaterialData(CROPS) -> Crops,
    new MaterialData(SOIL) -> Soil,
    new MaterialData(FURNACE) -> Furnace,
    new MaterialData(BURNING_FURNACE) -> BurningFurnace,
    new MaterialData(SIGN_POST) -> SignPost,
    new MaterialData(WOODEN_DOOR) -> OakDoor,
    new MaterialData(LADDER) -> Ladder,
    new MaterialData(RAILS) -> Rails,
    new MaterialData(COBBLESTONE_STAIRS) -> CobblestoneStairs,
    new MaterialData(WALL_SIGN) -> WallSign,
    new MaterialData(LEVER) -> Lever,
    new MaterialData(STONE_PLATE) -> StonePressurePlate,
    new MaterialData(IRON_DOOR_BLOCK) -> IronDoorBlock,
    new MaterialData(WOOD_PLATE) -> WoodPressurePlate,
    new MaterialData(REDSTONE_ORE) -> RedstoneOre,
    new MaterialData(GLOWING_REDSTONE_ORE) -> GlowingRedstoneOre,
    new MaterialData(REDSTONE_TORCH_OFF) -> RedstoneTorchOff,
    new MaterialData(REDSTONE_TORCH_ON) -> RedstoneTorchOn,
    new MaterialData(STONE_BUTTON) -> StoneButton,
    new MaterialData(SNOW) -> Snow,
    new MaterialData(ICE) -> Ice,
    new MaterialData(SNOW_BLOCK) -> SnowBlock,
    new MaterialData(CACTUS) -> Cactus,
    new MaterialData(CLAY) -> Clay,
    new MaterialData(SUGAR_CANE_BLOCK) -> SugarCaneBlock,
    new MaterialData(JUKEBOX) -> Jukebox,
    new MaterialData(FENCE) -> OakFence,
    new MaterialData(PUMPKIN) -> Pumpkin,
    new MaterialData(NETHERRACK) -> Netherrack,
    new MaterialData(SOUL_SAND) -> SoulSand,
    new MaterialData(GLOWSTONE) -> Glowstone,
    new MaterialData(PORTAL) -> Portal,
    new MaterialData(JACK_O_LANTERN) -> JackOLantern,
    new MaterialData(CAKE_BLOCK) -> CakeBlock,
    new MaterialData(DIODE_BLOCK_OFF) -> RedstoneRepeaterOff,
    new MaterialData(DIODE_BLOCK_ON) -> RedstoneRepeaterOn,
    new MaterialData(STAINED_GLASS) -> WhiteGlass,
    new MaterialData(STAINED_GLASS, 1.toByte) -> OrangeGlass,
    new MaterialData(STAINED_GLASS, 2.toByte) -> MagentaGlass,
    new MaterialData(STAINED_GLASS, 3.toByte) -> LightBlueGlass,
    new MaterialData(STAINED_GLASS, 4.toByte) -> YellowGlass,
    new MaterialData(STAINED_GLASS, 5.toByte) -> LimeGlass,
    new MaterialData(STAINED_GLASS, 6.toByte) -> PinkGlass,
    new MaterialData(STAINED_GLASS, 7.toByte) -> GrayGlass,
    new MaterialData(STAINED_GLASS, 8.toByte) -> LightGrayGlass,
    new MaterialData(STAINED_GLASS, 9.toByte) -> CyanGlass,
    new MaterialData(STAINED_GLASS, 10.toByte) -> PurpleGlass,
    new MaterialData(STAINED_GLASS, 11.toByte) -> BlueGlass,
    new MaterialData(STAINED_GLASS, 12.toByte) -> BrownGlass,
    new MaterialData(STAINED_GLASS, 13.toByte) -> GreenGlass,
    new MaterialData(STAINED_GLASS, 14.toByte) -> RedGlass,
    new MaterialData(STAINED_GLASS, 15.toByte) -> BlackGlass,
    new MaterialData(TRAP_DOOR) -> TrapDoor,
    new MaterialData(MONSTER_EGGS) -> MonsterEggs,
    new MaterialData(SMOOTH_BRICK) -> StoneBrick,
    new MaterialData(SMOOTH_BRICK, 1.toByte) -> CrackedStoneBrick,
    new MaterialData(SMOOTH_BRICK, 2.toByte) -> MossyStoneBrick,
    new MaterialData(SMOOTH_BRICK, 3.toByte) -> ChiseledStoneBrick,
    new MaterialData(HUGE_MUSHROOM_1) -> BrownMushroomBlock,
    new MaterialData(HUGE_MUSHROOM_2) -> RedMushroomBlock,
    new MaterialData(IRON_FENCE) -> IronFence,
    new MaterialData(THIN_GLASS) -> GlassPane,
    new MaterialData(MELON_BLOCK) -> MelonBlock,
    new MaterialData(PUMPKIN_STEM) -> PumpkinStem,
    new MaterialData(MELON_STEM) -> MelonStem,
    new MaterialData(VINE) -> Vine,
    new MaterialData(FENCE_GATE) -> OakFenceGate,
    new MaterialData(BRICK_STAIRS) -> BrickStairs,
    new MaterialData(SMOOTH_STAIRS) -> StoneBrickStairs,
    new MaterialData(MYCEL) -> Mycelium,
    new MaterialData(WATER_LILY) -> WaterLily,
    new MaterialData(NETHER_BRICK) -> NetherBrick,
    new MaterialData(NETHER_FENCE) -> NetherBrickFence,
    new MaterialData(NETHER_BRICK_STAIRS) -> NetherBrickStairs,
    new MaterialData(NETHER_WARTS) -> NetherWarts,
    new MaterialData(ENCHANTMENT_TABLE) -> EnchantmentTable,
    new MaterialData(BREWING_STAND) -> BrewingStand,
    new MaterialData(CAULDRON) -> Cauldron,
    new MaterialData(ENDER_PORTAL) -> EnderPortal,
    new MaterialData(ENDER_PORTAL_FRAME) -> EnderPortalFrame,
    new MaterialData(ENDER_STONE) -> EndStone,
    new MaterialData(DRAGON_EGG) -> DragonEgg,
    new MaterialData(REDSTONE_LAMP_OFF) -> RedstoneLampOff,
    new MaterialData(REDSTONE_LAMP_ON) -> RedstoneLampOn,
    new WoodenStep(GENERIC) -> OakSingleSlab,
    new WoodenStep(REDWOOD) -> SpruceSingleSlab,
    new WoodenStep(BIRCH) -> BirchSingleSlab,
    new WoodenStep(JUNGLE) -> JungleSingleSlab,
    new WoodenStep(ACACIA) -> AcaciaSingleSlab,
    new WoodenStep(DARK_OAK) -> DarkOakSingleSlab,
    new MaterialData(WOOD_DOUBLE_STEP) -> OakDoubleSlab,
    new MaterialData(WOOD_DOUBLE_STEP, 1.toByte) -> SpruceDoubleSlab,
    new MaterialData(WOOD_DOUBLE_STEP, 2.toByte) -> BirchDoubleSlab,
    new MaterialData(WOOD_DOUBLE_STEP, 3.toByte) -> JungleDoubleSlab,
    new MaterialData(WOOD_DOUBLE_STEP, 4.toByte) -> AcaciaDoubleSlab,
    new MaterialData(WOOD_DOUBLE_STEP, 5.toByte) -> DarkOakDoubleSlab,
    new MaterialData(COCOA) -> Cocoa,
    new MaterialData(SANDSTONE_STAIRS) -> SandstoneStairs,
    new MaterialData(EMERALD_ORE) -> EmeraldOre,
    new MaterialData(ENDER_CHEST) -> EnderChest,
    new MaterialData(TRIPWIRE_HOOK) -> TripwireHook,
    new MaterialData(TRIPWIRE) -> Tripwire,
    new MaterialData(EMERALD_BLOCK) -> EmeraldBlock,
    new MaterialData(SPRUCE_WOOD_STAIRS) -> SpruceStairs,
    new MaterialData(BIRCH_WOOD_STAIRS) -> BirchStairs,
    new MaterialData(JUNGLE_WOOD_STAIRS) -> JungleStairs,
    new MaterialData(COMMAND) -> Command,
    new MaterialData(BEACON) -> Beacon,
    new MaterialData(COBBLE_WALL) -> CobblestoneWall,
    new MaterialData(COBBLE_WALL, 1.toByte) -> MossyCobblestoneWall,
    new MaterialData(FLOWER_POT) -> FlowerPot,
    new MaterialData(CARROT) -> Carrot,
    new MaterialData(POTATO) -> Potato,
    new MaterialData(WOOD_BUTTON) -> WoodButton,
    new MaterialData(SKULL) -> Skull,
    new MaterialData(ANVIL) -> Anvil,
    new MaterialData(TRAPPED_CHEST) -> TrappedChest,
    new MaterialData(GOLD_PLATE) -> GoldPressurePlate,
    new MaterialData(IRON_PLATE) -> IronPressurePlate,
    new MaterialData(REDSTONE_COMPARATOR_OFF) -> RedstoneComparatorOff,
    new MaterialData(REDSTONE_COMPARATOR_ON) -> RedstoneComparatorOn,
    new MaterialData(DAYLIGHT_DETECTOR) -> DaylightSensor,
    new MaterialData(REDSTONE_BLOCK) -> RedstoneBlock,
    new MaterialData(QUARTZ_ORE) -> QuartzOre,
    new MaterialData(HOPPER) -> Hopper,
    new MaterialData(QUARTZ_BLOCK) -> QuartzBlock,
    new MaterialData(QUARTZ_BLOCK, 1.toByte) -> ChiseledQuartzBlock,
    new MaterialData(QUARTZ_BLOCK, 2.toByte) -> PillarQuartzBlock,
    new MaterialData(QUARTZ_STAIRS) -> QuartzStairs,
    new MaterialData(ACTIVATOR_RAIL) -> ActivatorRails,
    new MaterialData(DROPPER) -> Dropper,
    new MaterialData(STAINED_CLAY) -> WhiteTerracotta,
    new MaterialData(STAINED_CLAY, 1.toByte) -> OrangeTerracotta,
    new MaterialData(STAINED_CLAY, 2.toByte) -> MagentaTerracotta,
    new MaterialData(STAINED_CLAY, 3.toByte) -> LightBlueTerracotta,
    new MaterialData(STAINED_CLAY, 4.toByte) -> YellowTerracotta,
    new MaterialData(STAINED_CLAY, 5.toByte) -> LimeTerracotta,
    new MaterialData(STAINED_CLAY, 6.toByte) -> PinkTerracotta,
    new MaterialData(STAINED_CLAY, 7.toByte) -> GrayTerracotta,
    new MaterialData(STAINED_CLAY, 8.toByte) -> LightGrayTerracotta,
    new MaterialData(STAINED_CLAY, 9.toByte) -> CyanTerracotta,
    new MaterialData(STAINED_CLAY, 10.toByte) -> PurpleTerracotta,
    new MaterialData(STAINED_CLAY, 11.toByte) -> BlueTerracotta,
    new MaterialData(STAINED_CLAY, 12.toByte) -> BrownTerracotta,
    new MaterialData(STAINED_CLAY, 13.toByte) -> GreenTerracotta,
    new MaterialData(STAINED_CLAY, 14.toByte) -> RedTerracotta,
    new MaterialData(STAINED_CLAY, 15.toByte) -> BlackTerracotta,
    new MaterialData(STAINED_GLASS_PANE) -> WhiteGlassPane,
    new MaterialData(STAINED_GLASS_PANE, 1.toByte) -> OrangeGlassPane,
    new MaterialData(STAINED_GLASS_PANE, 2.toByte) -> MagentaGlassPane,
    new MaterialData(STAINED_GLASS_PANE, 3.toByte) -> LightBlueGlassPane,
    new MaterialData(STAINED_GLASS_PANE, 4.toByte) -> YellowGlassPane,
    new MaterialData(STAINED_GLASS_PANE, 5.toByte) -> LimeGlassPane,
    new MaterialData(STAINED_GLASS_PANE, 6.toByte) -> PinkGlassPane,
    new MaterialData(STAINED_GLASS_PANE, 7.toByte) -> GrayGlassPane,
    new MaterialData(STAINED_GLASS_PANE, 8.toByte) -> LightGrayGlassPane,
    new MaterialData(STAINED_GLASS_PANE, 9.toByte) -> CyanGlassPane,
    new MaterialData(STAINED_GLASS_PANE, 10.toByte) -> PurpleGlassPane,
    new MaterialData(STAINED_GLASS_PANE, 11.toByte) -> BlueGlassPane,
    new MaterialData(STAINED_GLASS_PANE, 12.toByte) -> BrownGlassPane,
    new MaterialData(STAINED_GLASS_PANE, 13.toByte) -> GreenGlassPane,
    new MaterialData(STAINED_GLASS_PANE, 14.toByte) -> RedGlassPane,
    new MaterialData(STAINED_GLASS_PANE, 15.toByte) -> BlackGlassPane,
    new org.bukkit.material.Leaves(ACACIA) -> AcaciaLeaves,
    new org.bukkit.material.Leaves(DARK_OAK) -> DarkOakLeaves,
    new Tree(ACACIA) -> AcaciaLog,
    new Tree(DARK_OAK) -> DarkOakLog,
    new MaterialData(ACACIA_STAIRS) -> AcaciaStairs,
    new MaterialData(DARK_OAK_STAIRS) -> DarkOakStairs,
    new MaterialData(SLIME_BLOCK) -> SlimeBlock,
    new MaterialData(BARRIER) -> Barrier,
    new MaterialData(IRON_TRAPDOOR) -> IronTrapdoor,
    new MaterialData(PRISMARINE) -> Prismarine,
    new MaterialData(PRISMARINE, 1.toByte) -> PrismarineBrick,
    new MaterialData(PRISMARINE, 2.toByte) -> DarkPrismarine,
    new MaterialData(SEA_LANTERN) -> SeaLantern,
    new MaterialData(HAY_BLOCK) -> HayBale,
    new MaterialData(CARPET) -> WhiteCarpet,
    new MaterialData(CARPET, 1.toByte) -> OrangeCarpet,
    new MaterialData(CARPET, 2.toByte) -> MagentaCarpet,
    new MaterialData(CARPET, 3.toByte) -> LightBlueCarpet,
    new MaterialData(CARPET, 4.toByte) -> YellowCarpet,
    new MaterialData(CARPET, 5.toByte) -> LimeCarpet,
    new MaterialData(CARPET, 6.toByte) -> PinkCarpet,
    new MaterialData(CARPET, 7.toByte) -> GrayCarpet,
    new MaterialData(CARPET, 8.toByte) -> LightGrayCarpet,
    new MaterialData(CARPET, 9.toByte) -> CyanCarpet,
    new MaterialData(CARPET, 10.toByte) -> PurpleCarpet,
    new MaterialData(CARPET, 11.toByte) -> BlueCarpet,
    new MaterialData(CARPET, 12.toByte) -> BrownCarpet,
    new MaterialData(CARPET, 13.toByte) -> GreenCarpet,
    new MaterialData(CARPET, 14.toByte) -> RedCarpet,
    new MaterialData(CARPET, 15.toByte) -> BlackCarpet,

    new MaterialData(HARD_CLAY) -> Terracotta,
    
    new MaterialData(COAL_BLOCK) -> CoalBlock,
    new MaterialData(PACKED_ICE) -> PackedIce,
    new MaterialData(DOUBLE_PLANT) -> DoublePlant,
    new MaterialData(DOUBLE_PLANT, 1.toByte) -> Sunflower,
    new MaterialData(DOUBLE_PLANT, 2.toByte) -> Lilac,
    new MaterialData(DOUBLE_PLANT, 3.toByte) -> DoubleTallgrass,
    new MaterialData(DOUBLE_PLANT, 4.toByte) -> LargeFern,
    new MaterialData(DOUBLE_PLANT, 5.toByte) -> RoseBush,
    new MaterialData(DOUBLE_PLANT, 6.toByte) -> Peony,
    new MaterialData(DOUBLE_PLANT, 7.toByte) -> TopPlantHalf,
    new MaterialData(STANDING_BANNER) -> StandingBanner,
    new MaterialData(WALL_BANNER) -> WallBanner,
    new MaterialData(DAYLIGHT_DETECTOR_INVERTED) -> InvertedDaylightSensor,
    new MaterialData(RED_SANDSTONE) -> RedSandstone,
    new MaterialData(RED_SANDSTONE, 1.toByte) -> SmoothRedSandstone,
    new MaterialData(RED_SANDSTONE, 2.toByte) -> ChiseledRedSandstone,
    new MaterialData(RED_SANDSTONE_STAIRS) -> RedSandstoneStairs,
    new MaterialData(DOUBLE_STONE_SLAB2) -> RedSandstoneDoubleSlab,
    new MaterialData(STONE_SLAB2) -> RedSandstoneSingleSlab,
    new MaterialData(SPRUCE_FENCE_GATE) -> SpruceFenceGate,
    new MaterialData(BIRCH_FENCE_GATE) -> BirchFenceGate,
    new MaterialData(JUNGLE_FENCE_GATE) -> JungleFenceGate,
    new MaterialData(DARK_OAK_FENCE_GATE) -> DarkOakFenceGate,
    new MaterialData(ACACIA_FENCE_GATE) -> AcaciaFenceGate,
    new MaterialData(SPRUCE_FENCE) -> SpruceFence,
    new MaterialData(BIRCH_FENCE) -> BirchFence,
    new MaterialData(JUNGLE_FENCE) -> JungleFence,
    new MaterialData(DARK_OAK_FENCE) -> DarkOakFence,
    new MaterialData(ACACIA_FENCE) -> AcaciaFence,
    new MaterialData(SPRUCE_DOOR) -> SpruceDoor,
    new MaterialData(BIRCH_DOOR) -> BirchDoor,
    new MaterialData(JUNGLE_DOOR) -> JungleDoor,
    new MaterialData(ACACIA_DOOR) -> AcaciaDoor,
    new MaterialData(DARK_OAK_DOOR) -> DarkOakDoor,
    new MaterialData(END_ROD) -> EndRod,
    new MaterialData(CHORUS_PLANT) -> ChorusPlant,
    new MaterialData(CHORUS_FLOWER) -> ChorusFlower,
    new MaterialData(PURPUR_BLOCK) -> PurpurBlock,
    new MaterialData(PURPUR_PILLAR) -> PurpurPillar,
    new MaterialData(PURPUR_STAIRS) -> PurpurStairs,
    new MaterialData(PURPUR_DOUBLE_SLAB) -> PurpurDoubleSlab,
    new MaterialData(PURPUR_SLAB) -> PurpurSingleSlab,
    new MaterialData(END_BRICKS) -> EndStoneBricks,
    new MaterialData(BEETROOT_BLOCK) -> BeetrootPlantation,
    new MaterialData(GRASS_PATH) -> GrassPath,
    new MaterialData(END_GATEWAY) -> EndGateway,
    new MaterialData(COMMAND_REPEATING) -> CommandRepeating,
    new MaterialData(COMMAND_CHAIN) -> CommandChain,
    new MaterialData(FROSTED_ICE) -> FrostedIce,
    new MaterialData(MAGMA) -> Magma,
    new MaterialData(NETHER_WART_BLOCK) -> NetherWartBlock,
    new MaterialData(RED_NETHER_BRICK) -> RedNetherBrick,
    new MaterialData(BONE_BLOCK) -> BoneBlock,
    new MaterialData(STRUCTURE_VOID) -> StructureVoid,
    new MaterialData(OBSERVER) -> Observer,
    new MaterialData(WHITE_SHULKER_BOX) -> WhiteShulkerBox,
    new MaterialData(ORANGE_SHULKER_BOX) -> OrangeShulkerBox,
    new MaterialData(MAGENTA_SHULKER_BOX) -> MagentaShulkerBox,
    new MaterialData(LIGHT_BLUE_SHULKER_BOX) -> LightBlueShulkerBox,
    new MaterialData(YELLOW_SHULKER_BOX) -> YellowShulkerBox,
    new MaterialData(LIME_SHULKER_BOX) -> LimeShulkerBox,
    new MaterialData(PINK_SHULKER_BOX) -> PinkShulkerBox,
    new MaterialData(GRAY_SHULKER_BOX) -> GrayShulkerBox,
    new MaterialData(SILVER_SHULKER_BOX) -> LightGrayShulkerBox,
    new MaterialData(CYAN_SHULKER_BOX) -> CyanShulkerBox,
    new MaterialData(PURPLE_SHULKER_BOX) -> PurpleShulkerBox,
    new MaterialData(BLUE_SHULKER_BOX) -> BlueShulkerBox,
    new MaterialData(BROWN_SHULKER_BOX) -> BrownShulkerBox,
    new MaterialData(GREEN_SHULKER_BOX) -> GreenShulkerBox,
    new MaterialData(RED_SHULKER_BOX) -> RedShulkerBox,
    new MaterialData(BLACK_SHULKER_BOX) -> BlackShulkerBox,
    new MaterialData(WHITE_GLAZED_TERRACOTTA) -> WhiteGlazedTerracotta,
    new MaterialData(ORANGE_GLAZED_TERRACOTTA) -> OrangeGlazedTerracotta,
    new MaterialData(MAGENTA_GLAZED_TERRACOTTA) -> MagentaGlazedTerracotta,
    new MaterialData(LIGHT_BLUE_GLAZED_TERRACOTTA) -> LightBlueGlazedTerracotta,
    new MaterialData(YELLOW_GLAZED_TERRACOTTA) -> YellowGlazedTerracotta,
    new MaterialData(LIME_GLAZED_TERRACOTTA) -> LimeGlazedTerracotta,
    new MaterialData(PINK_GLAZED_TERRACOTTA) -> PinkGlazedTerracotta,
    new MaterialData(GRAY_GLAZED_TERRACOTTA) -> GrayGlazedTerracotta,
    new MaterialData(SILVER_GLAZED_TERRACOTTA) -> LightGrayGlazedTerracotta,
    new MaterialData(CYAN_GLAZED_TERRACOTTA) -> CyanGlazedTerracotta,
    new MaterialData(PURPLE_GLAZED_TERRACOTTA) -> PurpleGlazedTerracotta,
    new MaterialData(BLUE_GLAZED_TERRACOTTA) -> BlueGlazedTerracotta,
    new MaterialData(BROWN_GLAZED_TERRACOTTA) -> BrownGlazedTerracotta,
    new MaterialData(GREEN_GLAZED_TERRACOTTA) -> GreenGlazedTerracotta,
    new MaterialData(RED_GLAZED_TERRACOTTA) -> RedGlazedTerracotta,
    new MaterialData(BLACK_GLAZED_TERRACOTTA) -> BlackGlazedTerracotta,
    new MaterialData(CONCRETE) -> WhiteConcrete,
    new MaterialData(CONCRETE, 1.toByte) -> OrangeConcrete,
    new MaterialData(CONCRETE, 2.toByte) -> MagentaConcrete,
    new MaterialData(CONCRETE, 3.toByte) -> LightBlueConcrete,
    new MaterialData(CONCRETE, 4.toByte) -> YellowConcrete,
    new MaterialData(CONCRETE, 5.toByte) -> LimeConcrete,
    new MaterialData(CONCRETE, 6.toByte) -> PinkConcrete,
    new MaterialData(CONCRETE, 7.toByte) -> GrayConcrete,
    new MaterialData(CONCRETE, 8.toByte) -> LightGrayConcrete,
    new MaterialData(CONCRETE, 9.toByte) -> CyanConcrete,
    new MaterialData(CONCRETE, 10.toByte) -> PurpleConcrete,
    new MaterialData(CONCRETE, 11.toByte) -> BlueConcrete,
    new MaterialData(CONCRETE, 12.toByte) -> BrownConcrete,
    new MaterialData(CONCRETE, 13.toByte) -> GreenConcrete,
    new MaterialData(CONCRETE, 14.toByte) -> RedConcrete,
    new MaterialData(CONCRETE, 15.toByte) -> BlackConcrete,
    new MaterialData(CONCRETE_POWDER) -> WhiteConcretePowder,
    new MaterialData(CONCRETE_POWDER, 1.toByte) -> OrangeConcretePowder,
    new MaterialData(CONCRETE_POWDER, 2.toByte) -> MagentaConcretePowder,
    new MaterialData(CONCRETE_POWDER, 3.toByte) -> LightBlueConcretePowder,
    new MaterialData(CONCRETE_POWDER, 4.toByte) -> YellowConcretePowder,
    new MaterialData(CONCRETE_POWDER, 5.toByte) -> LimeConcretePowder,
    new MaterialData(CONCRETE_POWDER, 6.toByte) -> PinkConcretePowder,
    new MaterialData(CONCRETE_POWDER, 7.toByte) -> GrayConcretePowder,
    new MaterialData(CONCRETE_POWDER, 8.toByte) -> LightGrayConcretePowder,
    new MaterialData(CONCRETE_POWDER, 9.toByte) -> CyanConcretePowder,
    new MaterialData(CONCRETE_POWDER, 10.toByte) -> PurpleConcretePowder,
    new MaterialData(CONCRETE_POWDER, 11.toByte) -> BlueConcretePowder,
    new MaterialData(CONCRETE_POWDER, 12.toByte) -> BrownConcretePowder,
    new MaterialData(CONCRETE_POWDER, 13.toByte) -> GreenConcretePowder,
    new MaterialData(CONCRETE_POWDER, 14.toByte) -> RedConcretePowder,
    new MaterialData(CONCRETE_POWDER, 15.toByte) -> BlackConcretePowder,
    new MaterialData(STRUCTURE_BLOCK) -> StructureSaveBlock,
    new MaterialData(STRUCTURE_BLOCK, 1.toByte) -> StructureLoadBlock,
    new MaterialData(STRUCTURE_BLOCK, 2.toByte) -> StructureCornerBlock,
    new MaterialData(STRUCTURE_BLOCK, 3.toByte) -> StructureDataBlock,
    new MaterialData(IRON_SPADE) -> IronShovel,
    new MaterialData(IRON_PICKAXE) -> IronPickaxe,
    new MaterialData(IRON_AXE) -> IronAxe,
    new MaterialData(FLINT_AND_STEEL) -> FlintAndSteel,
    new MaterialData(APPLE) -> Apple,
    new MaterialData(BOW) -> Bow,
    new MaterialData(ARROW) -> Arrow,
    new Coal(CoalType.COAL) -> Coal,
    new Coal(CoalType.CHARCOAL) -> Charcoal,
    new MaterialData(DIAMOND) -> Diamond,
    new MaterialData(IRON_INGOT) -> IronIngot,
    new MaterialData(GOLD_INGOT) -> GoldIngot,
    new MaterialData(IRON_SWORD) -> IronSword,
    new MaterialData(WOOD_SWORD) -> WoodSword,
    new MaterialData(WOOD_SPADE) -> WoodShovel,
    new MaterialData(WOOD_PICKAXE) -> WoodPickaxe,
    new MaterialData(WOOD_AXE) -> WoodAxe,
    new MaterialData(STONE_SWORD) -> StoneSword,
    new MaterialData(STONE_SPADE) -> StoneShovel,
    new MaterialData(STONE_PICKAXE) -> StonePickaxe,
    new MaterialData(STONE_AXE) -> StoneAxe,
    new MaterialData(DIAMOND_SWORD) -> DiamondSword,
    new MaterialData(DIAMOND_SPADE) -> DiamondShovel,
    new MaterialData(DIAMOND_PICKAXE) -> DiamondPickaxe,
    new MaterialData(DIAMOND_AXE) -> DiamondAxe,
    new MaterialData(STICK) -> Stick,
    new MaterialData(BOWL) -> Bowl,
    new MaterialData(MUSHROOM_SOUP) -> MushroomStew,
    new MaterialData(GOLD_SWORD) -> GoldSword,
    new MaterialData(GOLD_SPADE) -> GoldShovel,
    new MaterialData(GOLD_PICKAXE) -> GoldPickaxe,
    new MaterialData(GOLD_AXE) -> GoldAxe,
    new MaterialData(STRING) -> String,
    new MaterialData(FEATHER) -> Feather,
    new MaterialData(SULPHUR) -> GunPowder,
    new MaterialData(WOOD_HOE) -> WoodHoe,
    new MaterialData(STONE_HOE) -> StoneHoe,
    new MaterialData(IRON_HOE) -> IronHoe,
    new MaterialData(DIAMOND_HOE) -> DiamondHoe,
    new MaterialData(GOLD_HOE) -> GoldHoe,
    new MaterialData(SEEDS) -> Seeds,
    new MaterialData(WHEAT) -> Wheat,
    new MaterialData(BREAD) -> Bread,
    new MaterialData(LEATHER_HELMET) -> LeatherHelmet,
    new MaterialData(LEATHER_CHESTPLATE) -> LeatherChestplate,
    new MaterialData(LEATHER_LEGGINGS) -> LeatherLeggings,
    new MaterialData(LEATHER_BOOTS) -> LeatherBoots,
    new MaterialData(CHAINMAIL_HELMET) -> ChainmailHelmet,
    new MaterialData(CHAINMAIL_CHESTPLATE) -> ChainmailChestplate,
    new MaterialData(CHAINMAIL_LEGGINGS) -> ChainmailLeggings,
    new MaterialData(CHAINMAIL_BOOTS) -> ChainmailBoots,
    new MaterialData(IRON_HELMET) -> IronHelmet,
    new MaterialData(IRON_CHESTPLATE) -> IronChestplate,
    new MaterialData(IRON_LEGGINGS) -> IronLeggings,
    new MaterialData(IRON_BOOTS) -> IronBoots,
    new MaterialData(DIAMOND_HELMET) -> DiamondHelmet,
    new MaterialData(DIAMOND_CHESTPLATE) -> DiamondChestplate,
    new MaterialData(DIAMOND_LEGGINGS) -> DiamondLeggings,
    new MaterialData(DIAMOND_BOOTS) -> DiamondBoots,
    new MaterialData(GOLD_HELMET) -> GoldHelmet,
    new MaterialData(GOLD_CHESTPLATE) -> GoldChestplate,
    new MaterialData(GOLD_LEGGINGS) -> GoldLeggings,
    new MaterialData(GOLD_BOOTS) -> GoldBoots,
    new MaterialData(FLINT) -> Flint,
    new MaterialData(PORK) -> RawPorkchop,
    new MaterialData(GRILLED_PORK) -> CookedPorkchop,
    new MaterialData(PAINTING) -> Painting,
    new MaterialData(GOLDEN_APPLE) -> GoldenApple,
    new MaterialData(GOLDEN_APPLE, 1.toByte) -> EnchantedGoldenApple,
    new MaterialData(SIGN) -> Sign,
    new MaterialData(WOOD_DOOR) -> OakDoorItem,
    new MaterialData(BUCKET) -> Bucket,
    new MaterialData(WATER_BUCKET) -> WaterBucket,
    new MaterialData(LAVA_BUCKET) -> LavaBucket,
    new MaterialData(MINECART) -> Minecart,
    new MaterialData(SADDLE) -> Saddle,
    new MaterialData(IRON_DOOR) -> IronDoor,
    new MaterialData(REDSTONE) -> Redstone,
    new MaterialData(SNOW_BALL) -> SnowBall,
    new MaterialData(BOAT) -> OakBoat,
    new MaterialData(LEATHER) -> Leather,
    new MaterialData(MILK_BUCKET) -> MilkBucket,
    new MaterialData(CLAY_BRICK) -> ClayBrick,
    new MaterialData(CLAY_BALL) -> ClayBall,
    new MaterialData(SUGAR_CANE) -> SugarCane,
    new MaterialData(PAPER) -> Paper,
    new MaterialData(BOOK) -> Book,
    new MaterialData(SLIME_BALL) -> SlimeBall,
    new MaterialData(STORAGE_MINECART) -> StorageMinecart,
    new MaterialData(POWERED_MINECART) -> PoweredMinecart,
    new MaterialData(EGG) -> Egg,
    new MaterialData(COMPASS) -> Compass,
    new MaterialData(FISHING_ROD) -> FishingRod,
    new MaterialData(WATCH) -> Watch,
    new MaterialData(GLOWSTONE_DUST) -> GlowstoneDust,
    new MaterialData(RAW_FISH) -> RawFish,
    new MaterialData(RAW_FISH, 1.toByte) -> RawSalmon,
    new MaterialData(RAW_FISH, 2.toByte) -> Clownfish,
    new MaterialData(RAW_FISH, 3.toByte) -> Pufferfish,
    new MaterialData(COOKED_FISH) -> CookedFish,
    new MaterialData(COOKED_FISH, 1.toByte) -> CookedSalmon,
    new org.bukkit.material.Dye(BLACK) -> InkSack,
    new org.bukkit.material.Dye(RED) -> RoseRed,
    new org.bukkit.material.Dye(GREEN) -> CactusGreen,
    new org.bukkit.material.Dye(BROWN) -> CocoaBeans,
    new org.bukkit.material.Dye(BLUE) -> LapisLazuli,
    new org.bukkit.material.Dye(PURPLE) -> PurpleDye,
    new org.bukkit.material.Dye(CYAN) -> CyanDye,
    new org.bukkit.material.Dye(SILVER) -> LightGrayDye,
    new org.bukkit.material.Dye(GRAY) -> GrayDye,
    new org.bukkit.material.Dye(PINK) -> PinkDye,
    new org.bukkit.material.Dye(LIME) -> LimeDye,
    new org.bukkit.material.Dye(YELLOW) -> DandelionYellow,
    new org.bukkit.material.Dye(LIGHT_BLUE) -> LightBlueDye,
    new org.bukkit.material.Dye(MAGENTA) -> MagentaDye,
    new org.bukkit.material.Dye(ORANGE) -> OrangeDye,
    new org.bukkit.material.Dye(WHITE) -> BoneMeal,
    new MaterialData(BONE) -> Bone,
    new MaterialData(SUGAR) -> Sugar,
    new MaterialData(CAKE) -> Cake,
    new MaterialData(BED) -> WhiteBed,
    new MaterialData(BED, 1.toByte) -> OrangeBed,
    new MaterialData(BED, 2.toByte) -> MagentaBed,
    new MaterialData(BED, 3.toByte) -> LightBlueBed,
    new MaterialData(BED, 4.toByte) -> YellowBed,
    new MaterialData(BED, 5.toByte) -> LimeBed,
    new MaterialData(BED, 6.toByte) -> PinkBed,
    new MaterialData(BED, 7.toByte) -> GrayBed,
    new MaterialData(BED, 8.toByte) -> LightGrayBed,
    new MaterialData(BED, 9.toByte) -> CyanBed,
    new MaterialData(BED, 10.toByte) -> PurpleBed,
    new MaterialData(BED, 11.toByte) -> BlueBed,
    new MaterialData(BED, 12.toByte) -> BrownBed,
    new MaterialData(BED, 13.toByte) -> GreenBed,
    new MaterialData(BED, 14.toByte) -> RedBed,
    new MaterialData(BED, 15.toByte) -> BlackBed,
    new MaterialData(DIODE) -> RedstoneRepeater,
    new MaterialData(COOKIE) -> Cookie,
    new MaterialData(MAP) -> DrawnMap,
    new MaterialData(SHEARS) -> Shears,
    new MaterialData(MELON) -> Melon,
    new MaterialData(PUMPKIN_SEEDS) -> PumpkinSeeds,
    new MaterialData(MELON_SEEDS) -> MelonSeeds,
    new MaterialData(RAW_BEEF) -> RawBeef,
    new MaterialData(COOKED_BEEF) -> CookedBeef,
    new MaterialData(RAW_CHICKEN) -> RawChicken,
    new MaterialData(COOKED_CHICKEN) -> CookedChicken,
    new MaterialData(ROTTEN_FLESH) -> RottenFlesh,
    new MaterialData(ENDER_PEARL) -> EnderPearl,
    new MaterialData(BLAZE_ROD) -> BlazeRod,
    new MaterialData(GHAST_TEAR) -> GhastTear,
    new MaterialData(GOLD_NUGGET) -> GoldNugget,
    new MaterialData(NETHER_STALK) -> NetherWart,
    new MaterialData(POTION) -> Potion,
    new MaterialData(GLASS_BOTTLE) -> GlassBottle,
    new MaterialData(SPIDER_EYE) -> SpiderEye,
    new MaterialData(FERMENTED_SPIDER_EYE) -> FermentedSpiderEye,
    new MaterialData(BLAZE_POWDER) -> BlazePowder,
    new MaterialData(MAGMA_CREAM) -> MagmaCream,
    new MaterialData(BREWING_STAND_ITEM) -> BrewingStandItem,
    new MaterialData(CAULDRON_ITEM) -> CauldronItem,
    new MaterialData(EYE_OF_ENDER) -> EyeOfEnder,
    new MaterialData(SPECKLED_MELON) -> GlisteringMelon,
    new MaterialData(MONSTER_EGG) -> MonsterEgg,
    new MaterialData(EXP_BOTTLE) -> ExpBottle,
    new MaterialData(FIREBALL) -> FireCharge,
    new MaterialData(BOOK_AND_QUILL) -> BookAndQuill,
    new MaterialData(WRITTEN_BOOK) -> WrittenBook,
    new MaterialData(EMERALD) -> Emerald,
    new MaterialData(ITEM_FRAME) -> ItemFrame,
    new MaterialData(FLOWER_POT_ITEM) -> FlowerPotItem,
    new MaterialData(CARROT_ITEM) -> CarrotItem,
    new MaterialData(POTATO_ITEM) -> PotatoItem,
    new MaterialData(BAKED_POTATO) -> BakedPotato,
    new MaterialData(POISONOUS_POTATO) -> PoisonousPotato,
    new MaterialData(EMPTY_MAP) -> EmptyMap,
    new MaterialData(GOLDEN_CARROT) -> GoldenCarrot,
    new MaterialData(SKULL_ITEM) -> SkeletonHead,
    new MaterialData(SKULL_ITEM, 1.toByte) -> WitherSkeletonHead,
    new MaterialData(SKULL_ITEM, 2.toByte) -> ZombieHead,
    new MaterialData(SKULL_ITEM, 3.toByte) -> PlayerHead,
    new MaterialData(SKULL_ITEM, 4.toByte) -> CreeperHead,
    new MaterialData(SKULL_ITEM, 5.toByte) -> DragonHead,
    new MaterialData(CARROT_STICK) -> CarrotStick,
    new MaterialData(NETHER_STAR) -> NetherStar,
    new MaterialData(PUMPKIN_PIE) -> PumpkinPie,
    new MaterialData(FIREWORK) -> FireworkRocket,
    new MaterialData(FIREWORK_CHARGE) -> FireworkStar,
    new MaterialData(ENCHANTED_BOOK) -> EnchantedBook,
    new MaterialData(REDSTONE_COMPARATOR) -> RedstoneComparator,
    new MaterialData(NETHER_BRICK_ITEM) -> NetherBrickItem,
    new MaterialData(QUARTZ) -> Quartz,
    new MaterialData(EXPLOSIVE_MINECART) -> ExplosiveMinecart,
    new MaterialData(HOPPER_MINECART) -> HopperMinecart,
    new MaterialData(PRISMARINE_SHARD) -> PrismarineShard,
    new MaterialData(PRISMARINE_CRYSTALS) -> PrismarineCrystals,
    new MaterialData(RABBIT) -> RawRabbit,
    new MaterialData(COOKED_RABBIT) -> CookedRabbit,
    new MaterialData(RABBIT_STEW) -> RabbitStew,
    new MaterialData(RABBIT_FOOT) -> RabbitFoot,
    new MaterialData(RABBIT_HIDE) -> RabbitHide,
    new MaterialData(ARMOR_STAND) -> ArmorStand,
    new MaterialData(IRON_BARDING) -> IronHorseArmor,
    new MaterialData(GOLD_BARDING) -> GoldHorseArmor,
    new MaterialData(DIAMOND_BARDING) -> DiamondHorseArmor,
    new MaterialData(LEASH) -> Leash,
    new MaterialData(NAME_TAG) -> NameTag,
    new MaterialData(COMMAND_MINECART) -> CommandMinecart,
    new MaterialData(MUTTON) -> RawMutton,
    new MaterialData(COOKED_MUTTON) -> CookedMutton,
    new MaterialData(BANNER) -> WhiteBanner,
    new MaterialData(BANNER, 1.toByte) -> OrangeBanner,
    new MaterialData(BANNER, 2.toByte) -> MagentaBanner,
    new MaterialData(BANNER, 3.toByte) -> LightBlueBanner,
    new MaterialData(BANNER, 4.toByte) -> YellowBanner,
    new MaterialData(BANNER, 5.toByte) -> LimeBanner,
    new MaterialData(BANNER, 6.toByte) -> PinkBanner,
    new MaterialData(BANNER, 7.toByte) -> GrayBanner,
    new MaterialData(BANNER, 8.toByte) -> LightGrayBanner,
    new MaterialData(BANNER, 9.toByte) -> CyanBanner,
    new MaterialData(BANNER, 10.toByte) -> PurpleBanner,
    new MaterialData(BANNER, 11.toByte) -> BlueBanner,
    new MaterialData(BANNER, 12.toByte) -> BrownBanner,
    new MaterialData(BANNER, 13.toByte) -> GreenBanner,
    new MaterialData(BANNER, 14.toByte) -> RedBanner,
    new MaterialData(BANNER, 15.toByte) -> BlackBanner,
    new MaterialData(END_CRYSTAL) -> EndCrystal,
    new MaterialData(SPRUCE_DOOR_ITEM) -> SpruceDoorItem,
    new MaterialData(BIRCH_DOOR_ITEM) -> BirchDoorItem,
    new MaterialData(JUNGLE_DOOR_ITEM) -> JungleDoorItem,
    new MaterialData(ACACIA_DOOR_ITEM) -> AcaciaDoorItem,
    new MaterialData(DARK_OAK_DOOR_ITEM) -> DarkOakDoorItem,
    new MaterialData(CHORUS_FRUIT) -> ChorusFruit,
    new MaterialData(CHORUS_FRUIT_POPPED) -> PoppedChorusFruit,
    new MaterialData(BEETROOT) -> Beetroot,
    new MaterialData(BEETROOT_SEEDS) -> BeetrootSeeds,
    new MaterialData(BEETROOT_SOUP) -> BeetrootSoup,
    new MaterialData(DRAGONS_BREATH) -> DragonsBreath,
    new MaterialData(SPLASH_POTION) -> SplashPotion,
    new MaterialData(SPECTRAL_ARROW) -> SpectralArrow,
    new MaterialData(TIPPED_ARROW) -> TippedArrow,
    new MaterialData(LINGERING_POTION) -> LingeringPotion,
    new MaterialData(SHIELD) -> Shield,
    new MaterialData(ELYTRA) -> Elytra,
    new MaterialData(BOAT_SPRUCE) -> SpruceBoat,
    new MaterialData(BOAT_BIRCH) -> BirchBoat,
    new MaterialData(BOAT_JUNGLE) -> JungleBoat,
    new MaterialData(BOAT_ACACIA) -> AcaciaBoat,
    new MaterialData(BOAT_DARK_OAK) -> DarkOakBoat,
    new MaterialData(TOTEM) -> TotemOfUndying,
    new MaterialData(SHULKER_SHELL) -> ShulkerShell,
    new MaterialData(IRON_NUGGET) -> IronNugget,
    new MaterialData(KNOWLEDGE_BOOK) -> KnowledgeBook,
    new MaterialData(GOLD_RECORD) -> GoldRecord,
    new MaterialData(GREEN_RECORD) -> GreenRecord,
    new MaterialData(RECORD_3) -> Record3,
    new MaterialData(RECORD_4) -> Record4,
    new MaterialData(RECORD_5) -> Record5,
    new MaterialData(RECORD_6) -> Record6,
    new MaterialData(RECORD_7) -> Record7,
    new MaterialData(RECORD_8) -> Record8,
    new MaterialData(RECORD_9) -> Record9,
    new MaterialData(RECORD_10) -> Record10,
    new MaterialData(RECORD_11) -> Record11,
    new MaterialData(RECORD_12) -> Record12,
  )

  private val materialToMaterialData: Map[Material, MaterialData] = materialDataToMaterial.map(_.swap)

  @silent implicit def materialData2Material(data: MaterialData): Material = {

    def isSpecialCase(data: MaterialData): Boolean = {
      //Directional stuff
      data.isInstanceOf[Directional] || data.isInstanceOf[RedstoneWire] ||
        //This is all kind of wierd stuff on your inventory, for instance, air(which is used to represent empty slots.
        data.getItemType.isBlock == false && data.getItemType == AIR/*data.getData == 3*/ ||
        //Materials with durability
        data.getItemType.getMaxDurability.toInt > 0 ||
        //Other stuff
        data.getItemType == ANVIL || data.getItemType == FIRE ||
        //Special Types
        data.getItemType == PISTON_MOVING_PIECE || data.getItemType == AIR ||
        //Generic Types
        data.getData == -1
    }

    materialDataToMaterial(if (isSpecialCase(data)) new MaterialData(data.getItemType) else data)
  }

  implicit def Material2MaterialData(material: Material): MaterialData = {
    materialToMaterialData(material)
  }

  override def values: IndexedSeq[Material] = findValues

  def filter[T: ClassTag]: Seq[Material with T] = values collect {
    case t: T => t.asInstanceOf[Material with T]
  }

  //@formatter:off
  object Air extends Material with Transparent with Crushable with NoEnergy
  object Stone extends Material(tier = T0) with Solid
  object Granite extends Material(tier = T0) with Solid
  object PolishedGranite extends Material with Solid
  object Diorite extends Material(tier = T0) with Solid
  object PolishedDiorite extends Material with Solid
  object Andesite extends Material(tier = T0) with Solid
  object PolishedAndesite extends Material with Solid
  object Grass extends Material(tier = T0) with Solid
  object Dirt extends Material(tier = T0) with Solid
  object CoarseDirt extends Material with Solid
  object Podzol extends Material(tier = T0) with Solid
  object Cobblestone extends Material(tier = T0) with Solid
  object OakWoodPlanks extends Material with Plank
  object SpruceWoodPlanks extends Material with Plank
  object BirchWoodPlanks extends Material with Plank
  object JungleWoodPlanks extends Material with Plank
  object AcaciaWoodPlanks extends Material with Plank
  object DarkOakWoodPlanks extends Material with Plank
  object OakSapling extends Material(tier = T2) with Sapling
  object SpruceSapling extends Material(tier = T2) with Sapling
  object BirchSapling extends Material(tier = T2) with Sapling
  object JungleSapling extends Material(tier = T2) with Sapling
  object AcaciaSapling extends Material(tier = T2) with Sapling
  object DarkOakSapling extends Material(tier = T2) with Sapling
  object Bedrock extends Material with Solid with NoEnergy
  object Water extends Material(energy = 14) with Transparent with Liquid
  object StationaryWater extends Material(energy = 14) with Transparent with Liquid
  object Lava extends Material(energy = 68) with Liquid
  object StationaryLava extends Material(energy = 68) with Liquid
  object Sand extends Material(tier = T0) with Solid with Gravity
  object RedSand extends Material(tier = T0) with Solid with Gravity
  object Gravel extends Material(tier = T2) with Solid with Gravity
  object GoldOre extends Material(energy = 1168) with Solid
  object IronOre extends Material(tier = T3) with Solid
  object CoalOre extends Material with Solid
  object OakLog extends Material(tier = T2) with Log
  object SpruceLog extends Material(tier = T2) with Log
  object BirchLog extends Material(tier = T2) with Log
  object JungleLog extends Material(tier = T2) with Log
  object OakLeaves extends Material(tier = T2) with Leaves
  object SpruceLeaves extends Material(tier = T2) with Leaves
  object BirchLeaves extends Material(tier = T2) with Leaves
  object JungleLeaves extends Material(tier = T2) with Leaves
  object Sponge extends Material(energy = 210) with Solid
  object WetSponge extends Material(energy = 18538) with Solid
  object Glass extends Material with Glass
  object LapisLazuliOre extends Material with Solid
  object LapisLazuliBlock extends Material with Solid
  object Dispenser extends Material with Rotates with Inventory
  object Sandstone extends Material with Solid
  object SmoothSandstone extends Material with Solid
  object ChiseledSandstone extends Material with Solid
  object NoteBlock extends Material with Solid with Fuel {
    override val burnTicks: Int = 300
  }
  object BedBlock extends Material with Solid with Rotates
  object PoweredRails extends Material with Rails
  object DetectorRails extends Material with Rails
  object StickyPiston extends Material with Solid
  object Cobweb extends Material(energy = 2279) with Plant
  object Shrub extends Material(tier = T0) with Plant
  object LongGrass extends Material(tier = T0) with Plant
  object Fern extends Material(tier = T0) with Plant
  object DeadBush extends Material(tier = T3) with Plant
  object Piston extends Material with Solid
  object PistonExtension extends Material with Solid with NoEnergy
  object WhiteWool extends Material with Wool
  object OrangeWool extends Material with Wool
  object MagentaWool extends Material with Wool
  object LightBlueWool extends Material with Wool
  object YellowWool extends Material with Wool
  object LimeWool extends Material with Wool
  object PinkWool extends Material with Wool
  object GrayWool extends Material with Wool
  object LightGrayWool extends Material with Wool
  object CyanWool extends Material with Wool
  object PurpleWool extends Material with Wool
  object BlueWool extends Material with Wool
  object BrownWool extends Material with Wool
  object GreenWool extends Material with Wool
  object RedWool extends Material with Wool
  object BlackWool extends Material with Wool
  object PistonMovingPiece extends Material with Solid with NoEnergy
  object Dandelion extends Material(energy = 14) with Plant
  object Poppy extends Material(energy = 14) with Plant
  object BlueOrchid extends Material(energy = 14) with Plant
  object Allium extends Material(energy = 14) with Plant
  object AzureBluet extends Material(energy = 14) with Plant
  object RedTulip extends Material(energy = 14) with Plant
  object OrangeTulip extends Material(energy = 14) with Plant
  object WhiteTulip extends Material(energy = 14) with Plant
  object PinkTulip extends Material(energy = 14) with Plant
  object OxeyeDaisy extends Material(energy = 14) with Plant
  object BrownMushroom extends Material(energy = 159) with Plant
  object RedMushroom extends Material(energy = 159) with Plant
  object GoldBlock extends Material with Solid
  object IronBlock extends Material with Solid
  object StoneDoubleSlab extends Material with DoubleSlab
  object SandstoneDoubleSlab extends Material with DoubleSlab
  object OldWoodDoubleSlab extends Material with NoEnergy/* with WoodenDoubleSlab*/
  object CobblestoneDoubleSlab extends Material with DoubleSlab
  object BrickDoubleSlab extends Material with DoubleSlab
  object StoneBrickDoubleSlab extends Material with DoubleSlab
  object NetherBrickDoubleSlab extends Material with DoubleSlab
  object QuartzDoubleSlab extends Material with DoubleSlab
  object StoneSingleSlab extends Material with SingleSlab
  object SandstoneSingleSlab extends Material with SingleSlab
  object OldWoodSingleSlab extends Material with NoEnergy/* with WoodenSingleSlab*/
  object CobblestoneSingleSlab extends Material with SingleSlab
  object BrickSingleSlab extends Material with SingleSlab
  object StoneBrickSingleSlab extends Material with SingleSlab
  object NetherBrickSingleSlab extends Material with SingleSlab
  object QuartzSingleSlab extends Material with SingleSlab
  object BrickBlock extends Material with Solid
  object TNT extends Material with Solid
  object Bookshelf extends Material with Solid with Fuel {
    override val burnTicks: Int = 300
  }
  object MossyCobblestone extends Material with Solid
  object Obsidian extends Material(energy = 68) with Solid
  object Torch extends Material with Torch with Crushable
  object Fire extends Material(tier = T3) with Transparent with Attaches with Crushable
  object MobSpawner extends Material(tier = T6) with Solid
  object OakStairs extends Material with Stairs
  object Chest extends Material with Solid with Rotates with Inventory with Fuel {
    override val burnTicks: Int = 300
  }
  object RedstoneWire extends Material with Transparent with Attaches
  object DiamondOre extends Material with Solid
  object DiamondBlock extends Material with Solid
  object CraftingTable extends Material with Solid with Fuel {
    override val burnTicks: Int = 300
  }
  object Crops extends Material(tier = T2) with Plant
  object Soil extends Material(tier = T0) with Solid
  object Furnace extends Material with Solid with Rotates with Inventory
  object BurningFurnace extends Material with Solid with Rotates with Inventory
  object SignPost extends Material with Solid with Rotates with Sign
  object OakDoor extends Material with Solid with Rotates with Door
  object Ladder extends Material with Transparent with Attaches with Rotates with Fuel {
    override val burnTicks: Int = 300
  }
  object Rails extends Material with Rails
  object CobblestoneStairs extends Material with Stairs
  object WallSign extends Material with Transparent with Rotates with Sign
  object Lever extends Material with Transparent with Attaches with Rotates
  object StonePressurePlate extends Material with Transparent with Attaches
  object IronDoorBlock extends Material with Solid with Transparent with Attaches with Rotates with Door
  object WoodPressurePlate extends Material with Transparent with Attaches with Fuel {
    override val burnTicks: Int = 300
  }
  object RedstoneOre extends Material with Solid
  object GlowingRedstoneOre extends Material with Solid
  object RedstoneTorchOff extends Material with Torch
  object RedstoneTorchOn extends Material with Torch
  object StoneButton extends Material with Transparent with Attaches with Rotates
  object Snow extends Material(tier = T0) with Transparent with Attaches with Crushable
  object Ice extends Material(tier = T0) with Solid
  object SnowBlock extends Material(tier = T0) with Solid
  object Cactus extends Material(tier = T3) with Solid with Attaches
  object Clay extends Material with Solid
  object SugarCaneBlock extends Material(tier = T2) with Transparent with Attaches with Crushable
  object Jukebox extends Material with Solid with Fuel {
    override val burnTicks: Int = 300
  }
  object OakFence extends Material with Fence
  object Pumpkin extends Material(tier = T3) with Solid with Rotates
  object Netherrack extends Material(tier = T0) with Solid
  object SoulSand extends Material with Solid
  object Glowstone extends Material with Solid
  object Portal extends Material with Transparent with NoEnergy
  object JackOLantern extends Material with Solid
  object CakeBlock extends Material with Solid with Transparent with PlayerConsumable
  object RedstoneRepeaterOff extends Material
  object RedstoneRepeaterOn extends Material
  object WhiteGlass extends Material with Glass
  object OrangeGlass extends Material with Glass
  object MagentaGlass extends Material with Glass
  object LightBlueGlass extends Material with Glass
  object YellowGlass extends Material with Glass
  object LimeGlass extends Material with Glass
  object PinkGlass extends Material with Glass
  object GrayGlass extends Material with Glass
  object LightGrayGlass extends Material with Glass
  object CyanGlass extends Material with Glass
  object PurpleGlass extends Material with Glass
  object BlueGlass extends Material with Glass
  object BrownGlass extends Material with Glass
  object GreenGlass extends Material with Glass
  object RedGlass extends Material with Glass
  object BlackGlass extends Material with Glass
  object TrapDoor extends Material with Solid with Transparent with Rotates with Door
  object MonsterEggs extends Material(tier = T6) with Solid
  object StoneBrick extends Material(tier = T0) with Solid
  object CrackedStoneBrick extends Material(tier = T5) with Solid
  object MossyStoneBrick extends Material with Solid
  object ChiseledStoneBrick extends Material with Solid
  object BrownMushroomBlock extends Material(tier = T3) with Solid
  object RedMushroomBlock extends Material(tier = T3) with Solid
  object IronFence extends Material with Fence
  object GlassPane extends Material with GlassPane
  object MelonBlock extends Material with Solid
  object PumpkinStem extends Material(tier = T2) with Plant with Rotates
  object MelonStem extends Material(tier = T2) with Plant with Rotates
  object Vine extends Material(tier = T5) with Transparent with Crushable
  object OakFenceGate extends Material with FenceGate
  object BrickStairs extends Material with Stairs
  object StoneBrickStairs extends Material with Stairs
  object Mycelium extends Material(tier = T0) with Solid
  object WaterLily extends Material(tier = T5) with Plant
  object NetherBrick extends Material with Solid
  object NetherBrickFence extends Material with Fence
  object NetherBrickStairs extends Material with Stairs
  object NetherWarts extends Material(tier = T4) with Plant
  object EnchantmentTable extends Material with Solid
  object BrewingStand extends Material with Solid
  object Cauldron extends Material with Solid
  object EnderPortal extends Material with Transparent with NoEnergy
  object EnderPortalFrame extends Material with Solid with NoEnergy
  object EndStone extends Material(tier = T0) with Solid
  object DragonEgg extends Material(energy = 140369) with Solid
  object RedstoneLampOff extends Material with Solid
  object RedstoneLampOn extends Material with Solid
  object OakDoubleSlab extends Material with WoodenDoubleSlab
  object SpruceDoubleSlab extends Material with WoodenDoubleSlab
  object BirchDoubleSlab extends Material with WoodenDoubleSlab
  object JungleDoubleSlab extends Material with WoodenDoubleSlab
  object AcaciaDoubleSlab extends Material with WoodenDoubleSlab
  object DarkOakDoubleSlab extends Material with WoodenDoubleSlab
  object OakSingleSlab extends Material with WoodenSingleSlab
  object SpruceSingleSlab extends Material with WoodenSingleSlab
  object BirchSingleSlab extends Material with WoodenSingleSlab
  object JungleSingleSlab extends Material with WoodenSingleSlab
  object AcaciaSingleSlab extends Material with WoodenSingleSlab
  object DarkOakSingleSlab extends Material with WoodenSingleSlab
  object Cocoa extends Material(tier = T4) with Plant
  object SandstoneStairs extends Material with Stairs
  object EmeraldOre extends Material with Solid
  object EnderChest extends Material with Solid with Rotates
  object TripwireHook extends Material with Transparent with Attaches with Rotates
  object Tripwire extends Material with Transparent with Attaches
  object EmeraldBlock extends Material with Solid
  object SpruceStairs extends Material with Stairs
  object BirchStairs extends Material with Stairs
  object JungleStairs extends Material with Stairs
  object Command extends Material with Solid with NoEnergy
  object Beacon extends Material with Solid
  object CobblestoneWall extends Material with Solid
  object MossyCobblestoneWall extends Material with Solid
  object FlowerPot extends Material with Transparent
  object Carrot extends Material(tier = T2) with Plant
  object Potato extends Material(tier = T2) with Plant
  object WoodButton extends Material with Transparent with Rotates with Fuel {
    override val burnTicks: Int = 100
  }
  object Skull extends Material with Transparent with Attaches with Rotates with NoEnergy
  object Anvil extends Material with Solid with Transparent with Gravity with Inventory
  object TrappedChest extends Material with Solid with Rotates with Fuel {
    override val burnTicks: Int = 300
  }
  object GoldPressurePlate extends Material with Solid
  object IronPressurePlate extends Material with Solid
  object RedstoneComparatorOff extends Material with Transparent with Attaches with Rotates
  object RedstoneComparatorOn extends Material with Transparent with Attaches with Rotates
  object DaylightSensor extends Material with Solid with Fuel {
    override val burnTicks: Int = 300
  }
  object RedstoneBlock extends Material with Solid
  object QuartzOre extends Material with Solid
  object Hopper extends Material with Solid with Inventory
  object QuartzBlock extends Material with Solid
  object ChiseledQuartzBlock extends Material with Solid
  object PillarQuartzBlock extends Material with Solid
  object QuartzStairs extends Material with Stairs
  object ActivatorRails extends Material with Rails
  object Dropper extends Material with Solid with Inventory
  object Terracotta extends Material with Terracotta
  object WhiteGlassPane extends Material with GlassPane
  object OrangeGlassPane extends Material with GlassPane
  object MagentaGlassPane extends Material with GlassPane
  object LightBlueGlassPane extends Material with GlassPane
  object YellowGlassPane extends Material with GlassPane
  object LimeGlassPane extends Material with GlassPane
  object PinkGlassPane extends Material with GlassPane
  object GrayGlassPane extends Material with GlassPane
  object LightGrayGlassPane extends Material with GlassPane
  object CyanGlassPane extends Material with GlassPane
  object PurpleGlassPane extends Material with GlassPane
  object BlueGlassPane extends Material with GlassPane
  object BrownGlassPane extends Material with GlassPane
  object GreenGlassPane extends Material with GlassPane
  object RedGlassPane extends Material with GlassPane
  object BlackGlassPane extends Material with GlassPane
  object AcaciaLeaves extends Material(tier = T2) with Leaves
  object DarkOakLeaves extends Material(tier = T2) with Leaves
  object AcaciaLog extends Material(tier = T2) with Log
  object DarkOakLog extends Material(tier = T2) with Log
  object AcaciaStairs extends Material with Stairs
  object DarkOakStairs extends Material with Stairs
  object SlimeBlock extends Material with Solid
  object Barrier extends Material with Solid with NoEnergy
  object IronTrapdoor extends Material with Solid
  object Prismarine extends Material with Solid
  object PrismarineBrick extends Material with Solid
  object DarkPrismarine extends Material with Solid
  object SeaLantern extends Material with Solid
  object HayBale extends Material with Solid
  object WhiteCarpet extends Material with Carpet
  object OrangeCarpet extends Material with Carpet
  object MagentaCarpet extends Material with Carpet
  object LightBlueCarpet extends Material with Carpet
  object YellowCarpet extends Material with Carpet
  object LimeCarpet extends Material with Carpet
  object PinkCarpet extends Material with Carpet
  object GrayCarpet extends Material with Carpet
  object LightGrayCarpet extends Material with Carpet
  object CyanCarpet extends Material with Carpet
  object PurpleCarpet extends Material with Carpet
  object BlueCarpet extends Material with Carpet
  object BrownCarpet extends Material with Carpet
  object GreenCarpet extends Material with Carpet
  object RedCarpet extends Material with Carpet
  object BlackCarpet extends Material with Carpet
  object WhiteTerracotta extends Material with DyedTerracotta
  object OrangeTerracotta extends Material with DyedTerracotta
  object MagentaTerracotta extends Material with DyedTerracotta
  object LightBlueTerracotta extends Material with DyedTerracotta
  object YellowTerracotta extends Material with DyedTerracotta
  object LimeTerracotta extends Material with DyedTerracotta
  object PinkTerracotta extends Material with DyedTerracotta
  object GrayTerracotta extends Material with DyedTerracotta
  object LightGrayTerracotta extends Material with DyedTerracotta
  object CyanTerracotta extends Material with DyedTerracotta
  object PurpleTerracotta extends Material with DyedTerracotta
  object BlueTerracotta extends Material with DyedTerracotta
  object BrownTerracotta extends Material with DyedTerracotta
  object GreenTerracotta extends Material with DyedTerracotta
  object RedTerracotta extends Material with DyedTerracotta
  object BlackTerracotta extends Material with DyedTerracotta
  object CoalBlock extends Material with Solid with Fuel {
    override val burnTicks: Int = 16000
  }
  object PackedIce extends Material(tier = T3) with Solid
  object DoublePlant extends Material(tier = T2) with DoublePlant
  object Sunflower extends Material(tier = T2) with DoublePlant
  object Lilac extends Material(tier = T2) with DoublePlant
  object DoubleTallgrass extends Material(tier = T2) with DoublePlant
  object LargeFern extends Material(tier = T2) with DoublePlant
  object RoseBush extends Material(tier = T2) with DoublePlant
  object Peony extends Material(tier = T2) with DoublePlant
  object TopPlantHalf extends Material(tier = T0) with DoublePlant
  object StandingBanner extends Material with Transparent with Attaches with Banner
  object WallBanner extends Material with Transparent with Attaches with Banner
  object InvertedDaylightSensor extends Material with Solid
  object RedSandstone extends Material with Solid
  object SmoothRedSandstone extends Material with Solid
  object ChiseledRedSandstone extends Material with Solid
  object RedSandstoneStairs extends Material with Stairs
  object RedSandstoneDoubleSlab extends Material with DoubleSlab
  object RedSandstoneSingleSlab extends Material with SingleSlab
  object SpruceFenceGate extends Material with FenceGate
  object BirchFenceGate extends Material with FenceGate
  object JungleFenceGate extends Material with FenceGate
  object DarkOakFenceGate extends Material with FenceGate
  object AcaciaFenceGate extends Material with FenceGate
  object SpruceFence extends Material with Fence
  object BirchFence extends Material with Fence
  object JungleFence extends Material with Fence
  object DarkOakFence extends Material with Fence
  object AcaciaFence extends Material with Fence
  object SpruceDoor extends Material with Solid with Transparent with Door
  object BirchDoor extends Material with Solid with Transparent with Door
  object JungleDoor extends Material with Solid with Transparent with Door
  object AcaciaDoor extends Material with Solid with Transparent with Door
  object DarkOakDoor extends Material with Solid with Transparent with Door
  object EndRod extends Material(tier = T5) with Solid with Transparent
  object ChorusPlant extends Material(tier = T2) with Solid
  object ChorusFlower extends Material(tier = T2) with Solid with Crushable
  object PurpurBlock extends Material(tier = T4) with Solid
  object PurpurPillar extends Material(tier = T4) with Solid
  object PurpurStairs extends Material with Stairs
  object PurpurDoubleSlab extends Material with DoubleSlab
  object PurpurSingleSlab extends Material with SingleSlab
  object EndStoneBricks extends Material
  object BeetrootPlantation extends Material with Plant
  object GrassPath extends Material with Solid
  object EndGateway extends Material with Solid with NoEnergy
  object CommandRepeating extends Material with Solid with NoEnergy
  object CommandChain extends Material with Solid with NoEnergy
  object FrostedIce extends Material with Solid
  object Magma extends Material(tier = T3) with Solid
  object NetherWartBlock extends Material with Solid
  object RedNetherBrick extends Material with Solid
  object BoneBlock extends Material with Solid
  object StructureVoid extends Material with Solid with NoEnergy
  object Observer extends Material with Solid with NoEnergy
  object WhiteShulkerBox extends Material with Solid with Inventory with ShulkerBox
  object OrangeShulkerBox extends Material with Solid with Inventory with ShulkerBox
  object MagentaShulkerBox extends Material with Solid with Inventory with ShulkerBox
  object LightBlueShulkerBox extends Material with Solid with Inventory with ShulkerBox
  object YellowShulkerBox extends Material with Solid with Inventory with ShulkerBox
  object LimeShulkerBox extends Material with Solid with Inventory with ShulkerBox
  object PinkShulkerBox extends Material with Solid with Inventory with ShulkerBox
  object GrayShulkerBox extends Material with Solid with Inventory with ShulkerBox
  object LightGrayShulkerBox extends Material with Solid with Inventory with ShulkerBox
  object CyanShulkerBox extends Material with Solid with Inventory with ShulkerBox
  object PurpleShulkerBox extends Material with Solid with Inventory with ShulkerBox
  object BlueShulkerBox extends Material with Solid with Inventory with ShulkerBox
  object BrownShulkerBox extends Material with Solid with Inventory with ShulkerBox
  object GreenShulkerBox extends Material with Solid with Inventory with ShulkerBox
  object RedShulkerBox extends Material with Solid with Inventory with ShulkerBox
  object BlackShulkerBox extends Material with Solid with Inventory with ShulkerBox
  object WhiteGlazedTerracotta extends Material with GlazedTerracotta
  object OrangeGlazedTerracotta extends Material with GlazedTerracotta
  object MagentaGlazedTerracotta extends Material with GlazedTerracotta
  object LightBlueGlazedTerracotta extends Material with GlazedTerracotta
  object YellowGlazedTerracotta extends Material with GlazedTerracotta
  object LimeGlazedTerracotta extends Material with GlazedTerracotta
  object PinkGlazedTerracotta extends Material with GlazedTerracotta
  object GrayGlazedTerracotta extends Material with GlazedTerracotta
  object LightGrayGlazedTerracotta extends Material with GlazedTerracotta
  object CyanGlazedTerracotta extends Material with GlazedTerracotta
  object PurpleGlazedTerracotta extends Material with GlazedTerracotta
  object BlueGlazedTerracotta extends Material with GlazedTerracotta
  object BrownGlazedTerracotta extends Material with GlazedTerracotta
  object GreenGlazedTerracotta extends Material with GlazedTerracotta
  object RedGlazedTerracotta extends Material with GlazedTerracotta
  object BlackGlazedTerracotta extends Material with GlazedTerracotta
  object WhiteConcrete extends Material with Solid with Concrete
  object OrangeConcrete extends Material with Solid with Concrete
  object MagentaConcrete extends Material with Solid with Concrete
  object LightBlueConcrete extends Material with Solid with Concrete
  object YellowConcrete extends Material with Solid with Concrete
  object LimeConcrete extends Material with Solid with Concrete
  object PinkConcrete extends Material with Solid with Concrete
  object GrayConcrete extends Material with Solid with Concrete
  object LightGrayConcrete extends Material with Solid with Concrete
  object CyanConcrete extends Material with Solid with Concrete
  object PurpleConcrete extends Material with Solid with Concrete
  object BlueConcrete extends Material with Solid with Concrete
  object BrownConcrete extends Material with Solid with Concrete
  object GreenConcrete extends Material with Solid with Concrete
  object RedConcrete extends Material with Solid with Concrete
  object BlackConcrete extends Material with Solid with Concrete
  object WhiteConcretePowder extends Material with Solid with Gravity with ConcretePowder
  object OrangeConcretePowder extends Material with Solid with Gravity with ConcretePowder
  object MagentaConcretePowder extends Material with Solid with Gravity with ConcretePowder
  object LightBlueConcretePowder extends Material with Solid with Gravity with ConcretePowder
  object YellowConcretePowder extends Material with Solid with Gravity with ConcretePowder
  object LimeConcretePowder extends Material with Solid with Gravity with ConcretePowder
  object PinkConcretePowder extends Material with Solid with Gravity with ConcretePowder
  object GrayConcretePowder extends Material with Solid with Gravity with ConcretePowder
  object LightGrayConcretePowder extends Material with Solid with Gravity with ConcretePowder
  object CyanConcretePowder extends Material with Solid with Gravity with ConcretePowder
  object PurpleConcretePowder extends Material with Solid with Gravity with ConcretePowder
  object BlueConcretePowder extends Material with Solid with Gravity with ConcretePowder
  object BrownConcretePowder extends Material with Solid with Gravity with ConcretePowder
  object GreenConcretePowder extends Material with Solid with Gravity with ConcretePowder
  object RedConcretePowder extends Material with Solid with Gravity with ConcretePowder
  object BlackConcretePowder extends Material with Solid with Gravity with ConcretePowder
  object StructureSaveBlock extends Material with Solid with Inventory with NoEnergy
  object StructureLoadBlock extends Material with NoEnergy
  object StructureCornerBlock extends Material with NoEnergy
  object StructureDataBlock extends Material with NoEnergy

  object IronShovel extends Material with Shovel
  object IronPickaxe extends Material with Pickaxe
  object IronAxe extends Material /*(energy = 48052)*/ with Axe
  object FlintAndSteel extends Material with Durable
  object Apple extends Material with PlayerConsumable
  object Bow extends Material(energy = 118) with Durable with Fuel {
    override val burnTicks: Int = 300
  }
  object Arrow extends Material(energy = 5)
  object Coal extends Material(energy = 72) with Fuel {
    override val burnTicks: Int = 1600
  }
  object Charcoal extends Material with Fuel {
    override val burnTicks: Int = 1600
  }
  object Diamond extends Material(energy = 2869)
  object IronIngot extends Material(energy = 459)
  object GoldIngot extends Material
  object IronSword extends Material with Usable with Sword
  object WoodSword extends Material with Usable with Sword with Fuel {
    override val burnTicks: Int = 200
  }
  object WoodShovel extends Material with Shovel with Fuel {
    override val burnTicks: Int = 200
  }
  object WoodPickaxe extends Material with Pickaxe with Fuel {
    override val burnTicks: Int = 200
  }
  object WoodAxe extends Material with Axe with Fuel {
    override val burnTicks: Int = 200
  }
  object StoneSword extends Material(tier = T3) with Usable with Sword
  object StoneShovel extends Material with Shovel
  object StonePickaxe extends Material with Pickaxe
  object StoneAxe extends Material with Axe
  object DiamondSword extends Material with Usable with Sword
  object DiamondShovel extends Material with Shovel
  object DiamondPickaxe extends Material with Pickaxe
  object DiamondAxe extends Material with Axe
  object Stick extends Material(energy = 31) with Fuel {
    override val burnTicks: Int = 100
  }
  object Bowl extends Material with Fuel {
    override val burnTicks: Int = 100
  }
  object MushroomStew extends Material with PlayerConsumable
  object GoldSword extends Material(energy = 43307) with Usable with Sword
  object GoldShovel extends Material with Shovel
  object GoldPickaxe extends Material with Pickaxe
  object GoldAxe extends Material with Axe
  object String extends Material(energy = 9)
  object Feather extends Material(tier = T0)
  object GunPowder extends Material(energy = 35)
  object WoodHoe extends Material with Hoe with Fuel {
    override val burnTicks: Int = 200
  }
  object StoneHoe extends Material with Hoe
  object IronHoe extends Material with Hoe
  object DiamondHoe extends Material with Hoe
  object GoldHoe extends Material with Hoe
  object Seeds extends Material(tier = T2) with Consumable
  object Wheat extends Material(energy = 69) with Consumable
  object Bread extends Material with PlayerConsumable
  object LeatherHelmet extends Material with Helmet
  object LeatherChestplate extends Material with Chestplate
  object LeatherLeggings extends Material with Leggings
  object LeatherBoots extends Material with Boots
  object ChainmailHelmet extends Material with Helmet
  object ChainmailChestplate extends Material with Chestplate
  object ChainmailLeggings extends Material with Leggings
  object ChainmailBoots extends Material with Boots
  object IronHelmet extends Material with Helmet
  object IronChestplate extends Material with Chestplate
  object IronLeggings extends Material with Leggings
  object IronBoots extends Material with Boots
  object DiamondHelmet extends Material with Helmet
  object DiamondChestplate extends Material with Chestplate
  object DiamondLeggings extends Material with Leggings
  object DiamondBoots extends Material with Boots
  object GoldHelmet extends Material with Helmet
  object GoldChestplate extends Material with Chestplate
  object GoldLeggings extends Material with Leggings
  object GoldBoots extends Material with Boots
  object Flint extends Material
  object RawPorkchop extends Material(tier = T0) with PlayerConsumable
  object CookedPorkchop extends Material with PlayerConsumable
  object Painting extends Material
  object GoldenApple extends Material with PlayerConsumable
  object EnchantedGoldenApple extends Material with PlayerConsumable
  object Sign extends Material with Sign
  object OakDoorItem extends Material with Door
  object Bucket extends Material
  object WaterBucket extends Material
  object LavaBucket extends Material with Fuel {
    override val burnTicks: Int = 20000
  }
  object Minecart extends Material
  object Saddle extends Material(tier = T3)
  object IronDoor extends Material with Door
  object Redstone extends Material(energy = 35)
  object SnowBall extends Material(tier = T0)
  object OakBoat extends Material with Boat
  object Leather extends Material(tier = T0)
  object MilkBucket extends Material with PlayerConsumable
  object ClayBrick extends Material with Terracotta
  object ClayBall extends Material(tier = T2)
  object SugarCane extends Material(tier = T2)
  object Paper extends Material
  object Book extends Material
  object SlimeBall extends Material(energy = 2)
  object StorageMinecart extends Material
  object PoweredMinecart extends Material
  object Egg extends Material(tier = T1)
  object Compass extends Material
  object FishingRod extends Material with Durable with Fuel {
    override val burnTicks: Int = 300
  }
  object Watch extends Material
  object GlowstoneDust extends Material(energy = 35)
  object RawFish extends Material(energy = 33)with PlayerConsumable
  object RawSalmon extends Material(energy = 43) with PlayerConsumable
  object Clownfish extends Material(energy = 3910) with PlayerConsumable
  object Pufferfish extends Material(energy = 3903) with PlayerConsumable
  object CookedFish extends Material with PlayerConsumable
  object CookedSalmon extends Material with PlayerConsumable
  object InkSack extends Material(tier = T0) with Dye
  object RoseRed extends Material with Dye
  object CactusGreen extends Material with Dye
  object CocoaBeans extends Material(tier = T2) with Dye
  object LapisLazuli extends Material(tier = T4) with Dye
  object PurpleDye extends Material with Dye
  object CyanDye extends Material with Dye
  object LightGrayDye extends Material with Dye
  object GrayDye extends Material with Dye
  object PinkDye extends Material with Dye
  object LimeDye extends Material with Dye
  object DandelionYellow extends Material with Dye
  object LightBlueDye extends Material with Dye
  object MagentaDye extends Material with Dye
  object OrangeDye extends Material with Dye
  object BoneMeal extends Material with Dye
  object Bone extends Material(energy = 5)
  object Sugar extends Material(energy = 35)
  object Cake extends Material with PlayerConsumable
  object WhiteBed extends Material with Transparent with Bed
  object OrangeBed extends Material with Transparent with Bed
  object MagentaBed extends Material with Transparent with Bed
  object LightBlueBed extends Material with Transparent with Bed
  object YellowBed extends Material with Transparent with Bed
  object LimeBed extends Material with Transparent with Bed
  object PinkBed extends Material with Transparent with Bed
  object GrayBed extends Material with Transparent with Bed
  object LightGrayBed extends Material with Transparent with Bed
  object CyanBed extends Material with Transparent with Bed
  object PurpleBed extends Material with Transparent with Bed
  object BlueBed extends Material with Transparent with Bed
  object BrownBed extends Material with Transparent with Bed
  object GreenBed extends Material with Transparent with Bed
  object RedBed extends Material with Transparent with Bed
  object BlackBed extends Material with Transparent with Bed
  object RedstoneRepeater extends Material
  object Cookie extends Material with PlayerConsumable
  object DrawnMap extends Material with NoEnergy
  object Shears extends Material
  object Melon extends Material with PlayerConsumable
  object PumpkinSeeds extends Material
  object MelonSeeds extends Material
  object RawBeef extends Material(tier = T0) with PlayerConsumable
  object CookedBeef extends Material with PlayerConsumable
  object RawChicken extends Material(tier = T0) with PlayerConsumable
  object CookedChicken extends Material with PlayerConsumable
  object RottenFlesh extends Material(energy = 10) with PlayerConsumable
  object EnderPearl extends Material(energy = 1090)
  object BlazeRod extends Material(energy = 2322) with Fuel {
    override val burnTicks: Int = 2400
  }
  object GhastTear extends Material(energy = 269)
  object GoldNugget extends Material(energy = 2490)
  object NetherWart extends Material(tier = T3)
  object Potion extends Material with PlayerConsumable with NoEnergy
  object GlassBottle extends Material(energy = 35) with Glass
  object SpiderEye extends Material(energy = 35) with PlayerConsumable
  object FermentedSpiderEye extends Material
  object BlazePowder extends Material
  object MagmaCream extends Material(energy = 386)
  object BrewingStandItem extends Material with Inventory
  object CauldronItem extends Material
  object EyeOfEnder extends Material
  object GlisteringMelon extends Material
  object MonsterEgg extends Material with NoEnergy
  object ExpBottle extends Material with PlayerConsumable with NoEnergy
  object FireCharge extends Material with Usable
  object BookAndQuill extends Material with Usable
  object WrittenBook extends Material with Usable with NoEnergy
  object Emerald extends Material(energy = 464)
  object ItemFrame extends Material
  object FlowerPotItem extends Material
  object CarrotItem extends Material(energy = 5498) with PlayerConsumable
  object PotatoItem extends Material(energy = 5498) with PlayerConsumable
  object BakedPotato extends Material with PlayerConsumable
  object PoisonousPotato extends Material with PlayerConsumable
  object EmptyMap extends Material with Usable
  object GoldenCarrot extends Material with PlayerConsumable
  object SkeletonHead extends Material with NoEnergy
  object WitherSkeletonHead extends Material with NoEnergy
  object ZombieHead extends Material with NoEnergy
  object PlayerHead extends Material with NoEnergy
  object CreeperHead extends Material with NoEnergy
  object DragonHead extends Material with NoEnergy
  object CarrotStick extends Material
  object NetherStar extends Material(energy = 62141)
  object PumpkinPie extends Material with PlayerConsumable
  object FireworkRocket extends Material with Usable with NoEnergy
  object FireworkStar extends Material with NoEnergy
  object EnchantedBook extends Material with NoEnergy
  object RedstoneComparator extends Material
  object NetherBrickItem extends Material
  object Quartz extends Material(tier = T3)
  object ExplosiveMinecart extends Material
  object HopperMinecart extends Material with Inventory
  object PrismarineShard extends Material(energy = 469)
  object PrismarineCrystals extends Material(energy =   5332)
  object RawRabbit extends Material(tier = T0) with PlayerConsumable
  object CookedRabbit extends Material with PlayerConsumable
  object RabbitStew extends Material with PlayerConsumable
  object RabbitFoot extends Material(energy = 11)
  object RabbitHide extends Material(tier = T0)
  object ArmorStand extends Material
  object IronHorseArmor extends Material(tier = T3)
  object GoldHorseArmor extends Material(tier = T4)
  object DiamondHorseArmor extends Material(tier = T5)
  object Leash extends Material
  object NameTag extends Material(tier = T4)
  object CommandMinecart extends Material with NoEnergy
  object RawMutton extends Material(tier = T0)
  object CookedMutton extends Material
  object WhiteBanner extends Material with Banner
  object OrangeBanner extends Material with Banner
  object MagentaBanner extends Material with Banner
  object LightBlueBanner extends Material with Banner
  object YellowBanner extends Material with Banner
  object LimeBanner extends Material with Banner
  object PinkBanner extends Material with Banner
  object GrayBanner extends Material with Banner
  object LightGrayBanner extends Material with Banner
  object CyanBanner extends Material with Banner
  object PurpleBanner extends Material with Banner
  object BlueBanner extends Material with Banner
  object BrownBanner extends Material with Banner
  object GreenBanner extends Material with Banner
  object RedBanner extends Material with Banner
  object BlackBanner extends Material with Banner
  object EndCrystal extends Material
  object SpruceDoorItem extends Material with Door
  object BirchDoorItem extends Material with Door
  object JungleDoorItem extends Material with Door
  object AcaciaDoorItem extends Material(tier = T2) with Door
  object DarkOakDoorItem extends Material with Door
  object ChorusFruit extends Material(tier = T6)
  object PoppedChorusFruit extends Material
  object Beetroot extends Material with PlayerConsumable
  object BeetrootSeeds extends Material(tier = T2)
  object BeetrootSoup extends Material with PlayerConsumable
  object DragonsBreath extends Material(tier = T6)
  object SplashPotion extends Material with Consumable with NoEnergy
  object SpectralArrow extends Material with NoEnergy
  object TippedArrow extends Material with NoEnergy
  object LingeringPotion extends Material with Consumable with NoEnergy
  object Shield extends Material with Durable
  object Elytra extends Material(tier = T6) with Durable
  object SpruceBoat extends Material with Boat
  object BirchBoat extends Material with Boat
  object JungleBoat extends Material with Boat
  object AcaciaBoat extends Material with Boat
  object DarkOakBoat extends Material with Boat
  object TotemOfUndying extends Material(energy = 488)
  object ShulkerShell extends Material(energy = 951)
  object IronNugget extends Material
  object KnowledgeBook extends Material with NoEnergy
  object GoldRecord extends Material(tier = T4)
  object GreenRecord extends Material(tier = T4)
  object Record3 extends Material(tier = T4)
  object Record4 extends Material(tier = T4)
  object Record5 extends Material(tier = T4)
  object Record6 extends Material(tier = T4)
  object Record7 extends Material(tier = T4)
  object Record8 extends Material(tier = T4)
  object Record9 extends Material(tier = T4)
  object Record10 extends Material(tier = T4)
  object Record11 extends Material(tier = T4)
  object Record12 extends Material(tier = T4)
  //@formatter:on
}