package scalaadaptive.core.runtime.selection.support

import scalaadaptive.core.runtime.history.runhistory.RunHistory

/**
  * Created by pk250187 on 6/8/17.
  */
trait WindowSizeProvider[TMeasurement] {
  def selectWindowSize(runHistory: RunHistory[TMeasurement]): Int
}
