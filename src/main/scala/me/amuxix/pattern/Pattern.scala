package me.amuxix.pattern

import me.amuxix.logging.Logger.{severe, trace}
import me.amuxix.pattern.matching.BoundingCube
import me.amuxix.runes.Rune
import me.amuxix.util.Block.Location
import me.amuxix.util.{Block, Matrix4, Player, Vector3}
import org.bukkit.ChatColor

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
  def apply[R <: Rune](creator: (Location, Player, Array[Array[Array[Block]]], Matrix4, Vector3[Int], Pattern) => R, width: Int,
            numberOfMirroredAxis: Boolean = true, verticality: Boolean = false, directional: Boolean = false,
            canBeBuiltOnCeiling: Boolean = true)(layers: Layer*): Pattern = {
    val activationLayer = layers.indexWhere(_.isInstanceOf[ActivationLayer])
    if (width % 2 == 0) {
      severe("Rune pattern has even width making it impossible to have a center!")
    }
    val elements: Seq[Seq[Seq[Element]]] = layers.map(_.toElementsArray(width))
    new Pattern(activationLayer, elements, numberOfMirroredAxis, verticality, directional, canBeBuiltOnCeiling) {
      def createRune(location: Location, activator: Player, blocks: Array[Array[Array[Block]]], rotation: Matrix4, rotationCenter: Vector3[Int]): Rune = {
        creator(location, activator, blocks, rotation, rotationCenter, this)
      }
    }
  }
}


/**
  * Pattern for a rune
  * @param activationLayer Layer where the activation block is
  * @param elements The pattern itself, layer by layer
  * @param hasTwoMirroredAxis 0 - no mirroring, 1 - mirrored vertically horizontally, 2 - mirrored in both axis.
  * @param verticality Whether the rune can be made vertically
  * @param directional True when the rune has to be built in a certain direction
  * @param canBeBuiltOnCeiling True if this rune can have its layer order inverted to be activated looking from ground to ceiling
  */
