package scalaadaptive.core.functions.analytics

import scalaadaptive.analytics.AnalyticsData
import scalaadaptive.core.functions.RunData

/**
  * Created by pk250187 on 6/5/17.
  */
class EmptyAnalyticsCollector extends AnalyticsCollector {
  override def applyRunData(data: RunData): Unit = {}
  override def getAnalyticsData: Option[AnalyticsData] = None
}
