import java.util

import org.bukkit.block.{Block, BlockState}
import org.bukkit.material.MaterialData
import org.bukkit.metadata.MetadataValue
import org.bukkit.plugin.Plugin
import org.bukkit.{Chunk, Location, Material, World}

/**
  * Created by Amuxix on 22/01/2017.
  */
class TestableBlockState(var materialData: MaterialData) extends BlockState {

  override def setType(material: Material): Unit = materialData = new MaterialData(material)

  override def setData(materialData: MaterialData): Unit = this.materialData = materialData

  override def getType: Material = materialData.getItemType

  override def update(): Boolean = true

  override def update(b: Boolean): Boolean = true

  override def update(b: Boolean, b1: Boolean): Boolean = true

  override def setTypeId(i: Int): Boolean = ???

  override def getY: Int = ???

  override def getData: MaterialData = ???

  override def getLightLevel: Byte = ???

  override def getTypeId: Int = ???

  override def getX: Int = ???

  override def getRawData: Byte = ???

  override def getBlock: Block = ???

  override def isPlaced: Boolean = ???

  override def getWorld: World = ???

  override def setRawData(b: Byte): Unit = ???

  override def getZ: Int = ???

  override def getLocation: Location = ???

  override def getLocation(location: Location): Location = ???

  override def getChunk: Chunk = ???

  override def hasMetadata(s: String): Boolean = ???

  override def getMetadata(s: String): util.List[MetadataValue] = ???

  override def setMetadata(s: String, metadataValue: MetadataValue): Unit = ???

  override def removeMetadata(s: String, plugin: Plugin): Unit = ???
}
