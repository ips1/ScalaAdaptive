package performance

import runtime.history.RunData

/**
  * Created by pk250187 on 3/19/17.
  */
trait PerformanceProvider[TMeasurement] {
  def measureFunctionRun[TOut](fun: () => TOut) : (TOut, TMeasurement)
}
