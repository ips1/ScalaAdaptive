package scalaadaptive.core.runtime.history.runhistory

import scalaadaptive.core.runtime.history.rundata.{ArtificialRunData, GroupedRunData, RunData}
import scalaadaptive.core.runtime.history.runhistory.defaults.DefaultStatistics
import scalaadaptive.core.runtime.history.HistoryKey
import scalaadaptive.extensions.Averageable
import scalaadaptive.extensions.IntExtensions._

/**
  * Created by pk250187 on 4/25/17.
  */

class GroupedRunHistory[TMeasurement] private (override val key: HistoryKey,
                                      override val runCount: Int,
                                      private val data: Map[Option[Long], GroupedRunData[TMeasurement]])
                                     (implicit override val num: Averageable[TMeasurement]) extends RunHistory[TMeasurement]
  with DefaultStatistics[TMeasurement] {

  def this(key: HistoryKey)(implicit num: Averageable[TMeasurement]) =
    this(key, 0, Map())

  override def runItems: Iterable[RunData[TMeasurement]] = data
    .values
    .flatMap(data => data.runCount.times(() => data.averageRunData))

  override def average(): Option[Double] =
    if (runCount == 0) None else
      Some(data
        .values
        .map(data => data.runCount * num.toDouble(data.averageRunData.measurement))
        .sum / runCount)

  override def best(): Option[Double] =
    if (runCount == 0) None else
      Some(num.toDouble(
        data
        .values
        .map(_.averageRunData.measurement)
        .min)
  )

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

    new GroupedRunHistory[TMeasurement](key, runCount + 1, data + (runResult.inputDescriptor -> newData))
  }

  override def runAveragesGroupedByDescriptor: Map[Option[Long], GroupedRunData[TMeasurement]] = data

  // TODO: Implement this
  override def takeWhile(filter: (RunData[TMeasurement]) => Boolean): RunHistory[TMeasurement] = ???
}
