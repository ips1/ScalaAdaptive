package scalaadaptive.core.runtime.selection

import org.apache.commons.math3.analysis.interpolation.{LinearInterpolator, LoessInterpolator}
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction
import org.apache.commons.math3.exception.NumberIsTooSmallException

import scalaadaptive.core.runtime.history.rundata.GroupedRunData
import scalaadaptive.core.runtime.history.runhistory.RunHistory

/**
  * Created by pk250187 on 4/23/17.
  */
class InterpolationSelector[TMeasurement](implicit num: Numeric[TMeasurement])
  extends RunSelector[TMeasurement] {

  // TODO : Chose interpolator
  private def getInterpolator = new LoessInterpolator()

  /**
    * Interpolates polynomial, returns the function in case of success, returns None if there is not enough data
    * @param sortedData
    * @return
    */
  private def interpolate(sortedData: Seq[(Option[Long], GroupedRunData[TMeasurement])]): Option[PolynomialSplineFunction] = {
    try {
      Some(
        getInterpolator.interpolate(
          sortedData.map(i => i._1.get.toDouble).toArray,
          sortedData.map(i => num.toDouble(i._2.averageRunData.measurement)).toArray
        )
      )
    } catch {
      // TODO: Handle more exceptions
      case ex: NumberIsTooSmallException => None
    }
  }

  private def selectUsingInterpolation(records: Seq[RunHistory[TMeasurement]],
                                       descriptor: Long) = {
    val polynomials =
      records.map(runHistory => {
        val sortedData = runHistory.runAveragesGroupedByDescriptor
          .toList
          .filter(i => i._1.isDefined)
          .sortBy(i => i._1)
        (runHistory, interpolate(sortedData))
      })

    val min = polynomials.minBy(p => if (p._2.isEmpty || !p._2.get.isValidPoint(descriptor)) {
      // TODO: Replace by logger
      println("Invalid point!")
      -1
    } else p._2.get.value(descriptor))
    min._1
  }

  override def selectOption(records: Seq[RunHistory[TMeasurement]],
                            inputDescriptor: Option[Long]): RunHistory[TMeasurement] = {
    logger.log("Selecting using InterpolationSelector")

    val descriptor = inputDescriptor match {
      case Some(d) => d
      case _ => return records.head
    }

    selectUsingInterpolation(records, descriptor)
  }
}
