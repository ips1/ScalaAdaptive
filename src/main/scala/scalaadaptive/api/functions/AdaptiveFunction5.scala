package scalaadaptive.api.functions

import scala.language.experimental.macros
import scalaadaptive.api.grouping.Group
import scalaadaptive.core.macros.OrMacroImpl

/**
  * Created by Petr Kubat on 5/27/17.
  *
  * A type representing an adaptive function with 5 arguments and a return value. Can be used anywhere a normal
  * [[Function5]] could be used. Contains a set of implementations that are of the same argument arity and types.
  *
  * Methods are exactly the same as for [[AdaptiveFunction0]], detailed documentation can be found there.
  *
  */
trait AdaptiveFunction5[T1, T2, T3, T4, T5, R]
  extends AdaptiveFunctionCommon[(T1, T2, T3, T4, T5), R, AdaptiveFunction5[T1, T2, T3, T4, T5, R]]
    with Function5[T1, T2, T3, T4, T5, R] {

  def or(fun: (T1, T2, T3, T4, T5) => R): AdaptiveFunction5[T1, T2, T3, T4, T5, R] =
    macro OrMacroImpl.or_impl[(T1, T2, T3, T4, T5) => R, AdaptiveFunction5[T1, T2, T3, T4, T5, R]]

  def by(selector: (T1, T2, T3, T4, T5) => Long): AdaptiveFunction5[T1, T2, T3, T4, T5, R]
  def groupBy(selector: (T1, T2, T3, T4, T5) => Group): AdaptiveFunction5[T1, T2, T3, T4, T5, R]

  def applyWithoutMeasuring(arg1: T1, arg2: T2, arg3: T3, arg4: T4, arg5: T5): (R, InvocationToken)
  def ^(arg1: T1, arg2: T2, arg3: T3, arg4: T4, arg5: T5): (R, InvocationToken) =
    applyWithoutMeasuring(arg1, arg2, arg3, arg4, arg5)

  def orAdaptiveFunction(fun: AdaptiveFunction5[T1, T2, T3, T4, T5, R]): AdaptiveFunction5[T1, T2, T3, T4, T5, R]
}
