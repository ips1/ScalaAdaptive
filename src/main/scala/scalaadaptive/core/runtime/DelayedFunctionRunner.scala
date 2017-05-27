package scalaadaptive.core.runtime

import scalaadaptive.core.performance.PerformanceTracker
import scalaadaptive.core.runtime.history.HistoryKey

/**
  * Created by pk250187 on 5/21/17.
  */
trait DelayedFunctionRunner {
  def runMeasuredFunction[TReturnType](fun: () => TReturnType,
                                       key: HistoryKey,
                                       inputDescriptor: Option[Long],
                                       tracker: PerformanceTracker): RunResult[TReturnType]
}
