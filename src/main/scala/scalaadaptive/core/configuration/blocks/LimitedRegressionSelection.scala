package scalaadaptive.core.configuration.blocks

import scalaadaptive.core.configuration.BaseLongConfiguration
import scalaadaptive.core.logging.Logger
import scalaadaptive.core.runtime.selection.support.{AverageForSampleCountProvider, FixedSizeProvider}
import scalaadaptive.core.runtime.selection.{LeastDataSelectionStrategy, LimitedRegressionSelectionStategy, LowRunAwareSelectionStrategy, SelectionStrategy}
import scalaadaptive.math.RegressionTTestRunner

/**
  * Created by pk250187 on 6/7/17.
  */
trait LimitedRegressionSelection extends BaseLongConfiguration {
  override val createContinuousRunSelector: (Logger) => SelectionStrategy[Long] =
    (log: Logger) => new LowRunAwareSelectionStrategy[Long](
      log,
      new LeastDataSelectionStrategy[Long](log),
      new LimitedRegressionSelectionStategy[Long](
        log,
        new AverageForSampleCountProvider(50),
        //new FixedSizeSelector(1000),
        new RegressionTTestRunner,
        new LeastDataSelectionStrategy[Long](log),
        0.05),
      30)
}
