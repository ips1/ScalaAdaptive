package runtime.history

import references.FunctionReference

import scala.collection.mutable.ArrayBuffer

/**
  * Created by pk250187 on 3/21/17.
  */
class FullRunHistory[TMeasurement](val reference: FunctionReference,
                                   val runItems: ArrayBuffer[RunData[TMeasurement]])
                                  (implicit num: Numeric[TMeasurement]) extends RunHistory[TMeasurement] {
  private var sum: TMeasurement = num.zero

  override def runCount: Int = runItems.size
  override def applyNewRun(runResult: RunData[TMeasurement]): Unit = {
    sum = num.plus(sum, runResult.measurement)
    runItems.append(runResult)
  }

  override def average(): Double = num.toDouble(sum) / runItems.size
}
