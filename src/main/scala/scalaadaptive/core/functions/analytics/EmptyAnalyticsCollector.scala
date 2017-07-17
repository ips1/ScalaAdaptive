package scalaadaptive.core.functions.analytics

import scalaadaptive.analytics.AnalyticsData
import scalaadaptive.core.functions.RunData

/**
  * Created by Petr Kubat on 6/5/17.
  *
  * A dummy implementation of [[AnalyticsCollector]] that throws the data away.
  * The point of this implementation is to save overhead when user does not work with the [[AnalyticsData]].
  *
  */
class EmptyAnalyticsCollector extends AnalyticsCollector {
  override def applyRunData(data: RunData): Unit = {}
  override def getAnalyticsData: Option[AnalyticsData] = None
}
