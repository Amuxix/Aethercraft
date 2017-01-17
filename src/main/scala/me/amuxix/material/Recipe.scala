package me.amuxix.material

/**
  * Created by Amuxix on 08/01/2017.
  */
case class Recipe(ingredients: Map[Material, Double], result: (Material, Int)) {
  def this(bukkitIngredients: Iterable[(Material, Double)], result: (Material, Int)) = this(bukkitIngredients.groupBy(_._1).mapValues(_.map(_._2).sum), result)
  def this(ingredient: Material, result: Material) = this(Map(ingredient -> 1.toDouble), (result, 1))
  def this(ingredient: Material, result: (Material, Int)) = this(Map(ingredient -> 1.toDouble), result)
  def this(ingredient: (Material, Double), result: Material) = this(Map(ingredient._1 -> ingredient._2), (result, 1))

  override def toString: String = "\t" * (9 - ingredients.size) + ingredients.map(entry => entry._1.toString + " * " + entry._2).mkString("\t") + "\t'=>\t" + result._1.toString + " * " + result._2
}