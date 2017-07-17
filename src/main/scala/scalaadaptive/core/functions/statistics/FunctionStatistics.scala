package scalaadaptive.core.functions.statistics

import java.time.Instant

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scalaadaptive.analytics.AnalyticsRunRecord
import scalaadaptive.core.functions.identifiers.{FunctionIdentifier, IdentifiedFunction}
import scalaadaptive.core.functions.RunData

/**
  * Created by Petr Kubat on 5/20/17.
  *
  * An implementation of the [[StatisticsHolder]] for a [[scalaadaptive.core.functions.CombinedFunction]].
  * Uses a resolver function to retrieve implementations for identifiers (necessary to update the functions from
  * [[StatisticFunctionProvider]].
  *
  * @param defaultLast The function that will act as last selected at the beginning.
  * @param functionResolver Resolver function that transforms function identifiers to actual functions. Provided by the
  *                         [[scalaadaptive.core.functions.CombinedFunction]].
  *
  */
class FunctionStatistics[TArgType, TRetType](defaultLast: IdentifiedFunction[TArgType, TRetType],
                                             val functionResolver: (FunctionIdentifier) => Option[IdentifiedFunction[TArgType,TRetType]])
  extends StatisticsHolder[TArgType, TRetType] {
  val timesSelected: mutable.HashMap[FunctionIdentifier, Long] = new mutable.HashMap[FunctionIdentifier, Long]
  private var last: IdentifiedFunction[TArgType, TRetType] = defaultLast
  private var streakLength: Long = 0
  private var totalRunCount: Long = 0
  private var totalSelectCount: Long = 0
  private var totalGatherCount: Long = 0
  private var totalFunctionTime: Long = 0
  private var totalOverheadTime: Long = 0
  private var totalGatherTime: Long = 0

  private def getMostSelectedRecord: (FunctionIdentifier, Long) =
    timesSelected.maxBy(_._2)

  override def applyRunData(data: RunData, markAsGather: Boolean): Unit = {
    // The run is only counted into streaks and selected counts if it wasn't marked as gather
    // (when gathering, the running function isn't selected)
    if (!markAsGather) {
      val timesBefore: Long = timesSelected.getOrElse(data.selectedFunction, 0)
      timesSelected.put(data.selectedFunction, timesBefore + 1)
      if (last.identifier == data.selectedFunction) {
        streakLength += 1
      }
      else {
        last = functionResolver(data.selectedFunction).getOrElse(defaultLast)
        streakLength = 1
      }
      totalSelectCount += 1
    }

    totalFunctionTime += data.performance.getFunctionTime
    totalOverheadTime += data.performance.getOverheadTime

    if (markAsGather) {
      totalGatherTime += (data.performance.getFunctionTime + data.performance.getOverheadTime)
      totalGatherCount += 1
    }
  }

  override def markRun(): Unit = {
    totalRunCount += 1
  }

  override def getLast: IdentifiedFunction[TArgType, TRetType] = last
  override def getStreakLength: Long = streakLength
  override def getTotalOverheadTime: Long = totalOverheadTime

  override def getMostSelectedFunction: IdentifiedFunction[TArgType, TRetType] =
    if (timesSelected.isEmpty)
      defaultLast
    else
      functionResolver(getMostSelectedRecord._1).getOrElse(defaultLast)

  override def getLeastSelectedFunction: IdentifiedFunction[TArgType, TRetType] =
    if (timesSelected.isEmpty)
      defaultLast
    else
      functionResolver(timesSelected.minBy(_._2)._1).getOrElse(defaultLast)

  override def getTotalRunCount: Long = totalRunCount
  override def getLastSelectCount: Long = timesSelected.getOrElse(last.identifier, 0)
  override def getMostSelectCount: Long = if (timesSelected.isEmpty) 0 else getMostSelectedRecord._2
  override def getTotalGatherTime: Long = totalGatherTime
  override def getTotalTime: Long = totalFunctionTime + totalOverheadTime
  override def getTotalFunctionTime: Long = totalFunctionTime
  override def getTotalGatherCount: Long = totalGatherCount
  override def getTotalSelectCount: Long = totalSelectCount
}
