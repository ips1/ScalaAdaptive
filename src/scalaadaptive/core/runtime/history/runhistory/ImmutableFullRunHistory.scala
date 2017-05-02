package scalaadaptive.core.runtime.history.runhistory

import scalaadaptive.core.runtime.history.runhistory.defaults.{DefaultAverage, DefaultBest, DefaultGrouping, DefaultStatistics}
import scalaadaptive.core.runtime.history.{HistoryKey, RunData}
import scalaadaptive.extensions.Averageable

/**
  * Created by pk250187 on 5/1/17.
  */
class ImmutableFullRunHistory[TMeasurement] private (override val key: HistoryKey,
                                            override val runItems: List[RunData[TMeasurement]],
                                            val sum: TMeasurement)
                                           (implicit override val num: Averageable[TMeasurement])
  extends RunHistory[TMeasurement]
    with DefaultGrouping[TMeasurement]
    with DefaultAverage[TMeasurement]
    with DefaultBest[TMeasurement]
    with DefaultStatistics[TMeasurement] {

  def this(key: HistoryKey)(implicit num: Averageable[TMeasurement]) =
    this(key, List(), num.zero)

  override def runCount: Int = runItems.size
  override def applyNewRun(runResult: RunData[TMeasurement]): ImmutableFullRunHistory[TMeasurement] = {
    val newSum = num.plus(sum, runResult.measurement)
    val newItems = runResult :: runItems
    new ImmutableFullRunHistory[TMeasurement](key, newItems, newSum)
  }
}
