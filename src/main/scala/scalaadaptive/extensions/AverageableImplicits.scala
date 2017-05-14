package scalaadaptive.extensions

import scala.math.Numeric.LongIsIntegral
import scala.math.Ordering

/**
  * Created by pk250187 on 5/1/17.
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
