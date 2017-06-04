package scalaadaptive.core.runtime.history.historystorage

import scala.collection.mutable
import scalaadaptive.core.references.FunctionReference
import scalaadaptive.core.runtime.history.runhistory.RunHistory
import scalaadaptive.core.runtime.history.HistoryKey
import scalaadaptive.core.runtime.history.evaluation.data.EvaluationData

/**
  * Created by pk250187 on 3/21/17.
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

  override def getKeysForFunction(function: FunctionReference): Iterable[HistoryKey] =
    histories.keys.filter(p => p.function == function)

  /** Removes all runs corresponding to the history key */
  override def flushHistory(key: HistoryKey): Unit =
    histories.remove(key)
}
