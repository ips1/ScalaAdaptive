package scalaadaptive.api.adaptors


import scalaadaptive.core.adaptors.FunctionAdaptor2
import scalaadaptive.core.runtime.invocationtokens.InvocationToken
import scala.language.experimental.macros

/**
  * Created by pk250187 on 5/27/17.
  */
trait MultiFunction2[T1, T2, R]
  extends MultiFunctionCommon[(T1, T2), R, MultiFunction2[T1, T2, R]]
    with Function2[T1, T2, R] {

  def or(fun: (T1, T2) => R): MultiFunction2[T1, T2, R] = macro FunctionAdaptor2.or_impl[T1, T2, R]

  def by(selector: (T1, T2) => Long): MultiFunction2[T1, T2, R]

  def applyWithoutMeasuring(arg1: T1, arg2: T2): (R, InvocationToken)
  def ^(arg1: T1, arg2: T2): (R, InvocationToken) = applyWithoutMeasuring(arg1, arg2)

  // This method unfortunately has to accept the concrete type because of Scala macro magic :/
  def orMultiFunction(fun: FunctionAdaptor2[T1, T2, R]): MultiFunction2[T1, T2, R]
}
