package runtime.history

import references.FunctionReference

/**
  * Created by pk250187 on 3/21/17.
  */
trait RunHistory[TMeasurement] {
  def reference: FunctionReference
  def key: HistoryKey
  def runCount: Int
  def average(): Double
  def best(): Double
  def applyNewRun(runResult: RunData[TMeasurement]): RunHistory[TMeasurement]
}
