package scalaadaptive.core.runtime.history.runhistory

import scalaadaptive.core.runtime.history.runhistory.defaults.DefaultStatistics
import scalaadaptive.core.runtime.history.HistoryKey
import scalaadaptive.core.runtime.history.evaluation.data.{EvaluationData, GroupedEvaluationData}
import scalaadaptive.extensions.Averageable
import scalaadaptive.math.SimpleTestableRegression

/**
  * Created by Petr Kubat on 4/25/17.
  */

class CachedGroupedRunHistory[TMeasurement] private(override val runCount: Int,
                                                    private val internalHistory: RunHistory[TMeasurement],
                                                    private val data: Map[Long, GroupedEvaluationData[TMeasurement]])
                                                   (implicit override val num: Averageable[TMeasurement]) extends RunHistory[TMeasurement]
  with DefaultStatistics[TMeasurement] {

  def this(internalHistory: RunHistory[TMeasurement])(implicit num: Averageable[TMeasurement]) =
    this(0, internalHistory, Map())

  private def applyNewRunToTheGroupedRunData(groupedRunData: GroupedEvaluationData[TMeasurement],
                                             newRun: EvaluationData[TMeasurement]) = {
    val newCount = groupedRunData.runCount + 1
    val newAverage =
      num.newAverage(groupedRunData.averageMeasurement, groupedRunData.runCount, newRun.measurement)

    new GroupedEvaluationData[TMeasurement](newAverage, newCount)
  }

  override def applyNewRun(runResult: EvaluationData[TMeasurement]): RunHistory[TMeasurement] = {
    val newInternalHistory = internalHistory.applyNewRun(runResult)

    val updatedData = if (runResult.inputDescriptor.isEmpty)
      data
    else {
      val group = data.get(runResult.inputDescriptor.get)

      val newData = group match {
        case Some(existingGroup) => applyNewRunToTheGroupedRunData(existingGroup, runResult)
        case None => new GroupedEvaluationData[TMeasurement](runResult.measurement, 1)
      }

      data + (runResult.inputDescriptor.get -> newData)
    }



    new CachedGroupedRunHistory[TMeasurement](
      runCount + 1,
      newInternalHistory,
      updatedData
    )
  }

  override def runAveragesGroupedByDescriptor: Map[Long, GroupedEvaluationData[TMeasurement]] = data

  // Delegations:
  override def runItems: Iterable[EvaluationData[TMeasurement]] = internalHistory.runItems
  override def takeWhile(filter: (EvaluationData[TMeasurement]) => Boolean): RunHistory[TMeasurement] =
    internalHistory.takeWhile(filter)
  override def filter(filter: (EvaluationData[TMeasurement]) => Boolean): RunHistory[TMeasurement] =
    internalHistory.filter(filter)
  override def key: HistoryKey = internalHistory.key

  override def minDescriptor: Option[Long] = internalHistory.minDescriptor
  override def maxDescriptor: Option[Long] = internalHistory.maxDescriptor

  override def runRegression: SimpleTestableRegression = internalHistory.runRegression
}
