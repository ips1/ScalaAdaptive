package scalaadaptive.analytics

import java.io.{File, FileOutputStream, PrintWriter}

import scalaadaptive.core.functions.statistics.StatisticsRecord

/**
  * Created by pk250187 on 6/5/17.
  */
trait AnalyticsData {
  def getAllRunInfo: Seq[StatisticsRecord]
  def saveData(out: PrintWriter)
  def saveData(file: File): Unit = {
    file.getParentFile.mkdirs()
    val writer = new PrintWriter(new FileOutputStream(file, false))
    saveData(writer)
    writer.close()
  }
}
