package scalaadaptive.core.runtime.history.serialization

import scalaadaptive.core.functions.identifiers.FunctionIdentifier
import scalaadaptive.core.runtime.history.HistoryKey
import scalaadaptive.core.runtime.history.evaluation.data.EvaluationData

/**
  * Created by Petr Kubat on 4/23/17.
  *
  * A class that handles the persistence of [[scalaadaptive.core.runtime.history.runhistory.RunHistory]] data. It is
  * used by the [[scalaadaptive.core.runtime.history.historystorage.PersistentHistoryStorage]] wrapper fo the
  * [[scalaadaptive.core.runtime.history.historystorage.HistoryStorage]] implementations to save and load the individual
  * records.
  *
  */
trait HistorySerializer[TMeasurement] {
  /**
    * Serializes and persists one new run evaluation of a function.
    * @param key The key of the function.
    * @param run The run evaluation data to persist.
    */
  def serializeNewRun(key: HistoryKey, run: EvaluationData[TMeasurement]): Unit =
    serializeMultipleRuns(key, List(run))

  /**
    * Serializes and persists multiple new run evaluations of a function.
    * @param key The key of the function.
    * @param runs The run evaluations to persist.
    */
  def serializeMultipleRuns(key: HistoryKey, runs: Seq[EvaluationData[TMeasurement]])

  /**
    * Tries to locate and deserialize all evaluation records for given function.
    * @param key The key of the function.
    * @return Either the sequence of persisted evaluation data or None if none were located.
    */
  def deserializeHistory(key: HistoryKey): Option[Seq[EvaluationData[TMeasurement]]]

  /**
    * Removes all persisted evaluation records for given function.
    * @param key The key of the function.
    */
  def removeHistory(key: HistoryKey): Unit
}
