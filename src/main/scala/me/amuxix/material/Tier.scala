package me.amuxix.material

import com.github.ghik.silencer.silent
import me.amuxix.Runecraft
import me.amuxix.logging.Logger
import me.amuxix.material.Ingredient.possibleDataByteFor
import me.amuxix.material.Material._




class Tier {
  def groupSimilar(stacks: Seq[ItemStack]): Seq[ItemStack] = {
    var remainingStacks = stacks
    var seqs = Seq.empty[Seq[ItemStack]]
    while (remainingStacks.nonEmpty) {
      val similar = remainingStacks.filter(_.isSimilar(remainingStacks.head))
      seqs :+= similar
      remainingStacks = remainingStacks diff similar
    }
    seqs.map(_.reduce{ (acc, elem) =>
      acc.setAmount(acc.getAmount + elem.getAmount)
      acc
    })
  }

  @silent def makeRecipes(bukkitIngredients: Iterable[ItemStack], bukkitResult: ItemStack): Seq[Recipe] = {
    var recipes = Seq.empty[Recipe]
    val groupedIngredients = groupSimilar(bukkitIngredients.filter(_.getType != AIR).toSeq)
    if (bukkitResult.getType == AIR) return recipes
    if (bukkitResult.getData.getData == -1) {
      // In this case the result data will be the same as the data of the ingredients
      // All ingredients with data -1 must share the same value.
      // There must be at least 1 ingredient with negative data
      possibleDataByteFor(bukkitResult.getType).foreach{ byte =>
        val ingredients = groupedIngredients.map { ingredient =>
          if (ingredient.getData.getData == -1) {
            DefinedIngredient(new MaterialData(ingredient.getType, byte), ingredient.getAmount)
          } else {
            DefinedIngredient(ingredient.getData, ingredient.getAmount)
          }
        }
        val result = Result(new MaterialData(bukkitResult.getType, byte), bukkitResult.getAmount)
        recipes +:= Recipe(ingredients, result)
      }
    } else {
      val ingredients = groupedIngredients.map {
        case stack if stack.getData.getData == -1 =>
          UndefinedIngredient(stack.getType, stack.getAmount)
        case stack =>
          DefinedIngredient(stack.getData, stack.getAmount)
      }
      val result = Result(bukkitResult.getData, bukkitResult.getAmount)
      recipes +:= Recipe(ingredients, result)
    }
    recipes
  }


