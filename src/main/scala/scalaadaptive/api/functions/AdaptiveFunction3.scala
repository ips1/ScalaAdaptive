package scalaadaptive.api.functions

import scala.language.experimental.macros
import scalaadaptive.api.grouping.Group
import scalaadaptive.core.macros.OrMacroImpl

/**
  * Created by Petr Kubat on 5/27/17.
  *
  * * A type representing an adaptive function with 3 arguments and a return value. Can be used anywhere a normal
  * [[Function3]] could be used. Contains a set of implementations that are of the same argument arity and types.
  *
  * Methods are exactly the same as for [[AdaptiveFunction0]], detailed documentation can be found there.
  *
  */
trait AdaptiveFunction3[T1, T2, T3, R]
  extends AdaptiveFunctionCommon[(T1, T2, T3), R, AdaptiveFunction3[T1, T2, T3, R]]
    with Function3[T1, T2, T3, R] {
  def or(fun: (T1, T2, T3) => R): AdaptiveFunction3[T1, T2, T3, R] =
    macro OrMacroImpl.or_impl[(T1, T2, T3) => R, AdaptiveFunction3[T1, T2, T3, R]]

  def by(selector: (T1, T2, T3) => Long): AdaptiveFunction3[T1, T2, T3, R]
  def groupBy(selector: (T1, T2, T3) => Group): AdaptiveFunction3[T1, T2, T3, R]

  def applyWithoutMeasuring(arg1: T1, arg2: T2, arg3: T3): (R, InvocationToken)
  def ^(arg1: T1, arg2: T2, arg3: T3): (R, InvocationToken) = applyWithoutMeasuring(arg1, arg2, arg3)

  def orAdaptiveFunction(fun: AdaptiveFunction3[T1, T2, T3, R]): AdaptiveFunction3[T1, T2, T3, R]
}
