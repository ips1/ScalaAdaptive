package scalaadaptive.core.runtime

import java.time.{Duration, Instant}

import scalaadaptive.core.runtime.grouping.GroupSelector
import scalaadaptive.core.logging.Logger
import scalaadaptive.api.options.Selection
import scalaadaptive.api.options.Selection.Selection
import scalaadaptive.core.performance.{BasicPerformanceTracker, PerformanceTracker}
import scalaadaptive.core.functions.references.FunctionReference
import scalaadaptive.core.runtime.history.historystorage.HistoryStorage
import scalaadaptive.core.runtime.history.HistoryKey
import scalaadaptive.core.runtime.history.evaluation.EvaluationProvider
import scalaadaptive.core.runtime.history.evaluation.data.EvaluationData
import scalaadaptive.core.runtime.history.runhistory.RunHistory
import scalaadaptive.core.runtime.invocationtokens.{InvocationTokenWithCallbacks, MeasuringInvocationToken}
import scalaadaptive.core.runtime.selection.RunSelector
import scalaadaptive.core.functions.{ReferencedFunction, RunData, RunResult}

/**
  * Created by pk250187 on 3/19/17.
  */
class HistoryBasedAdaptiveRunner[TMeasurement](historyStorage: HistoryStorage[TMeasurement],
                                               discreteRunSelector: RunSelector[TMeasurement],
                                               continuousRunSelector: RunSelector[TMeasurement],
                                               measurementProvider: EvaluationProvider[TMeasurement],
                                               bucketSelector: GroupSelector,
                                               logger: Logger) extends AdaptiveRunner with DelayedFunctionRunner {

  private def filterHistoryByDuration(history: RunHistory[TMeasurement], duration: Duration) = {
    val currentTime = Instant.now()
    history.takeWhile(
      rd => Duration.between(rd.time, currentTime).compareTo(duration) < 0)
  }

  private def getSelectorForSelection(selection: Selection) = selection match {
    case Selection.Continuous => continuousRunSelector
    case Selection.Discrete => discreteRunSelector
  }

  private def selectRecord[TArgType, TReturnType](options: Seq[ReferencedFunction[TArgType, TReturnType]],
                                                  runSelector: RunSelector[TMeasurement],
                                                  inputDescriptor: Option[Long],
                                                  limitedBy: Option[Duration]) = {
    val targetBucket = inputDescriptor match {
      case Some(descriptor) => bucketSelector.selectGroupForValue(descriptor)
      case _ => bucketSelector.defaultGroup
    }

    logger.log(s"Target bucket: $targetBucket, byValue: $inputDescriptor")

    val allHistories = options.map(f => historyStorage.getHistory(HistoryKey(f.reference, targetBucket)))

    val histories = limitedBy match {
      case Some(duration) => allHistories.map(h => filterHistoryByDuration(h, duration))
      case _ => allHistories
    }

    val selectedRecord =
      if (histories.length > 1)
        runSelector.selectOption(histories, inputDescriptor)
      else
        histories.head

    logger.log(s"Selected option: ${selectedRecord.reference}")
    selectedRecord
  }

  override def runOption[TArgType, TReturnType](options: Seq[ReferencedFunction[TArgType, TReturnType]],
                                                arguments: TArgType,
                                                inputDescriptor: Option[Long],
                                                limitedBy: Option[Duration],
                                                selection: Selection): RunResult[TReturnType] = {
    val tracker = new BasicPerformanceTracker
    tracker.startTracking()

    val selectedRecord = selectRecord(options, getSelectorForSelection(selection), inputDescriptor, limitedBy)
    val functionToRun = options.find(_.reference == selectedRecord.reference).get.fun

    tracker.addSelectionTime()
    runMeasuredFunction(functionToRun, arguments, selectedRecord.key, inputDescriptor, tracker)
  }

  override def runOptionWithDelayedMeasure[TArgType, TReturnType](options: Seq[ReferencedFunction[TArgType, TReturnType]],
                                                                  arguments: TArgType,
                                                                  inputDescriptor: Option[Long],
                                                                  limitedBy: Option[Duration],
                                                                  selection: Selection): (TReturnType, InvocationTokenWithCallbacks) = {
    val tracker = new BasicPerformanceTracker
    tracker.startTracking()

    val selectedRecord = selectRecord(options, getSelectorForSelection(selection), inputDescriptor, limitedBy)
    val functionToRun = options.find(_.reference == selectedRecord.reference).get.fun

    tracker.addSelectionTime()
    (functionToRun(arguments), new MeasuringInvocationToken(this, inputDescriptor, selectedRecord.key, tracker))
  }

  override def runMeasuredFunction[TArgType, TReturnType](fun: (TArgType) => TReturnType,
                                                          arguments: TArgType,
                                                          key: HistoryKey,
                                                          inputDescriptor: Option[Long],
                                                          tracker: PerformanceTracker): RunResult[TReturnType] = {
    tracker.startTracking()
    val (result, measurement) = measurementProvider.evaluateFunctionRun(fun, arguments)
    tracker.addFunctionTime()
    logger.log(s"Performance on $inputDescriptor measured: $measurement")
    historyStorage.applyNewRun(key, new EvaluationData[TMeasurement](inputDescriptor, Instant.now, measurement))
    tracker.addStoringTime()
    tracker.getStatistics.lines.foreach(logger.log)
    new RunResult(result, new RunData(key.function, inputDescriptor, tracker))
  }

  override def flushHistory(reference: FunctionReference): Unit =
    historyStorage.flushHistory(reference)
}
