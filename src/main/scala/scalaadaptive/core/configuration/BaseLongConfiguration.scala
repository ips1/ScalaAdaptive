package scalaadaptive.core.configuration

import java.nio.file.Path

import scalaadaptive.core.runtime.history.historystorage.PersistentHistoryStorage
import scalaadaptive.core.runtime.history.serialization.{BasicFileNameForKeyProvider, BasicHistorySerializer, LongEvaluationDataSerializer}
import scalaadaptive.extensions.AverageableImplicits.LongIsAverageable

/**
  * Created by Petr Kubat on 4/22/17.
  *
  * Extends the [[scalaadaptive.core.configuration.BaseConfiguration]] by fixing the TMeasurement type to [[Long]]
  * to be able to set up a persistent storage.
  *
  * Uses the following implementations for the persistent storage:
  * [[scalaadaptive.core.runtime.history.historystorage.PersistentHistoryStorage]],
  * [[scalaadaptive.core.runtime.history.serialization.BasicHistorySerializer]],
  * [[scalaadaptive.core.runtime.history.serialization.BasicFileNameForKeyProvider]],
  * [[scalaadaptive.core.runtime.history.serialization.LongEvaluationDataSerializer]]
  *
  */
trait BaseLongConfiguration extends BaseConfiguration {
  type TMeasurement = Long
  /**
    * The root path to the persisted histories, used as an argument of
    * [[scalaadaptive.core.runtime.history.serialization.BasicHistorySerializer]].
    */
  protected val rootPath: Path

  override val num = LongIsAverageable
  override def createPersistentHistoryStorage: Option[PersistentHistoryStorage[TMeasurement]] =
    Some(new PersistentHistoryStorage[TMeasurement](
        createHistoryStorage,
        new BasicHistorySerializer(
          rootPath,
          new BasicFileNameForKeyProvider,
          new LongEvaluationDataSerializer(','),
          createLogger)
    ))
}
