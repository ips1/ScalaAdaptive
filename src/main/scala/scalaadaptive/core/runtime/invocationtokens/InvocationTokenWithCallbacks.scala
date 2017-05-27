package scalaadaptive.core.runtime.invocationtokens

import scalaadaptive.core.performance.PerformanceProvider
import scalaadaptive.core.runtime.{RunData, RunResult}

/**
  * Created by pk250187 on 5/21/17.
  */
/**
  * Measurement token interface that allows binding a callback after the invocation is finished.
  *
  * Whenever a function is invoked using this token, the RunData is passed to the callback (if it is set).
  */
trait InvocationTokenWithCallbacks extends InvocationToken {
  def setAfterInvocationCallback(callback: (RunData) => Unit)
}
