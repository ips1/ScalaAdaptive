package scalaadaptive.api.adaptors

import scala.language.experimental.macros
import scalaadaptive.api.grouping.GroupId
import scalaadaptive.core.macros.OrMacroImpl

/**
  * Created by pk250187 on 5/27/17.
  */
trait MultiFunction1[T1, R] extends MultiFunctionCommon[T1, R, MultiFunction1[T1, R]] with Function1[T1, R] {
  def or(fun: (T1) => R): MultiFunction1[T1, R] = macro OrMacroImpl.or_impl[(T1) => R, MultiFunction1[T1, R]]

  def by(selector: (T1) => Long): MultiFunction1[T1, R]
  def groupBy(selector: (T1) => GroupId): MultiFunction1[T1, R]

  def applyWithoutMeasuring(arg1: T1): (R, InvocationToken)
  def ^(arg1: T1): (R, InvocationToken) = applyWithoutMeasuring(arg1)

  def orMultiFunction(fun: MultiFunction1[T1, R]): MultiFunction1[T1, R]
}
