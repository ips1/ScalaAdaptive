package scalaadaptive.core.runtime.invocationtokens

/**
  * Created by pk250187 on 4/23/17.
  */
trait InvocationToken {
  def apply[TReturnValue](fun: () => TReturnValue): TReturnValue = runMeasuredFunction(fun)
  def runMeasuredFunction[TReturnValue](fun: () => TReturnValue): TReturnValue
}
