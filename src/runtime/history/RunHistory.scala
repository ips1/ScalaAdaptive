package runtime.history

import references.FunctionReference

import scala.collection.mutable.ArrayBuffer

/**
  * Created by pk250187 on 3/21/17.
  */
class RunHistory[TMeasurement](val reference: FunctionReference, val runItems: ArrayBuffer[RunData[TMeasurement]]) {
  def runCount: Int = runItems.size
  def applyNewRun(runResult: RunData[TMeasurement]): Unit = runItems.append(runResult)
}
