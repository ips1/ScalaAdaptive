package scalaadaptive.core.configuration.defaults

import scalaadaptive.core.configuration.BaseLongConfiguration
import scalaadaptive.core.configuration.blocks._

/**
  * Created by pk250187 on 7/3/17.
  */
class DefaultConfiguration extends BaseLongConfiguration
  with RunTimeMeasurement
  with TTestNonPredictiveStrategy
  with LimitedRegressionPredictiveStrategy
  with CachedStatisticsStorage
  with DefaultHistoryPath
  with BufferedSerialization
  with NoLogging

