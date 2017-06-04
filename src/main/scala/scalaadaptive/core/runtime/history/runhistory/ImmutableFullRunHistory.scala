package scalaadaptive.core.runtime.history.runhistory

import scalaadaptive.core.runtime.history.runhistory.defaults.{DefaultAverage, DefaultBest, DefaultGrouping, DefaultStatistics}
import scalaadaptive.core.runtime.history.HistoryKey
import scalaadaptive.core.runtime.history.evaluation.data.EvaluationData
import scalaadaptive.extensions.Averageable

/**
  * Created by pk250187 on 5/1/17.
  */
class ImmutableFullRunHistory[TMeasurement] (override val key: HistoryKey,
                                             override val runItems: Iterable[EvaluationData[TMeasurement]])
                                           (implicit override val num: Averageable[TMeasurement])
  extends RunHistory[TMeasurement]
    with DefaultGrouping[TMeasurement]
    with DefaultAverage[TMeasurement]
    with DefaultBest[TMeasurement]
    with DefaultStatistics[TMeasurement] {

  def this(key: HistoryKey)(implicit num: Averageable[TMeasurement]) =
    this(key, List())

  override def runCount: Int = runItems.size
  override def applyNewRun(runResult: EvaluationData[TMeasurement]): ImmutableFullRunHistory[TMeasurement] = {
    val newItems = runResult :: runItems.toList
    new ImmutableFullRunHistory[TMeasurement](key, newItems)
  }

  override def takeWhile(filter: (EvaluationData[TMeasurement]) => Boolean): RunHistory[TMeasurement] = {
    val filteredItems = runItems.takeWhile(filter)
    new ImmutableFullRunHistory[TMeasurement](key, filteredItems)
  }
}
