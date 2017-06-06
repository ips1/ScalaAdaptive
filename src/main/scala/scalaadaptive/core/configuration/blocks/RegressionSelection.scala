package scalaadaptive.core.configuration.blocks

import scalaadaptive.core.configuration.BaseLongConfiguration
import scalaadaptive.core.logging.Logger
import scalaadaptive.core.runtime.selection._

/**
  * Created by pk250187 on 5/2/17.
  */
trait RegressionSelection extends BaseLongConfiguration {
  override val createContinuousRunSelector: (Logger) => RunSelector[Long] =
    (log: Logger) => new LowRunAwareSelector[Long](
      log,
      new LeastDataSelector[Long](log),
      new RegressionSelector[Long](log),
      30)
}
