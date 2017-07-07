package scalaadaptive.core.configuration.defaults

import scalaadaptive.core.configuration.BaseLongConfiguration
import scalaadaptive.core.configuration.blocks._

/**
  * Created by pk250187 on 6/26/17.
  */
class FullHistoryRegressionConfiguration
  extends BaseLongConfiguration
    with TTestNonPredictiveStrategy
    with RunTimeMeasurement
    with DefaultHistoryPath
    with BufferedSerialization
    with LimitedRegressionPredictiveStrategy
