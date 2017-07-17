package scalaadaptive.core.functions.analytics

import scalaadaptive.analytics.AnalyticsData
import scalaadaptive.core.functions.RunData

/**
  * Created by Petr Kubat on 6/5/17.
  *
  * A collector of [[AnalyticsData]].
  *
  */
trait AnalyticsCollector {
  /** Adds run data to the analytics */
  def applyRunData(data: RunData): Unit
  /** Retrieves collected [[scalaadaptive.analytics.AnalyticsData]] */
  def getAnalyticsData: Option[AnalyticsData]
}
