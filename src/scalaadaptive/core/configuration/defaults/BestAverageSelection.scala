package scalaadaptive.core.configuration.defaults

import scalaadaptive.core.configuration.BaseLongConfiguration
import scalaadaptive.core.runtime.selection.{BestAverageSelector, LowRunAwareSelector, RoundRobinSelector, RunSelector}

/**
  * Created by pk250187 on 5/1/17.
  */
trait BestAverageSelection extends BaseLongConfiguration {
  override val runSelector: RunSelector[Long] = new LowRunAwareSelector[Long](
    new RoundRobinSelector[Long](),
    new BestAverageSelector(),
    20)
}
