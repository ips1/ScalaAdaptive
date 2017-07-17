package scalaadaptive.core.configuration.blocks

import scalaadaptive.core.configuration.BaseLongConfiguration
import scalaadaptive.core.configuration.blocks.helper.{BlockWithAlpha, BlockWithLowRunLimit, BlockWithWindowAverageSize}
import scalaadaptive.core.logging.Logger
import scalaadaptive.core.runtime.selection.strategies._
import scalaadaptive.core.runtime.selection.strategies.support.AverageForSampleCountProvider
import scalaadaptive.math.PredictionConfidenceTestRunner

/**
  * Created by Petr Kubat on 6/7/17.
  */
trait WindowBoundRegressionInputBasedStrategy extends BaseLongConfiguration
  with BlockWithWindowAverageSize
  with BlockWithAlpha
  with BlockWithLowRunLimit {

  override def createInputBasedStrategy(log: Logger): SelectionStrategy[Long] = {
    val leastDataSelectionStrategy = new LeastDataSelectionStrategy[Long](log)
    new LowRunAwareSelectionStrategy[Long](
      log,
      leastDataSelectionStrategy,
      new WindowBoundSelectionStrategy[Long](log,
        new AverageForSampleCountProvider(windowAverageSize),
        new RegressionSelectionStrategy[Long](log,
          new PredictionConfidenceTestRunner(log),
          leastDataSelectionStrategy,
          createMeanBasedStrategy(log),
          alpha
        )
      ),
      lowRunLimit
    )
  }
}
