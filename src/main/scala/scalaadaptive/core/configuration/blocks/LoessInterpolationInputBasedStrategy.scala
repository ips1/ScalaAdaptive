package scalaadaptive.core.configuration.blocks

import scalaadaptive.core.configuration.BaseLongConfiguration
import scalaadaptive.core.configuration.blocks.helper.{BlockWithAlpha, BlockWithLowRunLimit}
import scalaadaptive.core.logging.Logger
import scalaadaptive.core.runtime.selection._
import scalaadaptive.core.runtime.selection.strategies.{LeastDataSelectionStrategy, LoessInterpolationSelectionStrategy, LowRunAwareSelectionStrategy, SelectionStrategy}

/**
  * Created by Petr Kubat on 5/1/17.
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
