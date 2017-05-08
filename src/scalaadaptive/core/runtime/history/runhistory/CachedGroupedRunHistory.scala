package scalaadaptive.core.runtime.history.runhistory

import scalaadaptive.core.runtime.history.rundata.{ArtificialRunData, GroupedRunData, RunData}
import scalaadaptive.core.runtime.history.runhistory.defaults.DefaultStatistics
import scalaadaptive.core.runtime.history.HistoryKey
import scalaadaptive.extensions.Averageable
import scalaadaptive.extensions.IntExtensions._

/**
  * Created by pk250187 on 4/25/17.
  */

class CachedGroupedRunHistory[TMeasurement] private(override val runCount: Int,
                                                    private val internalHistory: RunHistory[TMeasurement],
                                                    private val data: Map[Option[Long], GroupedRunData[TMeasurement]])
                                                   (implicit override val num: Averageable[TMeasurement]) extends RunHistory[TMeasurement]
  with DefaultStatistics[TMeasurement] {

  def this(internalHistory: RunHistory[TMeasurement])(implicit num: Averageable[TMeasurement]) =
    this(0, internalHistory, Map())

  private def applyNewRunToTheGroupedRunData(groupedRunData: GroupedRunData[TMeasurement],
                                             runData: RunData[TMeasurement]) = {
    val newCount = groupedRunData.runCount + 1
    val newAverage =
      num.newAverage(groupedRunData.averageRunData.measurement, groupedRunData.runCount, runData.measurement)

    new GroupedRunData[TMeasurement](new ArtificialRunData[TMeasurement](runData.inputDescriptor, newAverage), newCount)
  }

  override def applyNewRun(runResult: RunData[TMeasurement]): RunHistory[TMeasurement] = {
    val group = data.get(runResult.inputDescriptor)

    val newData = group match {
      case Some(existingGroup) => applyNewRunToTheGroupedRunData(existingGroup, runResult)
      case None => new GroupedRunData[TMeasurement](runResult, 1)
    }

    val newInternalHistory = internalHistory.applyNewRun(runResult)

    new CachedGroupedRunHistory[TMeasurement](
      runCount + 1,
      newInternalHistory,
      data + (runResult.inputDescriptor -> newData))
  }

  override def runAveragesGroupedByDescriptor: Map[Option[Long], GroupedRunData[TMeasurement]] = data

  // Delegations:
  override def runItems: Iterable[RunData[TMeasurement]] = internalHistory.runItems
  override def average(): Option[Double] = internalHistory.average()
  override def best(): Option[Double] = internalHistory.best()
  override def takeWhile(filter: (RunData[TMeasurement]) => Boolean): RunHistory[TMeasurement] =
    internalHistory.takeWhile(filter)
  override def key: HistoryKey = internalHistory.key
}
