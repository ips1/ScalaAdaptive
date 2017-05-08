package scalaadaptive.core.configuration.blocks

import scalaadaptive.core.configuration.BaseLongConfiguration
import scalaadaptive.core.runtime.selection.{InterpolationSelector, LowRunAwareSelector, RoundRobinSelector, RunSelector}

/**
  * Created by pk250187 on 5/1/17.
  */
trait InterpolationSelection extends BaseLongConfiguration {
  override val continuousRunSelector: RunSelector[Long] =
    new LowRunAwareSelector[Long](
      new RoundRobinSelector[Long](),
      new InterpolationSelector[Long](),
      30)
}
