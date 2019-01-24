package me.amuxix.pattern

import me.amuxix._
import me.amuxix.block.Block
import me.amuxix.block.Block.Location
import me.amuxix.material.Material
import me.amuxix.pattern.matching.BoundingCube
import me.amuxix.runes.Rune

/**
  * Created by Amuxix on 21/11/2016.
  */
sealed trait BaseLayer {
  def elements: Seq[Element]
  def toElementsArray(width: Int): Seq[Seq[Element]] = {
    elements.grouped(width).toSeq
  }
}
case class Layer(elements: Element*) extends BaseLayer
case class ActivationLayer(elements: Element*) extends BaseLayer

object Pattern {
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

  def apply[R <: Rune](
      runeCreator: (Array[Array[Array[Block]]], Location, Player, Direction, Pattern) => R,
      width: Option[Int] = None,
      verticality: Boolean = false,
      directional: Boolean = false,
      buildableOnCeiling: Boolean = true,
      activatesWith: PartialFunction[Material, Boolean] = { case m if !m.isBlock => true })
      (layers: BaseLayer*): Pattern = {
    val activationLayer = layers.indexWhere(_.isInstanceOf[ActivationLayer])
    val finalWidth = width match {
      case None =>
        val sqrt = Math.sqrt(layers.head.elements.size)
        if (sqrt.isValidInt) {
          sqrt.toInt
        } else {
          throw new Exception("Rune has invalid width!")
        }
      case Some(width) if width % 2 == 0 => throw new Exception("Rune width needs to be odd!")
      case Some(width) => width
    }

    if (layers.exists(_.elements.size % finalWidth != 0)) {
      throw new Exception("At least a layer in the pattern does not form a rectangle!")
    }
    val hasTwoMirroredAxis = layers.forall(isMirrored(_, finalWidth))
    val elements = layers.map(_.toElementsArray(finalWidth))
    new Pattern(activationLayer, elements, hasTwoMirroredAxis, verticality, directional, buildableOnCeiling, activatesWith) {
      override def createRune(blocks: Array[Array[Array[Block]]], center: Location, creator: Player, direction: Direction): Rune = {
        runeCreator(blocks, center, creator, direction, this)
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
  * @param buildableOnCeiling True if this rune can have its layer order inverted to be activated looking from ground to ceiling
  */
abstract class Pattern private(activationLayer: Int, elements: Seq[Seq[Seq[Element]]], hasTwoMirroredAxis: Boolean, verticality: Boolean, directional: Boolean,
                       buildableOnCeiling: Boolean, val activatesWith: PartialFunction[Material, Boolean]) extends Ordered[Pattern] {
	/* IN GAME AXIS
		 *          Y axis
		 *          |
		 *          |     X axis
		 *          /----->  East
		 *        /
		 *      /Z axis
		 *    South
		 */
  private val height: Int = elements.length //Distance along the Y axis in the default orientation
  private val width: Int = elements.head.length //Distance along the X axis in the default orientation
  private val depth: Int = elements.head.head.length //Distance along the Z axis in the default orientation
	private val lowHeight: Int = activationLayer //Distance from bottom layer to the activation layer INCLUDING the activation layer
	private val highHeight: Int = height - activationLayer //Distance from the top layer to the activation layer EXCLUDING the activation layer
	val largestDimension: Int = (((lowHeight max (highHeight - 1)) * 2) + 1) max width max depth
  val volume: Int = height * width * depth
	val patternMaterials: Set[Material] = elements.flatten.flatten.collect { case m: Material => m }.toSet //Set of materials the rune contains

  /**
    * Attempts to create the rune, may fail.
    */
  protected[pattern] def createRune(blocks: Array[Array[Array[Block]]], center: Location, creator: Player, direction: Direction): Rune


  /**
    * Checks if the center block can be the given material
    * @param centerMaterial Material we are matching against
    * @return true if this pattern can be made with the center of the given material
    */
  def centerCanBe(centerMaterial: Material): Boolean = {
    centerElement match {
      case material: Material if centerMaterial != material || centerMaterial.hasNoEnergy => false
      case Tier if patternMaterials.contains(centerMaterial) => false
      case _ => true
    }
  }


  /**
    * Checks if this pattern exists in the given bounding cube
    * @param boundingCube Cube to look for the pattern in
    * @return The rotation matrix the rune was found in
    */
  def findMatchingRotation(boundingCube: BoundingCube): Option[Matrix4] = {
    val rotationMatrix = Matrix4.IDENTITY
    def rotateZ(degrees: Int) = findMatchingHorizontalRotation(boundingCube, rotationMatrix.rotateZ(degrees))
    def rotateX(degrees: Int) = findMatchingHorizontalRotation(boundingCube, rotationMatrix.rotateX(degrees))

    findMatchingHorizontalRotation(boundingCube, rotationMatrix)
      .orFlatWhen(verticality){
        rotateZ(90)
        rotateX(90)
      }.orFlatWhen(height > 1) {
        Option.flatWhen(verticality) {
          rotateZ(-90)
            .orElse(rotateX(-90))
        }.orFlatWhen(buildableOnCeiling) {
          rotateX(180)
        }
      }
  }

  private def findMatchingHorizontalRotation(boundingCube: BoundingCube, rotationMatrix: Matrix4): Option[Matrix4] = {
    def rotateY(degrees: Int) = find(boundingCube, rotationMatrix.rotateY(degrees))

    find(boundingCube, rotationMatrix)
      .orFlatUnless(directional) {
        Option.flatUnless(hasTwoMirroredAxis) {
          rotateY(90)
            .orElse(rotateY(180))
            .orElse(rotateY(270))
        }.orFlatWhen(hasTwoMirroredAxis && width != depth)(rotateY(90))
      }
  }

  /**
    * This method checks if the blocks in the boundingCube match this pattern,
    *
    * A pattern matches a rune if:
    *  1) All materials specified in the pattern match the materials in the world
    *  2) All [[MaterialChoice]]s must have a possibility matching the material in the world
    *  3) All existing tier blocks are of the same material
    *  4) All existing tier blocks must be of a different material to all of the specified materials and the choices for each [[MaterialChoice]]
    *  5) Remaining elements must be of a different material from those covered in the above cases
    *     meaning they must be different from all specific materials, choice for each [[MaterialChoice]] and material used for tier
    */
  private def find(boundingCube: BoundingCube, rotationMatrix: Matrix4): Option[Matrix4] = {
    def rotatedPosition(line: Int, layer: Int, block: Int): Vector3[Int] = {
      val relativePosition: Vector3[Int] = Vector3(line, layer, block) + offsetVectorFor(boundingCube)
      rotationMatrix.rotateAbout(boundingCube.center)(relativePosition)
    }

    val superposition = for {
      //This order ensures the first line checked is the northern most on the lowest layer
      layer <- Stream.range(0, height) //Y
      block <- Stream.range(0, width) //Z
      line <- Stream.range(0, depth) //X
    } yield {
      val element = elements(layer)(block)(line)
      val materialAtRotatedPosition = boundingCube.blockAt(rotatedPosition(line, layer, block)).material
      element -> materialAtRotatedPosition
    }

    lazy val specificMaterialsAndChoices = superposition.collect {
      case (_: Material, specific) => specific
      case (_: MaterialChoice, choice) => choice
    }

    lazy val tier = superposition.collectFirst {
      case (`Tier`, tierMaterial) => tierMaterial
    }

    superposition.collectFirst {
      case (specific: Material, material) if specific != material =>
        "Specific materials must match."
      case (MaterialChoice(possibilities @ _*), material) if possibilities.contains(material) == false =>
        "Material choice must be one of the possibilities."
      case (`Tier`, material) if !tier.contains(material) =>
        "All tier blocks must be of the same material."
      case (`Tier`, material) if specificMaterialsAndChoices.contains(material) =>
        "Tier block material must not be one of the pattern specific materials or one of the choices to a MaterialChoice."
      case (`Signature` | `Key` | `NotInRune`, material) if tier.contains(material) && specificMaterialsAndChoices.contains(material) =>
        "Signature blocks, Key blocks and blocks nearby must not be of a material used by the rune or the material used as tier."
    }.toLeft(rotationMatrix).toOption
  }

  /**
    * Applies the rotation given by the `rotationMatrix` to `point` about the `center` vector
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
  def offsetVectorFor(boundingCube: BoundingCube): Vector3[Int] = {
    Vector3(boundingCube.dimension - depth,  boundingCube.dimension - lowHeight, boundingCube.dimension - width) / 2
  }

  /**
    * Gets the blocks that belong to this pattern
    * @param boundingCube Cube this pattern fits in
    * @return Blocks that are inside the bounding cube that belong to this rune.
    */
  def runeBlocks(boundingCube: BoundingCube, rotate: Vector3[Int] => Vector3[Int]): Array[Array[Array[Block]]] = {
    val offsetVector: Vector3[Int] = offsetVectorFor(boundingCube)
    Array.tabulate(depth, height, width) { case (x, y, z) =>
      boundingCube.blockAt(rotate(offsetVector + Vector3(x, y, z)))
    }
  }

  def nonSpecialBlockVectors: Seq[Vector3[Int]] = {
    for {
      layer <- 0 until height
      line <- 0 until depth
      block <- 0 until width
      el = elements(layer)(block)(line) if el.isInstanceOf[Material]
    } yield Vector3(line, layer, block)
  }

  def specialBlockVectors(element: Element): Seq[Vector3[Int]] = {
    for {
      layer <- 0 until height
      line <- 0 until depth
      block <- 0 until width
      el = elements(layer)(block)(line) if el == element
    } yield Vector3(line, layer, block)
  }

  def allRuneBlockVectors: Seq[Vector3[Int]] = {
    for {
      layer <- 0 until height
      line <- 0 until depth
      block <- 0 until width
      el = elements(layer)(block)(line) if el != NotInRune
    } yield Vector3(line, layer, block)
  }

	def centerElement: Element = elements(activationLayer)(width / 2)(depth / 2)

  override def compare(that: Pattern): Int = this.volume.compare(that.volume) * -1
}