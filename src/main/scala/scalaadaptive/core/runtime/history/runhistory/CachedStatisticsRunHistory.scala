package scalaadaptive.core.runtime.history.runhistory

import org.apache.commons.math3.stat.descriptive.{StatisticalSummary, SummaryStatistics}

import scalaadaptive.core.runtime.history.HistoryKey
import scalaadaptive.core.runtime.history.evaluation.data.{GroupedEvaluationData, EvaluationData}
import scalaadaptive.extensions.Averageable

/**
  * Created by pk250187 on 5/2/17.
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
  override def best(): Option[Double] = internalHistory.best()
  override def runAveragesGroupedByDescriptor: Map[Option[Long], GroupedEvaluationData[TMeasurement]] =
    internalHistory.runAveragesGroupedByDescriptor
  override def average(): Option[Double] = internalHistory.average()
  override def takeWhile(filter: (EvaluationData[TMeasurement]) => Boolean): RunHistory[TMeasurement] =
    internalHistory.takeWhile(filter)
}
