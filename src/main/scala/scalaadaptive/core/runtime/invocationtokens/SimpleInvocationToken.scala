package scalaadaptive.core.runtime.invocationtokens

import scalaadaptive.api.functions.InvocationToken

/**
  * Created by Petr Kubat on 5/21/17.
  *
  * A dummy implementation of [[scalaadaptive.api.functions.InvocationToken]] that does not measure anything.
  *
  * It is used in the shortcut invocation cases after UseLast and UseMost policy results
  * (see [[scalaadaptive.api.policies.PolicyResult.PolicyResult]]).
  *
  */
class SimpleInvocationToken extends InvocationToken {
  override def runMeasuredFunction[TReturnValue](fun: () => TReturnValue): TReturnValue = fun()
}
