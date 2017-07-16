package scalaadaptive.api.adaptors

import scalaadaptive.analytics.AnalyticsData

/**
  * Created by Petr Kubat on 6/28/17.
  */
trait MultiFunctionAnalytics {
  def getAnalyticsData: Option[AnalyticsData]
}
