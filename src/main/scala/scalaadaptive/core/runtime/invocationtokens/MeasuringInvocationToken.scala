package scalaadaptive.core.runtime.invocationtokens

import scalaadaptive.core.functions.RunData
import scalaadaptive.core.performance.PerformanceTracker
import scalaadaptive.core.runtime.history.HistoryKey
import scalaadaptive.core.runtime.selection.DelayedFunctionRunner

/**
  * Created by Petr Kubat on 4/23/17.
  *
  * An invocation token that does the measurement by calling the provided
  * [[scalaadaptive.core.runtime.selection.DelayedFunctionRunner]] method runMeasuredFunction.
  *
  *
  * @param runner The [[scalaadaptive.core.runtime.selection.DelayedFunctionRunner]] that will be used to run the
  *               measured function.
  * @param inputDescriptor The input descriptor that was used to generate this invocation token (and will be stored
  *                        with the measured data).
  * @param key The [[scalaadaptive.core.runtime.history.HistoryKey]] of the original function that generated the
  *            invocation token.
  * @param tracker The [[scalaadaptive.core.performance.PerformanceTracker]] that will be used to track the invocation.
  *
  */
class MeasuringInvocationToken(private val runner: DelayedFunctionRunner,
                               private val inputDescriptor: Option[Long],
                               private val key: HistoryKey,
                               private val tracker: PerformanceTracker) extends InvocationTokenWithCallbacks {
  private var performanceCallback: Option[(RunData) => Unit] = None

  override def runMeasuredFunction[TReturnValue](fun: () => TReturnValue): TReturnValue = {
    val result = runner.runMeasuredFunction((_: Unit) => fun(), (), key, inputDescriptor, tracker)
    performanceCallback.foreach(c => c(result.runData))

    result.value
  }

  override def setAfterInvocationCallback(callback: (RunData) => Unit): Unit =
    performanceCallback = Some(callback)
}
