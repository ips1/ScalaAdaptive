package scalaadaptive.analytics

import java.io.{File, FileOutputStream, PrintWriter}

/**
  * Created by Petr Kubat on 6/5/17.
  *
  * A collection of analytics data from a single combined function ([[scalaadaptive.api.adaptors.MultiFunction0]] and
  * corresponding types). Allows accessing the data or saving them into a file in a fixed format.
  *
  */
trait AnalyticsData {
  /**
    * Retrieves collection of analytic records of all runs of the MultiFunction that this data come from.
    * @return Collection of AnalyticRecords
    */
  def getAllRunInfo: Seq[AnalyticsRunRecord]

  /**
    * Saves the data in a fixed format (determined by [[scalaadaptive.core.configuration.Configuration]]) using the
    * specified PrintWriter
    * @param out PrintWriter used to save the data
    */
  def saveData(out: PrintWriter)

  /**
    * Saves the data in a fixed format (determined by [[scalaadaptive.core.configuration.Configuration]]) the specified File
    * @param file File to save the data to
    */
  def saveData(file: File): Unit = {
    file.getParentFile.mkdirs()
    val writer = new PrintWriter(new FileOutputStream(file, false))
    saveData(writer)
    writer.close()
  }
}
