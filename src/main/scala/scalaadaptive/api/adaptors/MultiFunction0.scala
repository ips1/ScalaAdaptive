package scalaadaptive.api.adaptors

import scalaadaptive.core.options.Storage.Storage
import scalaadaptive.core.runtime.AppliedFunction
import scalaadaptive.core.runtime.invocationtokens.InvocationToken

/**
  * Created by pk250187 on 5/1/17.
  */
trait MultiFunction0[R] extends Function0[R] with MultiFunctionCommon {
  def or(fun: () => R): () => R
  def by(selector: () => Int): () => R
  def using(newStorage: Storage): () => R

  override def apply(): R
  def applyWithoutMeasuring(): (R, InvocationToken)
  def ^(): (R, InvocationToken) = applyWithoutMeasuring()
}
