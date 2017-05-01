package scalaadaptive.api.adaptors

import scalaadaptive.core.options.Storage.Storage
import scalaadaptive.core.runtime.MeasurementToken

/**
  * Created by pk250187 on 5/1/17.
  */
trait MultiFunction2[T1, T2, R] extends Function2[T1, T2, R] {
  def or(fun: (T1, T2) => R): (T1, T2) => R
  def by(selector: (T1, T2) => Int): (T1, T2) => R
  def using(newStorage: Storage): (T1, T2) => R

  override def apply(arg1: T1, arg2: T2): R
  def applyWithoutMeasuring(arg1: T1, arg2: T2): (R, MeasurementToken)
  def ^(arg1: T1, arg2: T2): (R, MeasurementToken) = applyWithoutMeasuring(arg1, arg2)

  def toDebugString: String
}
