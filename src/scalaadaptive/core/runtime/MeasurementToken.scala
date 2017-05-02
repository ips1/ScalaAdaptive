package scalaadaptive.core.runtime

/**
  * Created by pk250187 on 4/23/17.
  */
trait MeasurementToken {
  def apply[TReturnValue](fun: () => TReturnValue): TReturnValue = runMeasuredFunction(fun)
  def runMeasuredFunction[TReturnValue](fun: () => TReturnValue): TReturnValue
}
