package scalaadaptive.core.runtime.selection.strategies.support

import scalaadaptive.core.runtime.history.runhistory.RunHistory

/**
  * Created by Petr Kubat on 6/8/17.
  *
  * A [[WindowSizeProvider]] that generates the window using an average distance d of two input descriptors in the run
  * history. The window size is then averageSamplesPerWindow * d.
  *
  * Returns Int.MaxValue in case the window size cannot be derived. Returns 1 if the window size should be lower than 1.
  *
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
