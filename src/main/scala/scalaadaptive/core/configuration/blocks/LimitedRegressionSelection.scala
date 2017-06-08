package scalaadaptive.core.configuration.blocks

import scalaadaptive.core.configuration.BaseLongConfiguration
import scalaadaptive.core.logging.Logger
import scalaadaptive.core.runtime.selection.{LeastDataSelector, LimitedRegressionSelector, LowRunAwareSelector, RunSelector}
import scalaadaptive.math.RegressionTTestRunner

/**
  * Created by pk250187 on 6/7/17.
  */
trait LimitedRegressionSelection extends BaseLongConfiguration {
  override val createContinuousRunSelector: (Logger) => RunSelector[Long] =
    (log: Logger) => new LowRunAwareSelector[Long](
      log,
      new LeastDataSelector[Long](log),
      new LimitedRegressionSelector[Long](log, 40, new RegressionTTestRunner, 0.05),
      30)
}
