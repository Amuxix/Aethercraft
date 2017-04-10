package me.amuxix.material

import com.github.ghik.silencer.silent
import me.amuxix.material.Ingredient.possibleDataByteFor
import me.amuxix.material.Material.{materialData2Material, materialDataToMaterial}
import org.bukkit.material.MaterialData
import org.bukkit.{Material => BMaterial}

import scala.annotation.tailrec

/**
  * Created by Amuxix on 08/01/2017.
  */

sealed trait Constituent {
  def amount: Double
  def material: Option[Material]
}
object Ingredient {
  @silent def possibleDataByteFor(bukkitMaterial: BMaterial): Iterable[Byte] = {
    materialDataToMaterial.keys.collect {
      case m: MaterialData if m.getItemType == bukkitMaterial => m.getData
    }
  }
}
sealed trait Ingredient extends Constituent {
  def energy: Option[Double] = material.flatMap(_.energy).map(_ * amount)
}
case class UndefinedIngredient(bmaterial: BMaterial, amount: Double) extends Ingredient {
  @silent
  private val possibleMaterials = possibleDataByteFor(bmaterial) map { byte =>
    materialData2Material(new MaterialData(bmaterial, byte))
  }
  def material: Option[Material] = {
    @tailrec def findMin(remainingMaterials: List[Material], currentMin: Int, materialWithMin: Option[Material]): Option[Material] = remainingMaterials match {
      case Material(None) :: _ =>
        None
      case (m @ Material(Some(energy))) :: tail =>
        if (energy < currentMin) {
          findMin(tail, energy, Some(m))
        } else {
          findMin(tail, currentMin, materialWithMin)
        }
      case Nil =>
        materialWithMin
    }
    findMin(possibleMaterials.toList, Int.MaxValue, None)
  }
}
abstract sealed class DefinedMaterial(m: Material) extends Constituent {
  override def material: Option[Material] = Some(m)
}

case class DefinedIngredient(private val m: Material, amount: Double) extends DefinedMaterial(m) with Ingredient

case class Result(private val m: Material, amount: Double) extends DefinedMaterial(m)

object Recipe {
  def apply(ingredients: (Material, Double), result: Material): Recipe = new Recipe(Seq(DefinedIngredient(ingredients._1, ingredients._2)), Result(result, 1))
  def apply(ingredients: (Material, Double), result: (Material, Int)): Recipe = new Recipe(Seq(DefinedIngredient(ingredients._1, ingredients._2)), Result(result._1, result._2.toDouble))
  def apply(ingredients: Material, result: (Material, Int)): Recipe = new Recipe(Seq(DefinedIngredient(ingredients, 1)), Result(result._1, result._2.toDouble))
  def apply(ingredients: Material, result: Material): Recipe = new Recipe(Seq(DefinedIngredient(ingredients, 1)), Result(result, 1))
}

case class Recipe(ingredients: Seq[Ingredient], result: Result)