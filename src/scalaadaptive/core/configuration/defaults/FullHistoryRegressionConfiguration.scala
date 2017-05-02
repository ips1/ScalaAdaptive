package scalaadaptive.core.configuration.defaults

import scalaadaptive.core.configuration.BaseLongConfiguration
import scalaadaptive.core.configuration.blocks._

/**
  * Created by pk250187 on 5/2/17.
  */
object FullHistoryRegressionConfiguration
  extends BaseLongConfiguration
    with BestAverageSelection
    with RunTimeMeasurement
    with DefaultPath
    with BufferedSerialization
    // Temporary change:
    with NoGrouping
