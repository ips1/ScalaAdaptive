package scalaadaptive.core.runtime.selection.strategies

import scalaadaptive.core.logging.Logger
import scalaadaptive.core.runtime.history.runhistory.RunHistory
import scalaadaptive.core.runtime.selection.strategies.support.TestBasedSelectionStrategy
import scalaadaptive.math.TTestRunner
import scalaadaptive.math.TestResult.TestResult

/**
  * Created by Petr Kubat on 5/2/17.
  *
  * A mean based selection strategy that selects from multiple functions using a simple Welch's test. For more
  * information see the original thesis text.
  *
  * @param logger Logger used to log the selection process.
  * @param testRunner The runner that is used to perform Welch's TTests
  * @param secondaryStrategy The strategy that is used when the test can't decide for one option with given certainty
  * @param alpha The significance level of the selection - its only approximation, as multiple tests are performed
  *
  */
class TTestSelectionStrategy(val logger: Logger,
                             val testRunner: TTestRunner,
                             val secondaryStrategy: SelectionStrategy[Long],
                             val alpha: Double) extends TestBasedSelectionStrategy[Long] {
  override protected val runTestMultiple: (RunHistory[Long], Iterable[RunHistory[Long]], Double) => Option[TestResult] =
    (firstHistory, remaining, alpha) =>
      testRunner.runTest(firstHistory.runStatistics, remaining.map(_.runStatistics).toList, alpha)
  override protected val runTestTwo: (RunHistory[Long], RunHistory[Long], Double) => Option[TestResult] =
    (firstHistory, secondHistory, alpha) =>
      testRunner.runTest(firstHistory.runStatistics, secondHistory.runStatistics, alpha)
  override protected val name: String = "TTestSelectionStrategy"
}
