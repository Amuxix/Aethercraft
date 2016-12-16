package me.amuxix.pattern

import me.amuxix.pattern.matching.BoundingCube
import me.amuxix.runes.{Rune, Test}
import me.amuxix.util.{Location, Player, _}

/**
  * Created by Amuxix on 21/11/2016.
  */

case class Layer(elements: Element*) {
  def toElementsArray(width: Int): Seq[Seq[Element]] = {
    elements.grouped(width).toSeq
  }
}
object ActivationLayer {
  def apply(elements: Element*) = new ActivationLayer(elements:_*)
}
class ActivationLayer(_elements: Element*) extends Layer(_elements:_*)

object Pattern {
  def apply[R <: Rune](creator: (Location, Player, Array[Array[Array[Block]]], Matrix4, Pattern) => R, width: Int,
            numberOfMirroredAxis: Int = 2, verticality: Boolean = false, directional: Boolean = false,
            canBeBuiltOnCeiling: Boolean = true)(layers: Layer*): Pattern = {
    val activationLayer = layers.indexWhere(_.isInstanceOf[ActivationLayer])
    val elements: Seq[Seq[Seq[Element]]] = layers.map(_.toElementsArray(width))
    new Pattern(activationLayer, elements, numberOfMirroredAxis, verticality, directional, canBeBuiltOnCeiling) {
      def createRune(location: Location, activator: Player, blocks: Array[Array[Array[Block]]], rotation: Matrix4): Rune = {
        creator(location, activator, blocks, rotation, this)
      }
    }
  }
}



/**
  * Pattern for a rune
  * @param activationLayer Layer where the activation block is
  * @param elements the pattern itself, layer by layer
  * @param numberOfMirroredAxis 0 - no mirroring, 1 - mirrored vertically horizontally, 2 - mirrored in both axis.
  * @param verticality whether the rune can be made vertically
  */
