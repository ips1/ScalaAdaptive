package scalaadaptive.core.runtime.selection

import org.apache.commons.math3.analysis.interpolation.{LinearInterpolator, LoessInterpolator}
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction
import org.apache.commons.math3.exception.NumberIsTooSmallException

import scalaadaptive.core.logging.Logger
import scalaadaptive.core.runtime.history.HistoryKey
import scalaadaptive.core.runtime.history.evaluation.data.GroupedEvaluationData
import scalaadaptive.core.runtime.history.runhistory.RunHistory

/**
  * Created by Petr Kubat on 4/23/17.
  */
class LoessInterpolationSelectionStrategy[TMeasurement](val logger: Logger)(implicit num: Numeric[TMeasurement])
  extends SelectionStrategy[TMeasurement] {

  private def getInterpolator = new LoessInterpolator(0.5, 2)

  /**
    * Interpolates polynomial, returns the function in case of success, returns None if there is not enough data
    * @param sortedData
    * @return
    */
  private def interpolate(sortedData: Seq[(Long, GroupedEvaluationData[TMeasurement])]): Option[PolynomialSplineFunction] = {
    try {
      Some(
        getInterpolator.interpolate(
          sortedData.map(i => i._1.toDouble).toArray,
          sortedData.map(i => num.toDouble(i._2.averageMeasurement)).toArray
        )
      )
    } catch {
      // TODO: Handle more exceptions
      case ex: NumberIsTooSmallException => None
    }
  }

  private def selectUsingInterpolation(records: Seq[RunHistory[TMeasurement]],
                                       descriptor: Long): HistoryKey = {
    val polynomials =
      records.map(runHistory => {
        val sortedData = runHistory.runAveragesGroupedByDescriptor
          .toList
          .sortBy(i => i._1)
        (runHistory, interpolate(sortedData))
      })

    val min = polynomials.minBy(p => if (p._2.isEmpty || !p._2.get.isValidPoint(descriptor)) {
      // TODO: Add more logging!!!
      logger.log("Invalid point!")
      -1
    } else {
      val point = p._2.get.value(descriptor)
      if (point.isNaN)
        -1
      else
        point
    })
    min._1.key
  }

  override def selectOption(records: Seq[RunHistory[TMeasurement]],
                            inputDescriptor: Option[Long]): HistoryKey = {
    logger.log("Selecting using InterpolationSelector")

    val descriptor = inputDescriptor match {
      case Some(d) => d
      case _ => return records.head.key
    }

    selectUsingInterpolation(records, descriptor)
  }
}
