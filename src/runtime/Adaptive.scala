package runtime

import grouping.LogarithmBucketSelector
import performance.RunTimeProvider
import runtime.history.MapHistoryStorage

/**
  * Created by pk250187 on 3/19/17.
  */
object Adaptive {
  def initTracker(): RunTracker = {
    new RunTracker(
      new MapHistoryStorage[Long],
      new SimpleRunSelector,
      new RunTimeProvider,
      new LogarithmBucketSelector)
  }

  lazy val tracker = initTracker()
}
