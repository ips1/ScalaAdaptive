package scalaadaptive.core.configuration.blocks

import scalaadaptive.core.configuration.BaseLongConfiguration
import scalaadaptive.core.configuration.blocks.helper.{BlockWithAlpha, BlockWithLowRunLimit, BlockWithWindowAverageSize}
import scalaadaptive.core.logging.Logger
import scalaadaptive.core.runtime.selection._
import scalaadaptive.core.runtime.selection.strategies.{LeastDataSelectionStrategy, LowRunAwareSelectionStrategy, RegressionSelectionStrategy, SelectionStrategy}
import scalaadaptive.math.PredictionConfidenceTestRunner

/**
  * Created by Petr Kubat on 5/2/17.
  */
trait LinearRegressionInputBasedStrategy extends BaseLongConfiguration
  with BlockWithWindowAverageSize
  with BlockWithAlpha
  with BlockWithLowRunLimit {

  override val createInputBasedStrategy: (Logger) => SelectionStrategy[Long] =
    (log: Logger) => {
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
