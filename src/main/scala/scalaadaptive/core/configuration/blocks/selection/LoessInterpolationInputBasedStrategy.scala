package scalaadaptive.core.configuration.blocks.selection

import scalaadaptive.core.configuration.BaseLongConfiguration
import scalaadaptive.core.configuration.blocks.helper.BlockWithLowRunLimit
import scalaadaptive.core.logging.Logger
import scalaadaptive.core.runtime.selection.strategies.{LeastDataSelectionStrategy, LoessInterpolationSelectionStrategy, LowRunAwareSelectionStrategy, SelectionStrategy}

/**
  * Created by Petr Kubat on 5/1/17.
  *
  * A block that uses the [[scalaadaptive.core.runtime.selection.strategies.LoessInterpolationSelectionStrategy]]
  * as the input-based strategy.
  *
  * The strategy itself is wrapped inside a
  * [[scalaadaptive.core.runtime.selection.strategies.LowRunAwareSelectionStrategy]]. Its argument lowRunLimit can be
  * overriden.
  *
  */
trait LoessInterpolationInputBasedStrategy extends BaseLongConfiguration
  with BlockWithLowRunLimit {
  override def createInputBasedStrategy(log: Logger): SelectionStrategy[Long] =
    new LowRunAwareSelectionStrategy[Long](
      log,
      new LeastDataSelectionStrategy[Long](log),
      new LoessInterpolationSelectionStrategy[Long](log),
      lowRunLimit)
}
