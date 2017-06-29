package scalaadaptive.core.functions
import scalaadaptive.api.adaptors.InvocationToken

/**
  * Created by pk250187 on 6/29/17.
  */
trait CombinedFunctionInvoker {

  def invoke[TArgType, TRetType](function: CombinedFunction[TArgType, TRetType], arguments: TArgType): TRetType

  def invokeWithDelayedMeasure[TArgType, TRetType](function: CombinedFunction[TArgType, TRetType],
                                                   arguments: TArgType): (TRetType, InvocationToken)

  def train[TArgType, TRetType](function: CombinedFunction[TArgType, TRetType], dataSet: Seq[TArgType]): Unit

  def flushHistory[TArgType, TRetType](function: CombinedFunction[TArgType, TRetType]): Unit
}
