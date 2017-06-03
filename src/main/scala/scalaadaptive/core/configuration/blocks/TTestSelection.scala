package scalaadaptive.core.configuration.blocks

import scalaadaptive.core.configuration.BaseLongConfiguration
import scalaadaptive.core.runtime.selection._

/**
  * Created by pk250187 on 5/2/17.
  */
trait TTestSelection extends BaseLongConfiguration {
  private val leastDataSelector = new LeastDataSelector[Long]()
  override val discreteRunSelector: RunSelector[Long] = new LowRunAwareSelector[Long](
    leastDataSelector,
    new TTestSelector(leastDataSelector, 0.05),
    10)
}
