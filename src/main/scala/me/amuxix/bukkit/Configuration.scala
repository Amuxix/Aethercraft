package me.amuxix.bukkit

/**
  * Created by Amuxix on 03/01/2017.
  */
object Configuration {
  private lazy val config = Aethercraft.self.getConfig
  lazy val maxBlocksBouncedByTeleporter: Int = config.getInt("teleporter.maxBounce", 256)
  // Ores are worth this much more than their item counterparts
  lazy val oreMultiplier: Double = config.getDouble("energy.multipliers.ore", 1.5)
  // Chainmail pieces are worth this value as much as iron pieces are
  lazy val chainmailMultiplier: Double = config.getDouble("energy.multipliers.chainmail", 1.1)
  // Crafting increases energy by this percent of the material worth
  lazy val craftingMultiplier: Double = config.getDouble("energy.multipliers.crafting", 1.05)
}
