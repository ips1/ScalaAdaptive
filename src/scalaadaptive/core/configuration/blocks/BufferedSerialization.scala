package scalaadaptive.core.configuration.blocks

import scalaadaptive.core.configuration.BaseLongConfiguration
import scalaadaptive.core.runtime.history.historystorage.PersistentHistoryStorage
import scalaadaptive.core.runtime.history.serialization._

/**
  * Created by pk250187 on 5/1/17.
  */
trait BufferedSerialization extends BaseLongConfiguration {
  override val persistentHistoryStorageFactory: () => Option[PersistentHistoryStorage[MeasurementType]] = () =>
    Some(new PersistentHistoryStorage[MeasurementType](
      historyStorageFactory(),
      new BufferedHistorySerializer[Long](
        new BasicHistorySerializer(
          rootPath,
          new BasicFileNameForKeyProvider,
          new LongRunDataSerializer(','),
          logger),
        20)
    ))
}
