package scalaadaptive.core.runtime.selection.strategies.support

import scalaadaptive.core.runtime.history.runhistory.RunHistory

/**
  * Created by Petr Kubat on 6/8/17.
  */
trait WindowSizeProvider[TMeasurement] {
  def selectWindowSize(runHistory: RunHistory[TMeasurement]): Int
}
