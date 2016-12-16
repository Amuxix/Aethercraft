List(Seq(1, 2), Seq(3, 4)) map {
  case m @ Seq(x, y) => println("x: $x y: $y")

}