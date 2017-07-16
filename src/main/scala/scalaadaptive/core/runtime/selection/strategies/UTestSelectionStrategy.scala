package scalaadaptive.core.runtime.selection.strategies

import org.apache.commons.math3.stat.descriptive.StatisticalSummary

import scalaadaptive.core.logging.Logger
import scalaadaptive.core.runtime.history.runhistory.RunHistory
import scalaadaptive.core.runtime.selection.strategies.support.TestBasedSelectionStrategy
import scalaadaptive.math.TestResult.TestResult
import scalaadaptive.math.UTestRunner

/**
  * Created by Petr Kubat on 5/2/17.
  *
  * A mean based selection strategy that selects from multiple functions using a simple Mann-Whitney U-Test. For more
  * information see the original thesis text.
  *
  * @param logger Logger used to log the selection process.
  * @param testRunner The runner that is used to perform Mann-Whitney U-Test
  * @param secondaryStrategy The strategy that is used when the test can't decide for one option with given certainty
  * @param alpha The significance level of the selection - its only approximation, as multiple tests are performed
  *
  */
class UTestSelectionStrategy(val logger: Logger,
                             val testRunner: UTestRunner,
                             val secondaryStrategy: SelectionStrategy[Long],
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
