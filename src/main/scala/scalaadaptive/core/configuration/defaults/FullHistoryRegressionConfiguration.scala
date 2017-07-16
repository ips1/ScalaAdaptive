package scalaadaptive.core.configuration.defaults

import scalaadaptive.core.configuration.BaseLongConfiguration
import scalaadaptive.core.configuration.blocks._

/**
  * Created by Petr Kubat on 6/26/17.
  */
class FullHistoryRegressionConfiguration
  extends BaseLongConfiguration
    with TTestMeanBasedStrategy
    with RunTimeMeasurement
    with DefaultHistoryPath
    with BufferedSerialization
    with WindowBoundRegressionInputBasedStrategy
