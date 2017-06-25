package scalaadaptive.core.configuration.blocks

import scalaadaptive.core.configuration.BaseLongConfiguration
import scalaadaptive.core.logging.Logger
import scalaadaptive.core.runtime.selection._
import scalaadaptive.math.RegressionConfidenceTestRunner

/**
  * Created by pk250187 on 5/2/17.
  */
trait RegressionSelection extends BaseLongConfiguration {
  override val createPredictiveSelectionStrategy: (Logger) => SelectionStrategy[Long] =
    (log: Logger) => {
      val leastDataSelectionStrategy = new LeastDataSelectionStrategy[Long](log)
      new LowRunAwareSelectionStrategy[Long](
        log,
        leastDataSelectionStrategy,
        new LimitedRegressionSelectionStategy[Long](log,
          None,
          new RegressionConfidenceTestRunner(log),
          leastDataSelectionStrategy,
          0.05),
        30)
    }
}
