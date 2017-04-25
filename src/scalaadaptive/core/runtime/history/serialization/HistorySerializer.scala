package scalaadaptive.core.runtime.history.serialization

import scalaadaptive.core.references.FunctionReference
import scalaadaptive.core.runtime.history.{FullRunHistory, HistoryKey, RunData}

/**
  * Created by pk250187 on 4/23/17.
  */
trait HistorySerializer[TMeasurement] {
  def serializeNewRun(key: HistoryKey, run: RunData[TMeasurement])
  def deserializeHistory(key: HistoryKey): Option[FullRunHistory[TMeasurement]]
}
