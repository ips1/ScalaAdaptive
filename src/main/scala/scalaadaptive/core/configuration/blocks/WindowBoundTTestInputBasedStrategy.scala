package scalaadaptive.core.configuration.blocks

import scalaadaptive.core.configuration.BaseLongConfiguration
import scalaadaptive.core.configuration.blocks.helper.{BlockWithAlpha, BlockWithLowRunLimit, BlockWithWindowAverageSize}
import scalaadaptive.core.logging.Logger
import scalaadaptive.core.runtime.selection._
import scalaadaptive.core.runtime.selection.strategies._
import scalaadaptive.core.runtime.selection.strategies.support.AverageForSampleCountProvider
import scalaadaptive.math.WelchTTestRunner

/**
  * Created by Petr Kubat on 7/8/17.
  */
trait WindowBoundTTestInputBasedStrategy extends BaseLongConfiguration
  with BlockWithWindowAverageSize
  with BlockWithAlpha
  with BlockWithLowRunLimit {

  override def createInputBasedStrategy(log: Logger): SelectionStrategy[Long] = {
    val tTestRunner = new WelchTTestRunner(log)
    val leastDataSelector = new LeastDataSelectionStrategy[Long](log)
    new LowRunAwareSelectionStrategy[Long](
      log,
      leastDataSelector,
      new WindowBoundSelectionStrategy[Long](
        log,
        new AverageForSampleCountProvider(windowAverageSize),
        new TTestSelectionStrategy(log, tTestRunner, leastDataSelector, alpha)
      ),
      lowRunLimit
    )
  }
}
