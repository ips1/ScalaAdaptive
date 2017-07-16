package scalaadaptive.core.configuration.blocks

import scalaadaptive.core.configuration.Configuration
import scalaadaptive.core.functions.analytics.{AnalyticsCollector, EmptyAnalyticsCollector}

/**
  * Created by Petr Kubat on 7/12/17.
  */
trait NoAnalyticsCollection extends Configuration {
  override val createAnalyticsCollector: () => AnalyticsCollector = () => new EmptyAnalyticsCollector
}
