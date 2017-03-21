package performance

import runtime.history.RunData

/**
  * Created by pk250187 on 3/19/17.
  */
class RunTimeProvider extends PerformanceProvider[Long] {
  override def measureFunctionRun[TOut](fun: () => TOut): (TOut, RunData[Long]) = {
    val startTime = System.nanoTime()
    val result = fun()
    val elapsed = System.nanoTime() - startTime
    (result, new RunData(elapsed))
  }
}
