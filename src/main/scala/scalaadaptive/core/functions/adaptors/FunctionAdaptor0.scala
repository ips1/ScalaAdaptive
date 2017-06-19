package scalaadaptive.core.functions.adaptors

import scalaadaptive.api.adaptors.{InvocationToken, MultiFunction0}
import scalaadaptive.api.grouping.GroupId
import scalaadaptive.core.functions.{FunctionFactory, MultipleImplementationFunction}

/**
  * Created by pk250187 on 3/19/17.
  */
class FunctionAdaptor0[R](val function: MultipleImplementationFunction[Unit, R])
  extends FunctionAdaptorBase[Unit, R, FunctionAdaptor0[R]]
    with MultiFunction0[R] {

  override protected val createNew: (MultipleImplementationFunction[Unit, R]) => FunctionAdaptor0[R] =
    f => new FunctionAdaptor0[R](f)

  override def by(selector: () => Long): MultiFunction0[R] = byTupled((_) => selector())
  override def groupBy(selector: () => GroupId): MultiFunction0[R] = groupByTupled((_) => selector())

  override def apply(): R = function.invoke()

  override def applyWithoutMeasuring(): (R, InvocationToken) = function.invokeWithDelayedMeasure()

  override def orMultiFunction(otherFun: MultiFunction0[R]): FunctionAdaptor0[R] =
    createNew(functionFactory.mergeFunctions(function, Conversions.toAdaptor(otherFun).function))
}