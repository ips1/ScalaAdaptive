package scalaadaptive.core.runtime.history.serialization

import scalaadaptive.core.references.FunctionReference
import scalaadaptive.core.runtime.history.HistoryKey
import scalaadaptive.core.runtime.history.rundata.RunData

/**
  * Created by pk250187 on 4/23/17.
  */
trait HistorySerializer[TMeasurement] {
  def serializeNewRun(key: HistoryKey, run: RunData[TMeasurement]): Unit =
    serializeMultipleRuns(key, List(run))
  def serializeMultipleRuns(key: HistoryKey, runs: Seq[RunData[TMeasurement]])
  def deserializeHistory(key: HistoryKey): Option[Seq[RunData[TMeasurement]]]
  def removeHistory(key: HistoryKey): Unit
}
