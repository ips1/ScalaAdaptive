package scalaadaptive.core.configuration.blocks

import scalaadaptive.core.configuration.BaseLongConfiguration
import scalaadaptive.core.logging.Logger
import scalaadaptive.core.runtime.selection._

/**
  * Created by pk250187 on 5/2/17.
  */
trait TTestSelection extends BaseLongConfiguration {
  override val createDiscreteRunSelector: (Logger) => RunSelector[Long] =
    (log: Logger) => {
      val leastDataSelector = new LeastDataSelector[Long](log)
      new LowRunAwareSelector[Long](
        log,
        leastDataSelector,
        new TTestSelector(log, leastDataSelector, 0.05),
        10)
    }
}
