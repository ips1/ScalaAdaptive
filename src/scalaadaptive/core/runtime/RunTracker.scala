package scalaadaptive.core.runtime

import java.time.{Duration, Instant}

import scalaadaptive.core.grouping.GroupSelector
import scalaadaptive.core.logging.Logger
import scalaadaptive.core.options.Selection
import scalaadaptive.core.options.Selection.Selection
import scalaadaptive.core.performance.{PerformanceProvider, PerformanceTracker, PerformanceTrackerImpl}
import scalaadaptive.core.references.FunctionReference
import scalaadaptive.core.runtime.history.historystorage.HistoryStorage
import scalaadaptive.core.runtime.history.HistoryKey
import scalaadaptive.core.runtime.history.rundata.{RunData, TimestampedRunData}
import scalaadaptive.core.runtime.history.runhistory.RunHistory
import scalaadaptive.core.runtime.selection.RunSelector

/**
  * Created by pk250187 on 3/19/17.
  */
class RunTracker[TMeasurement](historyStorage: HistoryStorage[TMeasurement],
                               discreteRunSelector: RunSelector[TMeasurement],
                               continuousRunSelector: RunSelector[TMeasurement],
                               performanceProvider: PerformanceProvider[TMeasurement],
                               bucketSelector: GroupSelector,
                               logger: Logger) extends FunctionRunner {

  private def filterHistoryByDuration(history: RunHistory[TMeasurement], duration: Duration) = {
    val currentTime = Instant.now()
    history.takeWhile(
      rd => rd.time.isDefined && Duration.between(rd.time.get, currentTime).compareTo(duration) < 0)
  }

  private def getSelectorForSelection(selection: Selection) = selection match {
    case Selection.Continuous => continuousRunSelector
    case Selection.Discrete => discreteRunSelector
  }

  private def selectRecord[TReturnType](options: Seq[ReferencedFunction[TReturnType]],
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

  override def runOption[TReturnType](options: Seq[ReferencedFunction[TReturnType]],
                                      inputDescriptor: Option[Long],
                                      limitedBy: Option[Duration],
                                      selection: Selection): TReturnType = {
    // TODO: inputDescriptor not specified

    val tracker = new PerformanceTrackerImpl
    tracker.startTracking()

    val selectedRecord = selectRecord(options, getSelectorForSelection(selection), inputDescriptor, limitedBy)
    val functionToRun = options.find(_.reference == selectedRecord.reference).get.fun

    tracker.addSelectionTime()
    runMeasuredFunction(functionToRun, selectedRecord.key, inputDescriptor, tracker)
  }

  override def runOptionWithDelayedMeasure[TReturnType](options: Seq[ReferencedFunction[TReturnType]],
                                                        inputDescriptor: Option[Long],
                                                        limitedBy: Option[Duration],
                                                        selection: Selection): (TReturnType, MeasurementToken) = {
    val tracker = new PerformanceTrackerImpl
    tracker.startTracking()

    val selectedRecord = selectRecord(options, getSelectorForSelection(selection), inputDescriptor, limitedBy)
    val functionToRun = options.find(_.reference == selectedRecord.reference).get.fun

    tracker.addSelectionTime()
    (functionToRun(), new MeasurementTokenImplementation(this, inputDescriptor, selectedRecord.key, tracker))
  }

  override def runMeasuredFunction[TReturnType](fun: () => TReturnType,
                                                       key: HistoryKey,
                                                       inputDescriptor: Option[Long],
                                                       tracker: PerformanceTracker): TReturnType = {
    tracker.startTracking()
    val (result, performance) = performanceProvider.measureFunctionRun(fun)
    tracker.addFunctionTime()
    logger.log(s"Performance on $inputDescriptor measured: $performance")
    historyStorage.applyNewRun(key, new TimestampedRunData[TMeasurement](inputDescriptor, Instant.now, performance))
    tracker.addStoringTime()
    tracker.getStatistics.lines.foreach(logger.log)
    result
  }

  override def flushHistory(reference: FunctionReference): Unit =
    historyStorage.flushHistory(reference)
}
