package scalaadaptive.core.configuration.blocks

import scalaadaptive.core.configuration.BaseLongConfiguration
import scalaadaptive.core.runtime.selection._

/**
  * Created by pk250187 on 5/2/17.
  */
trait RegressionSelection extends BaseLongConfiguration {
  override val continuousRunSelector: RunSelector[Long] =
    new LowRunAwareSelector[Long](
      new LeastDataSelector[Long](),
      new RegressionSelector[Long](),
      30)
}
