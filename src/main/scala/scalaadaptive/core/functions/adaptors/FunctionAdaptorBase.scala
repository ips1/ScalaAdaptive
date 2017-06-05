package scalaadaptive.core.functions.adaptors

import java.time.Duration

import scalaadaptive.analytics.{AnalyticsData, BasicAnalyticsData, CsvAnalyticsSerializer}
import scalaadaptive.api.options.Selection.Selection
import scalaadaptive.api.options.Storage.Storage
import scalaadaptive.core.functions.policies.Policy
import scalaadaptive.core.functions.{FunctionFactory, MultipleImplementationFunction, ReferencedFunction}
import scalaadaptive.core.runtime.AdaptiveInternal

/**
  * Created by pk250187 on 5/27/17.
  */
abstract class FunctionAdaptorBase[TArgType, TRetType, TFunctionAdaptorType] {
  protected val function: MultipleImplementationFunction[TArgType, TRetType]
  protected val createNew: (MultipleImplementationFunction[TArgType, TRetType]) => TFunctionAdaptorType

  protected def functionFactory: FunctionFactory = AdaptiveInternal.getFunctionFactory

  private def updateFunctionsWithClosureIdentification(asClosures: Boolean) =
    function.functions.map(f =>
      new ReferencedFunction[TArgType, TRetType](f.fun, f.closureReference, f.customReference, asClosures))

  def byGrouped(newSelector: (TArgType) => Long): TFunctionAdaptorType =
    createNew(functionFactory.createFunction[TArgType, TRetType](function.functions, Some(newSelector), function.functionConfig))

  def selectUsing(newSelection: Selection): TFunctionAdaptorType =
    createNew(functionFactory.createFunction[TArgType, TRetType](function, function.functionConfig.selectUsing(newSelection)))
  def storeUsing(newStorage: Storage): TFunctionAdaptorType =
    createNew(functionFactory.createFunction[TArgType, TRetType](function, function.functionConfig.storeUsing(newStorage)))
  def limitedTo(newDuration: Duration): TFunctionAdaptorType =
    createNew(functionFactory.createFunction[TArgType, TRetType](function, function.functionConfig.limitedTo(newDuration)))
  def withPolicy(newPolicy: Policy): TFunctionAdaptorType =
    createNew(functionFactory.createFunction[TArgType, TRetType](function, function.functionConfig.withPolicy(newPolicy)))

  def asClosures(closureIdentification: Boolean): TFunctionAdaptorType =
    createNew(functionFactory.createFunction[TArgType, TRetType](
      updateFunctionsWithClosureIdentification(closureIdentification), function.inputDescriptorSelector,
      function.functionConfig.asClosures(closureIdentification)))

  def train(data: Seq[TArgType]): Unit = function.train(data)

  def toDebugString: String =
    function.functionReferences.map(r => r.toString).mkString(", ")

  def flushHistory(): Unit = function.flushHistory()

  def setPolicy(policy: Policy): Unit = function.setPolicy(policy)

  def resetPolicy(): Unit = function.resetPolicy()

  def getAnalyticsData: AnalyticsData = new BasicAnalyticsData(function.statistics.records, new CsvAnalyticsSerializer)
}
