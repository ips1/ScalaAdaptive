package scalaadaptive.api.adaptors

import scala.language.experimental.macros
import scalaadaptive.api.grouping.GroupId
import scalaadaptive.core.macros.OrMacroImpl

/**
  * Created by pk250187 on 5/27/17.
  */
trait MultiFunction5[T1, T2, T3, T4, T5, R]
  extends MultiFunctionCommon[(T1, T2, T3, T4, T5), R, MultiFunction5[T1, T2, T3, T4, T5, R]]
    with Function5[T1, T2, T3, T4, T5, R] {

  def or(fun: (T1, T2, T3, T4, T5) => R): MultiFunction5[T1, T2, T3, T4, T5, R] =
    macro OrMacroImpl.or_impl[(T1, T2, T3, T4, T5) => R, MultiFunction5[T1, T2, T3, T4, T5, R]]

  def by(selector: (T1, T2, T3, T4, T5) => Long): MultiFunction5[T1, T2, T3, T4, T5, R]
  def groupBy(selector: (T1, T2, T3, T4, T5) => GroupId): MultiFunction5[T1, T2, T3, T4, T5, R]

  def applyWithoutMeasuring(arg1: T1, arg2: T2, arg3: T3, arg4: T4, arg5: T5): (R, InvocationToken)
  def ^(arg1: T1, arg2: T2, arg3: T3, arg4: T4, arg5: T5): (R, InvocationToken) =
    applyWithoutMeasuring(arg1, arg2, arg3, arg4, arg5)

  def orMultiFunction(fun: MultiFunction5[T1, T2, T3, T4, T5, R]): MultiFunction5[T1, T2, T3, T4, T5, R]
}
