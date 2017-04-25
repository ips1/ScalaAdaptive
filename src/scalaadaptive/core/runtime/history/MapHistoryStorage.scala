package scalaadaptive.core.runtime.history

import scala.collection.mutable

/**
  * Created by pk250187 on 3/21/17.
  */
class MapHistoryStorage[TMeasurement](val newHistoryFactory: (HistoryKey) => RunHistory[TMeasurement])
  extends HistoryStorage[TMeasurement] {
  private val histories: mutable.HashMap[HistoryKey, RunHistory[TMeasurement]] =
    new mutable.HashMap[HistoryKey, RunHistory[TMeasurement]]()

  def hasHistory(key: HistoryKey): Boolean = histories.contains(key)
  def addHistory(key: HistoryKey, history: RunHistory[TMeasurement]): Unit = histories.put(key, history)

  override def getHistory(key: HistoryKey): RunHistory[TMeasurement] =
    histories.getOrElseUpdate(key, newHistoryFactory(key))

  override def applyNewRun(key: HistoryKey, run: RunData[TMeasurement]): Unit = {
    val oldHistory = histories.getOrElseUpdate(key, newHistoryFactory(key))
    histories.update(key, oldHistory.applyNewRun(run))
  }
}
