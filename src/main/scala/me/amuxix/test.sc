import scala.collection.immutable.TreeMap
import scala.math.{E, log, pow}

val cenas = (1 to pow(2 * E, 8).toInt)
  .map(e => ((log(e) / log(E * 2)).round.toInt, e))
  .filter(_._1 < 8)
  .groupBy(_._1)
  .mapValues(_.map(_._2))

TreeMap(cenas.toArray:_*)
  .map(e => s"Tier ${e._1} -> ${e._2.head} to ${e._2.last}")
  .mkString("\n")

