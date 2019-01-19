package me.amuxix.bukkit

import cats.data.NonEmptyList
import me.amuxix.Aethercraft
import me.amuxix.material._
import me.amuxix.material.Material._
import org.bukkit.inventory.RecipeChoice.MaterialChoice
import org.bukkit.inventory.{Recipe => _, _}

import scala.collection.JavaConverters._

object Recipes {
  implicit class MaterialChoiceOps(materialChoice: MaterialChoice) {
    def spread: Option[NonEmptyList[Material]] = NonEmptyList.fromList(materialChoice.getChoices.asScala.toList.collect {
      case material if material != null =>
        material.toMaterial
    })
  }

  implicit class RecipeChoices(choices: List[RecipeChoice]) {
    def toMaterialList: Option[NonEmptyList[NonEmptyList[Material]]] = choices match {
      case null => None
      case _ => NonEmptyList.fromList(choices.filter(_ != null).collect {
        case materialChoice: MaterialChoice => materialChoice.spread
      }.flatten)
    }
  }

  def createRecipe(ingredients: NonEmptyList[NonEmptyList[Material]], result: org.bukkit.inventory.ItemStack, requiresFuel: Boolean = false): Option[Recipe] = result match {
    case null => None
    case _ =>
      result.getType.toMaterial match {
        case `Air` | `CaveAir` | `VoidAir` | _: NoEnergy => None
        case resultMaterial =>
          Some(Recipe(ingredients, resultMaterial, result.getAmount, requiresFuel))
      }
  }

  val bukkitRecipes: Set[Recipe] = Aethercraft.server.recipeIterator.asScala
    .flatMap {
      case recipe: ShapedRecipe =>
        recipe.getChoiceMap.values.asScala.toList.toMaterialList.flatMap { ingredients =>
          createRecipe(ingredients, recipe.getResult)
        }

      case recipe: ShapelessRecipe =>
        recipe.getChoiceList.asScala.toList.toMaterialList.flatMap { ingredients =>
          createRecipe(ingredients, recipe.getResult)
        }

      case recipe: FurnaceRecipe =>
        (recipe.getInputChoice match {
          case materialChoice: MaterialChoice => materialChoice.spread
        }).flatMap { ingredient =>
        createRecipe(NonEmptyList.one(ingredient), recipe.getResult, requiresFuel = true)
      }
    }
    .toSet
    .filter {
      case Recipe(_, _: NoEnergy, _, _) => false
      case Recipe(ingredients, _, _, _) if ingredients.exists(_.forall(_.isInstanceOf[NoEnergy])) => false
      //Filter out smelting ore recipes, these are replaced by custom ones that increase ore value.
      case Recipe(ingredients, _, _, _) if ingredients.exists(_.exists(_.isInstanceOf[Ore])) => false
      case _ => true
    }
}