abstract class Pattern(activationLayer: Int, elements: Seq[Seq[Seq[Element]]], hasTwoMirroredAxis: Boolean, verticality: Boolean, directional: Boolean,
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
    Width and depth must be odd(even dimensions won't have a center block)
    Runes 1 layer high can always be built on ceiling
    Runes that implement Tiered must have tier blocks and vice versa
   */

  def createRune(center: Location, activator: Player, blocks: Array[Array[Array[Block]]], rotation: Matrix4, rotationCenter: Vector3[Int]): Rune

  /**
    * Looks for this pattern in the given bounding cube
    * @param boundingCube Cube to look for the pattern in
    * @return true if rune was found
    */
  def foundIn(boundingCube: BoundingCube): Option[Matrix4] = {
    val rotationMatrix = Matrix4.IDENTITY
    var possibleRotations = Seq(() => rotationMatrix)
    if (verticality) {
      possibleRotations :+= (() => rotationMatrix.rotateZ(90))
      possibleRotations :+= (() => rotationMatrix.rotateX(90))
    }
    if (height > 1) {
      if (verticality) {
        possibleRotations :+= (() => rotationMatrix.rotateZ(-90))
        possibleRotations :+= (() => rotationMatrix.rotateX(-90))
      }
      if (canBeBuiltOnCeiling) {
        possibleRotations :+= (() => rotationMatrix.rotateX(180))
      }
    }
    possibleRotations.find(f => matchHorizontalRotations(boundingCube, f()).isDefined).map(f => f())
  }

  private def matchHorizontalRotations(boundingCube: BoundingCube, rotationMatrix: Matrix4): Option[Matrix4] = {
    var possibleRotations = Seq(() => rotationMatrix)
    if (directional == false) {
      if (hasTwoMirroredAxis == false) {
        possibleRotations :+= (() => rotationMatrix.rotateY(90))
        possibleRotations :+= (() => rotationMatrix.rotateY(180))
        possibleRotations :+= (() => rotationMatrix.rotateY(270))
      }
      if (hasTwoMirroredAxis == true && width != depth) {
        possibleRotations :+= (() => rotationMatrix.rotateY(90))
      }
    }
    possibleRotations.find(f => matchesRotation(boundingCube, f())).map(f => f())
  }

  private def matchesRotation(boundingCube: BoundingCube, rotationMatrix: Matrix4): Boolean = {
    var firstTier: Option[Material] = None
    var signature = Set.empty[Material]
    var key = Set.empty[Material]
    var none = Set.empty[Material]
    trace("Bounding cube dimension: " + boundingCube.dimension)
    trace("Pattern Depth: " + depth)
    trace("Pattern Height: " + height)
    trace("Pattern Width: " + width)
    val offsetVector: Vector3[Int] = getOffsetVectorFor(boundingCube)
    trace("Offset Vector: " + offsetVector)
    //These offsets represent the difference in dimensions from the bounding cube to this pattern
    for {
      //This order ensures the first line checked is the northern most
      layer <- 0 until height //Y
      block <- 0 until width //Z
      line <- 0 until depth //X
    } {
      val relativePosition: Vector3[Int] = Vector3(line, layer, block) + offsetVector
      trace("Rotation Matrix: " + rotationMatrix)
      val rotatedPosition: Vector3[Int] = relativePosition.rotateAbout(rotationMatrix, boundingCube.center)
      trace("Relative Position: " + relativePosition)
      trace("Rotated Position: " + rotatedPosition)
      val block1: Block = boundingCube.getBlock(rotatedPosition)
      val blockMaterial: Material = block1.getType
      trace("Absolute Block position: " + block1.location)
      trace("Block   Type: " + blockMaterial)
      trace("Pattern Type: " + elements(layer)(block)(line))
      //Having elements checked in layer > block > line makes the pattern top line be the northern most one
      elements(layer)(block)(line) match {
        case material: Material if blockMaterial != material =>
          //Material different from pattern
          trace(ChatColor.RED + "Material does not match")
          return false
        case material: Material =>
          trace(ChatColor.GREEN + "Material matches")
        case Tier =>
          if ((patternMaterials contains blockMaterial)/* || blockMaterial.material == AIR*/) {
            trace("This block cannot be used as a tier material as its a material used by the rune")
            //Air is tier 0 and therefore cannot be used generalize later for all T0
            return false
          }
          if (firstTier.isDefined && blockMaterial != firstTier.get) {
            trace("All Tier blocks must be of same material")
            //All tier blocks must be the same material
            return false
          } else {
            firstTier = Some(blockMaterial)
            trace(ChatColor.YELLOW + "Its a tier block")
          }
        case Signature => signature += blockMaterial
          trace(ChatColor.YELLOW + "Its a Signature block")
        case Key => key += blockMaterial
          trace(ChatColor.YELLOW + "Its a Key block")
        case NotInRune => none += blockMaterial
          trace(ChatColor.YELLOW + "Its a NotInRune block")
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
    * Applies the rotation given by the {@param rotationMatrix} to {@param point} about the {@param center} vector
    * @param center A vector to rotate the point about
    * @param rotationMatrix The matrix that defines the rotation
    * @param point Point to be rotated
    * @return A point rotated about the center with the given rotation matrix
    */
  def conjugateMatrix(center: Vector3[Int], rotationMatrix: Matrix4, point: Vector3[Int]): Vector3[Int] = {
    Matrix4.IDENTITY.translate(center) * rotationMatrix * Matrix4.IDENTITY.translate(center * -1) * point
  }

  /**
    * Calculates a vector that gives an offset to the position this pattern is inside the given bounding cube
    * @param boundingCube Bounding cube that contains the rune
    * @return OffsetVector
    */
  def getOffsetVectorFor(boundingCube: BoundingCube): Vector3[Int] = {
    Vector3(boundingCube.dimension - depth,  boundingCube.dimension - lowHeight, boundingCube.dimension - width) / 2
  }

  def getRuneBlocks(boundingCube: BoundingCube): Array[Array[Array[Block]]] = {
    val offsetVector: Vector3[Int] = getOffsetVectorFor(boundingCube)
    Array.tabulate(depth, height, width) {
      //case (x, y, z) => (boundingCube.cubeOrigin + offsetVector + Vector3(x, y, z)).getBlock
      case (x, y, z) => boundingCube.getBlock(offsetVector + Vector3(x, y, z))
    }
  }

  def getSpecialBlocksVectors(element: Element): Seq[Vector3[Int]] = {
    for {
      layer <- 0 until height
      line <- 0 until depth
      block <- 0 until width
      el = elements(layer)(block)(line) if el == element
    } yield Vector3(line, layer, block)
  }

  def getAllRuneBlocksVectors: Seq[Vector3[Int]] = {
    for {
      layer <- 0 until height
      line <- 0 until depth
      block <- 0 until width
      el = elements(layer)(block)(line) if el != NotInRune
    } yield Vector3(line, layer, block)
  }

	def getCenterBlockType: Element = elements(activationLayer)(width / 2)(depth / 2)
}
