package scalaadaptive.api.adaptors

import scala.language.experimental.macros
import scalaadaptive.core.macros.OrMacroImpl
import scalaadaptive.api.grouping.Group

/**
  * Created by Petr Kubat on 5/27/17.
  */
trait MultiFunction0[R] extends MultiFunctionCommon[Unit, R, MultiFunction0[R]] with Function0[R] {
  def or(fun: () => R): MultiFunction0[R] = macro OrMacroImpl.or_impl[() => R, MultiFunction0[R]]

  def by(selector: () => Long): MultiFunction0[R]
  def groupBy(selector: () => Group): MultiFunction0[R]

  def applyWithoutMeasuring(): (R, InvocationToken)
  def ^(): (R, InvocationToken) = applyWithoutMeasuring()

  def orMultiFunction(fun: MultiFunction0[R]): MultiFunction0[R]
}
