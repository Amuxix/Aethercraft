import me.amuxix.pattern.Element
import me.amuxix.pattern.Element._
import org.bukkit.Material._
implicit class TuppleAdd(t: (Int, Int, Int)) {
	def +(p: (Int, Int, Int)) = (p._1 + t._1, p._2 + t._2, p._3 + t._3)
}
val a = (1,2,3)
val b = (1,4,3)
a+b

(List(1, 2, 3), List(15, 25, 35)).zipped


trait A {
  def banana: Int = ???
}

trait B { self: A =>

}

class Bla extends B with A {

}


trait Consumable {
  def consume: Unit = ???
}

trait Tiered {
  def consumeTierBlocks: Unit = ???
}

trait Cenas { self: Tiered with Consumable =>

}