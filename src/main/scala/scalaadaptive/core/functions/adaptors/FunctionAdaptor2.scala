package scalaadaptive.core.functions.adaptors

import scalaadaptive.api.adaptors.{InvocationToken, MultiFunction2}
import scalaadaptive.core.functions.{FunctionFactory, MultipleImplementationFunction}

/**
  * Created by pk250187 on 5/27/17.
  */
class FunctionAdaptor2[T1, T2, R](val function: MultipleImplementationFunction[(T1, T2), R])
  extends FunctionAdaptorBase[(T1, T2), R, FunctionAdaptor2[T1, T2, R]]
    with MultiFunction2[T1, T2, R] {

  override protected val createNew: (MultipleImplementationFunction[(T1, T2), R]) => FunctionAdaptor2[T1, T2, R] =
    f => new FunctionAdaptor2[T1, T2, R](f)

  override def by(selector: (T1, T2) => Long): MultiFunction2[T1, T2, R] =
    byGrouped((t: (T1, T2)) => selector(t._1, t._2))

  override def apply(arg1: T1, arg2: T2): R =
    function.invoke((arg1, arg2))

  override def applyWithoutMeasuring(arg1: T1, arg2: T2): (R, InvocationToken) =
    function.invokeWithDelayedMeasure((arg1, arg2))

  override def orMultiFunction(otherFun: MultiFunction2[T1, T2, R]): FunctionAdaptor2[T1, T2, R] =
    createNew(functionFactory.mergeFunctions(function, Conversions.toAdaptor(otherFun).function))
}