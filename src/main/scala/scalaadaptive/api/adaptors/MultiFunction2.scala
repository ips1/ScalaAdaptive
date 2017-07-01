package scalaadaptive.api.adaptors

import scala.language.experimental.macros
import scalaadaptive.api.grouping.Group
import scalaadaptive.core.macros.OrMacroImpl

/**
  * Created by pk250187 on 5/27/17.
  */
trait MultiFunction2[T1, T2, R]
  extends MultiFunctionCommon[(T1, T2), R, MultiFunction2[T1, T2, R]]
    with Function2[T1, T2, R] {
  def or(fun: (T1, T2) => R): MultiFunction2[T1, T2, R] =
    macro OrMacroImpl.or_impl[(T1, T2) => R, MultiFunction2[T1, T2, R]]

  def by(selector: (T1, T2) => Long): MultiFunction2[T1, T2, R]
  def groupBy(selector: (T1, T2) => Group): MultiFunction2[T1, T2, R]

  def applyWithoutMeasuring(arg1: T1, arg2: T2): (R, InvocationToken)
  def ^(arg1: T1, arg2: T2): (R, InvocationToken) = applyWithoutMeasuring(arg1, arg2)

  def orMultiFunction(fun: MultiFunction2[T1, T2, R]): MultiFunction2[T1, T2, R]
}
