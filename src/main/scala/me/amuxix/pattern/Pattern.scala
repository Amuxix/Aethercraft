package me.amuxix.pattern

import me.amuxix.logging.Logger.log
import me.amuxix.pattern.matching.BoundingCube
import me.amuxix.runes.Rune
import me.amuxix.util.Block.Location
import me.amuxix.util.{Block, Matrix4, Player, Vector3}

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

  def createRune(center: Location, activator: Player, blocks: Array[Array[Array[Block]]], rotation: Matrix4): Rune

  /**
    * Looks for this pattern in the given bounding cube
    * @param boundingCube Cube to look for the pattern in
    * @return true if rune was found
    */
  def foundIn(boundingCube: BoundingCube): Option[Matrix4] = {
    val rotationMatrix = Matrix4.IDENTITY
    log("Match without rotation")
    var maybeMatrix: Option[Matrix4] = matchHorizontalRotations(boundingCube, rotationMatrix)
    if (maybeMatrix.isDefined) return maybeMatrix
    if (verticality) {
      log("Match z vertical")
      val zRotationMatrix = rotationMatrix.rotateZ(90).translate((0, boundingCube.dimension, 0))
      maybeMatrix = matchHorizontalRotations(boundingCube, zRotationMatrix)
      if (maybeMatrix.isDefined) return maybeMatrix
      log("Match x vertical")
      val xRotationMatrix = rotationMatrix.rotateX(90).translate((0, 0, boundingCube.dimension))
      maybeMatrix = matchHorizontalRotations(boundingCube, xRotationMatrix)
      if (maybeMatrix.isDefined) return maybeMatrix
    }
    if (canBeBuiltOnCeiling && height > 1) {
      //No need to rotate if the rune only has 1 layer as it will be the same shape before and after rotating
      log("Match ceiling")
      val ceilingRotationMatrix = rotationMatrix.rotateX(180).translate((0, boundingCube.dimension, boundingCube.dimension))
      maybeMatrix = matchHorizontalRotations(boundingCube, ceilingRotationMatrix)
      if (maybeMatrix.isDefined) return maybeMatrix
    }
    None
  }

  private def matchHorizontalRotations(boundingCube: BoundingCube, originalMatrix: Matrix4): Option[Matrix4] = {
    var rotationMatrix: Matrix4 = originalMatrix
    log("Match default")
    if (matchesRotation(boundingCube, rotationMatrix)) return Some(rotationMatrix)
    if (directional == false && numberOfMirroredAxis < 2) {
      //Directional Runes don't need to rotate in Y axis
      log("Match at 90º")
      rotationMatrix = rotationMatrix.rotateY(90).translate((boundingCube.dimension, 0, 0))
      if (matchesRotation(boundingCube, rotationMatrix.invert)) return Some(rotationMatrix)
      if (numberOfMirroredAxis == 0) {
        log("Match at 180º")
        rotationMatrix = rotationMatrix.rotateY(90).translate((boundingCube.dimension, 0, 0))
        if (matchesRotation(boundingCube, rotationMatrix.invert)) return Some(rotationMatrix)
        log("Match at 270º")
        rotationMatrix = rotationMatrix.rotateY(90).translate((boundingCube.dimension, 0, 0))
        if (matchesRotation(boundingCube, rotationMatrix.invert)) return Some(rotationMatrix)
      }
    }
    None
  }

  private def matchesRotation(boundingCube: BoundingCube, rotationMatrix: Matrix4): Boolean = {
    var firstTier: Option[Material] = None
    var signature = Set.empty[Material]
    var key = Set.empty[Material]
    var none = Set.empty[Material]
    log("Bounding cube dimension: " + boundingCube.dimension)
    log("depth: " + depth)
    log("height: " + height)
    log("width: " + width)
    val offsetVector: Vector3[Int] = getOffsetVectorFor(boundingCube)
    log("Offset Vector: " + offsetVector)
    //These offsets represent the difference in dimensions from the bounding cube to this pattern
    for {
      block <- 0 until depth  //X
      layer <- 0 until height //Y
      line <- 0 until width   //Z
    } {
      val relativePosition: Vector3[Int] = Vector3(block, layer , line) + offsetVector
      log("Relative Position: " + Vector3(block, layer , line))
      /*log("Rotation Matrix: " + rotationMatrix)
      log("Rotated Position: " + rotationMatrix * relativePosition)*/
      val blockMaterial: Material = boundingCube.getBlock(rotationMatrix * relativePosition).getType
      log("Absolute Block position: " + boundingCube.getBlock(rotationMatrix * relativePosition).location)
      log("Block   Type: " + blockMaterial)
      log("Pattern Type: " + elements(layer)(line)(block))
      elements(layer)(line)(block) match {
        //Material different from pattern
        case material: Material if blockMaterial != material =>
          log("Material does not match")
          return false
        case material: Material =>
          log("Material matches")
        case Tier =>
          log("Its a tier block")
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
          log("Its a Signature block")
        case Key => key += blockMaterial
          log("Its a Key block")
        case NotInRune => none += blockMaterial
          log("Its a NotInRune block")
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

  /**
    * Calculates a vector that gives an offset to the position this pattern is inside the given bounding cube
    * @param boundingCube Bounding cube that contains the rune
    * @return OffsetVector
    */
  def getOffsetVectorFor(boundingCube: BoundingCube): Vector3[Int] = {
    Vector3(boundingCube.dimension - width,  boundingCube.dimension - height, boundingCube.dimension - depth) / 2
  }

  def getRuneBlocks(boundingCube: BoundingCube): Array[Array[Array[Block]]] = {
    val offsetVector: Vector3[Int] = getOffsetVectorFor(boundingCube)
    Array.tabulate(depth, height, width) {
      case (x, y, z) => (boundingCube.cubeOrigin + offsetVector + Vector3(x, y, z)).getBlock
    }
  }

  def getSpecialBlocksPositions(element: Element): Seq[Vector3[Int]] = {
    for {
      layer <- 0 to height
      line <- 0 to depth
      block <- 0 to width
      el = elements(layer)(line)(block) if el == element
    } yield Vector3(layer, line, block)
  }

	def getCenterBlockType: Element = elements(activationLayer)(width / 2)(depth / 2)
}
