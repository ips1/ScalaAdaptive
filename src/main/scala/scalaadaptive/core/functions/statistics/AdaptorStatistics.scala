package scalaadaptive.core.functions.statistics

import java.time.Instant

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scalaadaptive.analytics.{AnalyticsData, BasicAnalyticsData, CsvAnalyticsSerializer}
import scalaadaptive.core.performance.PerformanceProvider
import scalaadaptive.core.functions.references.FunctionReference
import scalaadaptive.core.functions.{ReferencedFunction, RunData}

/**
  * Created by pk250187 on 5/20/17.
  */
class AdaptorStatistics[TArgType, TRetType](defaultLast: ReferencedFunction[TArgType, TRetType],
                                            val referenceResolver: (FunctionReference) => Option[ReferencedFunction[TArgType,TRetType]])
  extends StatisticsHolder[TArgType, TRetType] {
  val records: ArrayBuffer[StatisticsRecord] = new ArrayBuffer[StatisticsRecord]()
  val timesSelected: mutable.HashMap[FunctionReference, Long] = new mutable.HashMap[FunctionReference, Long]
  private var last: ReferencedFunction[TArgType, TRetType] = defaultLast
  private var streakLength: Long = 0
  private var totalRunCount: Long = 0
  private var totalRunTime: Long = 0
  private var totalOverheadTime: Long = 0
  private var accumulatedOverheadTime: Long = 0
  private var totalOverheadPercentage: Double = 0

  override def applyRunData(data: RunData): Unit = {
    records.append(new StatisticsRecord(Instant.now,
      data.selectedFunction,
      data.inputDescriptor,
      data.performance.getFunctionTime,
      data.performance.getOverheadTime,
      data.performance.getOverheadPercentage))
    val timesBefore: Long = timesSelected.getOrElse(data.selectedFunction, 0)
    timesSelected.put(data.selectedFunction, timesBefore + 1)
    if (last.reference == data.selectedFunction) {
      streakLength += 1
    }
    else {
      last = referenceResolver(data.selectedFunction).getOrElse(defaultLast)
      streakLength = 1
    }

    totalRunCount += 1
    totalRunTime += data.performance.getFunctionTime
    accumulatedOverheadTime += data.performance.getOverheadTime
    totalOverheadTime += data.performance.getOverheadTime
    totalOverheadPercentage += totalOverheadTime.toDouble / totalRunTime
  }

  override def getLast: ReferencedFunction[TArgType, TRetType] = last
  override def getStreakLength: Long = streakLength
  override def getTotalOverheadTime: Long = totalOverheadTime
  override def getMostSelectedFunction: ReferencedFunction[TArgType, TRetType] =
    referenceResolver(timesSelected.maxBy(_._2)._1).getOrElse(defaultLast)
  override def getLeastSelectedFunction: ReferencedFunction[TArgType, TRetType] =
    referenceResolver(timesSelected.minBy(_._2)._1).getOrElse(defaultLast)
  override def getTotalRunCount: Long = totalRunCount
  override def getAccumulatedOverheadTime: Long = accumulatedOverheadTime
  override def getLastRunCount = timesSelected.getOrElse(last.reference, 0)
  override def resetAccumulatedOverheadTime(): Unit = {
    accumulatedOverheadTime = 0
  }
}
