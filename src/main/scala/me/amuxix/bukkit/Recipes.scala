package me.amuxix.bukkit

import cats.data.NonEmptyList
import me.amuxix.material._
import me.amuxix.bukkit.Material.BukkitMaterialOps
import me.amuxix.material.Material._
import org.bukkit.inventory.RecipeChoice.MaterialChoice
import org.bukkit.inventory.{Recipe => _, _}
import org.bukkit.inventory.{ItemStack => BukkitItemStack}

import scala.jdk.CollectionConverters._

object Recipes {
  /*implicit private class MaterialChoiceOps(materialChoice: MaterialChoice) {
    def spread: Option[NonEmptyList[Material]] = NonEmptyList.fromList {
      materialChoice.getChoices.asScala
        .flatMap(material => Option(material).map(_.aetherize))
        .toList
    }
  }*/

  implicit private class MaterialChoiceOps(recipeChoice: RecipeChoice) {
    def spread: Option[NonEmptyList[Material]] = {
      recipeChoice match {
        case materialChoice: MaterialChoice =>
          NonEmptyList.fromList {
            materialChoice.getChoices.asScala
              .flatMap(material => Option(material).map(_.aetherize))
              .toList
          }
      }
    }
  }

  implicit private class RecipeChoices(choices: List[RecipeChoice]) {
    def toMaterialList: Option[NonEmptyList[NonEmptyList[Material]]] = Option(choices).flatMap { choices =>
      NonEmptyList.fromList(choices.flatMap(Option(_)flatMap(_.spread)))
    }
  }

  private def createRecipe(ingredients: NonEmptyList[NonEmptyList[Material]], result: BukkitItemStack, requiresFuel: Boolean = false): Option[Recipe] =
    Option(result).flatMap { result =>
      result.getType.aetherize match {
        case `Air` | `CaveAir` | `VoidAir` => None
        case material if material.hasNoEnergy => None
        case resultMaterial =>
          Some(Recipe(ingredients, resultMaterial, result.getAmount, requiresFuel))
      }
  }

  private def createRecipeFromSingleInput(inputChoice: RecipeChoice, result: BukkitItemStack): Option[Recipe] =
    Option(inputChoice).flatMap(_.spread.flatMap { ingredient =>
      createRecipe(NonEmptyList.one(ingredient), result, requiresFuel = true)
    })

  private def createRecipeFromInputList(inputChoices: List[RecipeChoice], result: BukkitItemStack): Option[Recipe] =
    inputChoices.toMaterialList.flatMap { ingredients =>
      createRecipe(ingredients, result)
    }

  val bukkitRecipes: Set[Recipe] = Bukkit.server.recipeIterator.asScala
    .flatMap {
      case recipe: ShapedRecipe =>
        createRecipeFromInputList(recipe.getChoiceMap.values.asScala.toList, recipe.getResult)

      case recipe: ShapelessRecipe =>
        createRecipeFromInputList(recipe.getChoiceList.asScala.toList, recipe.getResult)

      case recipe: CookingRecipe[_] => createRecipeFromSingleInput(recipe.getInputChoice, recipe.getResult)

      case recipe: StonecuttingRecipe => createRecipeFromSingleInput(recipe.getInputChoice, recipe.getResult)

      case _: MerchantRecipe => None
    }
    .toSet
    .filter {
      case Recipe(_, material, _, _) if material.hasNoEnergy => false
      case Recipe(ingredients, _, _, _) if ingredients.exists(_.forall(_.hasNoEnergy)) => false
      //Filter out smelting ore recipes, these are replaced by custom ones that increase ore value.
      case Recipe(ingredients, _, _, _) if ingredients.exists(_.exists(_.isOre)) => false
      case _ => true
    }
}
