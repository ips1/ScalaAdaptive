package scalaadaptive.core.configuration.defaults

import scalaadaptive.core.configuration.BaseLongConfiguration
import scalaadaptive.core.configuration.blocks._

/**
  * Created by pk250187 on 5/2/17.
  */
class FullHistoryTTestConfiguration
  extends BaseLongConfiguration
    with TTestSelection
    with RunTimeMeasurement
    with DefaultPath
    with BufferedSerialization
    with InterpolationSelection
    with CachedStatisticsStorage