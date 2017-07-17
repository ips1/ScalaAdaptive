package scalaadaptive.core.configuration.blocks.selection

import scalaadaptive.core.configuration.BaseLongConfiguration
import scalaadaptive.core.configuration.blocks.helper.{BlockWithAlpha, BlockWithLowRunLimit, BlockWithWindowAverageSize}
import scalaadaptive.core.logging.Logger
import scalaadaptive.core.runtime.selection.strategies.{LeastDataSelectionStrategy, LowRunAwareSelectionStrategy, RegressionSelectionStrategy, SelectionStrategy}
import scalaadaptive.math.PredictionConfidenceTestRunner

/**
  * Created by Petr Kubat on 5/2/17.
  */
trait LinearRegressionInputBasedStrategy extends BaseLongConfiguration
  with BlockWithWindowAverageSize
  with BlockWithAlpha
  with BlockWithLowRunLimit {

  override def createInputBasedStrategy(log: Logger): SelectionStrategy[Long] = {
    val leastDataSelectionStrategy = new LeastDataSelectionStrategy[Long](log)
    new LowRunAwareSelectionStrategy[Long](
      log,
      leastDataSelectionStrategy,
      new RegressionSelectionStrategy[Long](log,
        new PredictionConfidenceTestRunner(log),
        leastDataSelectionStrategy,
        createInputBasedStrategy(log),
        alpha),
      lowRunLimit)
  }
}
