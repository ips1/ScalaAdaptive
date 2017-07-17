package scalaadaptive.core.configuration.blocks.selection

import scalaadaptive.core.configuration.BaseLongConfiguration
import scalaadaptive.core.configuration.blocks.helper.{BlockWithAlpha, BlockWithLowRunLimit}
import scalaadaptive.core.logging.Logger
import scalaadaptive.core.runtime.selection.strategies.{LeastDataSelectionStrategy, LowRunAwareSelectionStrategy, SelectionStrategy, TTestSelectionStrategy}
import scalaadaptive.math.WelchTTestRunner

/**
  * Created by Petr Kubat on 5/2/17.
  *
  * A block that uses the [[scalaadaptive.core.runtime.selection.strategies.TTestSelectionStrategy]] as the
  * mean-based strategy. The argument alpha can be overriden.
  *
  * The strategy itself is wrapped inside a
  * [[scalaadaptive.core.runtime.selection.strategies.LowRunAwareSelectionStrategy]]. Its argument lowRunLimit can be
  * overriden as well.
  *
  */
trait TTestMeanBasedStrategy extends BaseLongConfiguration
  with BlockWithLowRunLimit
  with BlockWithAlpha {
  override def createMeanBasedStrategy(log: Logger): SelectionStrategy[Long] = {
    val tTestRunner = new WelchTTestRunner(log)
    val leastDataSelector = new LeastDataSelectionStrategy[Long](log)
    new LowRunAwareSelectionStrategy[Long](
      log,
      leastDataSelector,
      new TTestSelectionStrategy(log, tTestRunner, leastDataSelector, alpha),
      lowRunLimit)
  }
}
