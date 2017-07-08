package scalaadaptive.core.runtime.history.runhistory

import org.apache.commons.math3.stat.descriptive.{StatisticalSummary, SummaryStatistics}

import scala.collection.immutable.SortedMap
import scalaadaptive.core.runtime.history.HistoryKey
import scalaadaptive.core.runtime.history.evaluation.data.{EvaluationData, GroupedEvaluationData}
import scalaadaptive.extensions.Averageable
import scalaadaptive.math.SimpleTestableRegression

/**
  * Created by pk250187 on 7/1/17.
  *
  * A RunHistory wrapper that computes linear regression of all the runs added.
  *
  * The implementation is mutable, applyNewRun() changes the original object.
  * (The reason is that the SimpleRegression class does not have a suitable copy / clone method and has inaccessible
  * state).
  *
  */
class CachedRegressionRunHistory[TMeasurement]private (private val internalHistory: RunHistory[TMeasurement],
                                                       private val internalRegression: SimpleTestableRegression)
                                              (implicit num: Averageable[TMeasurement])
  extends RunHistory[TMeasurement] {

  def this(internalHistory: RunHistory[TMeasurement])(implicit num: Averageable[TMeasurement]) =
    this(internalHistory, new SimpleTestableRegression())

  override def applyNewRun(runResult: EvaluationData[TMeasurement]): RunHistory[TMeasurement] = {
    if (runResult.inputDescriptor.isDefined) {
      internalRegression.addData(runResult.inputDescriptor.get, num.toDouble(runResult.measurement))
    }
    val newHistory = internalHistory.applyNewRun(runResult)
    new CachedRegressionRunHistory[TMeasurement](newHistory, internalRegression)
  }

  override def runRegression: SimpleTestableRegression = internalRegression

  // Delegations:
  override def key: HistoryKey = internalHistory.key
  override def runCount: Int = internalHistory.runCount
  override def runItems: Iterable[EvaluationData[TMeasurement]] = internalHistory.runItems
  override def best(): Option[Double] = internalHistory.best()
  override def runAveragesGroupedByDescriptor: Map[Long, GroupedEvaluationData[TMeasurement]] =
    internalHistory.runAveragesGroupedByDescriptor
  override def average(): Option[Double] = internalHistory.average()
  override def takeWhile(filter: (EvaluationData[TMeasurement]) => Boolean): RunHistory[TMeasurement] =
    internalHistory.takeWhile(filter)
  override def filter(filter: (EvaluationData[TMeasurement]) => Boolean): RunHistory[TMeasurement] =
    internalHistory.filter(filter)

  override def minDescriptor: Option[Long] = internalHistory.minDescriptor
  override def maxDescriptor: Option[Long] = internalHistory.maxDescriptor

  override def runStatistics: StatisticalSummary = internalHistory.runStatistics
}
