package scalaadaptive.api.functions

import scalaadaptive.analytics.AnalyticsData

/**
  * Created by Petr Kubat on 6/28/17.
  *
  * A trait with common analytics functionality for the [[AdaptiveFunction0]] and corresponding types.
  *
  */
trait AdaptiveFunctionAnalytics {
  /**
    * Retrieves the analytics data for the adaptive function. See [[scalaadaptive.analytics]]
    * @return The analytics data if they were collected, None otherwise
    */
  def getAnalyticsData: Option[AnalyticsData]
}
