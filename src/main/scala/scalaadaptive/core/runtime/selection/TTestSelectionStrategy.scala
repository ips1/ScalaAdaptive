package scalaadaptive.core.runtime.selection

import org.apache.commons.math3.stat.descriptive.{DescriptiveStatistics, SummaryStatistics}
import org.apache.commons.math3.stat.inference.TestUtils

import scalaadaptive.core.logging.Logger
import scalaadaptive.core.runtime.history.runhistory.RunHistory
import scalaadaptive.math.{MultipleSampleTTest, WelchTTestRunner, TestResult}

/**
  * Created by pk250187 on 5/2/17.
  */
class TTestSelectionStrategy(val logger: Logger,
                             val testRunner: MultipleSampleTTest,
                             val secondarySelector: SelectionStrategy[Long],
                             val alpha: Double) extends SelectionStrategy[Long] {

  override def selectOption(records: Seq[RunHistory[Long]], inputDescriptor: Option[Long]): RunHistory[Long] = {
    logger.log("Selecting using TTestSelectionStrategy")

    // Applying Bonferroni correction
    val oneTestAlpha = alpha / records.size

    // Using Scala's view for lazy mapping and filtering - we need just the first result
    val positiveResults = records
      .view
      .map(elem => {
        val remaining = records
          .filter(r => r != elem)
          .map(r => r.runStatistics)

        val result = testRunner.runTests(elem.runStatistics, remaining, oneTestAlpha)

        (elem, result)
      })

    positiveResults
      .find(_._2.contains(TestResult.ExpectedLower))
      .map(_._1)
      .getOrElse(secondarySelector.selectOption(records, inputDescriptor))
  }
}
