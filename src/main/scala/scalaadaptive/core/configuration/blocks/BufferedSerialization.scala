package scalaadaptive.core.configuration.blocks

import scalaadaptive.core.configuration.BaseLongConfiguration
import scalaadaptive.core.runtime.history.historystorage.PersistentHistoryStorage
import scalaadaptive.core.runtime.history.serialization._

/**
  * Created by Petr Kubat on 5/1/17.
  *
  * Block setting the history serialization to use the buffered option. The size of the buffer can be configured
  * by overriding the value.
  *
  */
trait BufferedSerialization extends BaseLongConfiguration {
  /**
    * Size of the serialization buffer.
    */
  protected val serializationBufferSize: Int = 15

  override val createPersistentHistoryStorage: Option[PersistentHistoryStorage[TMeasurement]] =
    Some(new PersistentHistoryStorage[TMeasurement](
      createHistoryStorage,
      new BufferedHistorySerializer[Long](
        new BasicHistorySerializer(
          rootPath,
          new BasicFileNameForKeyProvider,
          new LongEvaluationDataSerializer(','),
          createLogger),
        serializationBufferSize)
    ))
}
