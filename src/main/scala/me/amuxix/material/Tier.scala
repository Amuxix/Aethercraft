package me.amuxix.material

import com.github.ghik.silencer.silent
import me.amuxix.Runecraft
import me.amuxix.material.Material._
import org.bukkit.Material.{AIR, BANNER}
import org.bukkit.inventory._
import org.bukkit.material.MaterialData

import scala.collection.convert.ImplicitConversionsToScala._

/**
  * Created by Amuxix on 07/01/2017.
  */
class Tier {
  def makeRecipe(bukkitIngredients: Iterable[ItemStack], bukkitResult: ItemStack): Option[Recipe] = {
    val ingredients: Iterable[(Material, Double)] = bukkitIngredients.filter(_.getType != AIR).map(stack => (materialData2Material(stack.getData), stack.getAmount.toDouble))
    val result: (Material, Int) = (bukkitResult.getData, bukkitResult.getAmount)
    if (ingredients.nonEmpty && result._1 != Air) {
      Some(new Recipe(ingredients, result))
    } else {
      None
    }
  }

  @silent private val recipes: Set[Recipe] = {
    var recipeSet: Set[Recipe] = Runecraft.server.recipeIterator.flatMap {
      case recipe: ShapedRecipe =>
        makeRecipe(recipe.getIngredientMap.values, recipe.getResult)
      case recipe: ShapelessRecipe =>
        makeRecipe(recipe.getIngredientList, recipe.getResult)
      case recipe: FurnaceRecipe =>
        //TODO: Add fuel cost here
        makeRecipe(Iterable(recipe.getInput), recipe.getResult)
    }.toSet

    //Define energy values for ores as the average item drop * oreMult, this is done because ores must be mined while the items can be traded or farmed
    val oreMult = 1.1
    val chainmailMult = 1.5 // Chainmail pieces are worth this value as much as iron pieces are
    /*private val fortuneMult = 2.2 // Fortune lvl 3 increases drops by 120% in average
    private val fortuneAdd = 1.5 // Fortune lvl 3 adds 3 to maximum drops, or 1.5 in average*/
    /** Food quality is saturation + food restored */
    val foodQuality: Double = 11/3 //Bread has 11 and takes 3 wheat to craft, so each quality is worth 3/11 * Wheat.
    recipeSet ++= Set(new Recipe((Coal, oreMult), CoalOre),
      new Recipe((LapisLazuli, 6 * oreMult), LapisLazuliOre),
      new Recipe(Bed, (BedBlock, 2)),
      new Recipe((StoneSingleSlab, 2.0), StoneDoubleSlab),
      new Recipe((SandstoneSingleSlab, 2.0), SandstoneDoubleSlab),
      new Recipe((OldWoodSingleSlab, 2.0), OldWoodDoubleSlab),
      new Recipe((CobblestoneSingleSlab, 2.0), CobblestoneDoubleSlab),
      new Recipe((BrickSingleSlab, 2.0), BrickDoubleSlab),
      new Recipe((StoneBrickSingleSlab, 2.0), StoneBrickDoubleSlab),
      new Recipe((NetherBrickSingleSlab, 2.0), NetherBrickDoubleSlab),
      new Recipe((QuartzSingleSlab, 2.0), QuartzDoubleSlab),
      new Recipe(Redstone, RedstoneWire),
      new Recipe((Diamond, oreMult), DiamondOre),
      new Recipe(Furnace, BurningFurnace),
      new Recipe(Sign, SignPost),
      new Recipe(OakDoorItem, OakDoor),
      new Recipe(Sign, WallSign),
      new Recipe(IronDoor, IronDoorBlock),
      new Recipe((Redstone, 4.5 * oreMult), RedstoneOre),
      new Recipe(RedstoneOre, GlowingRedstoneOre),
      new Recipe(RedstoneTorchOn, RedstoneTorchOff),
      new Recipe(GoldOre, (SoulSand, 2)),
      new Recipe(Cake, CakeBlock),
      new Recipe(RedstoneRepeater, RedstoneRepeaterOff),
      new Recipe(RedstoneRepeater, RedstoneRepeaterOn),
      new Recipe(BrewingStandItem, BrewingStand),
      new Recipe(CauldronItem, Cauldron),
      new Recipe(RedstoneLampOff, RedstoneLampOn),
      new Recipe((Emerald, oreMult), EmeraldOre),
      new Recipe(String, Tripwire),
      new Recipe(FlowerPotItem, FlowerPot),
      new Recipe(RedstoneComparator, RedstoneComparatorOff),
      new Recipe(RedstoneComparator, RedstoneComparatorOn),
      new Recipe((Quartz, oreMult), QuartzOre),
      new Recipe(new MaterialData(BANNER, (-1).toByte), StandingBanner),
      new Recipe(new MaterialData(BANNER, (-1).toByte), WallBanner),
      new Recipe(DaylightSensor, InvertedDaylightSensor),
      new Recipe(SpruceDoorItem, (SpruceDoor, 2)),
      new Recipe(BirchDoorItem, (BirchDoor, 2)),
      new Recipe(JungleDoorItem, (JungleDoor, 2)),
      new Recipe(AcaciaDoorItem, (AcaciaDoor, 2)),
      new Recipe(DarkOakDoorItem, (DarkOakDoor, 2)),
      new Recipe(Crops, BeetrootPlantation),
      new Recipe(Dirt, GrassPath),
      new Recipe(Ice, FrostedIce),
      new Recipe((IronHelmet, 1.5), ChainmailHelmet),
      new Recipe((IronChestplate, 1.5), ChainmailChestplate),
      new Recipe((IronLeggings, 1.5), ChainmailLeggings),
      new Recipe((IronBoots, 1.5), ChainmailBoots),
      new Recipe((Gravel, 2.0), Flint),
      Recipe(Map(Bucket -> 1.0, Water -> 1.0), (WaterBucket, 1)),
      Recipe(Map(Bucket -> 1.0, Lava -> 1.0), (LavaBucket, 1)),
      Recipe(Map(Bucket -> 1.0, Wheat -> foodQuality * 10 ), (MilkBucket, 1)),
      new Recipe((Wheat, foodQuality * 22), RabbitStew),
      new Recipe((Wheat, foodQuality * 20.8), CookedPorkchop),
      new Recipe((Wheat, foodQuality * 20.8), CookedBeef),
      new Recipe((Wheat, foodQuality * 20.4), GoldenCarrot),
      new Recipe((Wheat, foodQuality * 16.8), Cake),
      new Recipe((Wheat, foodQuality * 15.6), CookedMutton),
      new Recipe((Wheat, foodQuality * 15.6), CookedSalmon),
      new Recipe((Wheat, foodQuality * 13.6), GoldenApple),
      new Recipe((Wheat, foodQuality * 13.6), EnchantedGoldenApple),
      new Recipe((Wheat, foodQuality * 13.2), BeetrootSoup),
      new Recipe((Wheat, foodQuality * 13.2), CookedChicken),
      new Recipe((Wheat, foodQuality * 13.2), MushroomStew),
      new Recipe((Wheat, foodQuality * 12.8), PumpkinPie),
      new Recipe((Wheat, foodQuality * 11), BakedPotato),
      new Recipe((Wheat, foodQuality * 11), Bread),
      new Recipe((Wheat, foodQuality * 11), CookedFish),
      new Recipe((Wheat, foodQuality * 11), CookedRabbit),
      new Recipe((Wheat, foodQuality * 6.6), Carrot),
      new Recipe((Wheat, foodQuality * 6.4), Apple),
      new Recipe((Wheat, foodQuality * 6.4), ChorusFruit),
      new Recipe((Wheat, foodQuality * 5.2), SpiderEye),
      new Recipe((Wheat, foodQuality * 4.8), RawBeef),
      new Recipe((Wheat, foodQuality * 4.8), RawPorkchop),
      new Recipe((Wheat, foodQuality * 4.8), RawRabbit),
      new Recipe((Wheat, foodQuality * 4.8), RottenFlesh),
      new Recipe((Wheat, foodQuality * 3.2), Melon),
      new Recipe((Wheat, foodQuality * 3.2), PoisonousPotato),
      new Recipe((Wheat, foodQuality * 3.2), RawChicken),
      new Recipe((Wheat, foodQuality * 3.2), RawMutton),
      new Recipe((Wheat, foodQuality * 2.4), Cookie),
      new Recipe((Wheat, foodQuality * 2.4), RawFish),
      new Recipe((Wheat, foodQuality * 2.4), RawSalmon),
      new Recipe((Wheat, foodQuality * 2.2), Beetroot),
      new Recipe((Wheat, foodQuality * 1.6), Potato),
      new Recipe((Wheat, foodQuality * 1.2), Clownfish),
      new Recipe((Wheat, foodQuality * 1.2), Pufferfish)
    )
    recipeSet
  }
  
