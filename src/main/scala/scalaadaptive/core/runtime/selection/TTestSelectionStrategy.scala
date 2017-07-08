package scalaadaptive.core.runtime.selection

import org.apache.commons.math3.stat.descriptive.{DescriptiveStatistics, SummaryStatistics}
import org.apache.commons.math3.stat.inference.TestUtils

import scalaadaptive.core.logging.Logger
import scalaadaptive.core.runtime.history.runhistory.RunHistory
import scalaadaptive.math.{TTestRunner, TestResult, WelchTTestRunner}

/**
  * Created by pk250187 on 5/2/17.
  */
class TTestSelectionStrategy(val logger: Logger,
                             val testRunner: TTestRunner,
                             val secondarySelector: SelectionStrategy[Long],
                             val alpha: Double) extends SelectionStrategy[Long] {

  private def selectFromTwo(firstRecord: RunHistory[Long],
                            secondRecord: RunHistory[Long],
                            inputDescriptor: Option[Long]): RunHistory[Long] = {
    val result = testRunner.runTest(firstRecord.runStatistics, secondRecord.runStatistics, alpha)
    result match {
      case TestResult.ExpectedLower => firstRecord
      case TestResult.ExpectedHigher => secondRecord
      case _ => secondarySelector.selectOption(List(firstRecord, secondRecord), inputDescriptor)
    }
  }

  override def selectOption(records: Seq[RunHistory[Long]],
                            inputDescriptor: Option[Long]): RunHistory[Long] = {
    logger.log("Selecting using TTestSelectionStrategy")

    // Fallback to the two sample test
    if (records.size == 2)
      return selectFromTwo(records.head, records.last, inputDescriptor)

    // Applying Bonferroni correction
    val oneTestAlpha = alpha / records.size

    // Using Scala's view for lazy mapping and filtering - we need just the first result
    val positiveResults = records
      .view
      .map(elem => {
        val remaining = records
          .filter(r => r != elem)
          .map(r => r.runStatistics)

        val result = testRunner.runTest(elem.runStatistics, remaining, oneTestAlpha)

        (elem, result)
      })

    positiveResults
      .find(_._2.contains(TestResult.ExpectedLower))
      .map(_._1)
      .getOrElse(secondarySelector.selectOption(records, inputDescriptor))
  }
}
