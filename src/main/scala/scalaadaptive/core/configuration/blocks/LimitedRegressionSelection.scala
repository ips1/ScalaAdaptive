package scalaadaptive.core.configuration.blocks

import scalaadaptive.core.configuration.BaseLongConfiguration
import scalaadaptive.core.logging.Logger
import scalaadaptive.core.runtime.selection.support.{AverageForSampleCountProvider, FixedSizeProvider}
import scalaadaptive.core.runtime.selection.{LeastDataSelectionStrategy, LimitedRegressionSelectionStategy, LowRunAwareSelectionStrategy, SelectionStrategy}
import scalaadaptive.math.{RegressionConfidenceTestRunner, RegressionTTestRunner}

/**
  * Created by pk250187 on 6/7/17.
  */
trait LimitedRegressionSelection extends BaseLongConfiguration {
  override val createPredictiveSelectionStrategy: (Logger) => SelectionStrategy[Long] =
    (log: Logger) => new LowRunAwareSelectionStrategy[Long](
      log,
      new LeastDataSelectionStrategy[Long](log),
      new LimitedRegressionSelectionStategy[Long](
        log,
        Some(new AverageForSampleCountProvider(50)),
        //new FixedSizeSelector(1000),
        new RegressionConfidenceTestRunner(log),
        new LeastDataSelectionStrategy[Long](log),
        0.05),
      30)
}
