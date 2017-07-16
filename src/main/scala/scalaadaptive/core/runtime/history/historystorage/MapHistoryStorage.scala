package scalaadaptive.core.runtime.history.historystorage

import scala.collection.mutable
import scalaadaptive.core.functions.identifiers.FunctionIdentifier
import scalaadaptive.core.runtime.history.runhistory.RunHistory
import scalaadaptive.core.runtime.history.HistoryKey
import scalaadaptive.core.runtime.history.evaluation.data.EvaluationData

/**
  * Created by Petr Kubat on 3/21/17.
  *
  * An implementation of [[HistoryStorage]] that uses mutable map to store the
  * [[scalaadaptive.core.runtime.history.runhistory.RunHistory]] instances.
  *
  */
class MapHistoryStorage[TMeasurement](val newHistoryFactory: (HistoryKey) => RunHistory[TMeasurement])
  extends HistoryStorage[TMeasurement] {
  private val histories: mutable.HashMap[HistoryKey, RunHistory[TMeasurement]] =
    new mutable.HashMap[HistoryKey, RunHistory[TMeasurement]]()

  override def hasHistory(key: HistoryKey): Boolean = histories.contains(key)

  override def getHistory(key: HistoryKey): RunHistory[TMeasurement] =
    histories.getOrElseUpdate(key, newHistoryFactory(key))

  override def applyNewRun(key: HistoryKey, run: EvaluationData[TMeasurement]): Unit = {
    val oldHistory = histories.getOrElseUpdate(key, newHistoryFactory(key))
    histories.update(key, oldHistory.applyNewRun(run))
  }

  override def getKeysForFunction(function: FunctionIdentifier): Iterable[HistoryKey] =
    histories.keys.filter(p => p.functionId == function)

  /** Removes all runs corresponding to the history key */
  override def flushHistory(key: HistoryKey): Unit =
    histories.remove(key)
}
