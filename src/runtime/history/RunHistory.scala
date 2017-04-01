package runtime.history

import references.FunctionReference

/**
  * Created by pk250187 on 3/21/17.
  */
trait RunHistory[TMeasurement] {
  def reference: FunctionReference
  def runCount: Int
  def average(implicit num: Numeric[TMeasurement]): Double
  def applyNewRun(runResult: RunData[TMeasurement]): Unit
}
