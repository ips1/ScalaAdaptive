package scalaadaptive.extensions

/**
  * Created by Petr Kubat on 5/1/17.
  *
  * A type class that allows computing average from a sequence of the type.
  *
  */
trait Averageable[T] extends Numeric[T] {
  /**
    * Computes average from an iterable of T.
    * @param values The values to compute the average from.
    * @return The average.
    */
  def average(values: Iterable[T]): Option[T]

  /**
    * Updates an average with a new item T (without having access to all the original items).
    * @param oldAverage The old average (excluding newItem).
    * @param oldCount The old count of items (excluding newItem).
    * @param newItem The new item.
    * @return The new average (including newItem).
    */
  def newAverage(oldAverage: T, oldCount: Int, newItem: T): T
}
