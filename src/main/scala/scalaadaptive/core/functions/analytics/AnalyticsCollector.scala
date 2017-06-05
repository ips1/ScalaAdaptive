package scalaadaptive.core.functions.analytics

import scalaadaptive.analytics.AnalyticsData
import scalaadaptive.core.functions.RunData

/**
  * Created by pk250187 on 6/5/17.
  */
trait AnalyticsCollector {
  def applyRunData(data: RunData): Unit
  def getAnalyticsData: Option[AnalyticsData]
}
