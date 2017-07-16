package scalaadaptive.core.configuration.blocks

import scalaadaptive.core.configuration.BaseLongConfiguration
import scalaadaptive.core.configuration.blocks.helper.{BlockWithAlpha, BlockWithLowRunLimit}
import scalaadaptive.core.logging.Logger
import scalaadaptive.core.runtime.selection._
import scalaadaptive.math.{MannWhitneyUTestRunner, WelchTTestRunner}

/**
  * Created by Petr Kubat on 5/2/17.
  */
trait UTestMeanBasedStrategy extends BaseLongConfiguration
  with BlockWithLowRunLimit
  with BlockWithAlpha {
  override val createMeanBasedStrategy: (Logger) => SelectionStrategy[Long] =
    (log: Logger) => {
      val tTestRunner = new MannWhitneyUTestRunner(log)
      val leastDataSelector = new LeastDataSelectionStrategy[Long](log)
      new LowRunAwareSelectionStrategy[Long](
        log,
        leastDataSelector,
        new UTestSelectionStrategy(log, tTestRunner, leastDataSelector, alpha),
        lowRunLimit)
    }
}
