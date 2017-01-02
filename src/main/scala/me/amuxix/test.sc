for {
  a <- 1 to 10
  b <- 'A' to 'B'
} yield s"$a $b"

for (a <- 1 to 10) yield for {b <- 'A' to 'B'} yield s"$a $b"