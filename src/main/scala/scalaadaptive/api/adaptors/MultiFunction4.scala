package scalaadaptive.api.adaptors

import scala.language.experimental.macros
import scalaadaptive.api.grouping.Group
import scalaadaptive.core.macros.OrMacroImpl

/**
  * Created by pk250187 on 5/27/17.
  */
trait MultiFunction4[T1, T2, T3, T4, R]
  extends MultiFunctionCommon[(T1, T2, T3, T4), R, MultiFunction4[T1, T2, T3, T4, R]]
    with Function4[T1, T2, T3, T4, R] {

  def or(fun: (T1, T2, T3, T4) => R): MultiFunction4[T1, T2, T3, T4, R] =
    macro OrMacroImpl.or_impl[(T1, T2, T3, T4) => R, MultiFunction4[T1, T2, T3, T4, R]]

  def by(selector: (T1, T2, T3, T4) => Long): MultiFunction4[T1, T2, T3, T4, R]
  def groupBy(selector: (T1, T2, T3, T4) => Group): MultiFunction4[T1, T2, T3, T4, R]

  def applyWithoutMeasuring(arg1: T1, arg2: T2, arg3: T3, arg4: T4): (R, InvocationToken)
  def ^(arg1: T1, arg2: T2, arg3: T3, arg4: T4): (R, InvocationToken) = applyWithoutMeasuring(arg1, arg2, arg3, arg4)

  def orMultiFunction(fun: MultiFunction4[T1, T2, T3, T4, R]): MultiFunction4[T1, T2, T3, T4, R]
}
