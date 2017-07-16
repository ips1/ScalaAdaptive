package scalaadaptive.core.functions.adaptors

import scalaadaptive.api.functions.{InvocationToken, AdaptiveFunction0}
import scalaadaptive.api.grouping.Group
import scalaadaptive.core.functions.{FunctionFactory, CombinedFunction}

/**
  * Created by Petr Kubat on 3/19/17.
  */
class FunctionAdaptor0[R](val function: CombinedFunction[Unit, R])
  extends FunctionAdaptorBase[Unit, R, FunctionAdaptor0[R]]
    with AdaptiveFunction0[R] {

  override protected val createNew: (CombinedFunction[Unit, R]) => FunctionAdaptor0[R] =
    f => new FunctionAdaptor0[R](f)

  override def by(selector: () => Long): AdaptiveFunction0[R] = byTupled((_) => selector())
  override def groupBy(selector: () => Group): AdaptiveFunction0[R] = groupByTupled((_) => selector())

  override def apply(): R = invoke()

  override def applyWithoutMeasuring(): (R, InvocationToken) = invokeWithDelayedMeasure()

  override def orMultiFunction(otherFun: AdaptiveFunction0[R]): FunctionAdaptor0[R] =
    createNew(function.mergeFunctions(Conversions.toAdaptor(otherFun).function))
}