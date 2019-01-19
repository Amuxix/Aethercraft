package me.amuxix

import me.amuxix.material.Material.BukkitMaterialOps
import org.bukkit.block.{Block => BukkitBlock}
import org.bukkit.{Location => BLocation}

package object bukkit {
  implicit class BukkitBlockOps(block: BukkitBlock) {
    def toBlock: Block = Block(bukkitLocation2Position(block.getLocation), block.getState.getType.toMaterial)
  }

  import scala.math.Numeric.DoubleAsIfIntegral
  //implicit val doubleAsIfIntegral = DoubleAsIfIntegral //Allows this method to be implicitly used by bukkitEntity2Position
  implicit def bukkitLocation2Position(location: BLocation): Position[Double] =
    Position(location.getWorld, Vector3(location.getX, location.getY, location.getZ))

  implicit def doublePosition2IntPosition(position: Position[Double]): Position[Int] = position.toIntPosition
}