  private def updateEnergyFromRecipe(recipe: Recipe): Boolean = {
    var changes = false
    if (recipe.ingredients.keys.forall(_.energy.isDefined)) {
      val craftingMultiplier = 1.05 //5% bonus energy for crafted goods.
      val newEnergy: Int = ((recipe.ingredients.map((entry) => entry._1.energy.get * entry._2).sum / recipe.result._2) * craftingMultiplier).ceil.toInt
      if (recipe.result._1.energy.isEmpty || recipe.result._1.energy.get > newEnergy) {
        recipe.result._1.energy = Some(newEnergy)
        changes = true
      }
    }
    changes
  }

  var changes = false
  do {
    changes = false
    for (recipe <- recipes) {
      if (updateEnergyFromRecipe(recipe)) {
        changes = true
      }
    }
  } while(changes)


  //Material.values.foreach(material => info(s"${material.name}\t${material.tier.getOrElse("-1")}\t${material.energy.getOrElse("-1")}"))

  /*def logToTable[A, B](list: Seq[A], items: Seq[B], result: Material): Unit = {
    Logger.info("\t" * (9 - list.size) + s"${items.mkString("\t")}\t'=>\t${result.name}")
  }*/

  //Logger.info(Material.values.length + " materials!")
  //recipes.foreach(recipe => info(recipe.toString))
  /*Runecraft.server.recipeIterator foreach { serverRecipe =>
    val cenas = serverRecipe match {
      case recipe: ShapedRecipe =>
        val ingredients: Iterable[(Material, Double)] = recipe.getIngredientMap.values.filter(_.getType != AIR).map(stack => (bukkitStack2Material(stack), stack.getAmount.toDouble))
        val result: (Material, Int) = (recipe.getResult, recipe.getResult.getAmount)
        new Recipe(ingredients, result)
      case recipe: ShapelessRecipe =>
        val ingredients: Iterable[(Material, Double)] = recipe.getIngredientList.filter(_.getType != AIR).map(stack => (bukkitStack2Material(stack), stack.getAmount.toDouble))
        val result: (Material, Int) = (recipe.getResult, recipe.getResult.getAmount)
        val rec = new Recipe(ingredients, result)
      case recipe: FurnaceRecipe =>
        //TODO: Add fuel cost here
        val ingredients: Iterable[(Material, Double)] = Iterable((bukkitStack2Material(recipe.getInput), recipe.getInput.getAmount))
        val result: (Material, Int) = (recipe.getResult, recipe.getResult.getAmount)
        new Recipe(ingredients, result)
    }
  }*/
}