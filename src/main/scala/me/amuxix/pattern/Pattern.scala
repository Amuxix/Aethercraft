package me.amuxix.pattern

import me.amuxix.util.Block


/**
  * Created by Amuxix on 21/11/2016.
  */
/**
  * Pattern for a rune
  * @param activationLayer Layer where the activation block is
  * @param elements the pattern itself, layer by layer
  * @param numberOfMirroredAxis 0 - no mirroring, 1 - mirrored vertically horizontally, 2 - mirrored in both axis.
  * @param verticality whether the rune can be made vertically
  */
case class Pattern(activationLayer: Int, elements: Array[Array[Array[Either[Material, Element]]]], numberOfMirroredAxis: Int = 2, verticality: Boolean = false, directional: Boolean = false) {
	//numberOFMirroredAxis: 0 - no mirroring, 1 - mirrored vertically horizontally, 2 - mirrored in both axis.
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
	private val width: Int = elements(0).length //Distance along the X axis in the default orientation
	private val depth: Int = elements(0)(0).length //Distance along the Z axis in the default orientation
	private val lowHeight: Int = activationLayer //Distance from bottom layer to the activation layer including the activation layer
	private val highHeight: Int = height - activationLayer //Distance from the top layer to the activation layer excluding the activation layer
	val boundingCubeDimensions: (Int, Int, Int) = calculateBoundingCubeDimensions
	val largestDimension: Int = ((lowHeight max highHeight) * 2) max width max depth
	private val patternMaterials: Set[Material] = elements.flatten.flatten.collect { case Left(material) => material }.toSet //Set of materials the rune contains

	private def calculateBoundingCubeDimensions: (Int, Int, Int) = {
		//Default value for when rune is direction but cannot be built vertically
		val height = (lowHeight max highHeight) * 2
		if (directional) {
			if (verticality) {
				//Rune must face a certain direction but can be built vertically
        (depth max width max height, width max height, depth max height)
			} else {
        (height, width, depth)
      }
		} else {
			if (verticality) {
				//Rune can be rotated to any direction and can be built vertically
        (depth max width max height, depth max width max height, depth max width max height)
			} else {
				//Rune can be rotated but can NOT be built vertically
        (height, depth max width, depth max width)
			}
		}
	}

	//def verifyRuneIntegrity //Verifies if pattern is possible

	def matches(blocks: Array[Array[Array[Block]]]): Boolean = {
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
	}

	def getCenterBlockType: Either[Material, Element] = elements(activationLayer)(width / 2)(depth / 2)
}
