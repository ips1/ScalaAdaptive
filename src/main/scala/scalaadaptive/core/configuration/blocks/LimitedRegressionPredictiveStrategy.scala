package scalaadaptive.core.configuration.blocks

import scalaadaptive.core.configuration.BaseLongConfiguration
import scalaadaptive.core.logging.Logger
import scalaadaptive.core.runtime.selection.support.{AverageForSampleCountProvider, FixedSizeProvider}
import scalaadaptive.core.runtime.selection.{LeastDataSelectionStrategy, LimitedRegressionSelectionStrategy, LowRunAwareSelectionStrategy, SelectionStrategy}
import scalaadaptive.math.{RegressionConfidenceTestRunner, RegressionTTestRunner}

/**
  * Created by pk250187 on 6/7/17.
  */
trait LimitedRegressionPredictiveStrategy extends BaseLongConfiguration {
  override val createPredictiveSelectionStrategy: (Logger) => SelectionStrategy[Long] =
    (log: Logger) => {
      val leastDataSelectionStrategy = new LeastDataSelectionStrategy[Long](log)
      new LowRunAwareSelectionStrategy[Long](
        log,
        leastDataSelectionStrategy,
        new LimitedRegressionSelectionStrategy[Long](log,
          Some(new AverageForSampleCountProvider(25)),
          new RegressionConfidenceTestRunner(log),
          leastDataSelectionStrategy,
          0.05),
        30)
    }
}
