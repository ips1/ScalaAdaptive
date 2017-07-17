package scalaadaptive.core.configuration.blocks.analytics

import scalaadaptive.core.configuration.Configuration
import scalaadaptive.core.functions.analytics.{AnalyticsCollector, EmptyAnalyticsCollector}

/**
  * Created by Petr Kubat on 7/12/17.
  *
  * A block that uses the [[scalaadaptive.core.functions.analytics.EmptyAnalyticsCollector]] as an implementation for
  * [[scalaadaptive.core.functions.analytics.AnalyticsCollector]], therefore does not collect any analytics.
  *
  */
trait NoAnalyticsCollection extends Configuration {
  override def createAnalyticsCollector: AnalyticsCollector = new EmptyAnalyticsCollector
}
