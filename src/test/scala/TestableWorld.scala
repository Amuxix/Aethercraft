import java.io.File
import java.util
import java.util.UUID

import com.github.ghik.silencer.silent
import org.bukkit.World.Environment
import org.bukkit._
import org.bukkit.block.{Biome, Block}
import org.bukkit.entity._
import org.bukkit.generator.{BlockPopulator, ChunkGenerator}
import org.bukkit.inventory.ItemStack
import org.bukkit.material.MaterialData
import org.bukkit.metadata.MetadataValue
import org.bukkit.plugin.Plugin
import org.bukkit.util.{Consumer, Vector}

/**
  * Created by Amuxix on 02/01/2017.
  */
class TestableWorld(underlyingBlocks: Array[Array[Array[Block]]]) extends World {

  override def getBlockAt(x: Int, y: Int, z: Int): Block = underlyingBlocks(x)(y)(z)


  override def getBlockAt(location: Location): Block = ???

  override def getFullTime: Long = ???

  override def dropItemNaturally(location: Location, item: ItemStack): Item = ???

  override def getWorldType: WorldType = ???

  override def getWeatherDuration: Int = ???

  override def getAllowMonsters: Boolean = ???

  override def getEntities: util.List[Entity] = ???

  override def getEntitiesByClass[T <: Entity](classes: Class[T]*): util.Collection[T] = ???

  override def getEntitiesByClass[T <: Entity](cls: Class[T]): util.Collection[T] = ???

  override def getMaxHeight: Int = ???

  override def getNearbyEntities(location: Location, x: Double, y: Double, z: Double): util.Collection[Entity] = ???

  override def setStorm(hasStorm: Boolean): Unit = ???

  override def playEffect(location: Location, effect: Effect, data: Int): Unit = ???

  override def playEffect(location: Location, effect: Effect, data: Int, radius: Int): Unit = ???

  override def playEffect[T](location: Location, effect: Effect, data: T): Unit = ???

  override def playEffect[T](location: Location, effect: Effect, data: T, radius: Int): Unit = ???

  override def setGameRuleValue(rule: String, value: String): Boolean = ???

  override def getHighestBlockAt(x: Int, z: Int): Block = ???

  override def getHighestBlockAt(location: Location): Block = ???

  override def spawn[T <: Entity](location: Location, clazz: Class[T]): T = ???

  override def spawn[T <: Entity](location: Location, clazz: Class[T], function: Consumer[T]): T = ???

  override def setAnimalSpawnLimit(limit: Int): Unit = ???

  override def regenerateChunk(x: Int, z: Int): Boolean = ???

  override def getName: String = ???

  override def getWorldFolder: File = ???

  override def setWaterAnimalSpawnLimit(limit: Int): Unit = ???

  override def getPVP: Boolean = ???

  override def getAmbientSpawnLimit: Int = ???

  override def setAmbientSpawnLimit(limit: Int): Unit = ???

  override def isChunkLoaded(chunk: Chunk): Boolean = ???

  override def isChunkLoaded(x: Int, z: Int): Boolean = ???

  override def getWorldBorder: WorldBorder = ???

  override def setFullTime(time: Long): Unit = ???

  override def loadChunk(chunk: Chunk): Unit = ???

  override def loadChunk(x: Int, z: Int): Unit = ???

  override def loadChunk(x: Int, z: Int, generate: Boolean): Boolean = ???

  override def getUID: UUID = ???

  override def getLoadedChunks: Array[Chunk] = ???

  override def unloadChunk(chunk: Chunk): Boolean = ???

  override def unloadChunk(x: Int, z: Int): Boolean = ???

  override def unloadChunk(x: Int, z: Int, save: Boolean): Boolean = ???

  override def unloadChunk(x: Int, z: Int, save: Boolean, safe: Boolean): Boolean = ???

  override def getPopulators: util.List[BlockPopulator] = ???

  override def getChunkAt(x: Int, z: Int): Chunk = ???

