package scalaadaptive.core.functions.adaptors

import scalaadaptive.api.adaptors.MultiFunction4
import scalaadaptive.core.functions.{FunctionFactory, MultipleImplementationFunction}
import scalaadaptive.core.runtime.invocationtokens.InvocationToken

/**
  * Created by pk250187 on 5/27/17.
  */
class FunctionAdaptor4[T1, T2, T3, T4, R](val function: MultipleImplementationFunction[(T1, T2, T3, T4), R])
  extends FunctionAdaptorBase[(T1, T2, T3, T4), R, FunctionAdaptor4[T1, T2, T3, T4, R]]
    with MultiFunction4[T1, T2, T3, T4, R] {

  override protected val createNew: (MultipleImplementationFunction[(T1, T2, T3, T4), R]) => FunctionAdaptor4[T1, T2, T3, T4, R] =
    f => new FunctionAdaptor4[T1, T2, T3, T4, R](f)

  override def by(selector: (T1, T2, T3, T4) => Long): MultiFunction4[T1, T2, T3, T4, R] =
    byGrouped((t: (T1, T2, T3, T4)) => selector(t._1, t._2, t._3, t._4))

  override def apply(arg1: T1, arg2: T2, arg3: T3, arg4: T4): R =
    function.invoke((arg1, arg2, arg3, arg4))

  override def applyWithoutMeasuring(arg1: T1, arg2: T2, arg3: T3, arg4: T4): (R, InvocationToken) =
    function.invokeWithDelayedMeasure((arg1, arg2, arg3, arg4))

  override def orMultiFunction(otherFun: MultiFunction4[T1, T2, T3, T4, R]): FunctionAdaptor4[T1, T2, T3, T4, R] =
    createNew(functionFactory.mergeFunctions(function, Conversions.toAdaptor(otherFun).function))
}