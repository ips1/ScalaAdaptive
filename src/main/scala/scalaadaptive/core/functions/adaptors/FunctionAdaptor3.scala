package scalaadaptive.core.functions.adaptors

import scalaadaptive.api.adaptors.{InvocationToken, MultiFunction3}
import scalaadaptive.api.grouping.Group
import scalaadaptive.core.functions.{FunctionFactory, CombinedFunction}

/**
  * Created by Petr Kubat on 5/27/17.
  */
class FunctionAdaptor3[T1, T2, T3, R](val function: CombinedFunction[(T1, T2, T3), R])
  extends FunctionAdaptorBase[(T1, T2, T3), R, FunctionAdaptor3[T1, T2, T3, R]]
    with MultiFunction3[T1, T2, T3, R] {

  override protected val createNew: (CombinedFunction[(T1, T2, T3), R]) => FunctionAdaptor3[T1, T2, T3, R] =
    f => new FunctionAdaptor3[T1, T2, T3, R](f)

  override def by(selector: (T1, T2, T3) => Long): MultiFunction3[T1, T2, T3, R] =
    byTupled((t: (T1, T2, T3)) => selector(t._1, t._2, t._3))

  override def groupBy(selector: (T1, T2, T3) => Group): MultiFunction3[T1, T2, T3, R] =
    groupByTupled((t: (T1, T2, T3)) => selector(t._1, t._2, t._3))

  override def apply(arg1: T1, arg2: T2, arg3: T3): R =
    invoke((arg1, arg2, arg3))

  override def applyWithoutMeasuring(arg1: T1, arg2: T2, arg3: T3): (R, InvocationToken) =
    invokeWithDelayedMeasure((arg1, arg2, arg3))

  override def orMultiFunction(otherFun: MultiFunction3[T1, T2, T3, R]): FunctionAdaptor3[T1, T2, T3, R] =
    createNew(function.mergeFunctions(Conversions.toAdaptor(otherFun).function))
}