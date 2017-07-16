package scalaadaptive.core.runtime.selection.strategies

import org.apache.commons.math3.stat.descriptive.StatisticalSummary

import scalaadaptive.core.logging.Logger
import scalaadaptive.core.runtime.history.runhistory.RunHistory
import scalaadaptive.core.runtime.selection.strategies.support.TestBasedSelectionStrategy
import scalaadaptive.math.TestResult.TestResult
import scalaadaptive.math.UTestRunner

/**
  * Created by Petr Kubat on 5/2/17.
  */
class UTestSelectionStrategy(val logger: Logger,
                             val testRunner: UTestRunner,
                             val secondarySelector: SelectionStrategy[Long],
                             val alpha: Double) extends TestBasedSelectionStrategy[Long] {

  private def getTestData(history: RunHistory[Long]): (Iterable[Double], StatisticalSummary) =
    (history.runItems.map(_.measurement.toDouble), history.runStatistics)
  override protected val runTestMultiple: (RunHistory[Long], Iterable[RunHistory[Long]], Double) => Option[TestResult] =
    (firstHistory, remaining, alpha) =>
      testRunner.runTest(getTestData(firstHistory), remaining.map(getTestData).toList, alpha)
  override protected val runTestTwo: (RunHistory[Long], RunHistory[Long], Double) => Option[TestResult] =
    (firstHistory, secondHistory, alpha) =>
      testRunner.runTest(getTestData(firstHistory), getTestData(secondHistory), alpha)
  override protected val name: String = "UTestSelectionStrategy"
}
