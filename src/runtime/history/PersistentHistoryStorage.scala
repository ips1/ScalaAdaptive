package runtime.history

import runtime.history.serialization.HistorySerializer

/**
  * Created by pk250187 on 4/23/17.
  */
class PersistentHistoryStorage[TMeasurement](private val localHistory: MapHistoryStorage[TMeasurement],
                                             private val historySerializer: HistorySerializer[TMeasurement])
  extends HistoryStorage[TMeasurement] {

  override def getHistory(key: HistoryKey): RunHistory[TMeasurement] = {
    if (localHistory.hasHistory(key)) {
      return localHistory.getHistory(key)
    }
    val loaded = historySerializer.deserializeHistory(key)

    loaded match {
      case Some(history) => {
        localHistory.addHistory(history.key, history)
        return history
      }
      case _ => localHistory.getHistory(key)
    }
  }

  override def applyNewRun(key: HistoryKey, run: RunData[TMeasurement]): Unit = {
    historySerializer.serializeNewRun(key, run)
    localHistory.applyNewRun(key, run)
  }
}
