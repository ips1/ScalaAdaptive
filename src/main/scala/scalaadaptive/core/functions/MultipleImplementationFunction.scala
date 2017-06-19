package scalaadaptive.core.functions

import scalaadaptive.analytics.AnalyticsData
import scalaadaptive.api.adaptors.InvocationToken
import scalaadaptive.api.grouping.GroupId
import scalaadaptive.core.functions.references.{FunctionReference, ReferencedFunction}
import scalaadaptive.core.runtime.invocationtokens.SimpleInvocationToken
import scalaadaptive.core.functions.policies.{Policy, PolicyResult}
import scalaadaptive.core.functions.statistics.AdaptorStatistics
import scalaadaptive.core.runtime.AdaptiveRunner
import scalaadaptive.core.functions.adaptors.FunctionConfig
import scalaadaptive.core.functions.analytics.AnalyticsCollector

/**
  * Created by pk250187 on 5/21/17.
  */
class MultipleImplementationFunction[TArgType, TRetType](val functions: Seq[ReferencedFunction[TArgType, TRetType]],
                                                         val inputDescriptorSelector: Option[(TArgType) => Long],
                                                         val groupSelector: (TArgType) => GroupId,
                                                         val selectionRunner: AdaptiveRunner,
                                                         val analytics: AnalyticsCollector,
                                                         val functionConfig: FunctionConfig) {
  private var currentPolicy = functionConfig.startPolicy

  private val statistics = new AdaptorStatistics[TArgType, TRetType](functions.head,
   ref => functions.find(f => f.reference == ref))

  val functionReferences: Seq[FunctionReference] =
    functions.map(f => if (functionConfig.closureReferences) f.closureReference else f.reference)

  private def generateInputDescriptor(arguments: TArgType): Option[Long] =
    inputDescriptorSelector.map(sel => sel(arguments))

  private def train(data: TArgType): Unit = functions.foreach(f => invokeUsingRunner(List(f), data, markAsGather = true))

  private def processRunData(runData: RunData, markAsGather: Boolean): Unit = {
    analytics.applyRunData(runData)
    statistics.applyRunData(runData, markAsGather)
  }

  private def invokeUsingRunner(functions: Seq[ReferencedFunction[TArgType, TRetType]],
                                arguments: TArgType,
                                markAsGather: Boolean): TRetType = {
    val runResult = selectionRunner.runOption(functions, arguments,
      generateInputDescriptor(arguments), functionConfig.duration, functionConfig.selection)
    processRunData(runResult.runData, markAsGather)
    runResult.value
  }

  private def invokeUsingRunnerWithDelayedMeasure(functions: Seq[ReferencedFunction[TArgType, TRetType]],
                                                  arguments: TArgType,
                                                  markAsGather: Boolean): (TRetType, InvocationToken) = {
    val (runResult, token) = selectionRunner.runOptionWithDelayedMeasure(functions, arguments,
      generateInputDescriptor(arguments), functionConfig.duration, functionConfig.selection)
    token.setAfterInvocationCallback(data => processRunData(data, markAsGather))
    (runResult, token)
  }

  def invoke(arguments: TArgType): TRetType = {
    val (result, newPolicy) = currentPolicy.decide(statistics)
    currentPolicy = newPolicy
    statistics.markRun()
    result match {
      // Fast results that avoid the RunTracker invocation
      case PolicyResult.UseLast => statistics.getLast.fun(arguments)
      case PolicyResult.UseMost => statistics.getMostSelectedFunction.fun(arguments)
      // Slow results that use the RunTracker and measure and gather data
      case PolicyResult.SelectNew =>
        invokeUsingRunner(functions, arguments, markAsGather = false)
      case PolicyResult.GatherData =>
        invokeUsingRunner(List(statistics.getLeastSelectedFunction), arguments, markAsGather = true)
    }
  }

  def invokeWithDelayedMeasure(arguments: TArgType): (TRetType, InvocationToken) = {
    val (result, newPolicy) = currentPolicy.decide(statistics)
    currentPolicy = newPolicy
    statistics.markRun()
    result match {
      // Fast results that avoid the RunTracker invocation
      case PolicyResult.UseLast => (statistics.getLast.fun(arguments), new SimpleInvocationToken)
      case PolicyResult.UseMost => (statistics.getMostSelectedFunction.fun(arguments), new SimpleInvocationToken)
      // Slow results that use the RunTracker and measure and gather data
      case PolicyResult.SelectNew =>
        invokeUsingRunnerWithDelayedMeasure(functions, arguments, markAsGather = false)
      case PolicyResult.GatherData =>
        invokeUsingRunnerWithDelayedMeasure(List(statistics.getLeastSelectedFunction), arguments, markAsGather = false)
    }
  }

  def train(dataSet: Seq[TArgType]): Unit = dataSet.foreach(d => train(d))

  def flushHistory(): Unit = functions.foreach(f => selectionRunner.flushHistory(f.reference))

  def setPolicy(policy: Policy): Unit = currentPolicy = policy

  def resetPolicy(): Unit = setPolicy(functionConfig.startPolicy)

  def getAnalyticsData: Option[AnalyticsData] = analytics.getAnalyticsData
}
