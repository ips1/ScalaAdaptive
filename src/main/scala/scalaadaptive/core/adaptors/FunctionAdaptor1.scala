package scalaadaptive.core.adaptors

import scala.reflect.macros.blackbox
import scalaadaptive.api.adaptors.MultiFunction1
import scalaadaptive.core.macros.AdaptiveMacrosHelper
import scalaadaptive.core.runtime.{FunctionFactory, MultipleImplementationFunction}
import scalaadaptive.core.runtime.invocationtokens.InvocationToken

/**
  * Created by pk250187 on 5/27/17.s
  */
class FunctionAdaptor1[T1, R](val function: MultipleImplementationFunction[T1, R],
                             val functionFactory: FunctionFactory)
    extends FunctionAdaptorBase[T1, R, FunctionAdaptor1[T1, R]]
    with MultiFunction1[T1, R] {

  override protected val createNew: (MultipleImplementationFunction[T1, R]) => FunctionAdaptor1[T1, R] =
    f => new FunctionAdaptor1[T1, R](f, functionFactory)


  override def by(selector: (T1) => Long): MultiFunction1[T1, R] = byGrouped((arg1: T1) => selector((arg1)))

  override def apply(arg1: T1): R = function.invoke((arg1))

  override def applyWithoutMeasuring(arg1: T1): (R, InvocationToken) = function.invokeWithDelayedMeasure((arg1))

  // This method unfortunately has to accept the concrete type because of Scala macro magic :/
  override def orMultiFunction(otherFun: FunctionAdaptor1[T1, R]): FunctionAdaptor1[T1, R] =
    createNew(functionFactory.createFunction[T1, R](function.functions ++ otherFun.function.functions, function.inputDescriptorSelector, function.adaptorConfig))
}

object FunctionAdaptor1 {
  def or_impl[T1, R](c: blackbox.Context)(fun: c.Expr[(T1) => R]): c.Expr[FunctionAdaptor1[T1, R]] = {
    val resultTree = new AdaptiveMacrosHelper[c.type](c)
      .wrapTreeInAdapterConversionAndOrCall(fun.tree)
    c.Expr[FunctionAdaptor1[T1, R]](resultTree)
  }
}