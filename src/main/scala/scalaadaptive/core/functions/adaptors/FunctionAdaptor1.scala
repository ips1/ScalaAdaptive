package scalaadaptive.core.functions.adaptors

import scalaadaptive.api.functions.{InvocationToken, AdaptiveFunction1}
import scalaadaptive.api.grouping.Group
import scalaadaptive.core.functions.{FunctionFactory, CombinedFunction}

/**
  * Created by Petr Kubat on 5/27/17.
  */
class FunctionAdaptor1[T1, R](val function: CombinedFunction[T1, R])
    extends FunctionAdaptorBase[T1, R, FunctionAdaptor1[T1, R]]
    with AdaptiveFunction1[T1, R] {

  override protected val createNew: (CombinedFunction[T1, R]) => FunctionAdaptor1[T1, R] =
    f => new FunctionAdaptor1[T1, R](f)

  override def by(selector: (T1) => Long): AdaptiveFunction1[T1, R] = byTupled((arg1: T1) => selector(arg1))
  override def groupBy(selector: (T1) => Group): AdaptiveFunction1[T1, R] = groupByTupled((arg1: T1) => selector(arg1))

  override def apply(arg1: T1): R = invoke(arg1)

  override def applyWithoutMeasuring(arg1: T1): (R, InvocationToken) = invokeWithDelayedMeasure(arg1)

  override def orMultiFunction(otherFun: AdaptiveFunction1[T1, R]): FunctionAdaptor1[T1, R] =
    createNew(function.mergeFunctions(Conversions.toAdaptor(otherFun).function))
}