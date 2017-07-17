package scalaadaptive.core.functions.adaptors

import scalaadaptive.api.functions.{InvocationToken, AdaptiveFunction5}
import scalaadaptive.api.grouping.Group
import scalaadaptive.core.functions.{FunctionFactory, CombinedFunction}

/**
  * Created by Petr Kubat on 5/27/17.
  *
  * An internal representation of [[scalaadaptive.api.functions.AdaptiveFunction5]] - works as an adaptor between the
  * classic Scala function API (from the [[Function5]] trait extended by the
  * [[scalaadaptive.api.functions.AdaptiveFunction5]]) and the internal
  * [[scalaadaptive.core.functions.CombinedFunction]] representation of function with multiple implementations.
  *
  */
class FunctionAdaptor5[T1, T2, T3, T4, T5, R](val function: CombinedFunction[(T1, T2, T3, T4, T5), R])
  extends FunctionAdaptorBase[(T1, T2, T3, T4, T5), R, FunctionAdaptor5[T1, T2, T3, T4, T5, R]]
    with AdaptiveFunction5[T1, T2, T3, T4, T5, R] {

  override protected val createNew: (CombinedFunction[(T1, T2, T3, T4, T5), R]) => FunctionAdaptor5[T1, T2, T3, T4, T5, R] =
    f => new FunctionAdaptor5[T1, T2, T3, T4, T5, R](f)

  override def by(selector: (T1, T2, T3, T4, T5) => Long): AdaptiveFunction5[T1, T2, T3, T4, T5, R] =
    byTupled((t: (T1, T2, T3, T4, T5)) => selector(t._1, t._2, t._3, t._4, t._5))

  override def groupBy(selector: (T1, T2, T3, T4, T5) => Group): AdaptiveFunction5[T1, T2, T3, T4, T5, R] =
    groupByTupled((t: (T1, T2, T3, T4, T5)) => selector(t._1, t._2, t._3, t._4, t._5))

  override def apply(arg1: T1, arg2: T2, arg3: T3, arg4: T4, arg5: T5): R =
    invoke((arg1, arg2, arg3, arg4, arg5))

  override def applyWithoutMeasuring(arg1: T1, arg2: T2, arg3: T3, arg4: T4, arg5: T5): (R, InvocationToken) =
    invokeWithDelayedMeasure((arg1, arg2, arg3, arg4, arg5))

  override def orAdaptiveFunction(otherFun: AdaptiveFunction5[T1, T2, T3, T4, T5, R]): FunctionAdaptor5[T1, T2, T3, T4, T5, R] =
    createNew(function.mergeFunctions(Conversions.toAdaptor(otherFun).function))
}