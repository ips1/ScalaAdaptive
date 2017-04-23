package runtime.history

import references.FunctionReference

import scala.collection.mutable.ArrayBuffer

/**
  * Created by pk250187 on 3/21/17.
  */
class FullRunHistory[TMeasurement](val key: HistoryKey,
                                   val runItems: ArrayBuffer[RunData[TMeasurement]])
                                  (implicit num: Numeric[TMeasurement]) extends RunHistory[TMeasurement] {
  // TODO: remove runItems from ctor OR add sum to ctor - inconsistency!!!
  private var sum: TMeasurement = num.zero

  override def reference: FunctionReference = key.function
  override def runCount: Int = runItems.size
  override def applyNewRun(runResult: RunData[TMeasurement]): FullRunHistory[TMeasurement] = {
    sum = num.plus(sum, runResult.measurement)
    runItems.append(runResult)
    this
  }

  override def average(): Double = num.toDouble(sum) / runItems.size
  override def best(): Double = num.toDouble(runItems.map(_.measurement).min)
}
