package scalaadaptive.api.adaptors

import scalaadaptive.core.runtime.invocationtokens.InvocationToken
import scala.language.experimental.macros
import scalaadaptive.core.macros.OrMacroImpl

/**
  * Created by pk250187 on 5/27/17.
  */
trait MultiFunction3[T1, T2, T3, R]
  extends MultiFunctionCommon[(T1, T2, T3), R, MultiFunction3[T1, T2, T3, R]]
    with Function3[T1, T2, T3, R] {
  def or(fun: (T1, T2, T3) => R): MultiFunction3[T1, T2, T3, R] =
    macro OrMacroImpl.or_impl[(T1, T2, T3) => R, MultiFunction3[T1, T2, T3, R]]

  def by(selector: (T1, T2, T3) => Long): MultiFunction3[T1, T2, T3, R]

  def applyWithoutMeasuring(arg1: T1, arg2: T2, arg3: T3): (R, InvocationToken)
  def ^(arg1: T1, arg2: T2, arg3: T3): (R, InvocationToken) = applyWithoutMeasuring(arg1, arg2, arg3)

  def orMultiFunction(fun: MultiFunction3[T1, T2, T3, R]): MultiFunction3[T1, T2, T3, R]
}
