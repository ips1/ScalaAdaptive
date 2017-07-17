package scalaadaptive.core.configuration

import java.nio.file.Path

import scalaadaptive.core.logging.Logger
import scalaadaptive.core.runtime.history.historystorage.PersistentHistoryStorage
import scalaadaptive.core.runtime.history.serialization.{BasicFileNameForKeyProvider, BasicHistorySerializer, LongEvaluationDataSerializer}
import scalaadaptive.extensions.AverageableImplicits.LongIsAverageable

/**
  * Created by Petr Kubat on 4/22/17.
  *
  * Extends the [[scalaadaptive.core.configuration.BaseConfiguration]] by fixing the TMeasurement type to [[Long]].
  *
  */
trait BaseLongConfiguration extends BaseConfiguration {
  type TMeasurement = Long
  override val num = LongIsAverageable
}
