package me.amuxix.material

import enumeratum._
import me.amuxix.Named
import me.amuxix.pattern.Element
import org.bukkit.Material.{TNT => BTNT, _}
import org.bukkit.{Material => BukkitMaterial}

import scala.collection.immutable.{HashMap, IndexedSeq}
import scala.math.{E, log}
import scala.reflect.ClassTag

/**
  * Created by Amuxix on 04/01/2017.
  */

sealed abstract case class Material(protected var _energy: Option[Int] = None) extends EnumEntry with Element with Named {
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

  def toBukkitMaterial: BukkitMaterial = Material.materialToBukkitMaterial(this)
  
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
  def isAttaches: Boolean = isInstanceOf[Attaches]
  def isCrushable: Boolean = isInstanceOf[Crushable]
  def isGravity: Boolean = isInstanceOf[Gravity]
  def hasEnergy: Boolean = !isInstanceOf[NoEnergy]
  def isUsable: Boolean = isInstanceOf[Usable]
  def isFuel: Boolean = isInstanceOf[Fuel]
}

//Generated from https://docs.google.com/spreadsheets/d/1h1gsL9rj9zVvhGUXnsIEpS1RtxSquCgItGQEmeubiTM/edit#gid=0

object Material extends CirceEnum[Material] with Enum[Material] {
  lazy val bukkitMaterialToMaterial: Map[BukkitMaterial, Material] = HashMap(
    ACACIA_BOAT -> AcaciaBoat,
    ACACIA_BUTTON -> AcaciaButton,
    ACACIA_DOOR -> AcaciaDoor,
    ACACIA_FENCE -> AcaciaFence,
    ACACIA_FENCE_GATE -> AcaciaFenceGate,
    ACACIA_LEAVES -> AcaciaLeaves,
    ACACIA_LOG -> AcaciaLog,
    ACACIA_PLANKS -> AcaciaPlanks,
    ACACIA_PRESSURE_PLATE -> AcaciaPressurePlate,
    ACACIA_SAPLING -> AcaciaSapling,
    ACACIA_SLAB -> AcaciaSlab,
    ACACIA_STAIRS -> AcaciaStairs,
    ACACIA_TRAPDOOR -> AcaciaTrapdoor,
    ACACIA_WOOD -> AcaciaWood,
    ACTIVATOR_RAIL -> ActivatorRail,
    AIR -> Air,
    ALLIUM -> Allium,
    ANDESITE -> Andesite,
    ANVIL -> Anvil,
    APPLE -> Apple,
    ARMOR_STAND -> ArmorStand,
    ARROW -> Arrow,
    ATTACHED_MELON_STEM -> AttachedMelonStem,
    ATTACHED_PUMPKIN_STEM -> AttachedPumpkinStem,
    AZURE_BLUET -> AzureBluet,
    BAKED_POTATO -> BakedPotato,
    BARRIER -> Barrier,
    BAT_SPAWN_EGG -> BatSpawnEgg,
    BEACON -> Beacon,
    BEDROCK -> Bedrock,
    BEEF -> RawBeef,
    BEETROOT -> Beetroot,
    BEETROOTS -> BeetrootPlantation,
    BEETROOT_SEEDS -> BeetrootSeeds,
    BEETROOT_SOUP -> BeetrootSoup,
    BIRCH_BOAT -> BirchBoat,
    BIRCH_BUTTON -> BirchButton,
    BIRCH_DOOR -> BirchDoor,
    BIRCH_FENCE -> BirchFence,
    BIRCH_FENCE_GATE -> BirchFenceGate,
    BIRCH_LEAVES -> BirchLeaves,
    BIRCH_LOG -> BirchLog,
    BIRCH_PLANKS -> BirchPlanks,
    BIRCH_PRESSURE_PLATE -> BirchPressurePlate,
    BIRCH_SAPLING -> BirchSapling,
    BIRCH_SLAB -> BirchSlab,
    BIRCH_STAIRS -> BirchStairs,
    BIRCH_TRAPDOOR -> BirchTrapdoor,
    BIRCH_WOOD -> BirchWood,
    BLACK_BANNER -> BlackBanner,
    BLACK_BED -> BlackBed,
    BLACK_CARPET -> BlackCarpet,
    BLACK_CONCRETE -> BlackConcrete,
    BLACK_CONCRETE_POWDER -> BlackConcretePowder,
    BLACK_GLAZED_TERRACOTTA -> BlackGlazedTerracotta,
    BLACK_SHULKER_BOX -> BlackShulkerBox,
    BLACK_STAINED_GLASS -> BlackGlass,
    BLACK_STAINED_GLASS_PANE -> BlackGlassPane,
    BLACK_TERRACOTTA -> BlackTerracotta,
    BLACK_WALL_BANNER -> BlackWallBanner,
    BLACK_WOOL -> BlackWool,
    BLAZE_POWDER -> BlazePowder,
    BLAZE_ROD -> BlazeRod,
    BLAZE_SPAWN_EGG -> BlazeSpawnEgg,
    BLUE_BANNER -> BlueBanner,
    BLUE_BED -> BlueBed,
    BLUE_CARPET -> BlueCarpet,
    BLUE_CONCRETE -> BlueConcrete,
    BLUE_CONCRETE_POWDER -> BlueConcretePowder,
    BLUE_GLAZED_TERRACOTTA -> BlueGlazedTerracotta,
    BLUE_ICE -> BlueIce,
    BLUE_ORCHID -> BlueOrchid,
    BLUE_SHULKER_BOX -> BlueShulkerBox,
    BLUE_STAINED_GLASS -> BlueGlass,
    BLUE_STAINED_GLASS_PANE -> BlueGlassPane,
    BLUE_TERRACOTTA -> BlueTerracotta,
    BLUE_WALL_BANNER -> BlueWallBanner,
    BLUE_WOOL -> BlueWool,
    BONE -> Bone,
    BONE_BLOCK -> BoneBlock,
    BONE_MEAL -> BoneMeal,
    BOOK -> Book,
    BOOKSHELF -> Bookshelf,
    BOW -> Bow,
    BOWL -> Bowl,
    BRAIN_CORAL -> BrainCoral,
    BRAIN_CORAL_BLOCK -> BrainCoralBlock,
    BRAIN_CORAL_FAN -> BrainCoralFan,
    BRAIN_CORAL_WALL_FAN -> BrainCoralWallFan,
    BREAD -> Bread,
    BREWING_STAND -> BrewingStand,
    BRICK -> Brick,
    BRICKS -> BrickBlock,
    BRICK_SLAB -> BrickSingleSlab,
    BRICK_STAIRS -> BrickStairs,
    BROWN_BANNER -> BrownBanner,
    BROWN_BED -> BrownBed,
    BROWN_CARPET -> BrownCarpet,
    BROWN_CONCRETE -> BrownConcrete,
    BROWN_CONCRETE_POWDER -> BrownConcretePowder,
    BROWN_GLAZED_TERRACOTTA -> BrownGlazedTerracotta,
    BROWN_MUSHROOM -> BrownMushroom,
    BROWN_MUSHROOM_BLOCK -> BrownMushroomBlock,
    BROWN_SHULKER_BOX -> BrownShulkerBox,
    BROWN_STAINED_GLASS -> BrownGlass,
    BROWN_STAINED_GLASS_PANE -> BrownGlassPane,
    BROWN_TERRACOTTA -> BrownTerracotta,
    BROWN_WALL_BANNER -> BrownWallBanner,
    BROWN_WOOL -> BrownWool,
    BUBBLE_COLUMN -> BubbleColumn,
    BUBBLE_CORAL -> BubbleCoral,
    BUBBLE_CORAL_BLOCK -> BubbleCoralBlock,
    BUBBLE_CORAL_FAN -> BubbleCoralFan,
    BUBBLE_CORAL_WALL_FAN -> BubbleCoralWallFan,
    BUCKET -> Bucket,
    CACTUS -> Cactus,
    CACTUS_GREEN -> CactusGreen,
    CAKE -> Cake,
    CARROT -> Carrot,
    CARROTS -> Carrots,
    CARROT_ON_A_STICK -> CarrotOnAStick,
    CARVED_PUMPKIN -> CarvedPumpkin,
    CAULDRON -> Cauldron,
    CAVE_AIR -> CaveAir,
    CAVE_SPIDER_SPAWN_EGG -> CaveSpiderSpawnEgg,
    CHAINMAIL_BOOTS -> ChainmailBoots,
    CHAINMAIL_CHESTPLATE -> ChainmailChestplate,
    CHAINMAIL_HELMET -> ChainmailHelmet,
    CHAINMAIL_LEGGINGS -> ChainmailLeggings,
    CHAIN_COMMAND_BLOCK -> ChainCommand,
    CHARCOAL -> Charcoal,
    CHEST -> Chest,
    CHEST_MINECART -> ChestMinecart,
    CHICKEN -> RawChicken,
    CHICKEN_SPAWN_EGG -> ChickenSpawnEgg,
    CHIPPED_ANVIL -> ChippedAnvil,
    CHISELED_QUARTZ_BLOCK -> ChiseledQuartzBlock,
    CHISELED_RED_SANDSTONE -> ChiseledRedSandstone,
    CHISELED_SANDSTONE -> ChiseledSandstone,
    CHISELED_STONE_BRICKS -> ChiseledStoneBricks,
    CHORUS_FLOWER -> ChorusFlower,
    CHORUS_FRUIT -> ChorusFruit,
    CHORUS_PLANT -> ChorusPlant,
    CLAY -> Clay,
    CLAY_BALL -> ClayBall,
    CLOCK -> Clock,
    COAL -> Coal,
    COAL_BLOCK -> CoalBlock,
    COAL_ORE -> CoalOre,
    COARSE_DIRT -> CoarseDirt,
    COBBLESTONE -> Cobblestone,
    COBBLESTONE_SLAB -> CobblestoneSlab,
    COBBLESTONE_STAIRS -> CobblestoneStairs,
    COBBLESTONE_WALL -> CobblestoneWall,
    COBWEB -> Cobweb,
    COCOA -> Cocoa,
    COCOA_BEANS -> CocoaBeans,
    COD -> Cod,
    COD_BUCKET -> CodBucket,
    COD_SPAWN_EGG -> CodSpawnEgg,
    COMMAND_BLOCK -> Command,
    COMMAND_BLOCK_MINECART -> CommandBlockMinecart,
    COMPARATOR -> RedstoneComparator,
    COMPASS -> Compass,
    CONDUIT -> Conduit,
    COOKED_BEEF -> CookedBeef,
    COOKED_CHICKEN -> CookedChicken,
    COOKED_COD -> CookedCod,
    COOKED_MUTTON -> CookedMutton,
    COOKED_PORKCHOP -> CookedPorkchop,
    COOKED_RABBIT -> CookedRabbit,
    COOKED_SALMON -> CookedSalmon,
    COOKIE -> Cookie,
    COW_SPAWN_EGG -> CowSpawnEgg,
    CRACKED_STONE_BRICKS -> CrackedStoneBricks,
    CRAFTING_TABLE -> CraftingTable,
    CREEPER_HEAD -> CreeperHead,
    CREEPER_SPAWN_EGG -> CreeperSpawnEgg,
    CREEPER_WALL_HEAD -> CreeperWallHead,
    CUT_RED_SANDSTONE -> CutRedSandstone,
    CUT_SANDSTONE -> CutSandstone,
    CYAN_BANNER -> CyanBanner,
    CYAN_BED -> CyanBed,
    CYAN_CARPET -> CyanCarpet,
    CYAN_CONCRETE -> CyanConcrete,
    CYAN_CONCRETE_POWDER -> CyanConcretePowder,
    CYAN_DYE -> CyanDye,
    CYAN_GLAZED_TERRACOTTA -> CyanGlazedTerracotta,
    CYAN_SHULKER_BOX -> CyanShulkerBox,
    CYAN_STAINED_GLASS -> CyanGlass,
    CYAN_STAINED_GLASS_PANE -> CyanGlassPane,
    CYAN_TERRACOTTA -> CyanTerracotta,
    CYAN_WALL_BANNER -> CyanWallBanner,
    CYAN_WOOL -> CyanWool,
    DAMAGED_ANVIL -> DamagedAnvil,
    DANDELION -> Dandelion,
    DANDELION_YELLOW -> DandelionYellow,
    DARK_OAK_BOAT -> DarkOakBoat,
    DARK_OAK_BUTTON -> DarkOakButton,
    DARK_OAK_DOOR -> DarkOakDoor,
    DARK_OAK_FENCE -> DarkOakFence,
    DARK_OAK_FENCE_GATE -> DarkOakFenceGate,
    DARK_OAK_LEAVES -> DarkOakLeaves,
    DARK_OAK_LOG -> DarkOakLog,
    DARK_OAK_PLANKS -> DarkOakPlanks,
    DARK_OAK_PRESSURE_PLATE -> DarkOakPressurePlate,
    DARK_OAK_SAPLING -> DarkOakSapling,
    DARK_OAK_SLAB -> DarkOakSlab,
    DARK_OAK_STAIRS -> DarkOakStairs,
    DARK_OAK_TRAPDOOR -> DarkOakTrapdoor,
    DARK_OAK_WOOD -> DarkOakWood,
    DARK_PRISMARINE -> DarkPrismarine,
    DARK_PRISMARINE_SLAB -> DarkPrismarineSlab,
    DARK_PRISMARINE_STAIRS -> DarkPrismarineStairs,
    DAYLIGHT_DETECTOR -> DaylightDetector,
    DEAD_BRAIN_CORAL -> DeadBrainCoral,
    DEAD_BRAIN_CORAL_BLOCK -> DeadBrainCoralBlock,
    DEAD_BRAIN_CORAL_FAN -> DeadBrainCoralFan,
    DEAD_BRAIN_CORAL_WALL_FAN -> DeadBrainCoralWallFan,
    DEAD_BUBBLE_CORAL -> DeadBubbleCoral,
    DEAD_BUBBLE_CORAL_BLOCK -> DeadBubbleCoralBlock,
    DEAD_BUBBLE_CORAL_FAN -> DeadBubbleCoralFan,
    DEAD_BUBBLE_CORAL_WALL_FAN -> DeadBubbleCoralWallFan,
    DEAD_BUSH -> DeadBush,
    DEAD_FIRE_CORAL -> DeadFireCoral,
    DEAD_FIRE_CORAL_BLOCK -> DeadFireCoralBlock,
    DEAD_FIRE_CORAL_FAN -> DeadFireCoralFan,
    DEAD_FIRE_CORAL_WALL_FAN -> DeadFireCoralWallFan,
    DEAD_HORN_CORAL -> DeadHornCoral,
    DEAD_HORN_CORAL_BLOCK -> DeadHornCoralBlock,
    DEAD_HORN_CORAL_FAN -> DeadHornCoralFan,
    DEAD_HORN_CORAL_WALL_FAN -> DeadHornCoralWallFan,
    DEAD_TUBE_CORAL -> DeadTubeCoral,
    DEAD_TUBE_CORAL_BLOCK -> DeadTubeCoralBlock,
    DEAD_TUBE_CORAL_FAN -> DeadTubeCoralFan,
    DEAD_TUBE_CORAL_WALL_FAN -> DeadTubeCoralWallFan,
    DEBUG_STICK -> DebugStick,
    DETECTOR_RAIL -> DetectorRail,
    DIAMOND -> Diamond,
    DIAMOND_AXE -> DiamondAxe,
    DIAMOND_BLOCK -> DiamondBlock,
    DIAMOND_BOOTS -> DiamondBoots,
    DIAMOND_CHESTPLATE -> DiamondChestplate,
    DIAMOND_HELMET -> DiamondHelmet,
    DIAMOND_HOE -> DiamondHoe,
    DIAMOND_HORSE_ARMOR -> DiamondHorseArmor,
    DIAMOND_LEGGINGS -> DiamondLeggings,
    DIAMOND_ORE -> DiamondOre,
    DIAMOND_PICKAXE -> DiamondPickaxe,
    DIAMOND_SHOVEL -> DiamondShovel,
    DIAMOND_SWORD -> DiamondSword,
    DIORITE -> Diorite,
    DIRT -> Dirt,
    DISPENSER -> Dispenser,
    DOLPHIN_SPAWN_EGG -> DolphinSpawnEgg,
    DONKEY_SPAWN_EGG -> DonkeySpawnEgg,
    DRAGON_BREATH -> DragonBreath,
    DRAGON_EGG -> DragonEgg,
    DRAGON_HEAD -> DragonHead,
    DRAGON_WALL_HEAD -> DragonWallHead,
    DRIED_KELP -> DriedKelp,
    DRIED_KELP_BLOCK -> DriedKelpBlock,
    DROPPER -> Dropper,
    DROWNED_SPAWN_EGG -> DrownedSpawnEgg,
    EGG -> Egg,
    ELDER_GUARDIAN_SPAWN_EGG -> ElderGuardianSpawnEgg,
    ELYTRA -> Elytra,
    EMERALD -> Emerald,
    EMERALD_BLOCK -> EmeraldBlock,
    EMERALD_ORE -> EmeraldOre,
    ENCHANTED_BOOK -> EnchantedBook,
    ENCHANTED_GOLDEN_APPLE -> EnchantedGoldenApple,
    ENCHANTING_TABLE -> EnchantingTable,
    ENDERMAN_SPAWN_EGG -> EndermanSpawnEgg,
    ENDERMITE_SPAWN_EGG -> EndermiteSpawnEgg,
    ENDER_CHEST -> EnderChest,
    ENDER_EYE -> EnderEye,
    ENDER_PEARL -> EnderPearl,
    END_CRYSTAL -> EndCrystal,
    END_GATEWAY -> EndGateway,
    END_PORTAL -> EndPortal,
    END_PORTAL_FRAME -> EndPortalFrame,
    END_ROD -> EndRod,
    END_STONE -> EndStone,
    END_STONE_BRICKS -> EndStoneBricks,
    EVOKER_SPAWN_EGG -> EvokerSpawnEgg,
    EXPERIENCE_BOTTLE -> ExperienceBottle,
    FARMLAND -> Farmland,
    FEATHER -> Feather,
    FERMENTED_SPIDER_EYE -> FermentedSpiderEye,
    FERN -> Fern,
    FILLED_MAP -> FilledMap,
    FIRE -> Fire,
    FIREWORK_ROCKET -> FireworkRocket,
    FIREWORK_STAR -> FireworkStar,
    FIRE_CHARGE -> FireCharge,
    FIRE_CORAL -> FireCoral,
    FIRE_CORAL_BLOCK -> FireCoralBlock,
    FIRE_CORAL_FAN -> FireCoralFan,
    FIRE_CORAL_WALL_FAN -> FireCoralWallFan,
    FISHING_ROD -> FishingRod,
    FLINT -> Flint,
    FLINT_AND_STEEL -> FlintAndSteel,
    FLOWER_POT -> FlowerPot,
    FROSTED_ICE -> FrostedIce,
    FURNACE -> Furnace,
    FURNACE_MINECART -> FurnaceMinecart,
    GHAST_SPAWN_EGG -> GhastSpawnEgg,
    GHAST_TEAR -> GhastTear,
    GLASS -> Glass,
    GLASS_BOTTLE -> GlassBottle,
    GLASS_PANE -> GlassPane,
    GLISTERING_MELON_SLICE -> GlisteringMelonSlice,
    GLOWSTONE -> Glowstone,
    GLOWSTONE_DUST -> GlowstoneDust,
    GOLDEN_APPLE -> GoldenApple,
    GOLDEN_AXE -> GoldenAxe,
    GOLDEN_BOOTS -> GoldenBoots,
    GOLDEN_CARROT -> GoldenCarrot,
    GOLDEN_CHESTPLATE -> GoldenChestplate,
    GOLDEN_HELMET -> GoldenHelmet,
    GOLDEN_HOE -> GoldenHoe,
    GOLDEN_HORSE_ARMOR -> GoldenHorseArmor,
    GOLDEN_LEGGINGS -> GoldenLeggings,
    GOLDEN_PICKAXE -> GoldenPickaxe,
    GOLDEN_SHOVEL -> GoldenShovel,
    GOLDEN_SWORD -> GoldenSword,
    GOLD_BLOCK -> GoldBlock,
    GOLD_INGOT -> GoldIngot,
    GOLD_NUGGET -> GoldNugget,
    GOLD_ORE -> GoldOre,
    GRANITE -> Granite,
    GRASS -> Grass,
    GRASS_BLOCK -> GrassBlock,
    GRASS_PATH -> GrassPath,
    GRAVEL -> Gravel,
    GRAY_BANNER -> GrayBanner,
    GRAY_BED -> GrayBed,
    GRAY_CARPET -> GrayCarpet,
    GRAY_CONCRETE -> GrayConcrete,
    GRAY_CONCRETE_POWDER -> GrayConcretePowder,
    GRAY_DYE -> GrayDye,
    GRAY_GLAZED_TERRACOTTA -> GrayGlazedTerracotta,
    GRAY_SHULKER_BOX -> GrayShulkerBox,
    GRAY_STAINED_GLASS -> GrayGlass,
    GRAY_STAINED_GLASS_PANE -> GrayGlassPane,
    GRAY_TERRACOTTA -> GrayTerracotta,
    GRAY_WALL_BANNER -> GrayWallBanner,
    GRAY_WOOL -> GrayWool,
    GREEN_BANNER -> GreenBanner,
    GREEN_BED -> GreenBed,
    GREEN_CARPET -> GreenCarpet,
    GREEN_CONCRETE -> GreenConcrete,
    GREEN_CONCRETE_POWDER -> GreenConcretePowder,
    GREEN_GLAZED_TERRACOTTA -> GreenGlazedTerracotta,
    GREEN_SHULKER_BOX -> GreenShulkerBox,
    GREEN_STAINED_GLASS -> GreenGlass,
    GREEN_STAINED_GLASS_PANE -> GreenGlassPane,
    GREEN_TERRACOTTA -> GreenTerracotta,
    GREEN_WALL_BANNER -> GreenWallBanner,
    GREEN_WOOL -> GreenWool,
    GUARDIAN_SPAWN_EGG -> GuardianSpawnEgg,
    GUNPOWDER -> Gunpowder,
    HAY_BLOCK -> HayBale,
    HEART_OF_THE_SEA -> HeartOfTheSea,
    HEAVY_WEIGHTED_PRESSURE_PLATE -> IronPressurePlate,
    HOPPER -> Hopper,
    HOPPER_MINECART -> HopperMinecart,
    HORN_CORAL -> HornCoral,
    HORN_CORAL_BLOCK -> HornCoralBlock,
    HORN_CORAL_FAN -> HornCoralFan,
    HORN_CORAL_WALL_FAN -> HornCoralWallFan,
    HORSE_SPAWN_EGG -> HorseSpawnEgg,
    HUSK_SPAWN_EGG -> HuskSpawnEgg,
    ICE -> Ice,
    INFESTED_CHISELED_STONE_BRICKS -> InfestedChiseledStoneBricks,
    INFESTED_COBBLESTONE -> InfestedCobblestone,
    INFESTED_CRACKED_STONE_BRICKS -> InfestedCrackedStoneBricks,
    INFESTED_MOSSY_STONE_BRICKS -> InfestedMossyStoneBricks,
    INFESTED_STONE -> InfestedStone,
    INFESTED_STONE_BRICKS -> InfestedStoneBricks,
    INK_SAC -> InkSac,
    IRON_AXE -> IronAxe,
    IRON_BARS -> IronBars,
    IRON_BLOCK -> IronBlock,
    IRON_BOOTS -> IronBoots,
    IRON_CHESTPLATE -> IronChestplate,
    IRON_DOOR -> IronDoor,
    IRON_HELMET -> IronHelmet,
    IRON_HOE -> IronHoe,
    IRON_HORSE_ARMOR -> IronHorseArmor,
    IRON_INGOT -> IronIngot,
    IRON_LEGGINGS -> IronLeggings,
    IRON_NUGGET -> IronNugget,
    IRON_ORE -> IronOre,
    IRON_PICKAXE -> IronPickaxe,
    IRON_SHOVEL -> IronShovel,
    IRON_SWORD -> IronSword,
    IRON_TRAPDOOR -> IronTrapdoor,
    ITEM_FRAME -> ItemFrame,
    JACK_O_LANTERN -> JackOLantern,
    JUKEBOX -> Jukebox,
    JUNGLE_BOAT -> JungleBoat,
    JUNGLE_BUTTON -> JungleButton,
    JUNGLE_DOOR -> JungleDoor,
    JUNGLE_FENCE -> JungleFence,
    JUNGLE_FENCE_GATE -> JungleFenceGate,
    JUNGLE_LEAVES -> JungleLeaves,
    JUNGLE_LOG -> JungleLog,
    JUNGLE_PLANKS -> JunglePlanks,
    JUNGLE_PRESSURE_PLATE -> JunglePressurePlate,
    JUNGLE_SAPLING -> JungleSapling,
    JUNGLE_SLAB -> JungleSlab,
    JUNGLE_STAIRS -> JungleStairs,
    JUNGLE_TRAPDOOR -> JungleTrapdoor,
    JUNGLE_WOOD -> JungleWood,
    KELP -> Kelp,
    KELP_PLANT -> KelpPlant,
    KNOWLEDGE_BOOK -> KnowledgeBook,
    LADDER -> Ladder,
    LAPIS_BLOCK -> LapisLazuliBlock,
    LAPIS_LAZULI -> LapisLazuli,
    LAPIS_ORE -> LapisLazuliOre,
    LARGE_FERN -> LargeFern,
    LAVA -> Lava,
    LAVA_BUCKET -> LavaBucket,
    LEAD -> Leash,
    LEATHER -> Leather,
    LEATHER_BOOTS -> LeatherBoots,
    LEATHER_CHESTPLATE -> LeatherChestplate,
    LEATHER_HELMET -> LeatherHelmet,
    LEATHER_LEGGINGS -> LeatherLeggings,
    LEVER -> Lever,
    LIGHT_BLUE_BANNER -> LightBlueBanner,
    LIGHT_BLUE_BED -> LightBlueBed,
    LIGHT_BLUE_CARPET -> LightBlueCarpet,
    LIGHT_BLUE_CONCRETE -> LightBlueConcrete,
    LIGHT_BLUE_CONCRETE_POWDER -> LightBlueConcretePowder,
    LIGHT_BLUE_DYE -> LightBlueDye,
    LIGHT_BLUE_GLAZED_TERRACOTTA -> LightBlueGlazedTerracotta,
    LIGHT_BLUE_SHULKER_BOX -> LightBlueShulkerBox,
    LIGHT_BLUE_STAINED_GLASS -> LightBlueGlass,
    LIGHT_BLUE_STAINED_GLASS_PANE -> LightBlueGlassPane,
    LIGHT_BLUE_TERRACOTTA -> LightBlueTerracotta,
    LIGHT_BLUE_WALL_BANNER -> LightBlueWallBanner,
    LIGHT_BLUE_WOOL -> LightBlueWool,
    LIGHT_GRAY_BANNER -> LightGrayBanner,
    LIGHT_GRAY_BED -> LightGrayBed,
    LIGHT_GRAY_CARPET -> LightGrayCarpet,
    LIGHT_GRAY_CONCRETE -> LightGrayConcrete,
    LIGHT_GRAY_CONCRETE_POWDER -> LightGrayConcretePowder,
    LIGHT_GRAY_DYE -> LightGrayDye,
    LIGHT_GRAY_GLAZED_TERRACOTTA -> LightGrayGlazedTerracotta,
    LIGHT_GRAY_SHULKER_BOX -> LightGrayShulkerBox,
    LIGHT_GRAY_STAINED_GLASS -> LightGrayGlass,
    LIGHT_GRAY_STAINED_GLASS_PANE -> LightGrayGlassPane,
    LIGHT_GRAY_TERRACOTTA -> LightGrayTerracotta,
    LIGHT_GRAY_WALL_BANNER -> LightGrayWallBanner,
    LIGHT_GRAY_WOOL -> LightGrayWool,
    LIGHT_WEIGHTED_PRESSURE_PLATE -> GoldenPressurePlate,
    LILAC -> Lilac,
    LILY_PAD -> LilyPad,
    LIME_BANNER -> LimeBanner,
    LIME_BED -> LimeBed,
    LIME_CARPET -> LimeCarpet,
    LIME_CONCRETE -> LimeConcrete,
    LIME_CONCRETE_POWDER -> LimeConcretePowder,
    LIME_DYE -> LimeDye,
    LIME_GLAZED_TERRACOTTA -> LimeGlazedTerracotta,
    LIME_SHULKER_BOX -> LimeShulkerBox,
    LIME_STAINED_GLASS -> LimeGlass,
    LIME_STAINED_GLASS_PANE -> LimeGlassPane,
    LIME_TERRACOTTA -> LimeTerracotta,
    LIME_WALL_BANNER -> LimeWallBanner,
    LIME_WOOL -> LimeWool,
    LINGERING_POTION -> LingeringPotion,
    LLAMA_SPAWN_EGG -> LlamaSpawnEgg,
    MAGENTA_BANNER -> MagentaBanner,
    MAGENTA_BED -> MagentaBed,
    MAGENTA_CARPET -> MagentaCarpet,
    MAGENTA_CONCRETE -> MagentaConcrete,
    MAGENTA_CONCRETE_POWDER -> MagentaConcretePowder,
    MAGENTA_DYE -> MagentaDye,
    MAGENTA_GLAZED_TERRACOTTA -> MagentaGlazedTerracotta,
    MAGENTA_SHULKER_BOX -> MagentaShulkerBox,
    MAGENTA_STAINED_GLASS -> MagentaGlass,
    MAGENTA_STAINED_GLASS_PANE -> MagentaGlassPane,
    MAGENTA_TERRACOTTA -> MagentaTerracotta,
    MAGENTA_WALL_BANNER -> MagentaWallBanner,
    MAGENTA_WOOL -> MagentaWool,
    MAGMA_BLOCK -> MagmaBlock,
    MAGMA_CREAM -> MagmaCream,
    MAGMA_CUBE_SPAWN_EGG -> MagmaCubeSpawnEgg,
    MAP -> EmptyMap,
    MELON -> Melon,
    MELON_SEEDS -> MelonSeeds,
    MELON_SLICE -> MelonSlice,
    MELON_STEM -> MelonStem,
    MILK_BUCKET -> MilkBucket,
    MINECART -> Minecart,
    MOOSHROOM_SPAWN_EGG -> MooshroomSpawnEgg,
    MOSSY_COBBLESTONE -> MossyCobblestone,
    MOSSY_COBBLESTONE_WALL -> MossyCobblestoneWall,
    MOSSY_STONE_BRICKS -> MossyStoneBricks,
    MOVING_PISTON -> PistonMovingPiece,
    MULE_SPAWN_EGG -> MuleSpawnEgg,
    MUSHROOM_STEM -> MushroomStem,
    MUSHROOM_STEW -> MushroomStew,
    MUSIC_DISC_11 -> MusicDisc11,
    MUSIC_DISC_13 -> MusicDisc13,
    MUSIC_DISC_BLOCKS -> MusicDiscBlocks,
    MUSIC_DISC_CAT -> MusicDiscCat,
    MUSIC_DISC_CHIRP -> MusicDiscChirp,
    MUSIC_DISC_FAR -> MusicDiscFar,
    MUSIC_DISC_MALL -> MusicDiscMall,
    MUSIC_DISC_MELLOHI -> MusicDiscMellohi,
    MUSIC_DISC_STAL -> MusicDiscStal,
    MUSIC_DISC_STRAD -> MusicDiscStrad,
    MUSIC_DISC_WAIT -> MusicDiscWait,
    MUSIC_DISC_WARD -> MusicDiscWard,
    MUTTON -> RawMutton,
    MYCELIUM -> Mycelium,
    NAME_TAG -> NameTag,
    NAUTILUS_SHELL -> NautilusShell,
    NETHERRACK -> Netherrack,
    NETHER_BRICK -> NetherBrick,
    NETHER_BRICKS -> NetherBricks,
    NETHER_BRICK_FENCE -> NetherBrickFence,
    NETHER_BRICK_SLAB -> NetherBrickSlab,
    NETHER_BRICK_STAIRS -> NetherBrickStairs,
    NETHER_PORTAL -> NetherPortal,
    NETHER_QUARTZ_ORE -> QuartzOre,
    NETHER_STAR -> NetherStar,
    NETHER_WART -> NetherWart,
    NETHER_WART_BLOCK -> NetherWartBlock,
    NOTE_BLOCK -> NoteBlock,
    OAK_BOAT -> OakBoat,
    OAK_BUTTON -> OakButton,
    OAK_DOOR -> OakDoor,
    OAK_FENCE -> OakFence,
    OAK_FENCE_GATE -> OakFenceGate,
    OAK_LEAVES -> OakLeaves,
    OAK_LOG -> OakLog,
    OAK_PLANKS -> OakPlanks,
    OAK_PRESSURE_PLATE -> OakPressurePlate,
    OAK_SAPLING -> OakSapling,
    OAK_SLAB -> OakSlab,
    OAK_STAIRS -> OakStairs,
    OAK_TRAPDOOR -> OakTrapdoor,
    OAK_WOOD -> OakWood,
    OBSERVER -> Observer,
    OBSIDIAN -> Obsidian,
    OCELOT_SPAWN_EGG -> OcelotSpawnEgg,
    ORANGE_BANNER -> OrangeBanner,
    ORANGE_BED -> OrangeBed,
    ORANGE_CARPET -> OrangeCarpet,
    ORANGE_CONCRETE -> OrangeConcrete,
    ORANGE_CONCRETE_POWDER -> OrangeConcretePowder,
    ORANGE_DYE -> OrangeDye,
    ORANGE_GLAZED_TERRACOTTA -> OrangeGlazedTerracotta,
    ORANGE_SHULKER_BOX -> OrangeShulkerBox,
    ORANGE_STAINED_GLASS -> OrangeGlass,
    ORANGE_STAINED_GLASS_PANE -> OrangeGlassPane,
    ORANGE_TERRACOTTA -> OrangeTerracotta,
    ORANGE_TULIP -> OrangeTulip,
    ORANGE_WALL_BANNER -> OrangeWallBanner,
    ORANGE_WOOL -> OrangeWool,
    OXEYE_DAISY -> OxeyeDaisy,
    PACKED_ICE -> PackedIce,
    PAINTING -> Painting,
    PAPER -> Paper,
    PARROT_SPAWN_EGG -> ParrotSpawnEgg,
    PEONY -> Peony,
    PETRIFIED_OAK_SLAB -> PetrifiedOakSlab,
    PHANTOM_MEMBRANE -> PhantomMembrane,
    PHANTOM_SPAWN_EGG -> PhantomSpawnEgg,
    PIG_SPAWN_EGG -> PigSpawnEgg,
    PINK_BANNER -> PinkBanner,
    PINK_BED -> PinkBed,
    PINK_CARPET -> PinkCarpet,
    PINK_CONCRETE -> PinkConcrete,
    PINK_CONCRETE_POWDER -> PinkConcretePowder,
    PINK_DYE -> PinkDye,
    PINK_GLAZED_TERRACOTTA -> PinkGlazedTerracotta,
    PINK_SHULKER_BOX -> PinkShulkerBox,
    PINK_STAINED_GLASS -> PinkGlass,
    PINK_STAINED_GLASS_PANE -> PinkGlassPane,
    PINK_TERRACOTTA -> PinkTerracotta,
    PINK_TULIP -> PinkTulip,
    PINK_WALL_BANNER -> PinkWallBanner,
    PINK_WOOL -> PinkWool,
    PISTON -> Piston,
    PISTON_HEAD -> PistonHead,
    PLAYER_HEAD -> PlayerHead,
    PLAYER_WALL_HEAD -> PlayerWallHead,
    PODZOL -> Podzol,
    POISONOUS_POTATO -> PoisonousPotato,
    POLAR_BEAR_SPAWN_EGG -> PolarBearSpawnEgg,
    POLISHED_ANDESITE -> PolishedAndesite,
    POLISHED_DIORITE -> PolishedDiorite,
    POLISHED_GRANITE -> PolishedGranite,
    POPPED_CHORUS_FRUIT -> PoppedChorusFruit,
    POPPY -> Poppy,
    PORKCHOP -> RawPorkchop,
    POTATO -> Potato,
    POTATOES -> Potatoes,
    POTION -> Potion,
    POTTED_ACACIA_SAPLING -> PottedAcaciaSapling,
    POTTED_ALLIUM -> PottedAllium,
    POTTED_AZURE_BLUET -> PottedAzureBluet,
    POTTED_BIRCH_SAPLING -> PottedBirchSapling,
    POTTED_BLUE_ORCHID -> PottedBlueOrchid,
    POTTED_BROWN_MUSHROOM -> PottedBrownMushroom,
    POTTED_CACTUS -> PottedCactus,
    POTTED_DANDELION -> PottedDandelion,
    POTTED_DARK_OAK_SAPLING -> PottedDarkOakSapling,
    POTTED_DEAD_BUSH -> PottedDeadBush,
    POTTED_FERN -> PottedFern,
    POTTED_JUNGLE_SAPLING -> PottedJungleSapling,
    POTTED_OAK_SAPLING -> PottedOakSapling,
    POTTED_ORANGE_TULIP -> PottedOrangeTulip,
    POTTED_OXEYE_DAISY -> PottedOxeyeDaisy,
    POTTED_PINK_TULIP -> PottedPinkTulip,
    POTTED_POPPY -> PottedPoppy,
    POTTED_RED_MUSHROOM -> PottedRedMushroom,
    POTTED_RED_TULIP -> PottedRedTulip,
    POTTED_SPRUCE_SAPLING -> PottedSpruceSapling,
    POTTED_WHITE_TULIP -> PottedWhiteTulip,
    POWERED_RAIL -> PoweredRail,
    PRISMARINE -> Prismarine,
    PRISMARINE_BRICKS -> PrismarineBricks,
    PRISMARINE_BRICK_SLAB -> PrismarineBrickSlab,
    PRISMARINE_BRICK_STAIRS -> PrismarineBrickStairs,
    PRISMARINE_CRYSTALS -> PrismarineCrystals,
    PRISMARINE_SHARD -> PrismarineShard,
    PRISMARINE_SLAB -> PrismarineSlab,
    PRISMARINE_STAIRS -> PrismarineStairs,
    PUFFERFISH -> Pufferfish,
    PUFFERFISH_BUCKET -> PufferfishBucket,
    PUFFERFISH_SPAWN_EGG -> PufferfishSpawnEgg,
    PUMPKIN -> Pumpkin,
    PUMPKIN_PIE -> PumpkinPie,
    PUMPKIN_SEEDS -> PumpkinSeeds,
    PUMPKIN_STEM -> PumpkinStem,
    PURPLE_BANNER -> PurpleBanner,
    PURPLE_BED -> PurpleBed,
    PURPLE_CARPET -> PurpleCarpet,
    PURPLE_CONCRETE -> PurpleConcrete,
    PURPLE_CONCRETE_POWDER -> PurpleConcretePowder,
    PURPLE_DYE -> PurpleDye,
    PURPLE_GLAZED_TERRACOTTA -> PurpleGlazedTerracotta,
    PURPLE_SHULKER_BOX -> PurpleShulkerBox,
    PURPLE_STAINED_GLASS -> PurpleGlass,
    PURPLE_STAINED_GLASS_PANE -> PurpleGlassPane,
    PURPLE_TERRACOTTA -> PurpleTerracotta,
    PURPLE_WALL_BANNER -> PurpleWallBanner,
    PURPLE_WOOL -> PurpleWool,
    PURPUR_BLOCK -> PurpurBlock,
    PURPUR_PILLAR -> PurpurPillar,
    PURPUR_SLAB -> PurpurSlab,
    PURPUR_STAIRS -> PurpurStairs,
    QUARTZ -> Quartz,
    QUARTZ_BLOCK -> QuartzBlock,
    QUARTZ_PILLAR -> QuartzPillar,
    QUARTZ_SLAB -> QuartzSlab,
    QUARTZ_STAIRS -> QuartzStairs,
    RABBIT -> RawRabbit,
    RABBIT_FOOT -> RabbitFoot,
    RABBIT_HIDE -> RabbitHide,
    RABBIT_SPAWN_EGG -> RabbitSpawnEgg,
    RABBIT_STEW -> RabbitStew,
    RAIL -> Rail,
    REDSTONE -> Redstone,
    REDSTONE_BLOCK -> RedstoneBlock,
    REDSTONE_LAMP -> RedstoneLamp,
    REDSTONE_ORE -> RedstoneOre,
    REDSTONE_TORCH -> RedstoneTorch,
    REDSTONE_WALL_TORCH -> RedstoneWallTorch,
    REDSTONE_WIRE -> RedstoneWire,
    RED_BANNER -> RedBanner,
    RED_BED -> RedBed,
    RED_CARPET -> RedCarpet,
    RED_CONCRETE -> RedConcrete,
    RED_CONCRETE_POWDER -> RedConcretePowder,
    RED_GLAZED_TERRACOTTA -> RedGlazedTerracotta,
    RED_MUSHROOM -> RedMushroom,
    RED_MUSHROOM_BLOCK -> RedMushroomBlock,
    RED_NETHER_BRICKS -> RedNetherBricks,
    RED_SAND -> RedSand,
    RED_SANDSTONE -> RedSandstone,
    RED_SANDSTONE_SLAB -> RedSandstoneSlab,
    RED_SANDSTONE_STAIRS -> RedSandstoneStairs,
    RED_SHULKER_BOX -> RedShulkerBox,
    RED_STAINED_GLASS -> RedGlass,
    RED_STAINED_GLASS_PANE -> RedGlassPane,
    RED_TERRACOTTA -> RedTerracotta,
    RED_TULIP -> RedTulip,
    RED_WALL_BANNER -> RedWallBanner,
    RED_WOOL -> RedWool,
    REPEATER -> Repeater,
    REPEATING_COMMAND_BLOCK -> CommandRepeating,
    ROSE_BUSH -> RoseBush,
    ROSE_RED -> RoseRed,
    ROTTEN_FLESH -> RottenFlesh,
    SADDLE -> Saddle,
    SALMON -> RawSalmon,
    SALMON_BUCKET -> SalmonBucket,
    SALMON_SPAWN_EGG -> SalmonSpawnEgg,
    SAND -> Sand,
    SANDSTONE -> Sandstone,
    SANDSTONE_SLAB -> SandstoneSlab,
    SANDSTONE_STAIRS -> SandstoneStairs,
    SCUTE -> Scute,
    SEAGRASS -> Seagrass,
    SEA_LANTERN -> SeaLantern,
    SEA_PICKLE -> SeaPickle,
    SHEARS -> Shears,
    SHEEP_SPAWN_EGG -> SheepSpawnEgg,
    SHIELD -> Shield,
    SHULKER_BOX -> ShulkerBox,
    SHULKER_SHELL -> ShulkerShell,
    SHULKER_SPAWN_EGG -> ShulkerSpawnEgg,
    SIGN -> Sign,
    SILVERFISH_SPAWN_EGG -> SilverfishSpawnEgg,
    SKELETON_HORSE_SPAWN_EGG -> SkeletonHorseSpawnEgg,
    SKELETON_SKULL -> SkeletonSkull,
    SKELETON_SPAWN_EGG -> SkeletonSpawnEgg,
    SKELETON_WALL_SKULL -> SkeletonWallSkull,
    SLIME_BALL -> SlimeBall,
    SLIME_BLOCK -> SlimeBlock,
    SLIME_SPAWN_EGG -> SlimeSpawnEgg,
    SMOOTH_QUARTZ -> SmoothQuartz,
    SMOOTH_RED_SANDSTONE -> SmoothRedSandstone,
    SMOOTH_SANDSTONE -> SmoothSandstone,
    SMOOTH_STONE -> SmoothStone,
    SNOW -> Snow,
    SNOWBALL -> Snowball,
    SNOW_BLOCK -> SnowBlock,
    SOUL_SAND -> SoulSand,
    SPAWNER -> Spawner,
    SPECTRAL_ARROW -> SpectralArrow,
    SPIDER_EYE -> SpiderEye,
    SPIDER_SPAWN_EGG -> SpiderSpawnEgg,
    SPLASH_POTION -> SplashPotion,
    SPONGE -> Sponge,
    SPRUCE_BOAT -> SpruceBoat,
    SPRUCE_BUTTON -> SpruceButton,
    SPRUCE_DOOR -> SpruceDoor,
    SPRUCE_FENCE -> SpruceFence,
    SPRUCE_FENCE_GATE -> SpruceFenceGate,
    SPRUCE_LEAVES -> SpruceLeaves,
    SPRUCE_LOG -> SpruceLog,
    SPRUCE_PLANKS -> SprucePlanks,
    SPRUCE_PRESSURE_PLATE -> SprucePressurePlate,
    SPRUCE_SAPLING -> SpruceSapling,
    SPRUCE_SLAB -> SpruceSlab,
    SPRUCE_STAIRS -> SpruceStairs,
    SPRUCE_TRAPDOOR -> SpruceTrapdoor,
    SPRUCE_WOOD -> SpruceWood,
    SQUID_SPAWN_EGG -> SquidSpawnEgg,
    STICK -> Stick,
    STICKY_PISTON -> StickyPiston,
    STONE -> Stone,
    STONE_AXE -> StoneAxe,
    STONE_BRICKS -> StoneBrick,
    STONE_BRICK_SLAB -> StoneBrickSlab,
    STONE_BRICK_STAIRS -> StoneBrickStairs,
    STONE_BUTTON -> StoneButton,
    STONE_HOE -> StoneHoe,
    STONE_PICKAXE -> StonePickaxe,
    STONE_PRESSURE_PLATE -> StonePressurePlate,
    STONE_SHOVEL -> StoneShovel,
    STONE_SLAB -> StoneSlab,
    STONE_SWORD -> StoneSword,
    STRAY_SPAWN_EGG -> StraySpawnEgg,
    STRING -> String,
    STRIPPED_ACACIA_LOG -> StrippedAcaciaLog,
    STRIPPED_ACACIA_WOOD -> StrippedAcaciaWood,
    STRIPPED_BIRCH_LOG -> StrippedBirchLog,
    STRIPPED_BIRCH_WOOD -> StrippedBirchWood,
    STRIPPED_DARK_OAK_LOG -> StrippedDarkOakLog,
    STRIPPED_DARK_OAK_WOOD -> StrippedDarkOakWood,
    STRIPPED_JUNGLE_LOG -> StrippedJungleLog,
    STRIPPED_JUNGLE_WOOD -> StrippedJungleWood,
    STRIPPED_OAK_LOG -> StrippedOakLog,
    STRIPPED_OAK_WOOD -> StrippedOakWood,
    STRIPPED_SPRUCE_LOG -> StrippedSpruceLog,
    STRIPPED_SPRUCE_WOOD -> StrippedSpruceWood,
    STRUCTURE_BLOCK -> StructureBlock,
    STRUCTURE_VOID -> StructureVoid,
    SUGAR -> Sugar,
    SUGAR_CANE -> SugarCane,
    SUNFLOWER -> Sunflower,
    TALL_GRASS -> TallGrass,
    TALL_SEAGRASS -> TallSeagrass,
    TERRACOTTA -> Terracotta,
    TIPPED_ARROW -> TippedArrow,
    BTNT -> TNT,
    TNT_MINECART -> TNTMinecart,
    TORCH -> Torch,
    TOTEM_OF_UNDYING -> TotemOfUndying,
    TRAPPED_CHEST -> TrappedChest,
    TRIDENT -> Trident,
    TRIPWIRE -> Tripwire,
    TRIPWIRE_HOOK -> TripwireHook,
    TROPICAL_FISH -> TropicalFish,
    TROPICAL_FISH_BUCKET -> TropicalFishBucket,
    TROPICAL_FISH_SPAWN_EGG -> TropicalFishSpawnEgg,
    TUBE_CORAL -> TubeCoral,
    TUBE_CORAL_BLOCK -> TubeCoralBlock,
    TUBE_CORAL_FAN -> TubeCoralFan,
    TUBE_CORAL_WALL_FAN -> TubeCoralWallFan,
    TURTLE_EGG -> TurtleEgg,
    TURTLE_HELMET -> TurtleHelmet,
    TURTLE_SPAWN_EGG -> TurtleSpawnEgg,
    VEX_SPAWN_EGG -> VexSpawnEgg,
    VILLAGER_SPAWN_EGG -> VillagerSpawnEgg,
    VINDICATOR_SPAWN_EGG -> VindicatorSpawnEgg,
    VINE -> Vine,
    VOID_AIR -> VoidAir,
    WALL_SIGN -> WallSign,
    WALL_TORCH -> WallTorch,
    WATER -> Water,
    WATER_BUCKET -> WaterBucket,
    WET_SPONGE -> WetSponge,
    WHEAT -> Wheat,
    WHEAT_SEEDS -> WheatSeeds,
    WHITE_BANNER -> WhiteBanner,
    WHITE_BED -> WhiteBed,
    WHITE_CARPET -> WhiteCarpet,
    WHITE_CONCRETE -> WhiteConcrete,
    WHITE_CONCRETE_POWDER -> WhiteConcretePowder,
    WHITE_GLAZED_TERRACOTTA -> WhiteGlazedTerracotta,
    WHITE_SHULKER_BOX -> WhiteShulkerBox,
    WHITE_STAINED_GLASS -> WhiteGlass,
    WHITE_STAINED_GLASS_PANE -> WhiteGlassPane,
    WHITE_TERRACOTTA -> WhiteTerracotta,
    WHITE_TULIP -> WhiteTulip,
    WHITE_WALL_BANNER -> WhiteWallBanner,
    WHITE_WOOL -> WhiteWool,
    WITCH_SPAWN_EGG -> WitchSpawnEgg,
    WITHER_SKELETON_SKULL -> WitherSkeletonSkull,
    WITHER_SKELETON_SPAWN_EGG -> WitherSkeletonSpawnEgg,
    WITHER_SKELETON_WALL_SKULL -> WitherSkeletonWallSkull,
    WOLF_SPAWN_EGG -> WolfSpawnEgg,
    WOODEN_AXE -> WoodenAxe,
    WOODEN_HOE -> WoodenHoe,
    WOODEN_PICKAXE -> WoodenPickaxe,
    WOODEN_SHOVEL -> WoodenShovel,
    WOODEN_SWORD -> WoodenSword,
    WRITABLE_BOOK -> WritableBook,
    WRITTEN_BOOK -> WrittenBook,
    YELLOW_BANNER -> YellowBanner,
    YELLOW_BED -> YellowBed,
    YELLOW_CARPET -> YellowCarpet,
    YELLOW_CONCRETE -> YellowConcrete,
    YELLOW_CONCRETE_POWDER -> YellowConcretePowder,
    YELLOW_GLAZED_TERRACOTTA -> YellowGlazedTerracotta,
    YELLOW_SHULKER_BOX -> YellowShulkerBox,
    YELLOW_STAINED_GLASS -> YellowGlass,
    YELLOW_STAINED_GLASS_PANE -> YellowGlassPane,
    YELLOW_TERRACOTTA -> YellowTerracotta,
    YELLOW_WALL_BANNER -> YellowWallBanner,
    YELLOW_WOOL -> YellowWool,
    ZOMBIE_HEAD -> ZombieHead,
    ZOMBIE_HORSE_SPAWN_EGG -> ZombieHorseSpawnEgg,
    ZOMBIE_PIGMAN_SPAWN_EGG -> ZombiePigmanSpawnEgg,
    ZOMBIE_SPAWN_EGG -> ZombieSpawnEgg,
    ZOMBIE_VILLAGER_SPAWN_EGG -> ZombieVillagerSpawnEgg,
    ZOMBIE_WALL_HEAD -> ZombieWallHead,
  )

  private lazy val materialToBukkitMaterial: Map[Material, BukkitMaterial] = bukkitMaterialToMaterial.map(_.swap)

  implicit class BukkitMaterialOps(material: BukkitMaterial) {
    def toMaterial: Material = bukkitMaterialToMaterial(material)
  }

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
  object Air extends Material with Transparent with Crushable with NoEnergy
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
  object CaveAir extends Material with Transparent with Crushable with NoEnergy
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
  object TrappedChest extends Material with Solid with Rotates with Fuel { override val burnTicks: Int = 300 }
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
  object VoidAir extends Material with Transparent with Crushable with NoEnergy
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