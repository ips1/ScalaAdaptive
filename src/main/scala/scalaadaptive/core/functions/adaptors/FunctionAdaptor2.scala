package scalaadaptive.core.functions.adaptors

import scalaadaptive.api.adaptors.{InvocationToken, MultiFunction2}
import scalaadaptive.api.grouping.Group
import scalaadaptive.core.functions.{FunctionFactory, CombinedFunction}

/**
  * Created by pk250187 on 5/27/17.
  */
class FunctionAdaptor2[T1, T2, R](val function: CombinedFunction[(T1, T2), R])
  extends FunctionAdaptorBase[(T1, T2), R, FunctionAdaptor2[T1, T2, R]]
    with MultiFunction2[T1, T2, R] {

  override protected val createNew: (CombinedFunction[(T1, T2), R]) => FunctionAdaptor2[T1, T2, R] =
    f => new FunctionAdaptor2[T1, T2, R](f)

  override def by(selector: (T1, T2) => Long): MultiFunction2[T1, T2, R] =
    byTupled((t: (T1, T2)) => selector(t._1, t._2))

  override def groupBy(selector: (T1, T2) => Group): MultiFunction2[T1, T2, R] =
    groupByTupled((t: (T1, T2)) => selector(t._1, t._2))

  override def apply(arg1: T1, arg2: T2): R =
    invoke((arg1, arg2))

  override def applyWithoutMeasuring(arg1: T1, arg2: T2): (R, InvocationToken) =
    invokeWithDelayedMeasure((arg1, arg2))

  override def orMultiFunction(otherFun: MultiFunction2[T1, T2, R]): FunctionAdaptor2[T1, T2, R] =
    createNew(function.mergeFunctions(Conversions.toAdaptor(otherFun).function))
}