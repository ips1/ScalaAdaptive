package scalaadaptive.core.runtime.history

/**
  * Created by pk250187 on 3/21/17.
  */
trait HistoryStorage[TMeasurement] {
  def getHistory(key: HistoryKey): RunHistory[TMeasurement]
  def applyNewRun(key: HistoryKey, run: RunData[TMeasurement])
}
