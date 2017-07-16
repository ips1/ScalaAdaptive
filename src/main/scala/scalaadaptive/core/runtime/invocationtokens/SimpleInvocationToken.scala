package scalaadaptive.core.runtime.invocationtokens

import scalaadaptive.api.adaptors.InvocationToken

/**
  * Created by Petr Kubat on 5/21/17.
  */
class SimpleInvocationToken extends InvocationToken {
  override def runMeasuredFunction[TReturnValue](fun: () => TReturnValue): TReturnValue = fun()
}
