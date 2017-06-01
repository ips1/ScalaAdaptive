package scalaadaptive.core.runtime

import java.time.Duration

import scalaadaptive.core.adaptors.AdaptorConfig
import scalaadaptive.core.options.Selection.Selection
import scalaadaptive.core.references.FunctionReference
import scalaadaptive.core.runtime.invocationtokens.{InvocationToken, SimpleInvocationToken}
import scalaadaptive.core.runtime.policies.{Policy, PolicyResult}
import scalaadaptive.core.runtime.statistics.{AdaptorStatistics, StatisticsHolder}

/**
  * Created by pk250187 on 5/21/17.
  */
class MultipleImplementationFunction[TArgType, TRetType](val functions: Seq[ReferencedFunction[TArgType, TRetType]],
                                                         val inputDescriptorSelector: Option[(TArgType) => Long],
                                                         val selectionRunner: FunctionRunner,
                                                         val adaptorConfig: AdaptorConfig) {
  private var currentPolicy = adaptorConfig.startPolicy

  // TODO: Reference resolver - didn't find?
  private val statistics = new AdaptorStatistics[TArgType, TRetType](functions.head,
    ref => functions.find(f => f.reference == ref).get)

  val functionReferences: Seq[FunctionReference] =
    functions.map(f => if (adaptorConfig.closureReferences) f.closureReference else f.reference)

  private def generateInputDescriptor(arguments: TArgType): Option[Long] =
    inputDescriptorSelector.map(sel => sel(arguments))

  private def train(data: TArgType): Unit = functions.foreach(f => invokeUsingRunner(List(f), data))

  private def invokeUsingRunner(functions: Seq[ReferencedFunction[TArgType, TRetType]],
                                arguments: TArgType): TRetType = {
    val runResult = selectionRunner.runOption(functions, arguments,
      generateInputDescriptor(arguments), adaptorConfig.duration, adaptorConfig.selection)
    statistics.applyRunData(runResult.runData)
    runResult.value
  }

  private def invokeUsingRunnerWithDelayedMeasure(functions: Seq[ReferencedFunction[TArgType, TRetType]],
                                                  arguments: TArgType): (TRetType, InvocationToken) = {
    val (runResult, token) = selectionRunner.runOptionWithDelayedMeasure(functions, arguments,
      generateInputDescriptor(arguments), adaptorConfig.duration, adaptorConfig.selection)
    token.setAfterInvocationCallback(data => statistics.applyRunData(data))
    (runResult, token)
  }

  def invoke(arguments: TArgType): TRetType = {
    val (result, newPolicy) = currentPolicy.decide(statistics)
    currentPolicy = newPolicy
    result match {
      // Fast results that avoid the RunTracker invocation
      case PolicyResult.UseLast => statistics.getLast.fun(arguments)
      case PolicyResult.UseMost => statistics.getMostSelectedFunction.fun(arguments)
      // Slow results that use the RunTracker and measure and gather data
      case PolicyResult.SelectNew =>
        invokeUsingRunner(functions, arguments)
      case PolicyResult.GatherData =>
        invokeUsingRunner(List(statistics.getLeastSelectedFunction), arguments)
    }
  }

  def invokeWithDelayedMeasure(arguments: TArgType): (TRetType, InvocationToken) = {
    val (result, newPolicy) = currentPolicy.decide(statistics)
    currentPolicy = newPolicy
    result match {
      // Fast results that avoid the RunTracker invocation
      case PolicyResult.UseLast => (statistics.getLast.fun(arguments), new SimpleInvocationToken)
      case PolicyResult.UseMost => (statistics.getMostSelectedFunction.fun(arguments), new SimpleInvocationToken)
      // Slow results that use the RunTracker and measure and gather data
      case PolicyResult.SelectNew =>
        invokeUsingRunnerWithDelayedMeasure(functions, arguments)
      case PolicyResult.GatherData =>
        invokeUsingRunnerWithDelayedMeasure(List(statistics.getLeastSelectedFunction), arguments)
    }
  }

  def train(dataSet: Seq[TArgType]): Unit = dataSet.foreach(d => train(d))

  def flushHistory(): Unit = functions.foreach(f => selectionRunner.flushHistory(f.reference))
}
