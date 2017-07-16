package scalaadaptive.core.runtime.selection

import java.time.{Duration, Instant}

import scalaadaptive.api.grouping.Group
import scalaadaptive.api.options.Selection
import scalaadaptive.api.options.Selection.Selection
import scalaadaptive.core.functions.identifiers.{FunctionIdentifier, IdentifiedFunction}
import scalaadaptive.core.functions.{RunData, RunResult}
import scalaadaptive.core.logging.Logger
import scalaadaptive.core.performance.{BasicPerformanceTracker, PerformanceTracker}
import scalaadaptive.core.runtime.history.HistoryKey
import scalaadaptive.core.runtime.history.evaluation.EvaluationProvider
import scalaadaptive.core.runtime.history.evaluation.data.EvaluationData
import scalaadaptive.core.runtime.history.historystorage.HistoryStorage
import scalaadaptive.core.runtime.history.runhistory.RunHistory
import scalaadaptive.core.runtime.invocationtokens.{InvocationTokenWithCallbacks, MeasuringInvocationToken}
import scalaadaptive.core.runtime.selection.strategies.{LeastDataSelectionStrategy, SelectionStrategy}

/**
  * Created by Petr Kubat on 3/19/17.
  */
class HistoryBasedAdaptiveSelector[TMeasurement](historyStorage: HistoryStorage[TMeasurement],
                                                 meanBasedStrategy: SelectionStrategy[TMeasurement],
                                                 inputBasedStrategy: SelectionStrategy[TMeasurement],
                                                 evaluationProvider: EvaluationProvider[TMeasurement],
                                                 logger: Logger) extends AdaptiveSelector with DelayedFunctionRunner {

  private def filterHistoryByDuration(history: RunHistory[TMeasurement], duration: Duration) = {
    val currentTime = Instant.now()
    history.takeWhile(
      rd => Duration.between(rd.time, currentTime).compareTo(duration) < 0)
  }

  private def getStrategyForSelection(selection: Selection) = selection match {
    case Selection.InputBased => inputBasedStrategy
    case Selection.MeanBased => meanBasedStrategy
  }

  private def getStrategyForGather = new LeastDataSelectionStrategy[TMeasurement](logger)

  private def selectRecord[TArgType, TReturnType](input: SelectionInput[TArgType, TReturnType],
                                                  strategy: SelectionStrategy[TMeasurement]): HistoryKey = {
    logger.log(s"Target group: ${input.groupId}, byValue: ${input.inputDescriptor}")

    val allHistories = input.options.map(f => historyStorage.getHistory(HistoryKey(f.identifier, input.groupId)))

    val histories = input.limitedBy match {
      case Some(duration) => allHistories.map(h => filterHistoryByDuration(h, duration))
      case _ => allHistories
    }

    val selectedRecord =
      if (histories.length > 1)
        strategy.selectOption(histories, input.inputDescriptor)
      else
        histories.head.key

    logger.log(s"Selected option: ${selectedRecord.functionId}")
    selectedRecord
  }

  private def selectAndRun[TArgType, TReturnType](input: SelectionInput[TArgType, TReturnType],
                                                  strategy: SelectionStrategy[TMeasurement]): RunResult[TReturnType] = {
    val tracker = new BasicPerformanceTracker
    tracker.startTracking()

    // Select function to run
    val selectedKey = selectRecord(input, strategy)
    val functionToRun = input.options.find(_.identifier == selectedKey.functionId).get.fun

    tracker.addSelectionTime()

    // Execute the function
    runMeasuredFunction(functionToRun, input.arguments, selectedKey, input.inputDescriptor, tracker)
  }

  private def selectAndRunWithDelayedMeasure[TArgType, TReturnType](input: SelectionInput[TArgType, TReturnType],
                                                                    strategy: SelectionStrategy[TMeasurement]): (TReturnType, InvocationTokenWithCallbacks) = {
    val tracker = new BasicPerformanceTracker
    tracker.startTracking()

    // Select function to run
    val selectedKey = selectRecord(input, strategy)
    val functionToRun = input.options.find(_.identifier == selectedKey.functionId).get.fun

    tracker.addSelectionTime()

    // Execute the function
    (functionToRun(input.arguments), new MeasuringInvocationToken(this, input.inputDescriptor, selectedKey, tracker))
  }

  override def selectAndRun[TArgType, TReturnType](input: SelectionInput[TArgType, TReturnType]): RunResult[TReturnType] =
    selectAndRun(input, getStrategyForSelection(input.selection))

  override def selectAndRunWithDelayedMeasure[TArgType, TReturnType](input: SelectionInput[TArgType, TReturnType]): (TReturnType, InvocationTokenWithCallbacks) =
    selectAndRunWithDelayedMeasure(input, getStrategyForSelection(input.selection))

  override def runMeasuredFunction[TArgType, TReturnType](fun: (TArgType) => TReturnType,
                                                          arguments: TArgType,
                                                          key: HistoryKey,
                                                          inputDescriptor: Option[Long],
                                                          tracker: PerformanceTracker): RunResult[TReturnType] = {
    tracker.startTracking()

    // Execute the function with evaluation
    val (result, measurement) = evaluationProvider.evaluateFunctionRun(fun, arguments)

    tracker.addFunctionTime()
    logger.log(s"Performance on $inputDescriptor measured: $measurement")

    // Store the evaluation result to the history
    historyStorage.applyNewRun(key, new EvaluationData[TMeasurement](inputDescriptor, Instant.now, measurement))

    tracker.addStoringTime()

    val performance = tracker.getPerformance
    performance.toLogString.lines.foreach(logger.log)
    tracker.reset()

    new RunResult(result, new RunData(key.functionId, key.groupId, inputDescriptor, performance))
  }

  override def flushHistory(function: FunctionIdentifier): Unit =
    historyStorage.flushHistory(function)

  override def gatherData[TArgType, TReturnType](input: SelectionInput[TArgType, TReturnType]): RunResult[TReturnType] = {
    selectAndRun(input, getStrategyForGather)
  }

  override def gatherDataWithDelayedMeasure[TArgType, TReturnType](input: SelectionInput[TArgType, TReturnType]): (TReturnType, InvocationTokenWithCallbacks) = {
    selectAndRunWithDelayedMeasure(input, getStrategyForGather)
  }
}
