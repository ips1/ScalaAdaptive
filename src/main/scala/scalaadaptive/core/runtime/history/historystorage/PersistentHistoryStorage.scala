package scalaadaptive.core.runtime.history.historystorage

import scalaadaptive.core.references.FunctionReference
import scalaadaptive.core.runtime.history.runhistory.RunHistory
import scalaadaptive.core.runtime.history.serialization.HistorySerializer
import scalaadaptive.core.runtime.history.HistoryKey
import scalaadaptive.core.runtime.history.rundata.RunData

/**
  * Created by pk250187 on 4/23/17.
  */
class PersistentHistoryStorage[TMeasurement](private val localHistory: HistoryStorage[TMeasurement],
                                             private val historySerializer: HistorySerializer[TMeasurement])
  extends HistoryStorage[TMeasurement] {

  override def getHistory(key: HistoryKey): RunHistory[TMeasurement] = {
    val hasHistory = localHistory.hasHistory(key)

    if (!hasHistory) {
      val loaded = historySerializer.deserializeHistory(key)

      loaded match {
        case Some(data) => {
          data.foreach(item => localHistory.applyNewRun(key, item))
        }
        case _ =>
      }
    }

    localHistory.getHistory(key)
  }

  override def applyNewRun(key: HistoryKey, run: RunData[TMeasurement]): Unit = {
    historySerializer.serializeNewRun(key, run)
    localHistory.applyNewRun(key, run)
  }

  override def hasHistory(key: HistoryKey): Boolean = localHistory.hasHistory(key)

  override def getKeysForFunction(function: FunctionReference): Iterable[HistoryKey] =
    localHistory.getKeysForFunction(function)

  /** Removes all runs corresponding to the history key */
  override def flushHistory(key: HistoryKey): Unit = {
    historySerializer.removeHistory(key)
    localHistory.flushHistory(key)
  }
}
