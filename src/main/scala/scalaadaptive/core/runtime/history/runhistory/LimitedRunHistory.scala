package scalaadaptive.core.runtime.history.runhistory

import org.apache.commons.math3.stat.descriptive.{StatisticalSummary, SummaryStatistics}

import scalaadaptive.core.runtime.history.HistoryKey
import scalaadaptive.core.runtime.history.evaluation.data.{EvaluationData, GroupedEvaluationData}
import scalaadaptive.math.SimpleTestableRegression

/**
  * Created by Petr Kubat on 6/5/17.
  *
  * A wrapper for [[RunHistory]] that has a maximum number of records that can be present. After reaching that number,
  * adding a new record will cause throwing the older half of records away.
  *
  * @param limit The maximum number of records.
  * @param internalHistory The internal history that the calls are delegated to.
  * @param internalHistoryFactory A factory method that can be used to create new instances of the internal history.
  */
class LimitedRunHistory[TMeasurement](val limit: Int,
                                      private val internalHistory: RunHistory[TMeasurement],
                                      private val internalHistoryFactory: () => RunHistory[TMeasurement])
  extends RunHistory[TMeasurement] {

  override def applyNewRun(runResult: EvaluationData[TMeasurement]): RunHistory[TMeasurement] = {
    if (internalHistory.runCount < limit) {
      return new LimitedRunHistory(limit, internalHistory.applyNewRun(runResult), internalHistoryFactory)
    }

    val keep = limit / 2
    val toKeep = internalHistory.runItems.take(keep).toList
    val emptyHistory = internalHistoryFactory()
    val newHistory = toKeep.foldRight(emptyHistory)((data, h) => h.applyNewRun(data))
    new LimitedRunHistory(limit, newHistory, internalHistoryFactory)
  }

  // Delegations:
  override def key: HistoryKey = internalHistory.key
  override def runCount: Int = internalHistory.runCount
  override def runItems: Iterable[EvaluationData[TMeasurement]] = internalHistory.runItems
  override def runAveragesGroupedByDescriptor: Map[Long, GroupedEvaluationData[TMeasurement]] =
    internalHistory.runAveragesGroupedByDescriptor
  override def takeWhile(filter: (EvaluationData[TMeasurement]) => Boolean): RunHistory[TMeasurement] =
    internalHistory.takeWhile(filter)
  override def filter(filter: (EvaluationData[TMeasurement]) => Boolean): RunHistory[TMeasurement] =
    internalHistory.filter(filter)
  override def runStatistics: StatisticalSummary = internalHistory.runStatistics

  override def minDescriptor: Option[Long] = internalHistory.minDescriptor
  override def maxDescriptor: Option[Long] = internalHistory.maxDescriptor

  override def runRegression: SimpleTestableRegression = internalHistory.runRegression
}
