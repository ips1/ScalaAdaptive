package scalaadaptive.core.configuration.blocks

import scalaadaptive.core.configuration.BaseLongConfiguration
import scalaadaptive.core.performance.{MeasurementProvider, RunTimeProvider}

/**
  * Created by pk250187 on 5/1/17.
  */
trait RunTimeMeasurement extends BaseLongConfiguration {
  override val performanceProvider: MeasurementProvider[Long] = new RunTimeProvider
}
