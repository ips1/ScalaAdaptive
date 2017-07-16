package scalaadaptive.analytics

import java.io.PrintWriter

import scalaadaptive.core.runtime.AdaptiveInternal

/**
  * Created by Petr Kubat on 6/5/17.
  *
  * Simple implementation of [[AnalyticsData]] that internally holds a sequence of analytics records and uses the
  * pre-configured shared analytics serializer.
  *
  */
class BasicAnalyticsData(val records: Seq[AnalyticsRunRecord]) extends AnalyticsData {
  private def serializer: AnalyticsSerializer = AdaptiveInternal.getAnalyticsSerializer

  override def getAllRunInfo: Seq[AnalyticsRunRecord] = records

  override def saveData(out: PrintWriter): Unit = {
    records.foreach(r => out.println(serializer.serializeRecord(r)))
    out.flush()
  }
}
