package scalaadaptive.core.runtime.selection

import scalaadaptive.core.functions.RunResult
import scalaadaptive.core.functions.identifiers.FunctionIdentifier
import scalaadaptive.core.runtime.invocationtokens.InvocationTokenWithCallbacks

/**
  * Created by Petr Kubat on 3/26/17.
  *
  * A module that is used to select and invoke one of multiple options passed in.
  *
  */
trait AdaptiveSelector {
  /**
    * Selects and invokes one of given function options. Evaluates its run.
    *
    * @param input The input of the selection process, see [[SelectionInput]]
    * @return The result of the invocation containing the return value, see [[scalaadaptive.core.functions.RunResult]]
    */
  def selectAndRun[TArgType, TReturnType](input: SelectionInput[TArgType, TReturnType]): RunResult[TReturnType]

  /**
    * Selects and invokes one of given function options with delayed measure. Does not evaluate the function run
    * and return an [[scalaadaptive.api.functions.InvocationToken]] with a backward link to the selection instead.
    *
    * @param input The input of the selection process, see [[SelectionInput]]
    * @return The result of the invocation containing the return value, see [[scalaadaptive.core.functions.RunResult]],
    *         and an [[scalaadaptive.api.functions.InvocationToken]] to be used for feedback measurements of
    *         the function that was influenced by the selection.
    */
  def selectAndRunWithDelayedMeasure[TArgType, TReturnType](input: SelectionInput[TArgType, TReturnType]): (TReturnType, InvocationTokenWithCallbacks)

  /**
    * Invokes the function with least data records available. Evaluates its run.
    *
    * @param input The input data, only fields relevant to the gathering are used, see [[SelectionInput]]
    * @return The result of the invocation containing the return value, see [[scalaadaptive.core.functions.RunResult]]
    */
  def gatherData[TArgType, TReturnType](input: SelectionInput[TArgType, TReturnType]): RunResult[TReturnType]

  /**
    * Invokes the function with least data records available. Does not evaluate the function run
    * and return an [[scalaadaptive.api.functions.InvocationToken]] with a backward link to the gathering instead.
    *
    * @param input The input data, only fields relevant to the gathering are used, see [[SelectionInput]]
    * @return The result of the invocation containing the return value, see [[scalaadaptive.core.functions.RunResult]],
    *         and an [[scalaadaptive.api.functions.InvocationToken]] to be used for feedback measurements of the
    *         function that was influenced by the gathering.
    */
  def gatherDataWithDelayedMeasure[TArgType, TReturnType](input: SelectionInput[TArgType, TReturnType]): (TReturnType, InvocationTokenWithCallbacks)

  /**
    * Flushes the entire run history for a function with given identifier.
    * @param function The identifier of the function.
    */
  def flushHistory(function: FunctionIdentifier)
}