  override def getChunkAt(location: Location): Chunk = ???

  override def getChunkAt(block: Block): Chunk = ???

  override def getBlockTypeIdAt(x: Int, y: Int, z: Int): Int = ???

  override def getBlockTypeIdAt(location: Location): Int = ???

  override def unloadChunkRequest(x: Int, z: Int): Boolean = ???

  override def unloadChunkRequest(x: Int, z: Int, safe: Boolean): Boolean = ???

  override def getAnimalSpawnLimit: Int = ???

  override def setDifficulty(difficulty: Difficulty): Unit = ???

  override def dropItem(location: Location, item: ItemStack): Item = ???

  override def isThundering: Boolean = ???

  override def setTicksPerAnimalSpawns(ticksPerAnimalSpawns: Int): Unit = ???

  override def generateTree(location: Location, `type`: TreeType): Boolean = ???

  override def getEmptyChunkSnapshot(x: Int, z: Int, includeBiome: Boolean, includeBiomeTempRain: Boolean): ChunkSnapshot = ???

  override def getThunderDuration: Int = ???

  override def getBiome(x: Int, z: Int): Biome = ???

  override def createExplosion(x: Double, y: Double, z: Double, power: Float): Boolean = ???

  override def createExplosion(x: Double, y: Double, z: Double, power: Float, setFire: Boolean): Boolean = ???

  override def createExplosion(x: Double, y: Double, z: Double, power: Float, setFire: Boolean, breakBlocks: Boolean): Boolean = ???

  override def createExplosion(loc: Location, power: Float): Boolean = ???

  override def createExplosion(loc: Location, power: Float, setFire: Boolean): Boolean = ???

  override def spawnParticle(particle: Particle, location: Location, count: Int): Unit = ???

  override def spawnParticle(particle: Particle, x: Double, y: Double, z: Double, count: Int): Unit = ???

  override def spawnParticle[T](particle: Particle, location: Location, count: Int, data: T): Unit = ???

  override def spawnParticle[T](particle: Particle, x: Double, y: Double, z: Double, count: Int, data: T): Unit = ???

  override def spawnParticle(particle: Particle, location: Location, count: Int, offsetX: Double, offsetY: Double, offsetZ: Double): Unit = ???

  override def spawnParticle(particle: Particle, x: Double, y: Double, z: Double, count: Int, offsetX: Double, offsetY: Double, offsetZ: Double): Unit = ???

  override def spawnParticle[T](particle: Particle, location: Location, count: Int, offsetX: Double, offsetY: Double, offsetZ: Double, data: T): Unit = ???

  override def spawnParticle[T](particle: Particle, x: Double, y: Double, z: Double, count: Int, offsetX: Double, offsetY: Double, offsetZ: Double, data: T): Unit = ???

  override def spawnParticle(particle: Particle, location: Location, count: Int, offsetX: Double, offsetY: Double, offsetZ: Double, extra: Double): Unit = ???

  override def spawnParticle(particle: Particle, x: Double, y: Double, z: Double, count: Int, offsetX: Double, offsetY: Double, offsetZ: Double, extra: Double): Unit = ???

  override def spawnParticle[T](particle: Particle, location: Location, count: Int, offsetX: Double, offsetY: Double, offsetZ: Double, extra: Double, data: T): Unit = ???

  override def spawnParticle[T](particle: Particle, x: Double, y: Double, z: Double, count: Int, offsetX: Double, offsetY: Double, offsetZ: Double, extra: Double, data: T): Unit = ???

  override def getEntitiesByClasses(classes: Class[_]*): util.Collection[Entity] = ???

  override def spawnFallingBlock(location: Location, data: MaterialData): FallingBlock = ???

  override def spawnFallingBlock(location: Location, material: Material, data: Byte): FallingBlock = ???

  override def spawnFallingBlock(location: Location, blockId: Int, blockData: Byte): FallingBlock = ???

  override def getGameRuleValue(rule: String): String = ???

