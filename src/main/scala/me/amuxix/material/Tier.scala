package me.amuxix.material

import me.amuxix.logging.Logger
import me.amuxix.material.Material._
import me.amuxix.material.Tier._

object Tier {
  val oreMultiplier = 1.1
  val chainmailMult = 1.5 // Chainmail pieces are worth this value as much as iron pieces are
  /*private val fortuneMult = 2.2 // Fortune lvl 3 increases drops by 120% in average
  private val fortuneAdd = 1.5 // Fortune lvl 3 adds 3 to maximum drops, or 1.5 in average*/
  val craftingMultiplier = 1.05
}

class Tier {
  private val recipes: Set[Recipe] = {
    /** Food quality is saturation + food restored */
    val foodQuality: Double = 11/3 //Bread has 11 quality and takes 3 wheat to craft, so each quality is worth 3/11 * Wheat.
    (me.amuxix.bukkit.bukkitRecipes ++ Seq(
      //region Ores
      Recipe(Constituent(Coal, oreMultiplier), CoalOre),
      Recipe(Constituent(LapisLazuli, 6 * oreMultiplier), LapisLazuliOre),
      Recipe(Constituent(Diamond, oreMultiplier), DiamondOre),
      Recipe(Constituent(Redstone, 4.5 * oreMultiplier), RedstoneOre),
      Recipe(RedstoneOre, GlowingRedstoneOre),
      Recipe(Constituent(Emerald, oreMultiplier), EmeraldOre),
      Recipe(Constituent(Quartz, oreMultiplier), QuartzOre),
      //endregion
      //region Generics
      Recipe(Constituent(WhiteBed, isGeneric = true), Constituent(BedBlock, 2)),
      Recipe(Constituent(StoneDoubleSlab, isGeneric = true), Constituent(StoneSingleSlab, 2, isGeneric = true)),
      Recipe(Constituent(StoneSingleSlab, 2, isGeneric = true), Constituent(StoneDoubleSlab, isGeneric = true)),
      Recipe(Constituent(WhiteBanner, isGeneric = true), StandingBanner),
      Recipe(Constituent(WhiteBanner, isGeneric = true), WallBanner),
      Recipe(Constituent(WhiteConcretePowder, isGeneric = true), Constituent(WhiteConcrete, isGeneric = true)),
      //endregion
      //region Doors
      Recipe(OakDoorItem, Constituent(OakDoor, 2)),
      Recipe(SpruceDoorItem,Constituent(SpruceDoor, 2)),
      Recipe(BirchDoorItem,Constituent(BirchDoor, 2)),
      Recipe(JungleDoorItem, Constituent(JungleDoor, 2)),
      Recipe(AcaciaDoorItem, Constituent(AcaciaDoor, 2)),
      Recipe(DarkOakDoorItem, Constituent(DarkOakDoor, 2)),
      Recipe(IronDoor, IronDoorBlock),
      //endregion
      //region Redstone
      Recipe(Redstone, RedstoneWire),
      Recipe(RedstoneTorchOn, RedstoneTorchOff),
      Recipe(RedstoneRepeater, RedstoneRepeaterOff),
      Recipe(RedstoneRepeater, RedstoneRepeaterOn),
      Recipe(BrewingStandItem, BrewingStand),
      Recipe(RedstoneLampOff, RedstoneLampOn),
      Recipe(RedstoneComparator, RedstoneComparatorOff),
      Recipe(RedstoneComparator, RedstoneComparatorOn),
      Recipe(DaylightSensor, InvertedDaylightSensor),
      //endregion
      //region Misc
      Recipe(Furnace, BurningFurnace),
      Recipe(Sign, SignPost),
      Recipe(Sign, WallSign),
      Recipe(GoldOre, Constituent(SoulSand, 2)),
      Recipe(Cake, CakeBlock),
      Recipe(CauldronItem, Cauldron),
      Recipe(String, Tripwire),
      Recipe(FlowerPotItem, FlowerPot),
      Recipe(Crops, BeetrootPlantation),
      Recipe(Dirt, GrassPath),
      Recipe(Ice, FrostedIce),
      Recipe(Constituent(Gravel, 2.0), Flint),
      //endregion
      //region Chainmail
      Recipe(Constituent(IronHelmet, chainmailMult), ChainmailHelmet),
      Recipe(Constituent(IronChestplate, chainmailMult), ChainmailChestplate),
      Recipe(Constituent(IronLeggings, chainmailMult), ChainmailLeggings),
      Recipe(Constituent(IronBoots, chainmailMult), ChainmailBoots),
      //endregion
      //region Buckets
      Recipe(Seq(Constituent(Bucket), Constituent(Water)), WaterBucket),
      Recipe(Seq(Constituent(Bucket), Constituent(Lava)), LavaBucket),
      Recipe(Seq(Constituent(Bucket), Constituent(Wheat, foodQuality * 10)), MilkBucket),
      //endregion
      //region Food
      Recipe(Constituent(Wheat, foodQuality * 22), RabbitStew),
      Recipe(Constituent(Wheat, foodQuality * 20.8), CookedPorkchop),
      Recipe(Constituent(Wheat, foodQuality * 20.8), CookedBeef),
      Recipe(Constituent(Wheat, foodQuality * 20.4), GoldenCarrot),
      Recipe(Constituent(Wheat, foodQuality * 16.8), Cake),
      Recipe(Constituent(Wheat, foodQuality * 15.6), CookedMutton),
      Recipe(Constituent(Wheat, foodQuality * 15.6), CookedSalmon),
      Recipe(Constituent(Wheat, foodQuality * 13.6), GoldenApple),
      Recipe(Constituent(Wheat, foodQuality * 13.6), EnchantedGoldenApple),
      Recipe(Constituent(Wheat, foodQuality * 13.2), BeetrootSoup),
      Recipe(Constituent(Wheat, foodQuality * 13.2), CookedChicken),
      Recipe(Constituent(Wheat, foodQuality * 13.2), MushroomStew),
      Recipe(Constituent(Wheat, foodQuality * 12.8), PumpkinPie),
      Recipe(Constituent(Wheat, foodQuality * 11), BakedPotato),
      Recipe(Constituent(Wheat, foodQuality * 11), Bread),
      Recipe(Constituent(Wheat, foodQuality * 11), CookedFish),
      Recipe(Constituent(Wheat, foodQuality * 11), CookedRabbit),
      Recipe(Constituent(Wheat, foodQuality * 6.6), Carrot),
      Recipe(Constituent(Wheat, foodQuality * 6.4), Apple),
      Recipe(Constituent(Wheat, foodQuality * 6.4), ChorusFruit),
      Recipe(Constituent(Wheat, foodQuality * 5.2), SpiderEye),
      Recipe(Constituent(Wheat, foodQuality * 4.8), RawBeef),
      Recipe(Constituent(Wheat, foodQuality * 4.8), RawPorkchop),
      Recipe(Constituent(Wheat, foodQuality * 4.8), RawRabbit),
      Recipe(Constituent(Wheat, foodQuality * 4.8), RottenFlesh),
      Recipe(Constituent(Wheat, foodQuality * 3.2), Melon),
      Recipe(Constituent(Wheat, foodQuality * 3.2), PoisonousPotato),
      Recipe(Constituent(Wheat, foodQuality * 3.2), RawChicken),
      Recipe(Constituent(Wheat, foodQuality * 3.2), RawMutton),
      Recipe(Constituent(Wheat, foodQuality * 2.4), Cookie),
      Recipe(Constituent(Wheat, foodQuality * 2.4), RawFish),
      Recipe(Constituent(Wheat, foodQuality * 2.4), RawSalmon),
      Recipe(Constituent(Wheat, foodQuality * 2.2), Beetroot),
      Recipe(Constituent(Wheat, foodQuality * 1.6), Potato),
      Recipe(Constituent(Wheat, foodQuality * 1.2), Clownfish),
      Recipe(Constituent(Wheat, foodQuality * 1.2), Pufferfish),
      //endregion
      //region ShulkerBoxes
      Recipe(Seq(Constituent(WhiteShulkerBox, isGeneric = true), Constituent(BoneMeal)), WhiteShulkerBox),
      Recipe(Seq(Constituent(WhiteShulkerBox, isGeneric = true), Constituent(OrangeDye)), OrangeShulkerBox),
      Recipe(Seq(Constituent(WhiteShulkerBox, isGeneric = true), Constituent(MagentaDye)), MagentaShulkerBox),
      Recipe(Seq(Constituent(WhiteShulkerBox, isGeneric = true), Constituent(LightBlueDye)), LightBlueShulkerBox),
      Recipe(Seq(Constituent(WhiteShulkerBox, isGeneric = true), Constituent(DandelionYellow)), YellowShulkerBox),
      Recipe(Seq(Constituent(WhiteShulkerBox, isGeneric = true), Constituent(LimeDye)), LimeShulkerBox),
      Recipe(Seq(Constituent(WhiteShulkerBox, isGeneric = true), Constituent(PinkDye)), PinkShulkerBox),
      Recipe(Seq(Constituent(WhiteShulkerBox, isGeneric = true), Constituent(GrayDye)), GrayShulkerBox),
      Recipe(Seq(Constituent(WhiteShulkerBox, isGeneric = true), Constituent(LightGrayDye)), LightGrayShulkerBox),
      Recipe(Seq(Constituent(WhiteShulkerBox, isGeneric = true), Constituent(CyanDye)), CyanShulkerBox),
      Recipe(Seq(Constituent(WhiteShulkerBox, isGeneric = true), Constituent(PurpleDye)), PurpleShulkerBox),
      Recipe(Seq(Constituent(WhiteShulkerBox, isGeneric = true), Constituent(LapisLazuli)), BlueShulkerBox),
      Recipe(Seq(Constituent(WhiteShulkerBox, isGeneric = true), Constituent(CocoaBeans)), BrownShulkerBox),
      Recipe(Seq(Constituent(WhiteShulkerBox, isGeneric = true), Constituent(CactusGreen)), GreenShulkerBox),
      Recipe(Seq(Constituent(WhiteShulkerBox, isGeneric = true), Constituent(RoseBush)), RedShulkerBox),
      Recipe(Seq(Constituent(WhiteShulkerBox, isGeneric = true), Constituent(InkSack)), BlackShulkerBox)
      //endregion
    ))
      .filterNot {
        case Recipe(ingredients, result, _) if (ingredients :+ result).exists(_.material.isInstanceOf[NoEnergy]) => true
        case Recipe(List(ingredient), result, _) if ingredient == result => true
        case _ => false
      }
      .flatMap(_.spread)
      .toSet
  }

  /**
    * Tries to update the energy of the resulting material of this recipe
    * @return True energy was updated false otherwise.
    */
  private def updateEnergyFromRecipe(recipe: Recipe): Boolean = recipe match {
    case Recipe(ingredients, _, _) if ingredients.exists(_.material.energy.isEmpty) => false
    case Recipe(ingredients, result, requiresFuel) =>
      val ingredientsEnergy = ingredients.flatMap(constituent => constituent.material.energy.map(_ * constituent.amountDouble)).sum
      val fuelEnergy = Generic.cheapestFuel.smeltEnergy.filter(_ => requiresFuel).getOrElse(0)
      val newEnergy = ((ingredientsEnergy / result.amount) * craftingMultiplier + fuelEnergy).ceil.toInt

      result.material.energy.filter(newEnergy >= _).fold {
        Logger.trace(s"${result.material.name}: ${result.material.energy.getOrElse(0)} -> $newEnergy")
        result.material.energy = newEnergy
        true
      }(_ => false)
  }


  while(recipes.count(updateEnergyFromRecipe) > 0) {
    //Keep updating energy from recipes while at least one energy value is changed.
  }
}