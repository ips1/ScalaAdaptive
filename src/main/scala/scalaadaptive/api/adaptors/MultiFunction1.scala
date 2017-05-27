package scalaadaptive.api.adaptors

import java.time.Duration

import scalaadaptive.core.adaptors.FunctionAdaptor1
import scalaadaptive.core.options.Storage.Storage
import scala.language.experimental.macros
import scalaadaptive.core.options.Selection.Selection
import scalaadaptive.core.runtime.invocationtokens.InvocationToken

/**
  * Created by pk250187 on 5/1/17.
  */
trait MultiFunction1[T1, R] extends Function1[T1, R] with MultiFunctionCommon {
  def or(fun: (T1) => R): MultiFunction1[T1, R] = macro FunctionAdaptor1.or_impl[T1, R]
  // TODO: Change to Long
  def by(selector: (T1) => Int): MultiFunction1[T1, R]
  def selectUsing(selection: Selection): MultiFunction1[T1, R]
  def storeUsing(storage: Storage): MultiFunction1[T1, R]
  def limitedTo(duration: Duration): MultiFunction1[T1, R]
  def asClosures(closureIdentification: Boolean): MultiFunction1[T1, R]

  def train(data: Seq[T1]): Unit

  override def apply(arg1: T1): R

  def applyWithoutMeasuring(arg1: T1): (R, InvocationToken)
  def ^(arg1: T1): (R, InvocationToken) = applyWithoutMeasuring(arg1)

  // This method unfortunately has to accept the concrete type because of Scala macro magic :/
  def orMultiFunction(fun: FunctionAdaptor1[T1, R]): MultiFunction1[T1, R]
}