package me.amuxix.util

object Mergeable {
  implicit class MergeableList[T <: Mergeable[T]](seq: List[T]) {
    def merge: List[T] = seq match {
      case s if s.isEmpty => s
      case _ =>
        val h = seq.head
        val (mergeable, unmergeable) = seq.partition(h.canMerge)
        mergeable.reduceLeft(_ merge _) +: unmergeable.merge
    }
  }
}

trait Mergeable[T] {
  def canMerge(that: T): Boolean

  def merge(that: T): T
}