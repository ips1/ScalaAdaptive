package scalaadaptive.core.configuration.blocks

import scalaadaptive.core.configuration.BaseLongConfiguration
import scalaadaptive.core.runtime.selection._

/**
  * Created by pk250187 on 5/1/17.
  */
trait BestAverageSelection extends BaseLongConfiguration {
  override val createDiscreteRunSelector: () => RunSelector[Long] =
    () => new LowRunAwareSelector[Long](
      new LeastDataSelector[Long](),
      new BestAverageSelector(),
      20)
}
