package scalaadaptive.core.configuration.blocks

import scalaadaptive.core.configuration.BaseLongConfiguration
import scalaadaptive.core.runtime.selection._

/**
  * Created by pk250187 on 5/2/17.
  */
trait TTestSelection extends BaseLongConfiguration {
  private val roundRobinSelector = new RoundRobinSelector[Long]()
  override val runSelector: RunSelector[Long] = new LowRunAwareSelector[Long](
    roundRobinSelector,
    new TTestSelector(roundRobinSelector, 0.05),
    20)
}
