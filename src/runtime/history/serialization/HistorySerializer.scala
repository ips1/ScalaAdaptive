package runtime.history.serialization

import references.FunctionReference
import runtime.history.{FullRunHistory, HistoryKey, RunData}

/**
  * Created by pk250187 on 4/23/17.
  */
trait HistorySerializer[TMeasurement] {
  def serializeNewRun(key: HistoryKey, run: RunData[TMeasurement])
  def deserializeHistory(key: HistoryKey): Option[FullRunHistory[TMeasurement]]
}