  override def playSound(location: Location, sound: Sound, volume: Float, pitch: Float): Unit = ???

  override def playSound(location: Location, sound: String, volume: Float, pitch: Float): Unit = ???

  override def playSound(location: Location, sound: Sound, category: SoundCategory, volume: Float, pitch: Float): Unit = ???

  override def playSound(location: Location, sound: String, category: SoundCategory, volume: Float, pitch: Float): Unit = ???

  override def setTicksPerMonsterSpawns(ticksPerMonsterSpawns: Int): Unit = ???

  override def setSpawnLocation(x: Int, y: Int, z: Int): Boolean = ???

  override def setPVP(pvp: Boolean): Unit = ???

  override def getEnvironment: Environment = ???

  override def canGenerateStructures: Boolean = ???

  override def getWaterAnimalSpawnLimit: Int = ???

  override def getTime: Long = ???

  override def getGameRules: Array[String] = ???

  override def getHumidity(x: Int, z: Int): Double = ???

  override def getHighestBlockYAt(x: Int, z: Int): Int = ???

  override def getHighestBlockYAt(location: Location): Int = ???

  override def isAutoSave: Boolean = ???

  override def setAutoSave(value: Boolean): Unit = ???

  override def getLivingEntities: util.List[LivingEntity] = ???

  override def setThundering(thundering: Boolean): Unit = ???

  override def setBiome(x: Int, z: Int, bio: Biome): Unit = ???

  override def isGameRule(rule: String): Boolean = ???

  override def getSeed: Long = ???

  override def setWeatherDuration(duration: Int): Unit = ???

  override def refreshChunk(x: Int, z: Int): Boolean = ???

  override def isChunkInUse(x: Int, z: Int): Boolean = ???

  override def spawnEntity(loc: Location, `type`: EntityType): Entity = ???

  override def getPlayers: util.List[Player] = ???

  override def save(): Unit = ???

  override def hasStorm: Boolean = ???

  override def strikeLightning(loc: Location): LightningStrike = ???

  override def spawnArrow(location: Location, direction: Vector, speed: Float, spread: Float): Arrow = ???

  override def spawnArrow[T <: Arrow](location: Location, direction: Vector, speed: Float, spread: Float, clazz: Class[T]): T = ???

  override def getKeepSpawnInMemory: Boolean = ???

  override def setKeepSpawnInMemory(keepLoaded: Boolean): Unit = ???

  override def getSeaLevel: Int = ???

  override def strikeLightningEffect(loc: Location): LightningStrike = ???

  override def getGenerator: ChunkGenerator = ???

  override def setThunderDuration(duration: Int): Unit = ???

  override def getTicksPerMonsterSpawns: Long = ???

  override def setTime(time: Long): Unit = ???

  override def getSpawnLocation: Location = ???

  override def getAllowAnimals: Boolean = ???

  override def getTemperature(x: Int, z: Int): Double = ???

  override def getMonsterSpawnLimit: Int = ???

  override def setMonsterSpawnLimit(limit: Int): Unit = ???

  override def getDifficulty: Difficulty = ???

  override def setSpawnFlags(allowMonsters: Boolean, allowAnimals: Boolean): Unit = ???

  override def getTicksPerAnimalSpawns: Long = ???

  override def getMetadata(metadataKey: String): util.List[MetadataValue] = ???

  override def hasMetadata(metadataKey: String): Boolean = ???

  override def setMetadata(metadataKey: String, newMetadataValue: MetadataValue): Unit = ???

  override def removeMetadata(metadataKey: String, owningPlugin: Plugin): Unit = ???

  override def getListeningPluginChannels: util.Set[String] = ???

  override def sendPluginMessage(source: Plugin, channel: String, message: Array[Byte]): Unit = ???

  override def setSpawnLocation(location: Location): Boolean = ???

  @silent override def generateTree(loc: Location, `type`: TreeType, delegate: BlockChangeDelegate): Boolean = ???
}
