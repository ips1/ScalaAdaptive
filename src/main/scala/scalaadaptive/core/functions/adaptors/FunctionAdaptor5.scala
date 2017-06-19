package scalaadaptive.core.functions.adaptors

import scalaadaptive.api.adaptors.{InvocationToken, MultiFunction5}
import scalaadaptive.api.grouping.GroupId
import scalaadaptive.core.functions.{FunctionFactory, MultipleImplementationFunction}

/**
  * Created by pk250187 on 5/27/17.
  */
class FunctionAdaptor5[T1, T2, T3, T4, T5, R](val function: MultipleImplementationFunction[(T1, T2, T3, T4, T5), R])
  extends FunctionAdaptorBase[(T1, T2, T3, T4, T5), R, FunctionAdaptor5[T1, T2, T3, T4, T5, R]]
    with MultiFunction5[T1, T2, T3, T4, T5, R] {

  override protected val createNew: (MultipleImplementationFunction[(T1, T2, T3, T4, T5), R]) => FunctionAdaptor5[T1, T2, T3, T4, T5, R] =
    f => new FunctionAdaptor5[T1, T2, T3, T4, T5, R](f)

  override def by(selector: (T1, T2, T3, T4, T5) => Long): MultiFunction5[T1, T2, T3, T4, T5, R] =
    byTupled((t: (T1, T2, T3, T4, T5)) => selector(t._1, t._2, t._3, t._4, t._5))

  override def groupBy(selector: (T1, T2, T3, T4, T5) => GroupId): MultiFunction5[T1, T2, T3, T4, T5, R] =
    groupByTupled((t: (T1, T2, T3, T4, T5)) => selector(t._1, t._2, t._3, t._4, t._5))

  override def apply(arg1: T1, arg2: T2, arg3: T3, arg4: T4, arg5: T5): R =
    function.invoke((arg1, arg2, arg3, arg4, arg5))

  override def applyWithoutMeasuring(arg1: T1, arg2: T2, arg3: T3, arg4: T4, arg5: T5): (R, InvocationToken) =
    function.invokeWithDelayedMeasure((arg1, arg2, arg3, arg4, arg5))

  override def orMultiFunction(otherFun: MultiFunction5[T1, T2, T3, T4, T5, R]): FunctionAdaptor5[T1, T2, T3, T4, T5, R] =
    createNew(functionFactory.mergeFunctions(function, Conversions.toAdaptor(otherFun).function))
}