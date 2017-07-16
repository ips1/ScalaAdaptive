package scalaadaptive.core.runtime.history.historystorage

import scalaadaptive.core.functions.identifiers.FunctionIdentifier
import scalaadaptive.core.runtime.history.runhistory.RunHistory
import scalaadaptive.core.runtime.history.HistoryKey
import scalaadaptive.core.runtime.history.evaluation.data.EvaluationData

/**
  * Created by Petr Kubat on 3/21/17.
  *
  * A general storage for [[scalaadaptive.core.runtime.history.runhistory.RunHistory]] instances for all the functions
  * in the application.
  *
  */
trait HistoryStorage[TMeasurement] {
  /** Retrieves run history from the storage for a specified key */
  def getHistory(key: HistoryKey): RunHistory[TMeasurement]

  /** Retrieves all history keys present in the storage that contain the specified function. */
  def getKeysForFunction(function: FunctionIdentifier): Iterable[HistoryKey]

  /** Finds out whether the specified history exists in the storage */
  def hasHistory(key: HistoryKey): Boolean

  /** Applies new run to the history specified by key in the storage. Not thread-safe in general. */
  def applyNewRun(key: HistoryKey, run: EvaluationData[TMeasurement]): Unit

  /** Removes all runs corresponding to the history key */
  def flushHistory(key: HistoryKey): Unit

  /** Removes all runs corresponding to the function */
  def flushHistory(function: FunctionIdentifier): Unit = getKeysForFunction(function).foreach(flushHistory)
}
