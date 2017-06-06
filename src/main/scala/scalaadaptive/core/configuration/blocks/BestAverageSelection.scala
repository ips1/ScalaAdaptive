package scalaadaptive.core.configuration.blocks

import scalaadaptive.core.configuration.BaseLongConfiguration
import scalaadaptive.core.logging.Logger
import scalaadaptive.core.runtime.selection._

/**
  * Created by pk250187 on 5/1/17.
  */
trait BestAverageSelection extends BaseLongConfiguration {
  override val createDiscreteRunSelector: (Logger) => RunSelector[Long] =
    (log: Logger) => new LowRunAwareSelector[Long](
      log,
      new LeastDataSelector[Long](log),
      new BestAverageSelector(log),
      20)
}
