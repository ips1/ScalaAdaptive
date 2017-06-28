package scalaadaptive.api.adaptors

import scalaadaptive.analytics.AnalyticsData

/**
  * Created by pk250187 on 6/28/17.
  */
trait MultiFunctionAnalytics {
  def getAnalyticsData: Option[AnalyticsData]
}
