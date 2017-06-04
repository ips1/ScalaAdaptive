package scalaadaptive.api.adaptors

import java.time.Duration

import scalaadaptive.core.functions.adaptors.FunctionAdaptor1
import scalaadaptive.core.runtime.invocationtokens.InvocationToken
import scala.language.experimental.macros

/**
  * Created by pk250187 on 5/27/17.
  */
trait MultiFunction1[T1, R] extends MultiFunctionCommon[T1, R, MultiFunction1[T1, R]] with Function1[T1, R] {
  def or(fun: (T1) => R): MultiFunction1[T1, R] = macro FunctionAdaptor1.or_impl[T1, R]

  def by(selector: (T1) => Long): MultiFunction1[T1, R]

  def applyWithoutMeasuring(arg1: T1): (R, InvocationToken)
  def ^(arg1: T1): (R, InvocationToken) = applyWithoutMeasuring(arg1)

  // This method unfortunately has to accept the concrete type because of Scala macro magic :/
  def orMultiFunction(fun: FunctionAdaptor1[T1, R]): MultiFunction1[T1, R]
}
