package scalaadaptive.core.configuration.blocks.analytics

import scalaadaptive.core.configuration.Configuration
import scalaadaptive.core.functions.analytics.{AnalyticsCollector, BasicAnalyticsCollector}

/**
  * Created by Petr Kubat on 7/12/17.
  */
trait AnalyticsCollection extends Configuration {
  override def createAnalyticsCollector: AnalyticsCollector =
    new BasicAnalyticsCollector
}
