package scalaadaptive.core.runtime.selection

import org.apache.commons.math3.stat.descriptive.{DescriptiveStatistics, SummaryStatistics}
import org.apache.commons.math3.stat.inference.TestUtils

import scalaadaptive.core.logging.Logger
import scalaadaptive.core.runtime.history.HistoryKey
import scalaadaptive.core.runtime.history.runhistory.RunHistory
import scalaadaptive.core.runtime.selection.support.TestBasedSelectionStrategy
import scalaadaptive.math.TestResult.TestResult
import scalaadaptive.math.{TTestRunner, TestResult, WelchTTestRunner}

/**
  * Created by pk250187 on 5/2/17.
  */
class TTestSelectionStrategy(val logger: Logger,
                             val testRunner: TTestRunner,
                             val secondarySelector: SelectionStrategy[Long],
                             val alpha: Double) extends TestBasedSelectionStrategy[Long] {
  override protected val runTest: (RunHistory[Long], Iterable[RunHistory[Long]], Double) => Option[TestResult] =
    (firstHistory, remaining, alpha) =>
      testRunner.runTest(firstHistory.runStatistics, remaining.map(_.runStatistics).toList, alpha)
  override protected val name: String = "TTestSelectionStrategy"
}
