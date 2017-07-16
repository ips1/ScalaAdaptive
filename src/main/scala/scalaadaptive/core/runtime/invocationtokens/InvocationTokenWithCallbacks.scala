package scalaadaptive.core.runtime.invocationtokens

import scalaadaptive.api.functions.InvocationToken
import scalaadaptive.core.performance.PerformanceProvider
import scalaadaptive.core.functions.RunData

/**
  * Created by Petr Kubat on 5/21/17.
  *
  * Invocation token interface that allows binding a callback after the invocation is finished.
  *
  * Whenever a function is invoked using this token, the RunData is passed to the callback (if it is set).
  *
  */
trait InvocationTokenWithCallbacks extends InvocationToken {
  /**
    * Sets a callback on the token that will be invoked after each execution with the
    * [[scalaadaptive.core.functions.RunData]] measured.
    * @param callback
    */
  def setAfterInvocationCallback(callback: (RunData) => Unit)
}
