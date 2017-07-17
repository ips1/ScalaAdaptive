package scalaadaptive.core.configuration.defaults

import scalaadaptive.core.configuration.BaseLongConfiguration
import scalaadaptive.core.configuration.blocks._
import scalaadaptive.core.configuration.blocks.logging.NoLogging
import scalaadaptive.core.configuration.blocks.persistence.BufferedPersistence
import scalaadaptive.core.configuration.blocks.selection.{TTestMeanBasedStrategy, WindowBoundRegressionInputBasedStrategy}
import scalaadaptive.core.configuration.blocks.history.CachedStatisticsHistory

/**
  * Created by Petr Kubat on 7/3/17.
  *
  * The default configuration used to initialize the ScalaAdaptive framework. Build atop of
  * [[scalaadaptive.core.configuration.BaseLongConfiguration]], with more specific behavior set using
  * [[scalaadaptive.core.configuration.blocks]]. See the corresponding blocks for more information.
  *
  * Note that the class can be extended and different blocks can be mixed-in, overriding the current ones.
  *
  */
class DefaultConfiguration extends BaseLongConfiguration
  with RunTimeMeasurement
  with TTestMeanBasedStrategy
  with WindowBoundRegressionInputBasedStrategy
  with CachedStatisticsHistory
  with BufferedPersistence
  with NoLogging

