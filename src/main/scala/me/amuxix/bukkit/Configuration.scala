package me.amuxix.bukkit

import me.amuxix.Energy
import me.amuxix.bukkit.Bukkit.config

/**
  * Created by Amuxix on 03/01/2017.
  */
object Configuration {
  lazy val maxBlocksBouncedByTeleporter: Int = config.getInt("minEnergyLogThreshold", 100)
  lazy val minEnergyLogThreshold: Int = config.getInt("teleporter.minEnergyLogThreshold", 256)
  // Ores are worth this much more than their item counterparts
  lazy val oreMultiplier: Double = config.getDouble("energy.multipliers.ore", 1.5)
  // Chainmail pieces are worth this value as much as iron pieces are
  lazy val chainmailMultiplier: Double = config.getDouble("energy.multipliers.chainmail", 1.1)
  // Crafting increases energy by this percent of the material worth
  lazy val craftingMultiplier: Double = config.getDouble("energy.multipliers.crafting", 1.05)
  lazy val maxBurnDistance: Int = config.getInt("energy.maxBurnDistance", 30)

  lazy val move: Double = config.getDouble("energy.costs.move", 0.2)
  lazy val blockBreak: Energy = config.getInt("energy.costs.blockBreak", 8)
  lazy val smelt: Energy = config.getInt("energy.costs.smelt", 5)
  lazy val damage: Energy = config.getInt("energy.costs.damage", 5)
  lazy val absorb: Energy = config.getInt("energy.costs.absorb", 30)
  lazy val heal: Energy = config.getInt("energy.costs.heal", 15)
}
