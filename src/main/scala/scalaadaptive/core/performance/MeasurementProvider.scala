package scalaadaptive.core.performance

import scalaadaptive.core.runtime.history.rundata.RunData

/**
  * Created by pk250187 on 3/19/17.
  */
trait MeasurementProvider[TMeasurement] {
  def measureFunctionRun[TOut](fun: () => TOut) : (TOut, TMeasurement)
}
