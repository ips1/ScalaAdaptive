package scalaadaptive.core.functions.adaptors

import scalaadaptive.api.adaptors.{InvocationToken, MultiFunction1}
import scalaadaptive.core.functions.{FunctionFactory, MultipleImplementationFunction}

/**
  * Created by pk250187 on 5/27/17.
  */
class FunctionAdaptor1[T1, R](val function: MultipleImplementationFunction[T1, R])
    extends FunctionAdaptorBase[T1, R, FunctionAdaptor1[T1, R]]
    with MultiFunction1[T1, R] {

  override protected val createNew: (MultipleImplementationFunction[T1, R]) => FunctionAdaptor1[T1, R] =
    f => new FunctionAdaptor1[T1, R](f)


  override def by(selector: (T1) => Long): MultiFunction1[T1, R] = byGrouped((arg1: T1) => selector((arg1)))

  override def apply(arg1: T1): R = function.invoke((arg1))

  override def applyWithoutMeasuring(arg1: T1): (R, InvocationToken) = function.invokeWithDelayedMeasure((arg1))

  override def orMultiFunction(otherFun: MultiFunction1[T1, R]): FunctionAdaptor1[T1, R] =
    createNew(functionFactory.mergeFunctions(function, Conversions.toAdaptor(otherFun).function))
}