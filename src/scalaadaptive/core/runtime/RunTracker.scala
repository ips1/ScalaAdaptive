package scalaadaptive.core.runtime

import scalaadaptive.core.grouping.GroupSelector
import scalaadaptive.core.logging.Logger
import scalaadaptive.core.performance.PerformanceProvider
import scalaadaptive.core.runtime.history.historystorage.HistoryStorage
import scalaadaptive.core.runtime.history.{HistoryKey, RunData}
import scalaadaptive.core.runtime.selection.RunSelector

/**
  * Created by pk250187 on 3/19/17.
  */
class RunTracker[TMeasurement](historyStorage: HistoryStorage[TMeasurement],
                               runSelector: RunSelector[TMeasurement],
                               performanceProvider: PerformanceProvider[TMeasurement],
                               bucketSelector: GroupSelector,
                               logger: Logger) extends FunctionRunner {

  private def selectRecord[TReturnType](options: Seq[ReferencedFunction[TReturnType]], inputDescriptor: Long) = {
    val targetBucket =
      if (inputDescriptor != 0) bucketSelector.selectGroupForValue(inputDescriptor)
      else bucketSelector.defaultGroup

    logger.log(s"Target bucket: $targetBucket, byValue: $inputDescriptor")

    val histories = options.map(f => historyStorage.getHistory(HistoryKey(f.reference, targetBucket)))

    val selectedRecord = runSelector.selectOption(histories, inputDescriptor)
    logger.log(s"Selected option: ${selectedRecord.reference}")
    selectedRecord
  }

  override def runOption[TReturnType](options: Seq[ReferencedFunction[TReturnType]],
                                      inputDescriptor: Long = 0): TReturnType = {
    // TODO: inputDescriptor not specified
    val selectedRecord = selectRecord(options, inputDescriptor)
    val functionToRun = options.find(_.reference == selectedRecord.reference).get.fun

    runMeasuredFunction(functionToRun, selectedRecord.key, inputDescriptor)
  }

  override def runOptionWithDelayedMeasure[TReturnType](options: Seq[ReferencedFunction[TReturnType]],
                                               inputDescriptor: Long = 0): (TReturnType, MeasurementToken) = {
    val selectedRecord = selectRecord(options, inputDescriptor)
    val functionToRun = options.find(_.reference == selectedRecord.reference).get.fun
    (functionToRun(), new MeasurementTokenImplementation(this, inputDescriptor, selectedRecord.key))
  }

  override def runMeasuredFunction[TReturnType](fun: () => TReturnType, key: HistoryKey, inputDescriptor: Long): TReturnType = {
    val (result, performance) = performanceProvider.measureFunctionRun(fun)
    logger.log(s"Performance on $inputDescriptor measured: $performance")
    historyStorage.applyNewRun(key, new RunData(inputDescriptor, performance))
    result
  }
}
