package scalaadaptive.core.adaptors

import scala.reflect.macros.blackbox
import scalaadaptive.api.adaptors.MultiFunction2
import scalaadaptive.core.macros.AdaptiveMacrosHelper
import scalaadaptive.core.runtime.invocationtokens.InvocationToken
import scalaadaptive.core.runtime.{FunctionFactory, MultipleImplementationFunction}


/**
  * Created by pk250187 on 5/27/17.s
  */
class FunctionAdaptor2[T1, T2, R](val function: MultipleImplementationFunction[(T1, T2), R],
                                  val functionFactory: FunctionFactory)
  extends FunctionAdaptorBase[(T1, T2), R, FunctionAdaptor2[T1, T2, R]]
    with MultiFunction2[T1, T2, R] {

  override protected val createNew: (MultipleImplementationFunction[(T1, T2), R]) => FunctionAdaptor2[T1, T2, R] =
    f => new FunctionAdaptor2[T1, T2, R](f, functionFactory)

  override def by(selector: (T1, T2) => Long): MultiFunction2[T1, T2, R] =
    byGrouped((t: (T1, T2)) => selector(t._1, t._2))

  override def apply(arg1: T1, arg2: T2): R =
    function.invoke((arg1, arg2))

  override def applyWithoutMeasuring(arg1: T1, arg2: T2): (R, InvocationToken) =
    function.invokeWithDelayedMeasure((arg1, arg2))

  // This method unfortunately has to accept the concrete type because of Scala macro magic :/
  override def orMultiFunction(otherFun: FunctionAdaptor2[T1, T2, R]): FunctionAdaptor2[T1, T2, R] =
    createNew(functionFactory.createFunction[(T1, T2), R](function.functions ++ otherFun.function.functions, function.inputDescriptorSelector, function.adaptorConfig))
}

object FunctionAdaptor2 {
  def or_impl[T1, T2, R](c: blackbox.Context)(fun: c.Expr[(T1, T2) => R]): c.Expr[FunctionAdaptor2[T1, T2, R]] = {
    val resultTree = new AdaptiveMacrosHelper[c.type](c)
      .wrapTreeInAdapterConversionAndOrCall(fun.tree)
    c.Expr[FunctionAdaptor2[T1, T2, R]](resultTree)
  }
}