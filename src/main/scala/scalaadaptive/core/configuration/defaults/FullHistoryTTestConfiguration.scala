package scalaadaptive.core.configuration.defaults

import scalaadaptive.core.configuration.BaseLongConfiguration
import scalaadaptive.core.configuration.blocks._

/**
  * Created by Petr Kubat on 5/2/17.
  */
class FullHistoryTTestConfiguration
  extends BaseLongConfiguration
    with TTestMeanBasedStrategy
    with RunTimeMeasurement
    with DefaultHistoryPath
    with BufferedSerialization
    with LoessInterpolationInputBasedStrategy
    with CachedStatisticsStorage