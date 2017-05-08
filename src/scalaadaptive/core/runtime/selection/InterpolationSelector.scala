package scalaadaptive.core.runtime.selection

import org.apache.commons.math3.analysis.interpolation.{LinearInterpolator, LoessInterpolator}

import scalaadaptive.core.runtime.history.runhistory.RunHistory

/**
  * Created by pk250187 on 4/23/17.
  */
class InterpolationSelector[TMeasurement](implicit num: Numeric[TMeasurement])
  extends RunSelector[TMeasurement] {
  override def selectOption(records: Seq[RunHistory[TMeasurement]],
                            inputDescriptor: Option[Long]): RunHistory[TMeasurement] = {

    val descriptor = inputDescriptor match {
      case Some(d) => d
      case _ => return records.head
    }

    // TODO : Chose interpolator
    //val interpolator = new LinearInterpolator()
    val interpolator = new LoessInterpolator()

    // TODO: Exceptions thrown from interpolator!
    // Should not be used on ungrouped data!
    // TODO: Exception in thread "main" org.apache.commons.math3.exception.NumberIsTooSmallException: number of points (1)
    val polynomials =
      records.map(runHistory => {
        val sortedData = runHistory.runAveragesGroupedByDescriptor
          .toList
          .filter(i => i._1.isDefined)
          .sortBy(i => i._1)
        (runHistory,
         interpolator.interpolate(
           sortedData.map(i => i._1.get.toDouble).toArray,
           sortedData.map(i => num.toDouble(i._2.averageRunData.measurement)).toArray
         )
        )
      })

    val min = polynomials.minBy(p => if (!p._2.isValidPoint(descriptor)) {
      println("Invalid point!")
      -1
    } else p._2.value(descriptor))

    min._1
  }
}
