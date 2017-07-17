package scalaadaptive.core.runtime.selection

import scalaadaptive.core.functions.RunResult
import scalaadaptive.core.performance.PerformanceTracker
import scalaadaptive.core.runtime.history.HistoryKey

/**
  * Created by Petr Kubat on 5/21/17.
  *
  * A module that is used to execute functions with delayed measurement.
  *
  */
trait DelayedFunctionRunner {
  /**
    * Executes certain function (normal, not adapted) with given arguments, evaluates its run and stores the result
    * to the history of the function determined by a [[scalaadaptive.core.runtime.history.HistoryKey]] key.
    *
    * @param fun Function to execute.
    * @param arguments Arguments of the function.
    * @param key The key of the adaptive function that this execution provides evaluation for.
    * @param inputDescriptor The input descriptor of the original adaptive function.
    * @param tracker The tracker that is used to track performance connected with the original adaptive function.
    * @tparam TArgType Tupled argument type of the executed function.
    * @tparam TReturnType Return type of the executed function.
    * @return The [[RunResult]] of the function that gets executed containing data from the tracker.
    */
  def runMeasuredFunction[TArgType, TReturnType](fun: (TArgType) => TReturnType,
                                                 arguments: TArgType,
                                                 key: HistoryKey,
                                                 inputDescriptor: Option[Long],
                                                 tracker: PerformanceTracker): RunResult[TReturnType]
}
