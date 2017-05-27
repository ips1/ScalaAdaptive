package scalaadaptive.core.adaptors

import scala.reflect.macros.blackbox
import scalaadaptive.api.newadaptors.MultiFunction1
import scalaadaptive.core.macros.AdaptiveMacrosHelper
import scalaadaptive.core.runtime.{FunctionFactory, MultipleImplementationFunction}
import scalaadaptive.core.runtime.invocationtokens.InvocationToken

/**
  * Created by pk250187 on 5/27/17.s
  */
class NewFunctionAdaptor1[T, R](val function: MultipleImplementationFunction[T, R],
                                val functionFactory: FunctionFactory)
    extends FunctionAdaptorBase[T, R, NewFunctionAdaptor1[T, R]]
    with MultiFunction1[T, R] {

  override protected val createNew: (MultipleImplementationFunction[T, R]) => NewFunctionAdaptor1[T, R] =
    f => new NewFunctionAdaptor1[T, R](f, functionFactory)

  private def groupArgs(arg1: T): (T) = arg1

  override def by(selector: (T) => Long): MultiFunction1[T, R] = byGrouped((arg1: T) => selector(groupArgs(arg1)))

  override def apply(arg1: T): R = function.invoke(groupArgs(arg1))

  override def applyWithoutMeasuring(arg1: T): (R, InvocationToken) = function.invokeWithDelayedMeasure(groupArgs(arg1))

  // This method unfortunately has to accept the concrete type because of Scala macro magic :/
  override def orMultiFunction(otherFun: NewFunctionAdaptor1[T, R]): NewFunctionAdaptor1[T, R] =
    createNew(functionFactory.createFunction[T, R](function.functions ++ otherFun.function.functions, function.inputDescriptorSelector, function.adaptorConfig))
}

object NewFunctionAdaptor1 {
  def or_impl[J, S](c: blackbox.Context)(fun: c.Expr[(J) => S]): c.Expr[NewFunctionAdaptor1[J, S]] = {
    val resultTree = new AdaptiveMacrosHelper[c.type](c)
      .wrapTreeInAdapterConversionAndOrCall(fun.tree)
    c.Expr[NewFunctionAdaptor1[J, S]](resultTree)
  }
}