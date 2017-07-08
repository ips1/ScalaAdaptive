package scalaadaptive.core.runtime.selection

import org.apache.commons.math3.stat.descriptive.{DescriptiveStatistics, StatisticalSummary, SummaryStatistics}
import org.apache.commons.math3.stat.inference.TestUtils

import scalaadaptive.core.logging.Logger
import scalaadaptive.core.runtime.history.HistoryKey
import scalaadaptive.core.runtime.history.runhistory.RunHistory
import scalaadaptive.core.runtime.selection.support.TestBasedSelectionStrategy
import scalaadaptive.math.TestResult.TestResult
import scalaadaptive.math.{TestResult, UTestRunner, WelchTTestRunner}

/**
  * Created by pk250187 on 5/2/17.
  */
class UTestSelectionStrategy(val logger: Logger,
                             val testRunner: UTestRunner,
                             val secondarySelector: SelectionStrategy[Long],
                             val alpha: Double) extends TestBasedSelectionStrategy[Long] {

  private def getTestData(history: RunHistory[Long]): (Iterable[Double], StatisticalSummary) =
    (history.runItems.map(_.measurement.toDouble), history.runStatistics)

  override protected val runTest: (RunHistory[Long], Iterable[RunHistory[Long]], Double) => Option[TestResult] =
    (firstHistory, remaining, alpha) =>
      testRunner.runTest(getTestData(firstHistory), remaining.map(getTestData).toList, alpha)
  override protected val name: String = "UTestSelectionStrategy"
}
