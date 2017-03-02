
/*trait Type
object Apple extends Type
object Orange extends Type
object Potato extends Type

trait Color
object Red extends Color
object Blue extends Color

def join(i1: Item, i2: Item): Item = Item(i1.t, i1.c, i1.amount + i2.amount)
case class Item(t: Type, c: Color, amount: Int)

val items = Seq[Item](Item(Apple, Red, 1), Item(Apple, Blue, 30), Item(Orange, Blue, 1), Item(Apple, Red, 12), Item(Potato, Blue, 50))
val a = items.groupBy(i => (i.t, i.c)).values.map(_.reduce(join))*/

case class A(x: Int)
case class B(x: Int)

val e1: Either[A, B] = Left(A(1))
val e2: Either[A, B] = Right(B(1))
val e3: Either[A, B] = Left(A(3))
val e4: Either[A, B] = Right(B(5))

val s = Seq(e1,e2,e3,e4)
val a: Seq[Int] = s.map(_.fold(_.x, _.x))