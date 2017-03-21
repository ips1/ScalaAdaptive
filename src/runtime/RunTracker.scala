package runtime

import grouping.BucketSelector
import performance.PerformanceProvider
import runtime.history.{HistoryKey, HistoryStorage}

/**
  * Created by pk250187 on 3/19/17.
  */
class RunTracker(historyStorage: HistoryStorage[Long],
                 runSelector: RunSelector[Long],
                 performanceProvider: PerformanceProvider[Long],
                 bucketSelector: BucketSelector) {
  def runOption[TReturnType](options: Seq[ReferencedFunction[TReturnType]], byValue: Int = 0): TReturnType = {
    // TODO: byValue not specified
    val targetBucket = if (byValue != 0) bucketSelector.selectBucketForValue(byValue) else bucketSelector.defaultBucket
    println(s"Target bucket: ${targetBucket}, byValue: ${byValue}")

    val histories = options.map(f => historyStorage.getHistory(new HistoryKey(f.reference, targetBucket)))

    val selectedRecord = runSelector.selectOption(histories)
    val (result, performance) = performanceProvider.measureFunctionRun(options.find(_.reference == selectedRecord.reference).get.fun)
    selectedRecord.applyNewRun(performance)
    return result
  }
}
