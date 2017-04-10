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

/**
  * Created by Amuxix on 04/01/2017.
  */

sealed abstract case class Material(var energy: Option[Int] = None) extends EnumEntry with Element with Named {
  def this(energy: Int) = this(Some(energy))
  def this(tierString: String) = this(Math.pow(E * 2, tierString.substring(1).toDouble).round.toInt)
  lazy val tier: Option[Int] = {
    energy match {
      case Some(e) => Some((log(e) / log(E * 2)).round.toInt)
      case None => None
    }
  }

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

object Material extends CirceEnum[Material] with Enum[Material] {

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
    new Sapling(GENERIC) -> OakSapling,
    new Sapling(REDWOOD) -> SpruceSapling,
    new Sapling(BIRCH) -> BirchSapling,
    new Sapling(JUNGLE) -> JungleSapling,
    new Sapling(ACACIA) -> AcaciaSapling,
    new Sapling(DARK_OAK) -> DarkOakSapling,
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
    new Leaves(GENERIC) -> OakLeaves,
    new Leaves(REDWOOD) -> SpruceLeaves,
    new Leaves(BIRCH) -> BirchLeaves,
    new Leaves(JUNGLE) -> JungleLeaves,
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
    new Wool(WHITE) -> WhiteWool,
    new Wool(ORANGE) -> OrangeWool,
    new Wool(MAGENTA) -> MagentaWool,
    new Wool(LIGHT_BLUE) -> LightBlueWool,
    new Wool(YELLOW) -> YellowWool,
    new Wool(LIME) -> LimeWool,
    new Wool(PINK) -> PinkWool,
    new Wool(GRAY) -> GrayWool,
    new Wool(SILVER) -> LightGrayWool,
    new Wool(CYAN) -> CyanWool,
    new Wool(PURPLE) -> PurpleWool,
    new Wool(BLUE) -> BlueWool,
    new Wool(BROWN) -> BrownWool,
    new Wool(GREEN) -> GreenWool,
    new Wool(RED) -> RedWool,
    new Wool(BLACK) -> BlackWool,
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
    new MaterialData(HUGE_MUSHROOM_1) -> HugeMushroom1,
    new MaterialData(HUGE_MUSHROOM_2) -> HugeMushroom2,
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
    new Wood(GENERIC) -> OakDoubleSlab,
    new Wood(REDWOOD) -> SpruceDoubleSlab,
    new Wood(BIRCH) -> BirchDoubleSlab,
    new Wood(JUNGLE) -> JungleDoubleSlab,
    new Wood(ACACIA) -> AcaciaDoubleSlab,
    new Wood(DARK_OAK) -> DarkOakDoubleSlab,
    new WoodenStep(GENERIC) -> OakSingleSlab,
    new WoodenStep(REDWOOD) -> SpruceSingleSlab,
    new WoodenStep(BIRCH) -> BirchSingleSlab,
    new WoodenStep(JUNGLE) -> JungleSingleSlab,
    new WoodenStep(ACACIA) -> AcaciaSingleSlab,
    new WoodenStep(DARK_OAK) -> DarkOakSingleSlab,
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
    new MaterialData(STAINED_CLAY) -> WhiteHardenedClay,
    new MaterialData(STAINED_CLAY, 1.toByte) -> OrangeHardenedClay,
    new MaterialData(STAINED_CLAY, 2.toByte) -> MagentaHardenedClay,
    new MaterialData(STAINED_CLAY, 3.toByte) -> LightBlueHardenedClay,
    new MaterialData(STAINED_CLAY, 4.toByte) -> YellowHardenedClay,
    new MaterialData(STAINED_CLAY, 5.toByte) -> LimeHardenedClay,
    new MaterialData(STAINED_CLAY, 6.toByte) -> PinkHardenedClay,
    new MaterialData(STAINED_CLAY, 7.toByte) -> GrayHardenedClay,
    new MaterialData(STAINED_CLAY, 8.toByte) -> LightGrayHardenedClay,
    new MaterialData(STAINED_CLAY, 9.toByte) -> CyanHardenedClay,
    new MaterialData(STAINED_CLAY, 10.toByte) -> PurpleHardenedClay,
    new MaterialData(STAINED_CLAY, 11.toByte) -> BlueHardenedClay,
    new MaterialData(STAINED_CLAY, 12.toByte) -> BrownHardenedClay,
    new MaterialData(STAINED_CLAY, 13.toByte) -> GreenHardenedClay,
    new MaterialData(STAINED_CLAY, 14.toByte) -> RedHardenedClay,
    new MaterialData(STAINED_CLAY, 15.toByte) -> BlackHardenedClay,
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
    new Leaves(ACACIA) -> AcaciaLeaves,
    new Leaves(DARK_OAK) -> DarkOakLeaves,
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
    new MaterialData(HARD_CLAY) -> HardenedClay,
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
    new Dye(BLACK) -> InkSack,
    new Dye(RED) -> RoseRed,
    new Dye(GREEN) -> CactusGreen,
    new Dye(BROWN) -> CocoaBeans,
    new Dye(BLUE) -> LapisLazuli,
    new Dye(PURPLE) -> PurpleDye,
    new Dye(CYAN) -> CyanDye,
    new Dye(SILVER) -> LightGrayDye,
    new Dye(GRAY) -> GrayDye,
    new Dye(PINK) -> PinkDye,
    new Dye(LIME) -> LimeDye,
    new Dye(YELLOW) -> DandelionYellow,
    new Dye(LIGHT_BLUE) -> LightBlueDye,
    new Dye(MAGENTA) -> MagentaDye,
    new Dye(ORANGE) -> OrangeDye,
    new Dye(WHITE) -> BoneMeal,
    new MaterialData(BONE) -> Bone,
    new MaterialData(SUGAR) -> Sugar,
    new MaterialData(CAKE) -> Cake,
    new MaterialData(BED) -> Bed,
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
    new MaterialData(RECORD_12) -> Record12/*,
    new MaterialData(STONE, (-1).toByte) -> Seq(Stone, Granite, PolishedGranite, Diorite, PolishedDiorite, Andesite, PolishedAndesite).minBy(_.energy),
    new MaterialData(DIRT, (-1).toByte) -> Seq(Dirt, CoarseDirt, Podzol).minBy(_.energy),
    new MaterialData(WOOD, (-1).toByte) -> Seq(OakWoodPlanks, SpruceWoodPlanks, BirchWoodPlanks, JungleWoodPlanks, AcaciaWoodPlanks, DarkOakWoodPlanks).minBy(_.energy),
    new MaterialData(SAPLING, (-1).toByte) -> Seq(OakSapling, SpruceSapling, BirchSapling, JungleSapling, AcaciaSapling, DarkOakSapling).minBy(_.energy),
    new MaterialData(LOG, (-1).toByte) -> Seq(OakLog, SpruceLog, BirchLog, JungleLog).minBy(_.energy),
    new MaterialData(LEAVES, (-1).toByte) -> Seq(OakLeaves, SpruceLeaves, BirchLeaves, JungleLeaves).minBy(_.energy),
    new MaterialData(SPONGE, (-1).toByte) -> Seq(Sponge, WetSponge).minBy(_.energy),
    new MaterialData(SANDSTONE, (-1).toByte) -> Seq(Sandstone, SmoothSandstone, ChiseledSandstone).minBy(_.energy),
    new MaterialData(LONG_GRASS, (-1).toByte) -> Seq(Shrub, LongGrass, Fern).minBy(_.energy),
    new MaterialData(WOOL, (-1).toByte) -> Seq(WhiteWool, OrangeWool, MagentaWool, LightBlueWool, YellowWool, LimeWool, PinkWool, GrayWool, LightGrayWool, CyanWool, PurpleWool, BlueWool, BrownWool, GreenWool, RedWool, BlackWool).minBy(_.energy),
    new MaterialData(RED_ROSE, (-1).toByte) -> Seq(Poppy, BlueOrchid, Allium, AzureBluet, RedTulip, OrangeTulip, WhiteTulip, PinkTulip, OxeyeDaisy).minBy(_.energy),
    new MaterialData(DOUBLE_STEP, (-1).toByte) -> Seq(StoneDoubleSlab, SandstoneDoubleSlab, OldWoodDoubleSlab, CobblestoneDoubleSlab, BrickDoubleSlab, StoneBrickDoubleSlab, NetherBrickDoubleSlab, QuartzDoubleSlab).minBy(_.energy),
    new MaterialData(STEP, (-1).toByte) -> Seq(StoneSingleSlab, SandstoneSingleSlab, OldWoodSingleSlab, CobblestoneSingleSlab, BrickSingleSlab, StoneBrickSingleSlab, NetherBrickSingleSlab, QuartzSingleSlab).minBy(_.energy),
    new MaterialData(STAINED_GLASS, (-1).toByte) -> Seq(WhiteGlass, OrangeGlass, MagentaGlass, LightBlueGlass, YellowGlass, LimeGlass, PinkGlass, GrayGlass, LightGrayGlass, CyanGlass, PurpleGlass, BlueGlass, BrownGlass, GreenGlass, RedGlass, BlackGlass).minBy(_.energy),
    new MaterialData(SMOOTH_BRICK, (-1).toByte) -> Seq(StoneBrick, CrackedStoneBrick, MossyStoneBrick, ChiseledStoneBrick).minBy(_.energy),
    new MaterialData(WOOD_DOUBLE_STEP, (-1).toByte) -> Seq(OakDoubleSlab, SpruceDoubleSlab, BirchDoubleSlab, JungleDoubleSlab, AcaciaDoubleSlab, DarkOakDoubleSlab).minBy(_.energy),
    new MaterialData(WOOD_STEP, (-1).toByte) -> Seq(OakSingleSlab, SpruceSingleSlab, BirchSingleSlab, JungleSingleSlab, AcaciaSingleSlab, DarkOakSingleSlab).minBy(_.energy),
    new MaterialData(COBBLE_WALL, (-1).toByte) -> Seq(CobblestoneWall, MossyCobblestoneWall).minBy(_.energy),
    new MaterialData(QUARTZ_BLOCK, (-1).toByte) -> Seq(QuartzBlock, ChiseledQuartzBlock, PillarQuartzBlock).minBy(_.energy),
    new MaterialData(STAINED_CLAY, (-1).toByte) -> Seq(WhiteHardenedClay, OrangeHardenedClay, MagentaHardenedClay, LightBlueHardenedClay, YellowHardenedClay, LimeHardenedClay, PinkHardenedClay, GrayHardenedClay, LightGrayHardenedClay, CyanHardenedClay, PurpleHardenedClay, BlueHardenedClay, BrownHardenedClay, GreenHardenedClay, RedHardenedClay, BlackHardenedClay).minBy(_.energy),
    new MaterialData(STAINED_GLASS_PANE, (-1).toByte) -> Seq(WhiteGlassPane, OrangeGlassPane, MagentaGlassPane, LightBlueGlassPane, YellowGlassPane, LimeGlassPane, PinkGlassPane, GrayGlassPane, LightGrayGlassPane, CyanGlassPane, PurpleGlassPane, BlueGlassPane, BrownGlassPane, GreenGlassPane, RedGlassPane, BlackGlassPane).minBy(_.energy),
    new MaterialData(LEAVES_2, (-1).toByte) -> Seq(AcaciaLeaves, DarkOakLeaves).minBy(_.energy),
    new MaterialData(LOG_2, (-1).toByte) -> Seq(AcaciaLog, DarkOakLog).minBy(_.energy),
    new MaterialData(PRISMARINE, (-1).toByte) -> Seq(Prismarine, PrismarineBrick, DarkPrismarine).minBy(_.energy),
    new MaterialData(CARPET, (-1).toByte) -> Seq(WhiteCarpet, OrangeCarpet, MagentaCarpet, LightBlueCarpet, YellowCarpet, LimeCarpet, PinkCarpet, GrayCarpet, LightGrayCarpet, CyanCarpet, PurpleCarpet, BlueCarpet, BrownCarpet, GreenCarpet, RedCarpet, BlackCarpet).minBy(_.energy),
    new MaterialData(DOUBLE_PLANT, (-1).toByte) -> Seq(DoublePlant, Sunflower, Lilac, DoubleTallgrass, LargeFern, RoseBush, Peony, TopPlantHalf).minBy(_.energy),
    new MaterialData(RED_SANDSTONE, (-1).toByte) -> Seq(RedSandstone, SmoothRedSandstone, ChiseledRedSandstone).minBy(_.energy),
    new MaterialData(STRUCTURE_BLOCK, (-1).toByte) -> Seq(StructureSaveBlock, StructureLoadBlock, StructureCornerBlock, StructureDataBlock).minBy(_.energy),
    new MaterialData(COAL, (-1).toByte) -> Seq(Coal, Charcoal).minBy(_.energy),
    new MaterialData(GOLDEN_APPLE, (-1).toByte) -> Seq(GoldenApple, EnchantedGoldenApple).minBy(_.energy),
    new MaterialData(RAW_FISH, (-1).toByte) -> Seq(RawFish, RawSalmon, Clownfish, Pufferfish).minBy(_.energy),
    new MaterialData(COOKED_FISH, (-1).toByte) -> Seq(CookedFish, CookedSalmon).minBy(_.energy),
    new MaterialData(INK_SACK, (-1).toByte) -> Seq(InkSack, RoseRed, CactusGreen, CocoaBeans, LapisLazuli, PurpleDye, CyanDye, LightGrayDye, GrayDye, PinkDye, LimeDye, DandelionYellow, LightBlueDye, MagentaDye, OrangeDye, BoneMeal).minBy(_.energy),
    new MaterialData(SKULL_ITEM, (-1).toByte) -> Seq(SkeletonHead, WitherSkeletonHead, ZombieHead, PlayerHead, CreeperHead, DragonHead).minBy(_.energy),
    new MaterialData(SAND, (-1).toByte) -> Seq(Sand, RedSand).minBy(_.energy),
    new MaterialData(BANNER, (-1).toByte) -> Seq(WhiteBanner, OrangeBanner, MagentaBanner, LightBlueBanner, YellowBanner, LimeBanner, PinkBanner, GrayBanner, LightGrayBanner, CyanBanner, PurpleBanner, BlueBanner, BrownBanner, GreenBanner, RedBanner, BlackBanner).minBy(_.energy)*/
  )

  private val materialToMaterialData: Map[Material, MaterialData] = materialDataToMaterial.map(_.swap)

  @silent implicit def materialData2Material(data: MaterialData): Material = {

    def isSpecialCase(data: MaterialData): Boolean = {
      //Directional stuff
      data.isInstanceOf[Directional] || data.isInstanceOf[RedstoneWire] ||
        //This is all kind of wierd stuff on your inventory, for instance, air(which is used to represent empty slots.
        data.getItemType.isBlock == false && data.getData == 3 ||
        //Materials with durability
        data.getItemType.getMaxDurability.toInt > 0 ||
        //Other stuff
        data.getItemType == ANVIL || data.getItemType == FIRE ||
        //Special Types
        data.getItemType == PISTON_MOVING_PIECE || data.getItemType == AIR
    }

    materialDataToMaterial(if (isSpecialCase(data)) new MaterialData(data.getItemType) else data)
  }

  implicit def Material2MaterialData(material: Material): MaterialData = {
    materialToMaterialData(material)
  }

  override def values: IndexedSeq[Material] = findValues

  //@formatter:off
  object Air extends Material(energy = -1) with Transparent with Crushable with Unconsumable
  object Stone extends Material(energy = 1) with Solid
  object Granite extends Material(energy = 1) with Solid
  object PolishedGranite extends Material with Solid
  object Diorite extends Material(energy = 1) with Solid
  object PolishedDiorite extends Material with Solid
  object Andesite extends Material(energy = 1) with Solid
  object PolishedAndesite extends Material with Solid
  object Grass extends Material(energy = 1) with Solid
  object Dirt extends Material(energy = 1) with Solid
  object CoarseDirt extends Material with Solid
  object Podzol extends Material(energy = 1) with Solid
  object Cobblestone extends Material(energy = 1) with Solid
  object OakWoodPlanks extends Material with GenericPlank
  object SpruceWoodPlanks extends Material with GenericPlank
  object BirchWoodPlanks extends Material with GenericPlank
  object JungleWoodPlanks extends Material with GenericPlank
  object AcaciaWoodPlanks extends Material with GenericPlank
  object DarkOakWoodPlanks extends Material with GenericPlank
  object OakSapling extends Material(tierString = "T2") with GenericSapling
  object SpruceSapling extends Material(tierString = "T2") with GenericSapling
  object BirchSapling extends Material(tierString = "T2") with GenericSapling
  object JungleSapling extends Material(tierString = "T2") with GenericSapling
  object AcaciaSapling extends Material(tierString = "T2") with GenericSapling
  object DarkOakSapling extends Material(tierString = "T2") with GenericSapling
  object Bedrock extends Material(energy = -1) with Solid with Unconsumable
  object Water extends Material(energy = 14) with Transparent with Liquid
  object StationaryWater extends Material(energy = 14) with Transparent with Liquid
  object Lava extends Material(energy = 68) with Liquid
  object StationaryLava extends Material(energy = 68) with Liquid
  object Sand extends Material(energy = 1) with Solid with Gravity
  object RedSand extends Material(energy = 1) with Solid with Gravity
  object Gravel extends Material(tierString = "T2") with Solid with Gravity
  object GoldOre extends Material(energy = 1168) with Solid
  object IronOre extends Material(tierString = "T3") with Solid
  object CoalOre extends Material with Solid
  object OakLog extends Material(tierString = "T2") with GenericLog
  object SpruceLog extends Material(tierString = "T2") with GenericLog
  object BirchLog extends Material(tierString = "T2") with GenericLog
  object JungleLog extends Material(tierString = "T2") with GenericLog
  object OakLeaves extends Material(tierString = "T2") with GenericLeaves
  object SpruceLeaves extends Material(tierString = "T2") with GenericLeaves
  object BirchLeaves extends Material(tierString = "T2") with GenericLeaves
  object JungleLeaves extends Material(tierString = "T2") with GenericLeaves
  object Sponge extends Material(energy = 210) with Solid
  object WetSponge extends Material(energy = 18538) with Solid
  object Glass extends Material with GenericGlass
  object LapisLazuliOre extends Material with Solid
  object LapisLazuliBlock extends Material with Solid
  object Dispenser extends Material with Rotates with Inventory
  object Sandstone extends Material with Solid
  object SmoothSandstone extends Material with Solid
  object ChiseledSandstone extends Material with Solid
  object NoteBlock extends Material with Solid
  object BedBlock extends Material with Solid with Rotates
  object PoweredRails extends Material with GenericRails
  object DetectorRails extends Material with GenericRails
  object StickyPiston extends Material with Solid
  object Cobweb extends Material(energy = 2279) with GenericPlant
  object Shrub extends Material(energy = 1) with GenericPlant
  object LongGrass extends Material(energy = 1) with GenericPlant
  object Fern extends Material(energy = 1) with GenericPlant
  object DeadBush extends Material(tierString = "T3") with GenericPlant
  object Piston extends Material with Solid
  object PistonExtension extends Material(energy = -1) with Solid with Unconsumable
  object WhiteWool extends Material with GenericWool
  object OrangeWool extends Material with GenericWool
  object MagentaWool extends Material with GenericWool
  object LightBlueWool extends Material with GenericWool
  object YellowWool extends Material with GenericWool
  object LimeWool extends Material with GenericWool
  object PinkWool extends Material with GenericWool
  object GrayWool extends Material with GenericWool
  object LightGrayWool extends Material with GenericWool
  object CyanWool extends Material with GenericWool
  object PurpleWool extends Material with GenericWool
  object BlueWool extends Material with GenericWool
  object BrownWool extends Material with GenericWool
  object GreenWool extends Material with GenericWool
  object RedWool extends Material with GenericWool
  object BlackWool extends Material with GenericWool
  object PistonMovingPiece extends Material(energy = -1) with Solid with Unconsumable
  object Dandelion extends Material(energy = 14) with GenericPlant
  object Poppy extends Material(energy = 14) with GenericPlant
  object BlueOrchid extends Material(energy = 14) with GenericPlant
  object Allium extends Material(energy = 14) with GenericPlant
  object AzureBluet extends Material(energy = 14) with GenericPlant
  object RedTulip extends Material(energy = 14) with GenericPlant
  object OrangeTulip extends Material(energy = 14) with GenericPlant
  object WhiteTulip extends Material(energy = 14) with GenericPlant
  object PinkTulip extends Material(energy = 14) with GenericPlant
  object OxeyeDaisy extends Material(energy = 14) with GenericPlant
  object BrownMushroom extends Material(energy = 159) with GenericPlant
  object RedMushroom extends Material(energy = 159) with GenericPlant
  object GoldBlock extends Material with Solid
  object IronBlock extends Material with Solid
  object StoneDoubleSlab extends Material with GenericDoubleSlab
  object SandstoneDoubleSlab extends Material with GenericDoubleSlab
  object OldWoodDoubleSlab extends Material(energy = -1) with Unconsumable with GenericDoubleSlab
  object CobblestoneDoubleSlab extends Material with GenericDoubleSlab
  object BrickDoubleSlab extends Material with GenericDoubleSlab
  object StoneBrickDoubleSlab extends Material with GenericDoubleSlab
  object NetherBrickDoubleSlab extends Material with GenericDoubleSlab
  object QuartzDoubleSlab extends Material with GenericDoubleSlab
  object StoneSingleSlab extends Material with GenericSingleSlab
  object SandstoneSingleSlab extends Material with GenericSingleSlab
  object OldWoodSingleSlab extends Material(energy = -1) with Unconsumable with GenericSingleSlab
  object CobblestoneSingleSlab extends Material with GenericSingleSlab
  object BrickSingleSlab extends Material with GenericSingleSlab
  object StoneBrickSingleSlab extends Material with GenericSingleSlab
  object NetherBrickSingleSlab extends Material with GenericSingleSlab
  object QuartzSingleSlab extends Material with GenericSingleSlab
  object BrickBlock extends Material with Solid
  object TNT extends Material with Solid
  object Bookshelf extends Material with Solid
  object MossyCobblestone extends Material with Solid
  object Obsidian extends Material(energy = 68) with Solid
  object Torch extends Material with GenericTorch with Crushable
  object Fire extends Material(tierString = "T3") with Transparent with Attaches with Crushable
  object MobSpawner extends Material(tierString = "T6") with Solid
  object OakStairs extends Material with GenericStairs
  object Chest extends Material with Solid with Rotates with Inventory
  object RedstoneWire extends Material with Transparent with Attaches
  object DiamondOre extends Material with Solid
  object DiamondBlock extends Material with Solid
  object CraftingTable extends Material with Solid
  object Crops extends Material(tierString = "T2") with GenericPlant
  object Soil extends Material(energy = 1) with Solid
  object Furnace extends Material with Solid with Rotates with Inventory
  object BurningFurnace extends Material with Solid with Rotates with Inventory
  object SignPost extends Material with Solid with Rotates
  object OakDoor extends Material with Solid with Rotates with GenericDoor
  object Ladder extends Material with Transparent with Attaches with Rotates
  object Rails extends Material with GenericRails
  object CobblestoneStairs extends Material with GenericStairs
  object WallSign extends Material with Transparent with Rotates
  object Lever extends Material with Transparent with Attaches with Rotates
  object StonePressurePlate extends Material with Transparent with Attaches
  object IronDoorBlock extends Material with Solid with Transparent with Attaches with Rotates with GenericDoor
  object WoodPressurePlate extends Material with Transparent with Attaches
  object RedstoneOre extends Material with Solid
  object GlowingRedstoneOre extends Material with Solid
  object RedstoneTorchOff extends Material with GenericTorch
  object RedstoneTorchOn extends Material with GenericTorch
  object StoneButton extends Material with Transparent with Attaches with Rotates
  object Snow extends Material(energy = 1) with Transparent with Attaches with Crushable
  object Ice extends Material(energy = 1) with Solid
  object SnowBlock extends Material(energy = 1) with Solid
  object Cactus extends Material(tierString = "T3") with Solid with Attaches
  object Clay extends Material with Solid with GenericClayBlock
  object SugarCaneBlock extends Material(tierString = "T2") with Transparent with Attaches with Crushable
  object Jukebox extends Material with Solid
  object OakFence extends Material with GenericFence
  object Pumpkin extends Material(tierString = "T3") with Solid with Rotates
  object Netherrack extends Material(energy = 1) with Solid
  object SoulSand extends Material with Solid
  object Glowstone extends Material with Solid
  object Portal extends Material(energy = -1) with Transparent with Unconsumable
  object JackOLantern extends Material with Solid
  object CakeBlock extends Material with Solid with Transparent with Food
  object RedstoneRepeaterOff extends Material
  object RedstoneRepeaterOn extends Material
  object WhiteGlass extends Material with GenericGlass
  object OrangeGlass extends Material with GenericGlass
  object MagentaGlass extends Material with GenericGlass
  object LightBlueGlass extends Material with GenericGlass
  object YellowGlass extends Material with GenericGlass
  object LimeGlass extends Material with GenericGlass
  object PinkGlass extends Material with GenericGlass
  object GrayGlass extends Material with GenericGlass
  object LightGrayGlass extends Material with GenericGlass
  object CyanGlass extends Material with GenericGlass
  object PurpleGlass extends Material with GenericGlass
  object BlueGlass extends Material with GenericGlass
  object BrownGlass extends Material with GenericGlass
  object GreenGlass extends Material with GenericGlass
  object RedGlass extends Material with GenericGlass
  object BlackGlass extends Material with GenericGlass
  object TrapDoor extends Material with Solid with Transparent with Rotates with GenericDoor
  object MonsterEggs extends Material(tierString = "T6") with Solid
  object StoneBrick extends Material(energy = 1) with Solid
  object CrackedStoneBrick extends Material(tierString = "T5") with Solid
  object MossyStoneBrick extends Material with Solid
  object ChiseledStoneBrick extends Material with Solid
  object HugeMushroom1 extends Material(tierString = "T3") with Solid
  object HugeMushroom2 extends Material(tierString = "T3") with Solid
  object IronFence extends Material with GenericFence
  object GlassPane extends Material with GenericGlassPane
  object MelonBlock extends Material with Solid
  object PumpkinStem extends Material(energy = -1) with GenericPlant with Rotates
  object MelonStem extends Material(energy = -1) with GenericPlant with Rotates
  object Vine extends Material(tierString = "T5") with Transparent with Crushable
  object OakFenceGate extends Material with GenericFenceGate
  object BrickStairs extends Material with GenericStairs
  object StoneBrickStairs extends Material with GenericStairs
  object Mycelium extends Material(energy = 1) with Solid
  object WaterLily extends Material(tierString = "T5") with GenericPlant
  object NetherBrick extends Material with Solid
  object NetherBrickFence extends Material with GenericFence
  object NetherBrickStairs extends Material with GenericStairs
  object NetherWarts extends Material(tierString = "T4") with GenericPlant
  object EnchantmentTable extends Material with Solid
  object BrewingStand extends Material with Solid
  object Cauldron extends Material with Solid
  object EnderPortal extends Material(energy = -1) with Transparent with Unconsumable
  object EnderPortalFrame extends Material(energy = -1) with Solid with Unconsumable
  object EndStone extends Material(energy = 1) with Solid
  object DragonEgg extends Material(energy = 140369) with Solid
  object RedstoneLampOff extends Material with Solid
  object RedstoneLampOn extends Material with Solid
  object OakDoubleSlab extends Material with GenericDoubleSlab
  object SpruceDoubleSlab extends Material with GenericDoubleSlab
  object BirchDoubleSlab extends Material with GenericDoubleSlab
  object JungleDoubleSlab extends Material with GenericDoubleSlab
  object AcaciaDoubleSlab extends Material with GenericDoubleSlab
  object DarkOakDoubleSlab extends Material with GenericDoubleSlab
  object OakSingleSlab extends Material with GenericSingleSlab
  object SpruceSingleSlab extends Material with GenericSingleSlab
  object BirchSingleSlab extends Material with GenericSingleSlab
  object JungleSingleSlab extends Material with GenericSingleSlab
  object AcaciaSingleSlab extends Material with GenericSingleSlab
  object DarkOakSingleSlab extends Material with GenericSingleSlab
  object Cocoa extends Material(tierString = "T4") with GenericPlant
  object SandstoneStairs extends Material with GenericStairs
  object EmeraldOre extends Material with Solid
  object EnderChest extends Material with Solid with Rotates
  object TripwireHook extends Material with Transparent with Attaches with Rotates
  object Tripwire extends Material with Transparent with Attaches
  object EmeraldBlock extends Material with Solid
  object SpruceStairs extends Material with GenericStairs
  object BirchStairs extends Material with GenericStairs
  object JungleStairs extends Material with GenericStairs
  object Command extends Material(energy = -1) with Solid with Unconsumable
  object Beacon extends Material with Solid
  object CobblestoneWall extends Material with Solid
  object MossyCobblestoneWall extends Material with Solid
  object FlowerPot extends Material with Transparent
  object Carrot extends Material(tierString = "T2") with GenericPlant
  object Potato extends Material(tierString = "T2") with GenericPlant
  object WoodButton extends Material with Transparent with Rotates
  object Skull extends Material(energy = -1) with Transparent with Attaches with Rotates with Unconsumable
  object Anvil extends Material with Solid with Transparent with Gravity with Inventory
  object TrappedChest extends Material with Solid with Rotates
  object GoldPressurePlate extends Material with Solid
  object IronPressurePlate extends Material with Solid
  object RedstoneComparatorOff extends Material with Transparent with Attaches with Rotates
  object RedstoneComparatorOn extends Material with Transparent with Attaches with Rotates
  object DaylightSensor extends Material with Solid
  object RedstoneBlock extends Material with Solid
  object QuartzOre extends Material with Solid
  object Hopper extends Material with Solid with Inventory
  object QuartzBlock extends Material with Solid
  object ChiseledQuartzBlock extends Material with Solid
  object PillarQuartzBlock extends Material with Solid
  object QuartzStairs extends Material with GenericStairs
  object ActivatorRails extends Material with GenericRails
  object Dropper extends Material with Solid with Inventory
  object WhiteHardenedClay extends Material with GenericClayBlock
  object OrangeHardenedClay extends Material with GenericClayBlock
  object MagentaHardenedClay extends Material with GenericClayBlock
  object LightBlueHardenedClay extends Material with GenericClayBlock
  object YellowHardenedClay extends Material with GenericClayBlock
  object LimeHardenedClay extends Material with GenericClayBlock
  object PinkHardenedClay extends Material with GenericClayBlock
  object GrayHardenedClay extends Material with GenericClayBlock
  object LightGrayHardenedClay extends Material with GenericClayBlock
  object CyanHardenedClay extends Material with GenericClayBlock
  object PurpleHardenedClay extends Material with GenericClayBlock
  object BlueHardenedClay extends Material with GenericClayBlock
  object BrownHardenedClay extends Material with GenericClayBlock
  object GreenHardenedClay extends Material with GenericClayBlock
  object RedHardenedClay extends Material with GenericClayBlock
  object BlackHardenedClay extends Material with GenericClayBlock
  object WhiteGlassPane extends Material with GenericGlassPane
  object OrangeGlassPane extends Material with GenericGlassPane
  object MagentaGlassPane extends Material with GenericGlassPane
  object LightBlueGlassPane extends Material with GenericGlassPane
  object YellowGlassPane extends Material with GenericGlassPane
  object LimeGlassPane extends Material with GenericGlassPane
  object PinkGlassPane extends Material with GenericGlassPane
  object GrayGlassPane extends Material with GenericGlassPane
  object LightGrayGlassPane extends Material with GenericGlassPane
  object CyanGlassPane extends Material with GenericGlassPane
  object PurpleGlassPane extends Material with GenericGlassPane
  object BlueGlassPane extends Material with GenericGlassPane
  object BrownGlassPane extends Material with GenericGlassPane
  object GreenGlassPane extends Material with GenericGlassPane
  object RedGlassPane extends Material with GenericGlassPane
  object BlackGlassPane extends Material with GenericGlassPane
  object AcaciaLeaves extends Material(tierString = "T2") with GenericLeaves
  object DarkOakLeaves extends Material(tierString = "T2") with GenericLeaves
  object AcaciaLog extends Material(tierString = "T2") with GenericLog
  object DarkOakLog extends Material(tierString = "T2") with GenericLog
  object AcaciaStairs extends Material with GenericStairs
  object DarkOakStairs extends Material with GenericStairs
  object SlimeBlock extends Material with Solid
  object Barrier extends Material(energy = -1) with Solid with Unconsumable
  object IronTrapdoor extends Material with Solid
  object Prismarine extends Material with Solid
  object PrismarineBrick extends Material with Solid
  object DarkPrismarine extends Material with Solid
  object SeaLantern extends Material with Solid
  object HayBale extends Material with Solid
  object WhiteCarpet extends Material with GenericCarpet
  object OrangeCarpet extends Material with GenericCarpet
  object MagentaCarpet extends Material with GenericCarpet
  object LightBlueCarpet extends Material with GenericCarpet
  object YellowCarpet extends Material with GenericCarpet
  object LimeCarpet extends Material with GenericCarpet
  object PinkCarpet extends Material with GenericCarpet
  object GrayCarpet extends Material with GenericCarpet
  object LightGrayCarpet extends Material with GenericCarpet
  object CyanCarpet extends Material with GenericCarpet
  object PurpleCarpet extends Material with GenericCarpet
  object BlueCarpet extends Material with GenericCarpet
  object BrownCarpet extends Material with GenericCarpet
  object GreenCarpet extends Material with GenericCarpet
  object RedCarpet extends Material with GenericCarpet
  object BlackCarpet extends Material with GenericCarpet
  object HardenedClay extends Material with Solid with GenericClayBlock
  object CoalBlock extends Material with Solid
  object PackedIce extends Material(tierString = "T3") with Solid
  object DoublePlant extends Material(tierString = "T2") with GenericDoublePlant
  object Sunflower extends Material(tierString = "T2") with GenericDoublePlant
  object Lilac extends Material(tierString = "T2") with GenericDoublePlant
  object DoubleTallgrass extends Material(tierString = "T2") with GenericDoublePlant
  object LargeFern extends Material(tierString = "T2") with GenericDoublePlant
  object RoseBush extends Material(tierString = "T2") with GenericDoublePlant
  object Peony extends Material(tierString = "T2") with GenericDoublePlant
  object TopPlantHalf extends Material(energy = 1) with GenericDoublePlant
  object StandingBanner extends Material with Transparent with Attaches with GenericBanner
  object WallBanner extends Material with Transparent with Attaches with GenericBanner
  object InvertedDaylightSensor extends Material with Solid
  object RedSandstone extends Material with Solid
  object SmoothRedSandstone extends Material with Solid
  object ChiseledRedSandstone extends Material with Solid
  object RedSandstoneStairs extends Material with GenericStairs
  object RedSandstoneDoubleSlab extends Material with GenericDoubleSlab
  object RedSandstoneSingleSlab extends Material with GenericSingleSlab
  object SpruceFenceGate extends Material with GenericFenceGate
  object BirchFenceGate extends Material with GenericFenceGate
  object JungleFenceGate extends Material with GenericFenceGate
  object DarkOakFenceGate extends Material with GenericFenceGate
  object AcaciaFenceGate extends Material with GenericFenceGate
  object SpruceFence extends Material with GenericFence
  object BirchFence extends Material with GenericFence
  object JungleFence extends Material with GenericFence
  object DarkOakFence extends Material with GenericFence
  object AcaciaFence extends Material with GenericFence
  object SpruceDoor extends Material with Solid with Transparent with GenericDoor
  object BirchDoor extends Material with Solid with Transparent with GenericDoor
  object JungleDoor extends Material with Solid with Transparent with GenericDoor
  object AcaciaDoor extends Material with Solid with Transparent with GenericDoor
  object DarkOakDoor extends Material with Solid with Transparent with GenericDoor
  object EndRod extends Material(tierString = "T5") with Solid with Transparent
  object ChorusPlant extends Material(tierString = "T2") with Solid
  object ChorusFlower extends Material(tierString = "T2") with Solid with Crushable
  object PurpurBlock extends Material(tierString = "T4") with Solid
  object PurpurPillar extends Material(tierString = "T4") with Solid
  object PurpurStairs extends Material with GenericStairs
  object PurpurDoubleSlab extends Material with GenericDoubleSlab
  object PurpurSingleSlab extends Material with GenericSingleSlab
  object EndStoneBricks extends Material
  object BeetrootPlantation extends Material with GenericPlant
  object GrassPath extends Material with Solid
  object EndGateway extends Material(energy = -1) with Solid with Unconsumable
  object CommandRepeating extends Material(energy = -1) with Solid with Unconsumable
  object CommandChain extends Material(energy = -1) with Solid with Unconsumable
  object FrostedIce extends Material with Solid
  object Magma extends Material with Solid
  object NetherWartBlock extends Material with Solid
  object RedNetherBrick extends Material with Solid
  object BoneBlock extends Material with Solid
  object StructureVoid extends Material(energy = -1) with Solid with Unconsumable
  object Observer extends Material(energy = -1) with Solid with Unconsumable
  object WhiteShulkerBox extends Material with Solid with Inventory
  object OrangeShulkerBox extends Material with Solid with Inventory
  object MagentaShulkerBox extends Material with Solid with Inventory
  object LightBlueShulkerBox extends Material with Solid with Inventory
  object YellowShulkerBox extends Material with Solid with Inventory
  object LimeShulkerBox extends Material with Solid with Inventory
  object PinkShulkerBox extends Material with Solid with Inventory
  object GrayShulkerBox extends Material with Solid with Inventory
  object LightGrayShulkerBox extends Material with Solid with Inventory
  object CyanShulkerBox extends Material with Solid with Inventory
  object PurpleShulkerBox extends Material with Solid with Inventory
  object BlueShulkerBox extends Material with Solid with Inventory
  object BrownShulkerBox extends Material with Solid with Inventory
  object GreenShulkerBox extends Material with Solid with Inventory
  object RedShulkerBox extends Material with Solid with Inventory
  object BlackShulkerBox extends Material with Solid with Inventory
  object StructureSaveBlock extends Material(energy = -1) with Solid with Inventory with Unconsumable
  object StructureLoadBlock extends Material(energy = -1) with Unconsumable
  object StructureCornerBlock extends Material(energy = -1) with Unconsumable
  object StructureDataBlock extends Material(energy = -1) with Unconsumable

  object IronShovel extends Material with GenericShovel
  object IronPickaxe extends Material with GenericPickaxe
  object IronAxe extends Material(energy = 48052) with GenericAxe
  object FlintAndSteel extends Material with Consumable
  object Apple extends Material with Consumable with Food
  object Bow extends Material(energy = 118) with Consumable
  object Arrow extends Material(energy = 5)
  object Coal extends Material(energy = 72)
  object Charcoal extends Material
  object Diamond extends Material(energy = 2869)
  object IronIngot extends Material(energy = 459)
  object GoldIngot extends Material
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
  object ClayBrick extends Material with GenericClayBlock
  object ClayBall extends Material(tierString = "T2")
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
  object DrawnMap extends Material(energy = -1) with Unconsumable
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
  object Potion extends Material(energy = -1) with Consumable with Unconsumable
  object GlassBottle extends Material(energy = 35) with GenericGlass
  object SpiderEye extends Material(energy = 35) with Consumable with Food
  object FermentedSpiderEye extends Material
  object BlazePowder extends Material
  object MagmaCream extends Material(energy = 386)
  object BrewingStandItem extends Material with Inventory
  object CauldronItem extends Material
  object EyeOfEnder extends Material
  object GlisteringMelon extends Material
  object MonsterEgg extends Material(energy = -1) with Consumable with Unconsumable
  object ExpBottle extends Material(energy = -1) with Consumable with Unconsumable
  object FireCharge extends Material with Consumable
  object BookAndQuill extends Material with Usable
  object WrittenBook extends Material(energy = -1) with Usable with Unconsumable
  object Emerald extends Material(energy = 464)
  object ItemFrame extends Material
  object FlowerPotItem extends Material
  object CarrotItem extends Material(energy = 5498) with Consumable with Food
  object PotatoItem extends Material(energy = 5498) with Consumable with Food
  object BakedPotato extends Material with Consumable with Food
  object PoisonousPotato extends Material with Consumable with Food
  object EmptyMap extends Material with Usable
  object GoldenCarrot extends Material with Consumable with Food
  object SkeletonHead extends Material(energy = -1) with Unconsumable
  object WitherSkeletonHead extends Material(energy = -1) with Unconsumable
  object ZombieHead extends Material(energy = -1) with Unconsumable
  object PlayerHead extends Material(energy = -1) with Unconsumable
  object CreeperHead extends Material(energy = -1) with Unconsumable
  object DragonHead extends Material(energy = -1) with Unconsumable
  object CarrotStick extends Material
  object NetherStar extends Material(energy = 62141)
  object PumpkinPie extends Material with Consumable with Food
  object FireworkRocket extends Material(energy = -1) with Consumable with Unconsumable
  object FireworkStar extends Material(energy = -1) with Unconsumable
  object EnchantedBook extends Material(energy = -1) with Unconsumable
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
  object CommandMinecart extends Material(energy = -1) with Unconsumable
  object RawMutton extends Material(energy = 1) with Consumable
  object CookedMutton extends Material with Consumable
  object WhiteBanner extends Material with GenericBanner
  object OrangeBanner extends Material with GenericBanner
  object MagentaBanner extends Material with GenericBanner
  object LightBlueBanner extends Material with GenericBanner
  object YellowBanner extends Material with GenericBanner
  object LimeBanner extends Material with GenericBanner
  object PinkBanner extends Material with GenericBanner
  object GrayBanner extends Material with GenericBanner
  object LightGrayBanner extends Material with GenericBanner
  object CyanBanner extends Material with GenericBanner
  object PurpleBanner extends Material with GenericBanner
  object BlueBanner extends Material with GenericBanner
  object BrownBanner extends Material with GenericBanner
  object GreenBanner extends Material with GenericBanner
  object RedBanner extends Material with GenericBanner
  object BlackBanner extends Material with GenericBanner
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
  object SplashPotion extends Material(energy = -1) with Consumable with Unconsumable
  object SpectralArrow extends Material(energy = -1) with Unconsumable
  object TippedArrow extends Material(energy = -1) with Unconsumable
  object LingeringPotion extends Material(energy = -1) with Consumable with Unconsumable
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