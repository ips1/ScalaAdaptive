package scalaadaptive.core.configuration.blocks.analytics

import scalaadaptive.core.configuration.Configuration
import scalaadaptive.core.functions.analytics.{AnalyticsCollector, BasicAnalyticsCollector}

/**
  * Created by Petr Kubat on 7/12/17.
  *
  * A block that uses [[scalaadaptive.core.functions.analytics.BasicAnalyticsCollector]] as an implementation for
  * [[scalaadaptive.core.functions.analytics.AnalyticsCollector]], therefore collects the analytics data.
  *
  */
trait AnalyticsCollection extends Configuration {
  override def createAnalyticsCollector: AnalyticsCollector =
    new BasicAnalyticsCollector
}
