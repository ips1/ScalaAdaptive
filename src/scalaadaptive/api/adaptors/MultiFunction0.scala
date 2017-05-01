package scalaadaptive.api.adaptors

import scalaadaptive.core.options.Storage.Storage
import scalaadaptive.core.runtime.{MeasurementToken, ReferencedFunction}

/**
  * Created by pk250187 on 5/1/17.
  */
trait MultiFunction0[R] extends Function0[R] {
  def or(fun: () => R): () => R
  def by(selector: () => Int): () => R
  def using(newStorage: Storage): () => R

  override def apply(): R
  def applyWithoutMeasuring(): (R, MeasurementToken)
  def ^(): (R, MeasurementToken) = applyWithoutMeasuring()

  def toDebugString: String
}
