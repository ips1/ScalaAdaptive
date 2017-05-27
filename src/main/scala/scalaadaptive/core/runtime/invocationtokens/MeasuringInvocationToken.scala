package scalaadaptive.core.runtime.invocationtokens

import scalaadaptive.core.performance.{PerformanceProvider, PerformanceTracker}
import scalaadaptive.core.runtime.{DelayedFunctionRunner, RunData}
import scalaadaptive.core.runtime.history.HistoryKey

/**
  * Created by pk250187 on 4/23/17.
  */
class MeasuringInvocationToken(private val runner: DelayedFunctionRunner,
                               private val inputDescriptor: Option[Long],
                               private val key: HistoryKey,
                               private val tracker: PerformanceTracker) extends InvocationTokenWithCallbacks {
  private var performanceCallback: Option[(RunData) => Unit] = None

  override def runMeasuredFunction[TReturnValue](fun: () => TReturnValue): TReturnValue = {
    val result = runner.runMeasuredFunction(fun, key, inputDescriptor, tracker)
    performanceCallback match {
      case Some(callback) => callback(result.runData)
      case _ =>
    }

    result.value
  }

  override def setAfterInvocationCallback(callback: (RunData) => Unit): Unit =
    performanceCallback = Some(callback)
}
