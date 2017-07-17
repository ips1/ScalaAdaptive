package scalaadaptive.core.runtime.selection.strategies

import org.apache.commons.math3.analysis.interpolation.LoessInterpolator
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction
import org.apache.commons.math3.exception.NumberIsTooSmallException

import scalaadaptive.core.logging.Logger
import scalaadaptive.core.runtime.history.HistoryKey
import scalaadaptive.core.runtime.history.evaluation.data.GroupedEvaluationData
import scalaadaptive.core.runtime.history.runhistory.RunHistory

/**
  * Created by Petr Kubat on 4/23/17.
  *
  * An input based selection strategy that selects from multiple functions using predictions fetched from a LOESS
  * interpolation model. For more information see the original thesis text.
  *
  * @param logger Logger used to log the selection process.
  * @param secondaryStrategy Strategy that is used to select among the functions for which it was not possible to
  *                          generate prediction.
  *
  */
class LoessInterpolationSelectionStrategy[TMeasurement](val logger: Logger,
                                                        val secondaryStrategy: SelectionStrategy[TMeasurement])
                                                       (implicit num: Numeric[TMeasurement])
  extends SelectionStrategy[TMeasurement] {

  private def getInterpolator = new LoessInterpolator(0.6, 2)

  /**
    * Interpolates polynomial, returns the function in case of success, returns None if there is not enough data
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
      case ex: NumberIsTooSmallException => {
        logger.log(s"LoessInterpolator cannot interpolate with ${sortedData.size} items")
        None
      }
      // None of the other declared exceptions from interpolate() method should possibly occur in this case
      case ex: Exception => {
        logger.log(s"Unknown exception while interpolating: ${ex.getMessage}")
        None
      }
    }
  }

  private def getModelPrediction(model: Option[PolynomialSplineFunction], point: Double): Option[Double] = {
    if (model.isEmpty) {
      logger.log(s"The model is empty, cannot predict.")
      return None
    }
    if (!model.get.isValidPoint(point)) {
      logger.log(s"Invalid point $point in the model!")
      return None
    }

    import scalaadaptive.extensions.DoubleExtensions._
    val prediction = model.get.value(point)
    prediction.asOption
  }

  private def selectUsingInterpolation(records: Seq[RunHistory[TMeasurement]],
                                       descriptor: Long): HistoryKey = {
    val polynomials =
      records.map(runHistory => {
        val sortedData = runHistory.runAveragesGroupedByDescriptor
          .toList
          .sortBy(i => i._1)
        (runHistory, interpolate(sortedData))
      }).toMap

    val predictions = polynomials.mapValues(p => getModelPrediction(p, descriptor))

    val failedPredictions = predictions.filter(p => p._2.isEmpty)

    if (failedPredictions.nonEmpty) {
      logger.log(s"Prediction failed for ${failedPredictions.size} functions, using the secondary strategy.")
      return secondaryStrategy.selectOption(records, Some(descriptor))
    }

    predictions.minBy(p => p._2)._1.key
  }

  override def selectOption(records: Seq[RunHistory[TMeasurement]],
                            inputDescriptor: Option[Long]): HistoryKey = {
    logger.log("Selecting using InterpolationSelector")

    val descriptor = inputDescriptor match {
      case Some(d) => d
      case _ => {
        logger.log("Input selector undefined, using the secondary strategy")
        return secondaryStrategy.selectOption(records, None)
      }
    }

    selectUsingInterpolation(records, descriptor)
  }
}
