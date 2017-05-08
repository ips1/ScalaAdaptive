package scalaadaptive.core.runtime.history.runhistory

import scala.collection.mutable.ArrayBuffer
import scalaadaptive.core.runtime.history.runhistory.defaults.{DefaultAverage, DefaultBest, DefaultGrouping, DefaultStatistics}
import scalaadaptive.core.runtime.history.HistoryKey
import scalaadaptive.core.runtime.history.rundata.RunData
import scalaadaptive.extensions.Averageable

/**
  * Created by pk250187 on 3/21/17.
  */
class FullRunHistory[TMeasurement] private (override val key: HistoryKey,
                                   override val runItems: ArrayBuffer[RunData[TMeasurement]])
                                  (implicit override val num: Averageable[TMeasurement])
  extends RunHistory[TMeasurement]
    with DefaultGrouping[TMeasurement]
    with DefaultStatistics[TMeasurement]
    with DefaultBest[TMeasurement]
    with DefaultAverage[TMeasurement] {
  def this(key: HistoryKey)(implicit num: Averageable[TMeasurement]) =
    this(key, new ArrayBuffer[RunData[TMeasurement]]())

  override def runCount: Int = runItems.size
  override def applyNewRun(runResult: RunData[TMeasurement]): FullRunHistory[TMeasurement] = {
    runItems.append(runResult)
    this
  }

  override def takeWhile(filter: (RunData[TMeasurement]) => Boolean): RunHistory[TMeasurement] = {
    val filteredItems = runItems.reverseIterator.takeWhile(filter).toIterable
    new ImmutableFullRunHistory[TMeasurement](key, filteredItems)
  }
}
