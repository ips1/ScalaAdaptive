package scalaadaptive.core.runtime.history.historystorage

import scalaadaptive.core.runtime.history.runhistory.RunHistory
import scalaadaptive.core.runtime.history.{HistoryKey, RunData}

/**
  * Created by pk250187 on 3/21/17.
  */
trait HistoryStorage[TMeasurement] {
  def getHistory(key: HistoryKey): RunHistory[TMeasurement]
  def hasHistory(key: HistoryKey): Boolean
  def applyNewRun(key: HistoryKey, run: RunData[TMeasurement])
}
