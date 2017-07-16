package scalaadaptive.core.configuration.blocks

import scalaadaptive.core.configuration.BaseLongConfiguration
import scalaadaptive.core.logging.Logger
import scalaadaptive.core.runtime.selection._
import scalaadaptive.core.runtime.selection.strategies.{BestAverageSelectionStrategy, LeastDataSelectionStrategy, LowRunAwareSelectionStrategy, SelectionStrategy}

/**
  * Created by Petr Kubat on 5/1/17.
  */
trait BestAverageSelection extends BaseLongConfiguration {
  override val createMeanBasedStrategy: (Logger) => SelectionStrategy[Long] =
    (log: Logger) => new LowRunAwareSelectionStrategy[Long](
      log,
      new LeastDataSelectionStrategy[Long](log),
      new BestAverageSelectionStrategy(log),
      30)
}
