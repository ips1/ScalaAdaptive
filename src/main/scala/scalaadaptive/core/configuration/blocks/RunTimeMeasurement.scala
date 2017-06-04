package scalaadaptive.core.configuration.blocks

import scalaadaptive.core.configuration.BaseLongConfiguration
import scalaadaptive.core.runtime.history.evaluation.{EvaluationProvider, RunTimeProvider}

/**
  * Created by pk250187 on 5/1/17.
  */
trait RunTimeMeasurement extends BaseLongConfiguration {
  override val performanceProvider: EvaluationProvider[Long] = new RunTimeProvider
}
