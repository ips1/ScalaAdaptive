package scalaadaptive.api.adaptors

import scalaadaptive.core.adaptors.FunctionAdaptor0
import scalaadaptive.core.runtime.invocationtokens.InvocationToken
import scala.language.experimental.macros

/**
  * Created by pk250187 on 5/27/17.
  */
trait MultiFunction0[R] extends MultiFunctionCommon[Unit, R, MultiFunction0[R]] with Function0[R] {
  def or(fun: () => R): MultiFunction0[R] = macro FunctionAdaptor0.or_impl[R]

  def by(selector: () => Long): MultiFunction0[R]

  def applyWithoutMeasuring(): (R, InvocationToken)
  def ^(): (R, InvocationToken) = applyWithoutMeasuring()

  // This method unfortunately has to accept the concrete type because of Scala macro magic :/
  def orMultiFunction(fun: FunctionAdaptor0[R]): MultiFunction0[R]
}
