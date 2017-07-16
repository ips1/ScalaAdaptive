package scalaadaptive.core.runtime.selection.support

import scalaadaptive.core.runtime.history.runhistory.RunHistory

/**
  * Created by Petr Kubat on 6/8/17.
  */
class FixedSizeProvider[TMeasurement](val size: Int) extends WindowSizeProvider[TMeasurement] {
  override def selectWindowSize(runHistory: RunHistory[TMeasurement]): Int = size
}
