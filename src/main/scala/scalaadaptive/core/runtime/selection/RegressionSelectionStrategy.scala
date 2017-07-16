package scalaadaptive.core.runtime.selection

import org.apache.commons.math3.stat.regression.SimpleRegression

import scalaadaptive.core.functions.RunData
import scalaadaptive.core.logging.Logger
import scalaadaptive.core.runtime.history.HistoryKey
import scalaadaptive.core.runtime.history.evaluation.data.EvaluationData
import scalaadaptive.core.runtime.history.runhistory.RunHistory
import scalaadaptive.core.runtime.selection.support.{ClosestProvider, WindowSizeProvider}
import scalaadaptive.math._

/**
  * Created by Petr Kubat on 5/20/17.
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
