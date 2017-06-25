package scalaadaptive.core.runtime.history.runhistory

import org.apache.commons.math3.stat.descriptive.StatisticalSummary

import scalaadaptive.core.runtime.history.HistoryKey
import scalaadaptive.core.runtime.history.evaluation.data.{EvaluationData, GroupedEvaluationData}
import scalaadaptive.extensions.Averageable

/**
  * Created by pk250187 on 4/25/17.
  */
class CachedAverageRunHistory[TMeasurement] private (private val internalHistory: RunHistory[TMeasurement],
                                                     private val internalAverage: Option[TMeasurement])
                                           (implicit num: Averageable[TMeasurement]) extends RunHistory[TMeasurement] {

  def this(internalHistory: RunHistory[TMeasurement])(implicit num: Averageable[TMeasurement]) =
    this(internalHistory, None)

  override def applyNewRun(runResult: EvaluationData[TMeasurement]): RunHistory[TMeasurement] =
    new CachedAverageRunHistory(internalHistory.applyNewRun(runResult),
      Some(num.newAverage(internalAverage.getOrElse(num.zero), internalHistory.runCount, runResult.measurement)))

  override def average(): Option[Double] = internalAverage.map(num.toDouble)

  // Delegations:
  override def key: HistoryKey = internalHistory.key
  override def runCount: Int = internalHistory.runCount
  override def runItems: Iterable[EvaluationData[TMeasurement]] = internalHistory.runItems
  override def best(): Option[Double] = internalHistory.best()
  override def runAveragesGroupedByDescriptor: Map[Long, GroupedEvaluationData[TMeasurement]] =
    internalHistory.runAveragesGroupedByDescriptor
  override def runStatistics: StatisticalSummary = internalHistory.runStatistics
  override def takeWhile(filter: (EvaluationData[TMeasurement]) => Boolean): RunHistory[TMeasurement] =
    internalHistory.takeWhile(filter)
}
