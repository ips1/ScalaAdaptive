package scalaadaptive.extensions

import scala.math.Ordering

/**
  * Created by Petr Kubat on 5/1/17.
  *
  * Implicit member implementation of [[Averageable]] for the [[Long]] type.
  *
  */

object AverageableImplicits {

  trait LongIsAverageable extends Averageable[Long] {
    override def average(values: Iterable[Long]): Option[Long] = {
      val valueList = values.toList
      if (valueList.isEmpty)
        None
      else
        Some(valueList.sum(Numeric.LongIsIntegral) / valueList.length)
    }

    override def newAverage(oldAverage: Long, oldCount: Int, newItem: Long): Long =
      ((oldAverage * oldCount) + newItem) / (oldCount + 1)
  }

  implicit object LongIsAverageable extends LongIsAverageable with Numeric.LongIsIntegral with Ordering.LongOrdering

}
