package scalaadaptive.core.runtime.history.historystorage

import scala.collection.mutable
import scalaadaptive.core.runtime.history.runhistory.RunHistory
import scalaadaptive.core.runtime.history.HistoryKey
import scalaadaptive.core.runtime.history.rundata.RunData

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

  override def applyNewRun(key: HistoryKey, run: RunData[TMeasurement]): Unit = {
    val oldHistory = histories.getOrElseUpdate(key, newHistoryFactory(key))
    histories.update(key, oldHistory.applyNewRun(run))
  }
}
