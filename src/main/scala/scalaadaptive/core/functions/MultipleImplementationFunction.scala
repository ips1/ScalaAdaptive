package scalaadaptive.core.functions

import scala.collection.mutable
import scalaadaptive.analytics.AnalyticsData
import scalaadaptive.api.adaptors.InvocationToken
import scalaadaptive.api.grouping.{Group, GroupId, NoGroup}
import scalaadaptive.core.functions.references.{FunctionReference, ReferencedFunction}
import scalaadaptive.core.runtime.invocationtokens.SimpleInvocationToken
import scalaadaptive.api.policies.{Policy, PolicyResult}
import scalaadaptive.core.functions.statistics.FunctionStatistics
import scalaadaptive.core.runtime.AdaptiveSelector
import scalaadaptive.core.functions.adaptors.FunctionConfig
import scalaadaptive.core.functions.analytics.AnalyticsCollector

/**
  * Created by pk250187 on 5/21/17.
  */
class MultipleImplementationFunction[TArgType, TRetType](val functions: Seq[ReferencedFunction[TArgType, TRetType]],
                                                         val inputDescriptorSelector: Option[(TArgType) => Long],
                                                         val groupSelector: (TArgType) => GroupId,
                                                         val selector: AdaptiveSelector,
                                                         val analytics: AnalyticsCollector,
                                                         val functionConfig: FunctionConfig) {
  private def createDefaultPolicy() = functionConfig.startPolicy
  private def createDefaultStatistics() = new FunctionStatistics[TArgType, TRetType](functions.head,
    ref => functions.find(f => f.reference == ref))
  private def createDefaultFunctionData() = new FunctionData(createDefaultStatistics(), createDefaultPolicy())

  private val ungroupedData = createDefaultFunctionData()
  private val groupedData = new mutable.HashMap[Long, FunctionData[TArgType, TRetType]]

  val functionReferences: Seq[FunctionReference] =
    functions.map(f => if (functionConfig.closureReferences) f.closureReference else f.reference)

  private def getData(groupId: GroupId) = groupId match {
    case NoGroup() => ungroupedData
    case Group(id) => groupedData.getOrElseUpdate(id, createDefaultFunctionData())
  }

  private def generateInputDescriptor(arguments: TArgType): Option[Long] =
    inputDescriptorSelector.map(sel => sel(arguments))

  private def train(data: TArgType): Unit = {
    val groupId = groupSelector(data)
    functions.foreach(f => invokeUsingSelector(List(f), data, groupId, markAsGather = true))
  }

  private def processRunData(runData: RunData, markAsGather: Boolean): Unit = {
    analytics.applyRunData(runData)
    val data = getData(runData.groupId)
    data.statistics.applyRunData(runData, markAsGather)
  }

  private def invokeUsingSelector(functions: Seq[ReferencedFunction[TArgType, TRetType]],
                                  arguments: TArgType,
                                  groupId: GroupId,
                                  markAsGather: Boolean): TRetType = {
    val runResult = selector.runOption(functions, arguments, groupId,
      generateInputDescriptor(arguments), functionConfig.duration, functionConfig.selection)
    processRunData(runResult.runData, markAsGather)
    runResult.value
  }

  private def invokeUsingSelectorWithDelayedMeasure(functions: Seq[ReferencedFunction[TArgType, TRetType]],
                                                    arguments: TArgType,
                                                    groupId: GroupId,
                                                    markAsGather: Boolean): (TRetType, InvocationToken) = {
    val (runResult, token) = selector.runOptionWithDelayedMeasure(functions, arguments, groupId,
      generateInputDescriptor(arguments), functionConfig.duration, functionConfig.selection)
    token.setAfterInvocationCallback(data => processRunData(data, markAsGather))
    (runResult, token)
  }

  def invoke(arguments: TArgType): TRetType = {
    val groupId = groupSelector(arguments)
    val data = getData(groupId)
    val (result, newPolicy) = data.currentPolicy.decide(data.statistics)
    data.currentPolicy = newPolicy
    data.statistics.markRun()
    result match {
      // Fast results that avoid the RunTracker invocation
      case PolicyResult.UseLast => data.statistics.getLast.fun(arguments)
      case PolicyResult.UseMost => data.statistics.getMostSelectedFunction.fun(arguments)
      // Slow results that use the RunTracker and measure and gather data
      case PolicyResult.SelectNew =>
        invokeUsingSelector(functions, arguments, groupId, markAsGather = false)
      case PolicyResult.GatherData =>
        invokeUsingSelector(List(data.statistics.getLeastSelectedFunction), arguments, groupId, markAsGather = true)
    }
  }

  def invokeWithDelayedMeasure(arguments: TArgType): (TRetType, InvocationToken) = {
    val groupId = groupSelector(arguments)
    val data = getData(groupId)
    val (result, newPolicy) = data.currentPolicy.decide(data.statistics)
    data.currentPolicy = newPolicy
    data.statistics.markRun()
    result match {
      // Fast results that avoid the RunTracker invocation
      case PolicyResult.UseLast => (data.statistics.getLast.fun(arguments), new SimpleInvocationToken)
      case PolicyResult.UseMost => (data.statistics.getMostSelectedFunction.fun(arguments), new SimpleInvocationToken)
      // Slow results that use the RunTracker and measure and gather data
      case PolicyResult.SelectNew =>
        invokeUsingSelectorWithDelayedMeasure(functions, arguments, groupId, markAsGather = false)
      case PolicyResult.GatherData =>
        invokeUsingSelectorWithDelayedMeasure(List(data.statistics.getLeastSelectedFunction), arguments, groupId,
          markAsGather = false)
    }
  }

  def train(dataSet: Seq[TArgType]): Unit = dataSet.foreach(d => train(d))

  def flushHistory(): Unit = functions.foreach(f => selector.flushHistory(f.reference))

  def setPolicy(policy: Policy): Unit = {
    ungroupedData.currentPolicy = policy
    groupedData.foreach(_._2.currentPolicy = policy)
  }

  def resetPolicy(): Unit = setPolicy(createDefaultPolicy())

  def getAnalyticsData: Option[AnalyticsData] = analytics.getAnalyticsData
}
