package scalaadaptive.core.runtime.history.runhistory

import org.apache.commons.math3.stat.descriptive.{StatisticalSummary, SummaryStatistics}

import scalaadaptive.core.runtime.history.HistoryKey
import scalaadaptive.core.runtime.history.evaluation.data.{EvaluationData, GroupedEvaluationData}
import scalaadaptive.extensions.Averageable
import scalaadaptive.math.SimpleTestableRegression

/**
  * Created by Petr Kubat on 5/2/17.
  *
  * A wrapper for [[RunHistory]] which caches the statistical data.
  *
  * The implementation is immutable and thread-safe (as long as the internal implementation is).
  * Adding new data creates a new instance from the wrapper.
  *
  * @param internalHistory The internal history that the calls get delegated to.
  * @param internalStatistics The cached statistics.
  */
class CachedStatisticsRunHistory[TMeasurement] private (private val internalHistory: RunHistory[TMeasurement],
                                                     private val internalStatistics: SummaryStatistics)
                                                    (implicit num: Averageable[TMeasurement])
  extends RunHistory[TMeasurement] {

  def this(internalHistory: RunHistory[TMeasurement])(implicit num: Averageable[TMeasurement]) =
    this(internalHistory, new SummaryStatistics())

  override def applyNewRun(runResult: EvaluationData[TMeasurement]): RunHistory[TMeasurement] = {
    val newStatistics = new SummaryStatistics(internalStatistics)
    newStatistics.addValue(num.toDouble(runResult.measurement))
    new CachedStatisticsRunHistory[TMeasurement](internalHistory.applyNewRun(runResult), newStatistics)
  }

  override def runStatistics: StatisticalSummary = internalStatistics

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

  override def minDescriptor: Option[Long] = internalHistory.minDescriptor
  override def maxDescriptor: Option[Long] = internalHistory.maxDescriptor

  override def runRegression: SimpleTestableRegression = internalHistory.runRegression
}
