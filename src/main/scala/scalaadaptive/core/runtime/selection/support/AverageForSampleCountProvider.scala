package scalaadaptive.core.runtime.selection.support

import scalaadaptive.core.runtime.history.runhistory.RunHistory

/**
  * Created by pk250187 on 6/8/17.
  */
class AverageForSampleCountProvider[TMeasurement](val averageSamplesPerWindow: Int) extends WindowSizeProvider[TMeasurement] {
  override def selectWindowSize(runHistory: RunHistory[TMeasurement]): Int = {
    if (runHistory.minDescriptor.isEmpty || runHistory.maxDescriptor.isEmpty) {
      return Int.MaxValue
    }

    val range = runHistory.maxDescriptor.get - runHistory.minDescriptor.get

    val averageDistance = range.toDouble / runHistory.runCount

    Math.max((averageDistance * averageSamplesPerWindow).toInt, 1)
  }
}
