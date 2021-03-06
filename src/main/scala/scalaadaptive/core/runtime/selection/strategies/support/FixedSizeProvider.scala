package scalaadaptive.core.runtime.selection.strategies.support

import scalaadaptive.core.runtime.history.runhistory.RunHistory

/**
  * Created by Petr Kubat on 6/8/17.
  *
  * A [[WindowSizeProvider]] that always return fixed size.
  *
  * @param size The fixed size to be always returned.
  *
  */
class FixedSizeProvider[TMeasurement](val size: Int) extends WindowSizeProvider[TMeasurement] {
  override def selectWindowSize(runHistory: RunHistory[TMeasurement]): Int = size
}
