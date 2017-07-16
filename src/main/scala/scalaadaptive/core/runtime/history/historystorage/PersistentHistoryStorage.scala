package scalaadaptive.core.runtime.history.historystorage

import scalaadaptive.core.functions.identifiers.FunctionIdentifier
import scalaadaptive.core.runtime.history.runhistory.RunHistory
import scalaadaptive.core.runtime.history.serialization.HistorySerializer
import scalaadaptive.core.runtime.history.HistoryKey
import scalaadaptive.core.runtime.history.evaluation.data.EvaluationData

/**
  * Created by Petr Kubat on 4/23/17.
  *
  * A wrapper of [[HistoryStorage]] that uses a [[scalaadaptive.core.runtime.history.serialization.HistorySerializer]]
  * instance to to serialize all newly added runs before delegating the call to the internal [[HistoryStorage]].
  *
  * In addition, when retrieving a history, checks first whether a local history exists in the internal
  * [[HistoryStorage]] and if not, tries to deserialize it using the
  * [[scalaadaptive.core.runtime.history.serialization.HistorySerializer]]. If that fails too, delegates the call to
  * the internal [[HistoryStorage]], which should create the record.
  *
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

  override def applyNewRun(key: HistoryKey, run: EvaluationData[TMeasurement]): Unit = {
    historySerializer.serializeNewRun(key, run)
    localHistory.applyNewRun(key, run)
  }

  override def hasHistory(key: HistoryKey): Boolean = localHistory.hasHistory(key)

  override def getKeysForFunction(function: FunctionIdentifier): Iterable[HistoryKey] =
    localHistory.getKeysForFunction(function)

  /** Removes all runs corresponding to the history key */
  override def flushHistory(key: HistoryKey): Unit = {
    historySerializer.removeHistory(key)
    localHistory.flushHistory(key)
  }
}
