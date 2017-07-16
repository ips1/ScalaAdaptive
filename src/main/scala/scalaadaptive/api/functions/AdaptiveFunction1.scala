package scalaadaptive.api.functions

import scala.language.experimental.macros
import scalaadaptive.api.grouping.Group
import scalaadaptive.core.macros.OrMacroImpl

/**
  * Created by Petr Kubat on 5/27/17.
  *
  * A type representing an adaptive function with 1 argument and a return value. Can be used anywhere a normal
  * [[Function1]] could be used. Contains a set of implementations that are of the same argument arity and types.
  *
  * Methods are exactly the same as for [[AdaptiveFunction0]], detailed documentation can be found there.
  *
  */
trait AdaptiveFunction1[T1, R] extends AdaptiveFunctionCommon[T1, R, AdaptiveFunction1[T1, R]] with Function1[T1, R] {
  def or(fun: (T1) => R): AdaptiveFunction1[T1, R] = macro OrMacroImpl.or_impl[(T1) => R, AdaptiveFunction1[T1, R]]

  def by(selector: (T1) => Long): AdaptiveFunction1[T1, R]
  def groupBy(selector: (T1) => Group): AdaptiveFunction1[T1, R]

  def applyWithoutMeasuring(arg1: T1): (R, InvocationToken)
  def ^(arg1: T1): (R, InvocationToken) = applyWithoutMeasuring(arg1)

  def orMultiFunction(fun: AdaptiveFunction1[T1, R]): AdaptiveFunction1[T1, R]
}
