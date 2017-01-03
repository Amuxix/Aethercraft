package me.amuxix

import me.amuxix.runes.Rune
import me.amuxix.runes.traits.Persistent
import me.amuxix.util.Block

/**
  * Created by Amuxix on 03/01/2017.
  */
object IntegrityMonitor {
  //Maps bocks to the rune which they are tied to, modifying these blocks destroys the rune.
  private var monitoredBlocks = Map.empty[Seq[Block], Rune with Persistent]

  def addRune(rune: Rune with Persistent): Unit = monitoredBlocks += rune.monitoredBlocks -> rune
}
