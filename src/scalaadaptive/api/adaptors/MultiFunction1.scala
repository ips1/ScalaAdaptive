package scalaadaptive.api.adaptors

import scalaadaptive.core.adaptors.FunctionAdaptor1
import scalaadaptive.core.options.Storage.Storage
import scalaadaptive.core.runtime.MeasurementToken
import scala.language.experimental.macros

/**
  * Created by pk250187 on 5/1/17.
  */
trait MultiFunction1[T1, R] extends Function1[T1, R] {
  def or(fun: (T1) => R): (T1) => R = macro FunctionAdaptor1.or_impl[T1, R]
  def by(selector: (T1) => Int): (T1) => R
  def using(newStorage: Storage): (T1) => R

  def train(data: Seq[T1]): Unit

  override def apply(arg1: T1): R

  def applyWithoutMeasuring(arg1: T1): (R, MeasurementToken)
  def ^(arg1: T1): (R, MeasurementToken) = applyWithoutMeasuring(arg1)

  def toDebugString: String

  def orMultiFunction(fun: FunctionAdaptor1[T1, R]): (T1) => R = this match {
    case adaptor: FunctionAdaptor1[T1, R] => adaptor.orAdaptor(fun)
    case _ => fun
  }
}