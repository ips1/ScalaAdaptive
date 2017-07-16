package scalaadaptive.core.runtime.selection.strategies.support

import scalaadaptive.core.runtime.history.runhistory.RunHistory

/**
  * Created by Petr Kubat on 6/8/17.
  *
  * A provider of window size for given history. Can be either fixed (see [[FixedSizeProvider]]) or dynamic
  * (see [[AverageForSampleCountProvider]]).
  *
  */
trait WindowSizeProvider[TMeasurement] {
  /**
    * Selects the size of a window (width of the entire window, not just a radius).
    * @param runHistory The history to select the window for.
    * @return The size of the window.
    */
  def selectWindowSize(runHistory: RunHistory[TMeasurement]): Int
}
