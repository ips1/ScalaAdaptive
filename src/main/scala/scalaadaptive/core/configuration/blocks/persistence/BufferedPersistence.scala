package scalaadaptive.core.configuration.blocks.persistence

import scalaadaptive.core.configuration.BaseLongConfiguration
import scalaadaptive.core.configuration.blocks.helper.BlockWithHistoryPath
import scalaadaptive.core.logging.Logger
import scalaadaptive.core.runtime.history.historystorage.PersistentHistoryStorage
import scalaadaptive.core.runtime.history.serialization._

/**
  * Created by Petr Kubat on 5/1/17.
  *
  * Block setting the persistent storage history serialization to use the buffered option. The size of the buffer can
  * be configured by overriding the serializationBufferSize value.
  *
  */
trait BufferedPersistence extends BaseLongConfiguration with BlockWithHistoryPath {
  /**
    * Size of the serialization buffer.
    */
  protected val serializationBufferSize: Int = 10

  override def createPersistentHistoryStorage(log: Logger): Option[PersistentHistoryStorage[TMeasurement]] =
    Some(new PersistentHistoryStorage[TMeasurement](log,
      createHistoryStorage(log),
      new BufferedHistorySerializer[Long](
        new BasicHistorySerializer(
          rootHistoryPath,
          new BasicFileNameForKeyProvider,
          new LongEvaluationDataSerializer(','),
          createLogger),
        serializationBufferSize)
    ))
}
