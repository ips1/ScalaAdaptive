package scalaadaptive.extensions

/**
  * Created by pk250187 on 5/1/17.
  */
trait Averageable[T] extends Numeric[T] {
  def average(values: Iterable[T]): Option[T]
  def newAverage(oldAverage: T, oldCount: Int, newItem: T): T
}
