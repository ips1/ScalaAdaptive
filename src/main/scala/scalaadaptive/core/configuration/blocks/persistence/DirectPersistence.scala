package scalaadaptive.core.configuration.blocks.persistence

import scalaadaptive.core.configuration.BaseLongConfiguration
import scalaadaptive.core.configuration.blocks.helper.BlockWithHistoryPath
import scalaadaptive.core.logging.Logger
import scalaadaptive.core.runtime.history.historystorage.PersistentHistoryStorage
import scalaadaptive.core.runtime.history.serialization.{BasicFileNameForKeyProvider, BasicHistorySerializer, LongEvaluationDataSerializer}

/**
  * Created by Petr Kubat on 7/17/17.
  *
  * Block setting the persistent storage history serialization to store the values directly.
  *
  */
trait DirectPersistence extends BaseLongConfiguration with BlockWithHistoryPath {
  override def createPersistentHistoryStorage(log: Logger): Option[PersistentHistoryStorage[TMeasurement]] =
    Some(new PersistentHistoryStorage[TMeasurement](log,
      createHistoryStorage(log),
      new BasicHistorySerializer(
        rootHistoryPath,
        new BasicFileNameForKeyProvider,
        new LongEvaluationDataSerializer(','),
        createLogger)
    ))
}
