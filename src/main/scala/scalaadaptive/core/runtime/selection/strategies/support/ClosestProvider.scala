package scalaadaptive.core.runtime.selection.strategies.support

/**
  * Created by Petr Kubat on 6/11/17.
  */
class ClosestProvider[T](val sortedArray: Array[T], val selector: (T) => Long, val value: Long) {
  private var start = sortedArray.indexWhere(n => selector(n) > value)

  if (start == -1) {
    start = sortedArray.length - 1
  }

  private var end = start - 1

  def getNext: Option[T] = {
    val next = if (end < sortedArray.length - 1) Some(sortedArray(end + 1)) else None
    val previous = if (start > 0) Some(sortedArray(start - 1)) else None
    if (next.isEmpty && previous.isEmpty) return None
    if (next.isEmpty) {
      start -= 1
      return previous
    }
    if (previous.isEmpty) {
      end += 1
      return next
    }

    if (Math.abs(selector(next.get) - value) < Math.abs(selector(previous.get) - value)) {
      return next
    }
    previous
  }
}
