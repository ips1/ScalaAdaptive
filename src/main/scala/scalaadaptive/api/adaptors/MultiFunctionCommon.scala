package scalaadaptive.api.adaptors

import java.time.Duration

import scalaadaptive.core.options.Selection.Selection
import scalaadaptive.core.options.Storage.Storage
import scalaadaptive.core.runtime.policies.Policy

/**
  * Created by pk250187 on 5/27/17.
  */
trait MultiFunctionCommon[TArgType, TRetType, TFunctionAdaptorType] {
  def selectUsing(newSelection: Selection): TFunctionAdaptorType
  def storeUsing(newStorage: Storage): TFunctionAdaptorType
  def limitedTo(newDuration: Duration): TFunctionAdaptorType
  def withPolicy(newPolicy: Policy): TFunctionAdaptorType

  def asClosures(closureIdentification: Boolean): TFunctionAdaptorType

  def train(data: Seq[TArgType]): Unit
  def toDebugString: String
  def flushHistory(): Unit
}
