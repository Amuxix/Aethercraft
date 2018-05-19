package me.amuxix

import com.github.ghik.silencer.silent
import me.amuxix.material._
import org.bukkit.Material.AIR
import org.bukkit.inventory.{Recipe => _, _}

import scala.collection.JavaConverters._

package object bukkit {
  implicit def bukkitStack2Constituent(stack: org.bukkit.inventory.ItemStack): Constituent = {
    Constituent(stack.getData, stack.getAmount, stack.getData.getData == -1: @silent)
  }

  val bukkitRecipes: List[Recipe] = Runecraft.server.recipeIterator.asScala.map {
    case recipe: ShapedRecipe =>
      (recipe.getIngredientMap.values.asScala.toList, recipe.getResult, false)
    case recipe: ShapelessRecipe =>
      (recipe.getIngredientList.asScala.toList, recipe.getResult, false)
    case recipe: FurnaceRecipe =>
      (List(recipe.getInput), recipe.getResult, true)
  }.collect {
    case (ingredients, result, requiresFuel) if result != null && result.getType != AIR && ingredients.exists(_ != null) =>
      val mergedIngredients: List[Constituent] = ingredients.collect { case stack if stack != null && stack.getType != AIR => bukkitStack2Constituent(stack) }.merge
      Recipe(mergedIngredients, result, requiresFuel)
  }.toList
}
