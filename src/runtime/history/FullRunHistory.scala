package runtime.history

import references.FunctionReference

import scala.collection.mutable.ArrayBuffer

/**
  * Created by pk250187 on 3/21/17.
  */
class FullRunHistory[TMeasurement](val reference: FunctionReference,
                                   val runItems: ArrayBuffer[RunData[TMeasurement]]) extends RunHistory[TMeasurement] {
  override def runCount: Int = runItems.size
  override def applyNewRun(runResult: RunData[TMeasurement]): Unit = runItems.append(runResult)

  override def average(implicit num: Numeric[TMeasurement]): Double =
    num.toDouble(runItems.map(_.measurement).sum) / runItems.size
}
