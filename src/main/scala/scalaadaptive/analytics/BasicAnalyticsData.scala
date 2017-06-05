package scalaadaptive.analytics

import java.io.PrintWriter

import scalaadaptive.core.runtime.AdaptiveInternal

/**
  * Created by pk250187 on 6/5/17.
  */
class BasicAnalyticsData(val records: Seq[AnalyticsRecord]) extends AnalyticsData {
  private def serializer: AnalyticsSerializer = AdaptiveInternal.getAnalyticsSerializer

  override def getAllRunInfo: Seq[AnalyticsRecord] = records

  override def saveData(out: PrintWriter): Unit = {
    records.foreach(r => out.println(serializer.serializeRecord(r)))
    out.flush()
  }
}
