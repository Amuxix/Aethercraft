package me.amuxix.bukkit

import cats.data.NonEmptyList
import me.amuxix.material._
import me.amuxix.bukkit.Material.BukkitMaterialOps
import me.amuxix.material.Material._
import org.bukkit.inventory.RecipeChoice.MaterialChoice
import org.bukkit.inventory.{Recipe => _, _}
import org.bukkit.inventory.{ItemStack => BukkitItemStack}

import scala.collection.JavaConverters._

object Recipes {
  implicit class MaterialChoiceOps(materialChoice: MaterialChoice) {
    def spread: Option[NonEmptyList[Material]] = NonEmptyList.fromList {
      materialChoice.getChoices.asScala
        .flatMap(material => Option(material).map(_.aetherize))
        .toList
    }
  }

  implicit class RecipeChoices(choices: List[RecipeChoice]) {
    def toMaterialList: Option[NonEmptyList[NonEmptyList[Material]]] = Option(choices).flatMap { choices =>
      NonEmptyList.fromList {
        choices.collect {
          case materialChoice: MaterialChoice => Option(materialChoice).flatMap(_.spread)
        }.flatten
      }
    }
  }

  def createRecipe(ingredients: NonEmptyList[NonEmptyList[Material]], result: BukkitItemStack, requiresFuel: Boolean = false): Option[Recipe] =
    Option(result).flatMap { result =>
      result.getType.aetherize match {
        case `Air` | `CaveAir` | `VoidAir` => None
        case material if material.hasNoEnergy => None
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
      case Recipe(_, material, _, _) if material.hasNoEnergy => false
      case Recipe(ingredients, _, _, _) if ingredients.exists(_.forall(_.hasNoEnergy)) => false
      //Filter out smelting ore recipes, these are replaced by custom ones that increase ore value.
      case Recipe(ingredients, _, _, _) if ingredients.exists(_.exists(_.isOre)) => false
      case _ => true
    }
}
