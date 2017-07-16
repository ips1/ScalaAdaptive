package scalaadaptive.core.runtime.history.serialization

import scalaadaptive.core.functions.identifiers.FunctionIdentifier
import scalaadaptive.core.runtime.history.HistoryKey
import scalaadaptive.core.runtime.history.evaluation.data.EvaluationData

/**
  * Created by Petr Kubat on 4/23/17.
  */
trait HistorySerializer[TMeasurement] {
  def serializeNewRun(key: HistoryKey, run: EvaluationData[TMeasurement]): Unit =
    serializeMultipleRuns(key, List(run))
  def serializeMultipleRuns(key: HistoryKey, runs: Seq[EvaluationData[TMeasurement]])
  def deserializeHistory(key: HistoryKey): Option[Seq[EvaluationData[TMeasurement]]]
  def removeHistory(key: HistoryKey): Unit
}
