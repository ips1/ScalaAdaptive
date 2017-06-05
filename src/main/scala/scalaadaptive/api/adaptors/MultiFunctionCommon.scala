package scalaadaptive.api.adaptors

import java.time.Duration

import scalaadaptive.analytics.AnalyticsData
import scalaadaptive.api.options.Selection.Selection
import scalaadaptive.api.options.Storage.Storage
import scalaadaptive.core.functions.policies.Policy

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
  def setPolicy(policy: Policy): Unit
  def resetPolicy(): Unit

  def getAnalyticsData: Option[AnalyticsData]
}
