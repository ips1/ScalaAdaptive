package scalaadaptive.core.runtime.selection

import org.apache.commons.math3.stat.descriptive.{DescriptiveStatistics, StatisticalSummary, SummaryStatistics}
import org.apache.commons.math3.stat.inference.TestUtils

import scalaadaptive.core.logging.Logger
import scalaadaptive.core.runtime.history.runhistory.RunHistory
import scalaadaptive.math.{MultipleSampleTTest, TestResult, UTest, WelchTTestRunner}

/**
  * Created by pk250187 on 5/2/17.
  */
class UTestSelectionStrategy(val logger: Logger,
                             val testRunner: UTest,
                             val secondarySelector: SelectionStrategy[Long],
                             val alpha: Double) extends SelectionStrategy[Long] {

  private def getTestData(history: RunHistory[Long]): (Iterable[Double], StatisticalSummary) =
    (history.runItems.map(_.measurement.toDouble), history.runStatistics)

  override def selectOption(records: Seq[RunHistory[Long]], inputDescriptor: Option[Long]): RunHistory[Long] = {
    logger.log("Selecting using UTestSelectionStrategy")

    // Applying Bonferroni correction
    val oneTestAlpha = alpha / records.size

    // Using Scala's view for lazy mapping and filtering - we need just the first result
    val positiveResults = records
      .view
      .map(elem => {
        val remaining = records
          .filter(r => r != elem)
          .map(r => getTestData(r))

        val result = testRunner.runTest(getTestData(elem), remaining, oneTestAlpha)

        (elem, result)
      })

    positiveResults
      .find(_._2.contains(TestResult.ExpectedLower))
      .map(_._1)
      .getOrElse(secondarySelector.selectOption(records, inputDescriptor))
  }
}
