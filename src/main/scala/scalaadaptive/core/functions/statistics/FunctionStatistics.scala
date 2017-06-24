package scalaadaptive.core.functions.statistics

import java.time.Instant

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scalaadaptive.analytics.AnalyticsRecord
import scalaadaptive.core.functions.references.{FunctionReference, ReferencedFunction}
import scalaadaptive.core.functions.RunData

/**
  * Created by pk250187 on 5/20/17.
  */
class FunctionStatistics[TArgType, TRetType](defaultLast: ReferencedFunction[TArgType, TRetType],
                                             val referenceResolver: (FunctionReference) => Option[ReferencedFunction[TArgType,TRetType]])
  extends StatisticsHolder[TArgType, TRetType] {
  val timesSelected: mutable.HashMap[FunctionReference, Long] = new mutable.HashMap[FunctionReference, Long]
  private var last: ReferencedFunction[TArgType, TRetType] = defaultLast
  private var streakLength: Long = 0
  private var totalRunCount: Long = 0
  private var totalSelectCount: Long = 0
  private var totalGatherCount: Long = 0
  private var totalFunctionTime: Long = 0
  private var totalOverheadTime: Long = 0
  private var totalGatherTime: Long = 0

  private def getMostSelectedRecord: (FunctionReference, Long) =
    timesSelected.maxBy(_._2)

  override def applyRunData(data: RunData, markAsGather: Boolean): Unit = {
    // The run is only counted into streaks and selected counts if it wasn't marked as gather
    // (when gathering, the running function isn't selected)
    if (!markAsGather) {
      val timesBefore: Long = timesSelected.getOrElse(data.selectedFunction, 0)
      timesSelected.put(data.selectedFunction, timesBefore + 1)
      if (last.reference == data.selectedFunction) {
        streakLength += 1
      }
      else {
        last = referenceResolver(data.selectedFunction).getOrElse(defaultLast)
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

  override def getLast: ReferencedFunction[TArgType, TRetType] = last
  override def getStreakLength: Long = streakLength
  override def getTotalOverheadTime: Long = totalOverheadTime

  override def getMostSelectedFunction: ReferencedFunction[TArgType, TRetType] =
    if (timesSelected.isEmpty)
      defaultLast
    else
      referenceResolver(getMostSelectedRecord._1).getOrElse(defaultLast)

  override def getLeastSelectedFunction: ReferencedFunction[TArgType, TRetType] =
    if (timesSelected.isEmpty)
      defaultLast
    else
      referenceResolver(timesSelected.minBy(_._2)._1).getOrElse(defaultLast)

  override def getTotalRunCount: Long = totalRunCount
  override def getLastRunCount: Long = timesSelected.getOrElse(last.reference, 0)
  override def getMostRunCount: Long = if (timesSelected.isEmpty) 0 else getMostSelectedRecord._2
  override def getTotalGatherTime: Long = totalGatherTime
  override def getTotalTime: Long = totalFunctionTime + totalOverheadTime
  override def getTotalFunctionTime: Long = totalFunctionTime
  override def getTotalGatherCount: Long = totalGatherCount
  override def getTotalSelectCount: Long = totalSelectCount
}
