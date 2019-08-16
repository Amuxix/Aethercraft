package me.amuxix.pattern

import me.amuxix.inventory.Item
import me.amuxix.material.Material
import me.amuxix.material.Properties.BlockProperty
import me.amuxix.pattern.RunePattern.isMirrored
import me.amuxix.position.BlockPosition
import me.amuxix.runes.Rune
import me.amuxix.{=|>, Direction, Matrix4, Named, Player}

/*import scala.reflect.runtime.currentMirror
import scala.reflect.runtime.universe.TypeTag
import scala.util.Try*/

/**
  * Created by Amuxix on 01/12/2016.
  */
trait RunePattern[R <: Rune] extends Named {
  type RuneCreator = (BlockPosition, Player, Direction, Matrix4, Pattern) => R
  implicit def block2blockElement(block: Material with BlockProperty): BlockElement = BlockElement(block)

  /*def companionOf[T, CT](implicit tag: TypeTag[T]): CT =
    Try[CT] {
      val companionModule = tag.tpe.typeSymbol.companion.asModule
      currentMirror.reflectModule(companionModule).instance.asInstanceOf[CT]
    }.getOrElse(throw new RuntimeException(s"Could not get companion object for type ${tag.tpe}"))*/

  val runeCreator: RuneCreator
  def apply(location: BlockPosition, player: Player, direction: Direction, rotation: Matrix4, pattern: Pattern): R

  val width: Option[Int] = None
  val verticality: Boolean = false
  val directional: Boolean = false
  val buildableOnCeiling: Boolean = true
  val activatesWith: Option[Item] =|> Boolean = {
    case Some(item) => !item.material.isBlock
    case None       => true //Empty hand
  }

  val layers: List[BaseLayer]

  final lazy val activationLayer: Int = {
    val index = layers.indexWhere(_.isInstanceOf[ActivationLayer])
    if (index < 0) {
      throw new Exception(s"$name has no activation layer!")
    } else {
      index
    }
  }
  final lazy val finalWidth = width match {
    case None =>
      val sqrt = Math.sqrt(layers.head.elements.size)
      if (sqrt.isValidInt) {
        sqrt.toInt
      } else {
        throw new Exception(s"$name has invalid width!")
      }
    case Some(width) if width % 2 == 0 => throw new Exception(s"$name width needs to be odd!")
    case Some(width) => width
  }

  private lazy val hasTwoMirroredAxis = layers.forall(isMirrored(_, finalWidth))
  private lazy val elements = layers.map(_.toElementsArray(finalWidth))

  final lazy val pattern: Pattern = new Pattern(activationLayer, elements, hasTwoMirroredAxis, verticality, directional, buildableOnCeiling, activatesWith) {
    if (layers.exists(_.elements.size % finalWidth != 0)) {
      throw new Exception(s"At least a layer in the pattern does not form a rectangle in rune $name!")
    }
    override protected[pattern] def createRune(
      center: BlockPosition,
      creator: Player,
      direction: Direction,
      rotation: Matrix4
    ): Rune = runeCreator(center, creator, direction, rotation, this)
  }
}

object RunePattern {
  def isMirrored(layer: BaseLayer, width: Int): Boolean = {
    val layerArray: Seq[Seq[Element]] = layer.toElementsArray(width)
    val height = layerArray.size
    (for {
      i <- 0 until Math.ceil(width / 2).toInt
      j <- 0 until Math.ceil(height / 2).toInt
      w <- 0 until width
      h <- 0 until height
    } yield layerArray(j)(w) == layerArray(width - j - 1)(w) && layerArray(h)(i) == layerArray(h)(height - i - 1)).forall(identity)
  }
}
