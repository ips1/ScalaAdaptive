package scalaadaptive.core.functions.adaptors

import scalaadaptive.api.functions.{InvocationToken, AdaptiveFunction4}
import scalaadaptive.api.grouping.Group
import scalaadaptive.core.functions.{FunctionFactory, CombinedFunction}

/**
  * Created by Petr Kubat on 5/27/17.
  */
class FunctionAdaptor4[T1, T2, T3, T4, R](val function: CombinedFunction[(T1, T2, T3, T4), R])
  extends FunctionAdaptorBase[(T1, T2, T3, T4), R, FunctionAdaptor4[T1, T2, T3, T4, R]]
    with AdaptiveFunction4[T1, T2, T3, T4, R] {

  override protected val createNew: (CombinedFunction[(T1, T2, T3, T4), R]) => FunctionAdaptor4[T1, T2, T3, T4, R] =
    f => new FunctionAdaptor4[T1, T2, T3, T4, R](f)

  override def by(selector: (T1, T2, T3, T4) => Long): AdaptiveFunction4[T1, T2, T3, T4, R] =
    byTupled((t: (T1, T2, T3, T4)) => selector(t._1, t._2, t._3, t._4))

  override def groupBy(selector: (T1, T2, T3, T4) => Group): AdaptiveFunction4[T1, T2, T3, T4, R] =
    groupByTupled((t: (T1, T2, T3, T4)) => selector(t._1, t._2, t._3, t._4))

  override def apply(arg1: T1, arg2: T2, arg3: T3, arg4: T4): R =
    invoke((arg1, arg2, arg3, arg4))

  override def applyWithoutMeasuring(arg1: T1, arg2: T2, arg3: T3, arg4: T4): (R, InvocationToken) =
    invokeWithDelayedMeasure((arg1, arg2, arg3, arg4))

  override def orMultiFunction(otherFun: AdaptiveFunction4[T1, T2, T3, T4, R]): FunctionAdaptor4[T1, T2, T3, T4, R] =
    createNew(function.mergeFunctions(Conversions.toAdaptor(otherFun).function))
}