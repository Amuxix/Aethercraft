import scala.collection.immutable.TreeMap
import scala.math.{E, log, pow}

TreeMap((1 to pow(E * 2, 8).toInt).map(e => ((log(e) / log(E * 2)).round.toInt, e)).filter(_._1 < 8).groupBy(_._1).map(e => e._1 -> e._2.map(_._2)).toArray:_*).map(entry => (entry._1, (entry._2.head, entry._2.last))).map(e => s"Tier ${e._1} -> ${e._2._1} to ${e._2._2}").mkString("\n")

