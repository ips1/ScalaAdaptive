package scalaadaptive.analytics

import java.io.PrintWriter

import scalaadaptive.core.functions.statistics.StatisticsRecord

/**
  * Created by pk250187 on 6/5/17.
  */
class BasicAnalyticsData(val records: Seq[StatisticsRecord],
                         val serializer: AnalyticsSerializer) extends AnalyticsData {
  override def getAllRunInfo: Seq[StatisticsRecord] = records

  override def saveData(out: PrintWriter): Unit = {
    records.foreach(r => out.println(serializer.serializeRecord(r)))
    out.flush()
  }
}
