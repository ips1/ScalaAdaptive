package scalaadaptive.core.functions.analytics

import java.time.Instant

import scala.collection.mutable.ArrayBuffer
import scalaadaptive.analytics.{AnalyticsData, AnalyticsRecord, BasicAnalyticsData}
import scalaadaptive.core.functions.RunData

/**
  * Created by pk250187 on 6/5/17.
  */
class BasicAnalyticsCollector extends AnalyticsCollector {
  private val records: ArrayBuffer[AnalyticsRecord] = new ArrayBuffer[AnalyticsRecord]()

  override def applyRunData(data: RunData): Unit = records.append(new AnalyticsRecord(Instant.now,
    data.selectedFunction,
    data.inputDescriptor,
    data.performance.getFunctionTime,
    data.performance.getOverheadTime,
    data.performance.getOverheadPercentage))

  override def getAnalyticsData: Option[AnalyticsData] = Some(new BasicAnalyticsData(records))
}
