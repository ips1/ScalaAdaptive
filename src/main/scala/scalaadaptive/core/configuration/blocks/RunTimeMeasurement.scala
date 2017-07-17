package scalaadaptive.core.configuration.blocks

import scalaadaptive.core.configuration.BaseLongConfiguration
import scalaadaptive.core.runtime.history.evaluation.{EvaluationProvider, RunTimeProvider}

/**
  * Created by Petr Kubat on 5/1/17.
  *
  * Block setting the default [[scalaadaptive.core.runtime.history.evaluation.EvaluationProvider]] to
  * [[scalaadaptive.core.runtime.history.evaluation.RunTimeProvider]].
  *
  */
trait RunTimeMeasurement extends BaseLongConfiguration {
  override def createEvaluationProvider: EvaluationProvider[Long] =
    new RunTimeProvider
}