  private val recipes: Set[Recipe] = {
    var recipeSet: Set[Recipe] = Runecraft.server.recipeIterator.flatMap {
      case recipe: ShapedRecipe =>
        makeRecipes(recipe.getIngredientMap.values, recipe.getResult)
      case recipe: ShapelessRecipe =>
        makeRecipes(recipe.getIngredientList, recipe.getResult)
      case recipe: FurnaceRecipe =>
        //The stick is what we are using for a baseline fuel value.
        makeRecipes(Iterable(recipe.getInput, new ItemStack(STICK)), recipe.getResult)
    }.toSet

    //Define energy values for ores as the average item drop * oreMult, this is done because ores must be mined while the items can be traded or farmed
    val oreMult = 1.1
    val chainmailMult = 1.5 // Chainmail pieces are worth this value as much as iron pieces are
    /*private val fortuneMult = 2.2 // Fortune lvl 3 increases drops by 120% in average
    private val fortuneAdd = 1.5 // Fortune lvl 3 adds 3 to maximum drops, or 1.5 in average*/
    /** Food quality is saturation + food restored */
    val foodQuality: Double = 11/3 //Bread has 11 and takes 3 wheat to craft, so each quality is worth 3/11 * Wheat.
    recipeSet ++= Set(Recipe((Coal, oreMult), CoalOre),
      Recipe((LapisLazuli, 6 * oreMult), LapisLazuliOre),
      Recipe(Bed, (BedBlock, 2)),
      
      Recipe(StoneDoubleSlab, (StoneSingleSlab, 2)),
      Recipe(SandstoneDoubleSlab, (SandstoneSingleSlab, 2)),
      Recipe(OldWoodDoubleSlab, (OldWoodSingleSlab, 2)),
      Recipe(CobblestoneDoubleSlab, (CobblestoneSingleSlab, 2)),
      Recipe(BrickDoubleSlab, (BrickSingleSlab, 2)),
      Recipe(StoneBrickDoubleSlab, (StoneBrickSingleSlab, 2)),
      Recipe(NetherBrickDoubleSlab, (NetherBrickSingleSlab, 2)),
      Recipe(QuartzDoubleSlab, (QuartzSingleSlab, 2)),
      
      Recipe(StoneDoubleSlab, OakWoodPlanks),
      //Recipe(SandstoneDoubleSlab, SandstoneSingleSlab),
      //Recipe(OldWoodDoubleSlab, OldWood),
      //Recipe(CobblestoneDoubleSlab, CobblestoneSingleSlab),
      Recipe(BrickDoubleSlab, BrickSingleSlab),
      Recipe(StoneBrickDoubleSlab, StoneBrickSingleSlab),
      Recipe(NetherBrickDoubleSlab, NetherBrickSingleSlab),
      Recipe(QuartzDoubleSlab, QuartzSingleSlab),
      
      Recipe(Redstone, RedstoneWire),
      Recipe((Diamond, oreMult), DiamondOre),
      Recipe(Furnace, BurningFurnace),
      Recipe(Sign, SignPost),
      Recipe(OakDoorItem, OakDoor),
      Recipe(Sign, WallSign),
      Recipe(IronDoor, IronDoorBlock),
      Recipe((Redstone, 4.5 * oreMult), RedstoneOre),
      Recipe(RedstoneOre, GlowingRedstoneOre),
      Recipe(RedstoneTorchOn, RedstoneTorchOff),
      Recipe(GoldOre, (SoulSand, 2)),
      Recipe(Cake, CakeBlock),
      Recipe(RedstoneRepeater, RedstoneRepeaterOff),
      Recipe(RedstoneRepeater, RedstoneRepeaterOn),
      Recipe(BrewingStandItem, BrewingStand),
      Recipe(CauldronItem, Cauldron),
      Recipe(RedstoneLampOff, RedstoneLampOn),
      Recipe((Emerald, oreMult), EmeraldOre),
      Recipe(String, Tripwire),
      Recipe(FlowerPotItem, FlowerPot),
      Recipe(RedstoneComparator, RedstoneComparatorOff),
      Recipe(RedstoneComparator, RedstoneComparatorOn),
      Recipe((Quartz, oreMult), QuartzOre),
      Recipe(DaylightSensor, InvertedDaylightSensor),
      Recipe(SpruceDoorItem, (SpruceDoor, 2)),
      Recipe(BirchDoorItem, (BirchDoor, 2)),
      Recipe(JungleDoorItem, (JungleDoor, 2)),
      Recipe(AcaciaDoorItem, (AcaciaDoor, 2)),
      Recipe(DarkOakDoorItem, (DarkOakDoor, 2)),
      Recipe(Crops, BeetrootPlantation),
      Recipe(Dirt, GrassPath),
      Recipe(Ice, FrostedIce),
      Recipe((IronHelmet, 1.5), ChainmailHelmet),
      Recipe((IronChestplate, 1.5), ChainmailChestplate),
      Recipe((IronLeggings, 1.5), ChainmailLeggings),
      Recipe((IronBoots, 1.5), ChainmailBoots),
      Recipe((Gravel, 2.0), Flint),
      Recipe(Seq(DefinedIngredient(Bucket, 1.0), DefinedIngredient(Water, 1.0)), Result(WaterBucket, 1.0)),
      Recipe(Seq(DefinedIngredient(Bucket, 1.0), DefinedIngredient(Lava, 1.0)), Result(LavaBucket, 1.0)),
      Recipe(Seq(DefinedIngredient(Bucket, 1.0), DefinedIngredient(Wheat, foodQuality * 10)), Result(MilkBucket, 1.0)),
      Recipe((Wheat, foodQuality * 22), RabbitStew),
      Recipe((Wheat, foodQuality * 20.8), CookedPorkchop),
      Recipe((Wheat, foodQuality * 20.8), CookedBeef),
      Recipe((Wheat, foodQuality * 20.4), GoldenCarrot),
      Recipe((Wheat, foodQuality * 16.8), Cake),
      Recipe((Wheat, foodQuality * 15.6), CookedMutton),
      Recipe((Wheat, foodQuality * 15.6), CookedSalmon),
      Recipe((Wheat, foodQuality * 13.6), GoldenApple),
      Recipe((Wheat, foodQuality * 13.6), EnchantedGoldenApple),
      Recipe((Wheat, foodQuality * 13.2), BeetrootSoup),
      Recipe((Wheat, foodQuality * 13.2), CookedChicken),
      Recipe((Wheat, foodQuality * 13.2), MushroomStew),
      Recipe((Wheat, foodQuality * 12.8), PumpkinPie),
      Recipe((Wheat, foodQuality * 11), BakedPotato),
      Recipe((Wheat, foodQuality * 11), Bread),
      Recipe((Wheat, foodQuality * 11), CookedFish),
      Recipe((Wheat, foodQuality * 11), CookedRabbit),
      Recipe((Wheat, foodQuality * 6.6), Carrot),
      Recipe((Wheat, foodQuality * 6.4), Apple),
      Recipe((Wheat, foodQuality * 6.4), ChorusFruit),
      Recipe((Wheat, foodQuality * 5.2), SpiderEye),
      Recipe((Wheat, foodQuality * 4.8), RawBeef),
      Recipe((Wheat, foodQuality * 4.8), RawPorkchop),
      Recipe((Wheat, foodQuality * 4.8), RawRabbit),
      Recipe((Wheat, foodQuality * 4.8), RottenFlesh),
      Recipe((Wheat, foodQuality * 3.2), Melon),
      Recipe((Wheat, foodQuality * 3.2), PoisonousPotato),
      Recipe((Wheat, foodQuality * 3.2), RawChicken),
      Recipe((Wheat, foodQuality * 3.2), RawMutton),
      Recipe((Wheat, foodQuality * 2.4), Cookie),
      Recipe((Wheat, foodQuality * 2.4), RawFish),
      Recipe((Wheat, foodQuality * 2.4), RawSalmon),
      Recipe((Wheat, foodQuality * 2.2), Beetroot),
      Recipe((Wheat, foodQuality * 1.6), Potato),
      Recipe((Wheat, foodQuality * 1.2), Clownfish),
      Recipe((Wheat, foodQuality * 1.2), Pufferfish),
      Recipe(Seq(UndefinedIngredient(BANNER, 1.0)), Result(StandingBanner, 1.0)),
      Recipe(Seq(UndefinedIngredient(BANNER, 1.0)), Result(WallBanner, 1.0))
    )
    recipeSet
  }
  
  private def updateEnergyFromRecipe(recipe: Recipe): Boolean = {
    var changes = false
    if (recipe.ingredients.forall(_.energy.isDefined)) {
      val ingredientsEnergy = recipe.ingredients.map(_.energy.get).sum

      def computeEnergy(resultAmount: Double): Int = {
        //5% bonus energy for crafted goods.
        val craftingMultiplier = 1.05
        ((ingredientsEnergy / resultAmount) * craftingMultiplier).ceil.toInt
      }

      recipe.result match {
        case Result(material@Material(Some(energy)), amount) =>
          val newEnergy = computeEnergy(amount)
          if (energy > newEnergy) {
            material.energy = Some(newEnergy)
            changes = true
          }
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

  //recipes.foreach(Logger.info)

  Material.values.foreach(material => Logger.info(s"${material.name}\t${material.tier.getOrElse("-1")}\t${material.energy.getOrElse("-1")}"))

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