abstract class Pattern(activationLayer: Int, elements: Seq[Seq[Seq[Element]]], numberOfMirroredAxis: Int, verticality: Boolean, directional: Boolean,
                   canBeBuiltOnCeiling: Boolean) {
	/* IN GAME AXIS
		 *          Y axis
		 *          |
		 *          |     X axis
		 *          /----->  North
		 *        /
		 *      /Z axis
		 *    East
		 */
  private val height: Int = elements.length //Distance along the Y axis in the default orientation
  private val width: Int = elements.head.length //Distance along the X axis in the default orientation
  private val depth: Int = elements.head.head.length //Distance along the Z axis in the default orientation
	private val lowHeight: Int = activationLayer //Distance from bottom layer to the activation layer INCLUDING the activation layer
	private val highHeight: Int = height - activationLayer //Distance from the top layer to the activation layer EXCLUDING the activation layer
	val largestDimension: Int = ((lowHeight max highHeight) * 2) max width max depth
	private val patternMaterials: Set[Material] = elements.flatten.flatten.collect { case m: Material => m }.toSet //Set of materials the rune contains
  /*
	def verifyRuneIntegrity //Verifies if pattern is possible
    width and depth are odd(even dimensions won't have a center block)
    Runes 1 layer high can always be built on ceiling

   */

  def createRune(location: Location, activator: Player, blocks: Array[Array[Array[Block]]], rotation: Matrix4): Rune

  /**
    * Looks for this pattern in the given bounding cube
    * @param boundingCube Cube to look for the pattern in
    * @return true if rune was found
    */
  def foundIn(boundingCube: BoundingCube): Boolean = {
    val rotationMatrix = Matrix4.IDENTITY
    if (matchHorizontalRotations(boundingCube, rotationMatrix)) return true
    if (verticality) {
      val zRotationMatrix = rotationMatrix.rotateZ(90).translate((0, boundingCube.dimension, 0))
      if (matchHorizontalRotations(boundingCube, zRotationMatrix)) return true
      val xRotationMatrix = rotationMatrix.rotateX(90).translate((0, 0, boundingCube.dimension))
      if (matchHorizontalRotations(boundingCube, xRotationMatrix)) return true
    }
    if (canBeBuiltOnCeiling) {
      val ceilingRotationMatrix = rotationMatrix.rotateX(180).translate((0, boundingCube.dimension, boundingCube.dimension))
      if (matchHorizontalRotations(boundingCube, ceilingRotationMatrix)) return true
    }
    false
  }

  private def matchHorizontalRotations(boundingCube: BoundingCube, originalMatrix: Matrix4): Boolean = {
    var rotationMatrix: Matrix4 = originalMatrix
    if (matchesRotation(boundingCube, rotationMatrix)) return true
    if (directional == false && numberOfMirroredAxis < 2) {
      //Directional Runes don't need to rotate in Y axis
      rotationMatrix = rotationMatrix.rotateY(90).translate((boundingCube.dimension, 0, 0))
      if (matchesRotation(boundingCube, rotationMatrix.invert)) return true
      if (numberOfMirroredAxis == 0) {
        rotationMatrix = rotationMatrix.rotateY(90).translate((boundingCube.dimension, 0, 0))
        if (matchesRotation(boundingCube, rotationMatrix.invert)) return true
        rotationMatrix = rotationMatrix.rotateY(90).translate((boundingCube.dimension, 0, 0))
        if (matchesRotation(boundingCube, rotationMatrix.invert)) return true
      }
    }
    false
  }

  private def matchesRotation(boundingCube: BoundingCube, rotationMatrix: Matrix4): Boolean = {
    var firstTier: Option[Material] = None
    var signature = Set.empty[Material]
    var key = Set.empty[Material]
    var none = Set.empty[Material]
    val offsetVector: Vector3[Int] = Vector3(boundingCube.dimension - width,  boundingCube.dimension - height, boundingCube.dimension - depth) / 2 //Needs testing
    //These offsets represent the difference in dimensions from the bounding cube to this pattern
    for {
      layer <- 0 to height
      line <- 0 to width
      block <- 0 to depth
    } {
      val relativePosition: Vector3[Int] = Vector3(layer , line, block) + offsetVector
      val blockMaterial: Material = boundingCube.getBlock(rotationMatrix * relativePosition).getType
      elements(layer)(line)(block) match {
        //Material different from pattern
        case material: Material if blockMaterial != material => return false
        case Tier =>
          if (patternMaterials contains blockMaterial) {
            //
            return false
          }
          if (firstTier.isDefined && blockMaterial != firstTier.get) {
            //All tier blocks must be the same material
            return false
          } else {
            firstTier = Option(blockMaterial)
          }
        case Signature => signature += blockMaterial
        case Key => key += blockMaterial
        case NotInRune => none += blockMaterial
      }
    }

    if (firstTier.isDefined) {
      val specialBlocks: Set[Material] = signature ++ key ++ none
      if (specialBlocks contains firstTier.get) {
        //Signature, key and none can't be the same as tier or specific materials
        return false
      }
      if (patternMaterials.exists((specialBlocks ++ firstTier).contains)) {
        //Checks if any of the special block is a pattern material, we don't want that to avoid possible missmatches
        return false
      }
    }
    true
  }

  def getSpecialBlocksPositions(element: Element): Seq[Vector3[Int]] = {
    for {
      layer <- 0 to height
      line <- 0 to depth
      block <- 0 to width
      el = elements(layer)(line)(block) if el == element
    } yield Vector3(layer, line, block)
  }

  /*private def matchesRotation(blocks: Array[Array[Array[Block]]]): Boolean = {
		var firstTier: Option[Material] = None
		var signature = Set.empty[Material]
		var key = Set.empty[Material]
		var none = Set.empty[Material]

		for {
			layer <- 0 to blocks.length
			line <- 0 to blocks(0).length
			block <- 0 to blocks(0)(0).length
		} {
			val blockMaterial: Material = blocks(layer)(line)(block).getType
			elements(layer)(line)(block) match {
				case Right(patternElement) =>
					if (patternMaterials contains blockMaterial) {
						//Tier, signature, key and none blocks can't be specific rune materials
						return false
					}
					patternElement match {
						case Tier =>
							if (firstTier.isDefined && blockMaterial != firstTier.get) {
								//All tier blocks must be the same material
								return false
							} else {
								firstTier = Option(blockMaterial)
							}
						case Signature => signature += blockMaterial
						case Key => key += blockMaterial
						case NotInRune => none += blockMaterial
					}
				//Material different from pattern
				case Left(material) if blockMaterial != material => return false
 			}
		}

		if (firstTier.isDefined) {
			if (signature ++ key ++ none contains firstTier.get) {
				//Signature, key and none can't be the same as tier or specific materials
				return false
			}
		}
		true
	}*/

	def getCenterBlockType: Element = elements(activationLayer)(width / 2)(depth / 2)
}
