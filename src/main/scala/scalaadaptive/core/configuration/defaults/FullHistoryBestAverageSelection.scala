package scalaadaptive.core.configuration.defaults

import scalaadaptive.core.configuration.BaseLongConfiguration
import scalaadaptive.core.configuration.blocks._

/**
  * Created by pk250187 on 5/1/17.
  */
class FullHistoryBestAverageSelection
  extends BaseLongConfiguration
    with BestAverageSelection
    with RunTimeMeasurement
    with DefaultHistoryPath
    with BufferedSerialization
    with LoessInterpolationInputBasedStrategy
