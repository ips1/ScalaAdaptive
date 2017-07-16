package scalaadaptive.api.adaptors

import java.time.Duration

import scalaadaptive.analytics.AnalyticsData
import scalaadaptive.api.options.Selection.Selection
import scalaadaptive.api.options.Storage.Storage
import scalaadaptive.api.policies.Policy

/**
  * Created by Petr Kubat on 5/27/17.
  */
trait MultiFunctionCommon[TArgType, TRetType, TFunctionAdaptorType]
  extends MultiFunctionControl with MultiFunctionAnalytics {

  def selectUsing(newSelection: Selection): TFunctionAdaptorType
  def storeUsing(newStorage: Storage): TFunctionAdaptorType
  def limitedTo(newDuration: Duration): TFunctionAdaptorType
  def withPolicy(newPolicy: Policy): TFunctionAdaptorType

  def asClosures(closureIdentification: Boolean): TFunctionAdaptorType

  def train(data: Seq[TArgType]): Unit
}
