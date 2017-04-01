package runtime.selection

import runtime.history.RunHistory

/**
  * Created by pk250187 on 3/19/17.
  */
trait RunSelector[TMeasurement] {
  def selectOption(records: Seq[RunHistory[TMeasurement]]): RunHistory[TMeasurement]
}
