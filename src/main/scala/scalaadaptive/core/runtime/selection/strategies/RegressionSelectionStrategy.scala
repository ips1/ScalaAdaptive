package scalaadaptive.core.runtime.selection.strategies

import scalaadaptive.core.logging.Logger
import scalaadaptive.core.runtime.history.HistoryKey
import scalaadaptive.core.runtime.history.runhistory.RunHistory
import scalaadaptive.math._

/**
  * Created by Petr Kubat on 5/20/17.
  *
  * An input based selection strategy that selects from multiple functions using predictions fetched from a linear
  * regression model. For more information see the original thesis text.
  *
  * Apart from the common fallback strategy contains a mean-based strategy that is used when the regression model
  * of all the functions is built only from one value (even though with a lot of observations). This situation
  * indicates wrong use of the input based strategy, so this mean-based fallback limits the impact of the error.
  *
  * @param logger Logger used to log the selection process.
  * @param testRunner The runner that is used to test confidence intervals of linear regression predictions.
  * @param secondaryStrategy The strategy that is used when the confidence intervals overlap and this strategy can't
  *                          decide.
  * @param meanBasedStrategy The strategy that is used when all the inputs in all the regression models are the same.
  * @param alpha The significance level of the selection - its only approximation, as multiple tests are performed.
  *
  */
class RegressionSelectionStrategy[TMeasurement](val logger: Logger,
                                                val testRunner: RegressionConfidenceTestRunner,
                                                val secondaryStrategy: SelectionStrategy[TMeasurement],
                                                val meanBasedStrategy: SelectionStrategy[TMeasurement],
                                                val alpha: Double)(implicit num: Numeric[TMeasurement])
  extends SelectionStrategy[TMeasurement] {

  override def selectOption(records: Seq[RunHistory[TMeasurement]], inputDescriptor: Option[Long]): HistoryKey = {
    logger.log("Selecting using WindowBoundRegressionSelectionStrategy")

    val descriptor = inputDescriptor match {
      case Some(d) => d
      case _ => return secondaryStrategy.selectOption(records, inputDescriptor)
    }

    val regressions = records.map(r => (r, r.runRegression))

    // Are all of the regressions corectly built?
    val insufficientRegressions = regressions.filter(r => !r._2.hasEnoughX)
    if (insufficientRegressions.size == regressions.size) {
      // All of the functions have insufficient inputs observed, falling back to mean-based strategy
      return meanBasedStrategy.selectOption(records, inputDescriptor)
    }
    else if (insufficientRegressions.nonEmpty) {
      // Some of the functions have sufficient data while others not, using secondary strategy
      return secondaryStrategy.selectOption(records, inputDescriptor)
    }

    val positiveResults = regressions
      .view
      .map(regression => {
        val remaining = regressions
          .filter(r => r != regression)
          .map(_._2)

        val result = testRunner.runTest(regression._2, remaining, descriptor.toDouble, alpha)

        (regression, result)
      })

    positiveResults
      .find(_._2.contains(TestResult.ExpectedLower))
      .map(_._1._1.key)
      .getOrElse(secondaryStrategy.selectOption(records, inputDescriptor))
  }
}
