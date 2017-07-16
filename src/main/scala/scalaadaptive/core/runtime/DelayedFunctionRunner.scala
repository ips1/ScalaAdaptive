package scalaadaptive.core.runtime

import scalaadaptive.core.performance.PerformanceTracker
import scalaadaptive.core.runtime.history.HistoryKey
import scalaadaptive.core.functions.RunResult

/**
  * Created by Petr Kubat on 5/21/17.
  */
trait DelayedFunctionRunner {
  def runMeasuredFunction[TArgType, TReturnType](fun: (TArgType) => TReturnType,
                                                 arguments: TArgType,
                                                 key: HistoryKey,
                                                 inputDescriptor: Option[Long],
                                                 tracker: PerformanceTracker): RunResult[TReturnType]
}
