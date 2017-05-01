package scalaadaptive.core.configuration.defaults

import scalaadaptive.core.configuration.BaseLongConfiguration
import scalaadaptive.core.performance.{PerformanceProvider, RunTimeProvider}

/**
  * Created by pk250187 on 5/1/17.
  */
trait RunTimeMeasurement extends BaseLongConfiguration {
  override val performanceProvider: PerformanceProvider[Long] = new RunTimeProvider
}
