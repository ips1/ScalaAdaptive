package scalaadaptive.core.functions.adaptors

import java.time.Duration

import scalaadaptive.analytics.AnalyticsData
import scalaadaptive.api.grouping.GroupId
import scalaadaptive.api.options.Selection.Selection
import scalaadaptive.api.options.Storage.Storage
import scalaadaptive.api.policies.Policy
import scalaadaptive.core.functions.{FunctionFactory, CombinedFunction}
import scalaadaptive.core.runtime.AdaptiveInternal

/**
  * Created by pk250187 on 5/27/17.
  */
abstract class FunctionAdaptorBase[TArgType, TRetType, TFunctionAdaptorType] {
  protected val function: CombinedFunction[TArgType, TRetType]
  protected val createNew: (CombinedFunction[TArgType, TRetType]) => TFunctionAdaptorType

  protected def functionFactory: FunctionFactory = AdaptiveInternal.getFunctionFactory

  def byTupled(newSelector: (TArgType) => Long): TFunctionAdaptorType =
    createNew(function.updateInputDescriptor(Some(newSelector)))
  def groupByTupled(newSelector: (TArgType) => GroupId): TFunctionAdaptorType =
    createNew(function.updateGroupSelector(newSelector))

  def selectUsing(newSelection: Selection): TFunctionAdaptorType =
    createNew(function.updateFunctionConfig(function.functionConfig.selectUsing(newSelection)))
  def storeUsing(newStorage: Storage): TFunctionAdaptorType =
    createNew(function.updateFunctionConfig(function.functionConfig.storeUsing(newStorage)))
  def limitedTo(newDuration: Duration): TFunctionAdaptorType =
    createNew(function.updateFunctionConfig(function.functionConfig.limitedTo(newDuration)))
  def withPolicy(newPolicy: Policy): TFunctionAdaptorType =
    createNew(function.updateFunctionConfig(function.functionConfig.withPolicy(newPolicy)))
  def asClosures(closureIdentification: Boolean): TFunctionAdaptorType =
    createNew(function.updateFunctionConfig(function.functionConfig.asClosures(closureIdentification)))

  def train(data: Seq[TArgType]): Unit = function.train(data)

  def toDebugString: String =
    function.functionReferences.map(r => r.toString).mkString(", ")

  def flushHistory(): Unit = function.flushHistory()

  def setPolicy(policy: Policy): Unit = function.setPolicy(policy)

  def resetPolicy(): Unit = function.resetPolicy()

  def getAnalyticsData: Option[AnalyticsData] = function.getAnalyticsData
}
