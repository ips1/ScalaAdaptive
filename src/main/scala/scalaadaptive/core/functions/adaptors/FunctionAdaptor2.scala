package scalaadaptive.core.functions.adaptors

import scalaadaptive.api.functions.{InvocationToken, AdaptiveFunction2}
import scalaadaptive.api.grouping.Group
import scalaadaptive.core.functions.{FunctionFactory, CombinedFunction}

/**
  * Created by Petr Kubat on 5/27/17.
  *
  * An internal representation of [[scalaadaptive.api.functions.AdaptiveFunction2]] - works as an adaptor between the
  * classic Scala function API (from the [[Function2]] trait extended by the
  * [[scalaadaptive.api.functions.AdaptiveFunction2]]) and the internal
  * [[scalaadaptive.core.functions.CombinedFunction]] representation of function with multiple implementations.
  *
  */
class FunctionAdaptor2[T1, T2, R](val function: CombinedFunction[(T1, T2), R])
  extends FunctionAdaptorBase[(T1, T2), R, FunctionAdaptor2[T1, T2, R]]
    with AdaptiveFunction2[T1, T2, R] {

  override protected val createNew: (CombinedFunction[(T1, T2), R]) => FunctionAdaptor2[T1, T2, R] =
    f => new FunctionAdaptor2[T1, T2, R](f)

  override def by(selector: (T1, T2) => Long): AdaptiveFunction2[T1, T2, R] =
    byTupled((t: (T1, T2)) => selector(t._1, t._2))

  override def groupBy(selector: (T1, T2) => Group): AdaptiveFunction2[T1, T2, R] =
    groupByTupled((t: (T1, T2)) => selector(t._1, t._2))

  override def apply(arg1: T1, arg2: T2): R =
    invoke((arg1, arg2))

  override def applyWithoutMeasuring(arg1: T1, arg2: T2): (R, InvocationToken) =
    invokeWithDelayedMeasure((arg1, arg2))

  override def orMultiFunction(otherFun: AdaptiveFunction2[T1, T2, R]): FunctionAdaptor2[T1, T2, R] =
    createNew(function.mergeFunctions(Conversions.toAdaptor(otherFun).function))
}