package runtime

import grouping.GroupSelector
import logging.Logger
import performance.PerformanceProvider
import runtime.history.{HistoryKey, HistoryStorage}
import runtime.selection.RunSelector

/**
  * Created by pk250187 on 3/19/17.
  */
class RunTracker[TMeasurement](historyStorage: HistoryStorage[TMeasurement],
                               runSelector: RunSelector[TMeasurement],
                               performanceProvider: PerformanceProvider[TMeasurement],
                               bucketSelector: GroupSelector,
                               logger: Logger) extends FunctionRunner {
  override def runOption[TReturnType](options: Seq[ReferencedFunction[TReturnType]], byValue: Int = 0): TReturnType = {
    // TODO: byValue not specified
    val targetBucket = if (byValue != 0) bucketSelector.selectGroupForValue(byValue) else bucketSelector.defaultGroup
    logger.log(s"Target bucket: ${targetBucket}, byValue: ${byValue}")

    val histories = options.map(f => historyStorage.getHistory(new HistoryKey(f.reference, targetBucket)))

    val selectedRecord = runSelector.selectOption(histories)
    logger.log(s"Selected option: ${selectedRecord.reference}")
    val (result, performance) = performanceProvider.measureFunctionRun(options.find(_.reference == selectedRecord.reference).get.fun)
    logger.log(s"Performance on ${byValue} measured: ${performance.measurement}")
    selectedRecord.applyNewRun(performance)
    return result
  }
}
