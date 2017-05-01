package scalaadaptive.core.configuration.defaults

import scalaadaptive.core.configuration.BaseLongConfiguration

/**
  * Created by pk250187 on 3/26/17.
  */
object FullHistoryInterpolationConfiguration
  extends BaseLongConfiguration
    with InterpolationSelection
    with RunTimeMeasurement
    with DefaultPath
    with BufferedSerialization
    // Temporary change:
    with NoGrouping
