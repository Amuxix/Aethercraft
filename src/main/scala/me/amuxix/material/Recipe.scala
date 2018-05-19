package me.amuxix.material

import me.amuxix.util.Mergeable
import me.amuxix.material.Generic.SpreadableMaterial

/**
  * Created by Amuxix on 08/01/2017.
  */
object Recipe {
  def apply(ingredient: Constituent, result: Constituent): Recipe = new Recipe(Seq(ingredient), result)
}

case class Recipe(ingredients: Seq[Constituent], result: Constituent, requiresFuel: Boolean = false) {
  private implicit class SpreadableList(val ingredients: Seq[Constituent]) {
    def spread: Seq[Seq[Constituent]] = {
      val (genericIngredients, specificIngredients) = ingredients/*.filter(_.material != Air)*/.partition(_.isGeneric)
      val spreadGenerics: Seq[Seq[Constituent]] = genericIngredients.map(_.spread)
      val recipeCopiesNumber = spreadGenerics.head.size
      val spreadIngredients: Seq[Seq[Constituent]] = spreadGenerics ++ specificIngredients.map(ingredient => List.fill(recipeCopiesNumber)(ingredient))
      require(spreadIngredients.forall(_.size == recipeCopiesNumber), s"Cannot spread generic ingredient list!")
      require(spreadIngredients.nonEmpty && spreadIngredients.size <= 9, s"Spread created invalid number of ingredients!")
      spreadIngredients.transpose
    }
  }

  require(ingredients.nonEmpty && ingredients.size <= 9, s"Recipe for ${result.material.name} has invalid number of ingredients.\n$ingredients")
  override def toString: String = s"$result\t<${if (requiresFuel) "= " else "-"}\t${ingredients.mkString("\t")}"

  def spread: Seq[Recipe] = {
    result match {
      //case Constituent(Air, _, _) => List.empty
      case Constituent(_, _, true) =>
        require(ingredients.exists(_.isGeneric), s"Recipe for $result has generic result but no generic ingredients!")
        val spreadIngredients = ingredients.spread
        val spreadResult = result.spread
        require(spreadIngredients.size == spreadResult.size,
          s"""Cannot spread generic materials in recipe: $result <- $ingredients
             |$spreadResult
             |$spreadIngredients""".stripMargin)

        (spreadIngredients zip spreadResult).map(((ingredients: Seq[Constituent],
                                                   result: Constituent) => Recipe(ingredients, result, requiresFuel)).tupled)
      case _ if ingredients.exists(_.isGeneric) =>
        val spreadIngredients = ingredients.spread
        spreadIngredients.map((ingredients: Seq[Constituent]) => Recipe(ingredients, result, requiresFuel))
      case _ =>
        Seq(this)
    }
  }
}

object Constituent {
  implicit def material2Constituent(material: Material): Constituent = Constituent(material)

  def apply(material: Material, amount: Double = 1, isGeneric: Boolean = false): Constituent = new Constituent(material, amount, isGeneric)
  def unapply(arg: Constituent): Option[(Material, Double, Boolean)] = Some((arg.material, arg.amountDouble, arg.isGeneric))
}

class Constituent(material: Material, val amountDouble: Double = 1, val isGeneric: Boolean = false) extends ItemStack(material, amountDouble.round.toInt) with Mergeable[Constituent] {
  override def canMerge(that: Constituent): Boolean = this.material == that.material && this.isGeneric == that.isGeneric

  override def merge(that: Constituent): Constituent = Constituent(material, this.amountDouble + that.amountDouble, isGeneric)

  /**
    * Creates a Seq with all specific materials of this Constituent material if its a generic material otherwise
    * returns a Seq with only this Constituent.
    * @return A Seq of Constituent.
    */
  def spread: Seq[Constituent] = this match {
    case Constituent(_: Tool, _, _) | Constituent(_: Armor, _, _) => Seq(new Constituent(material, amountDouble))
    case Constituent(_, _, true) => material.spread.map(specificMaterial => Constituent(specificMaterial, amountDouble))
    case _ => Seq(this)
  }

  override def toString: String = {
    val materialString = {
      if (isGeneric) {
        material.name.replaceFirst("\\w+", "Generic")
      } else {
        material.toString
      }
    }
    s"$materialString x $amountDouble"
  }
}
