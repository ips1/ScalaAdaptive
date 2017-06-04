package scalaadaptive.core.functions.adaptors

import scala.reflect.macros.blackbox
import scalaadaptive.api.adaptors.MultiFunction0
import scalaadaptive.core.macros.AdaptiveMacrosHelper
import scalaadaptive.core.runtime.invocationtokens.InvocationToken
import scalaadaptive.core.functions.{FunctionFactory, MultipleImplementationFunction}

/**
  * Created by pk250187 on 3/19/17.
  */
class FunctionAdaptor0[R](val function: MultipleImplementationFunction[Unit, R],
                          val functionFactory: FunctionFactory)
  extends FunctionAdaptorBase[Unit, R, FunctionAdaptor0[R]]
    with MultiFunction0[R] {

  override protected val createNew: (MultipleImplementationFunction[Unit, R]) => FunctionAdaptor0[R] =
    f => new FunctionAdaptor0[R](f, functionFactory)

  override def by(selector: () => Long): MultiFunction0[R] = byGrouped((_) => selector())

  override def apply(): R = function.invoke()

  override def applyWithoutMeasuring(): (R, InvocationToken) = function.invokeWithDelayedMeasure()

  // This method unfortunately has to accept the concrete type because of Scala macro magic :/
  override def orMultiFunction(otherFun: FunctionAdaptor0[R]): FunctionAdaptor0[R] =
    createNew(functionFactory.createFunction[Unit, R](function.functions ++ otherFun.function.functions, function.inputDescriptorSelector, function.adaptorConfig))
}

object FunctionAdaptor0 {
  def or_impl[R](c: blackbox.Context)(fun: c.Expr[() => R]): c.Expr[FunctionAdaptor0[R]] = {
    val resultTree = new AdaptiveMacrosHelper[c.type](c)
      .wrapTreeInAdapterConversionAndOrCall(fun.tree)
    c.Expr[FunctionAdaptor0[R]](resultTree)
  }
}