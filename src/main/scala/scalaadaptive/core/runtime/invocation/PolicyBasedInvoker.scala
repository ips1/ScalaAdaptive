package scalaadaptive.core.runtime.invocation

import scalaadaptive.api.functions.InvocationToken
import scalaadaptive.api.grouping.Group
import scalaadaptive.api.options.Storage
import scalaadaptive.api.policies.PolicyResult
import scalaadaptive.core.functions.{CombinedFunction, RunData}
import scalaadaptive.core.runtime.invocationtokens.SimpleInvocationToken
import scalaadaptive.core.runtime.AdaptiveInternal
import scalaadaptive.core.runtime.selection.{AdaptiveSelector, SelectionInput}

/**
  * Created by Petr Kubat on 6/29/17.
  */
class PolicyBasedInvoker extends CombinedFunctionInvoker {
  private def getSelector[TArgType, TRetType](function: CombinedFunction[TArgType, TRetType]): AdaptiveSelector = {
    if (function.localSelector.isDefined)
      function.localSelector.get
    else function.functionConfig.storage match {
      case Storage.Persistent => AdaptiveInternal.getSharedPersistentSelector
      case _ => AdaptiveInternal.getSharedSelector
    }
  }

  private def generateSelectionInput[TArgType, TRetType](function: CombinedFunction[TArgType, TRetType],
                                                         arguments: TArgType,
                                                         groupId: Group,
                                                         inputDescriptor: Option[Long]): SelectionInput[TArgType, TRetType] =
    new SelectionInput(function.functions, arguments, groupId, inputDescriptor,
      function.functionConfig.duration, function.getSelection)

  private def train[TArgType, TRetType](function: CombinedFunction[TArgType, TRetType],
                                        data: TArgType): Unit = {
    val groupId = function.getGroup(data)
    function.functions.foreach(f => {
      getSelector(function).gatherData(new SelectionInput(List(f), data, groupId, function.getInputDescriptor(data),
        function.functionConfig.duration, function.getSelection))
    })
  }

  private def processRunData[TArgType, TRetType](function: CombinedFunction[TArgType, TRetType],
                                                 runData: RunData,
                                                 markAsGather: Boolean): Unit = {
    function.analytics.applyRunData(runData)
    val data = function.getData(runData.groupId)
    data.statistics.applyRunData(runData, markAsGather)
  }

  private def invokeUsingSelector[TArgType, TRetType](function: CombinedFunction[TArgType, TRetType],
                                                      arguments: TArgType,
                                                      groupId: Group,
                                                      gather: Boolean): TRetType = {
    val inputDescriptor = function.getInputDescriptor(arguments)
    val selectionInput = generateSelectionInput(function, arguments, groupId, inputDescriptor)
    val runResult =
      if (gather)
        getSelector(function).gatherData(selectionInput)
      else
        getSelector(function).selectAndRun(selectionInput)

    processRunData(function, runResult.runData, gather)
    runResult.value
  }

  private def invokeUsingSelectorWithDelayedMeasure[TArgType, TRetType](function: CombinedFunction[TArgType, TRetType],
                                                                        arguments: TArgType,
                                                                        groupId: Group,
                                                                        gather: Boolean): (TRetType, InvocationToken) = {
    val inputDescriptor = function.getInputDescriptor(arguments)
    val selectionInput = generateSelectionInput(function, arguments, groupId, inputDescriptor)
    val (runResult, token) =
      if (gather)
        getSelector(function).gatherDataWithDelayedMeasure(selectionInput)
      else
        getSelector(function).selectAndRunWithDelayedMeasure(selectionInput)

    token.setAfterInvocationCallback(data => processRunData(function, data, gather))

    (runResult, token)
  }

  override def invoke[TArgType, TRetType](function: CombinedFunction[TArgType, TRetType], arguments: TArgType): TRetType = {
    val groupId = function.getGroup(arguments)
    val data = function.getData(groupId)
    val (result, newPolicy) = data.currentPolicy.decide(data.statistics)
    data.currentPolicy = newPolicy
    data.statistics.markRun()
    result match {
      // Fast results that avoid the RunTracker invocation
      case PolicyResult.UseLast => data.statistics.getLast.fun(arguments)
      case PolicyResult.UseMost => data.statistics.getMostSelectedFunction.fun(arguments)
      // Slow results that use the RunTracker and measure and gather data
      case PolicyResult.SelectNew =>
        invokeUsingSelector(function, arguments, groupId, gather = false)
      case PolicyResult.GatherData =>
        invokeUsingSelector(function, arguments, groupId, gather = true)
    }
  }

  override def invokeWithDelayedMeasure[TArgType, TRetType](function: CombinedFunction[TArgType, TRetType],
                                                            arguments: TArgType): (TRetType, InvocationToken) = {
    val groupId = function.getGroup(arguments)
    val data = function.getData(groupId)
    val (result, newPolicy) = data.currentPolicy.decide(data.statistics)
    data.currentPolicy = newPolicy
    data.statistics.markRun()
    result match {
      // Fast results that avoid the AdaptiveSelector invocation
      case PolicyResult.UseLast => (data.statistics.getLast.fun(arguments), new SimpleInvocationToken)
      case PolicyResult.UseMost => (data.statistics.getMostSelectedFunction.fun(arguments), new SimpleInvocationToken)
      // Slow results that use the RunTracker and measure and gather data
      case PolicyResult.SelectNew =>
        invokeUsingSelectorWithDelayedMeasure(function, arguments, groupId, gather = false)
      case PolicyResult.GatherData =>
        invokeUsingSelectorWithDelayedMeasure(function, arguments, groupId, gather = true)
    }
  }

  override def train[TArgType, TRetType](function: CombinedFunction[TArgType, TRetType], dataSet: Seq[TArgType]): Unit =
    dataSet.foreach(d => train(function, d))

  override def flushHistory[TArgType, TRetType](function: CombinedFunction[TArgType, TRetType]): Unit =
    function.functions.foreach(f => getSelector(function).flushHistory(f.identifier))
}
