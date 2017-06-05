package scalaadaptive.core.configuration

import java.nio.file.{Path, Paths}

import scalaadaptive.core.runtime.history._
import scalaadaptive.core.runtime.history.serialization.{BasicFileNameForKeyProvider, BasicHistorySerializer, HistorySerializer, LongRunDataSerializer}
import scala.collection.mutable.ArrayBuffer
import scalaadaptive.core.runtime.history.historystorage.PersistentHistoryStorage
import scalaadaptive.extensions.AverageableImplicits.LongIsAverageable

/**
  * Created by pk250187 on 4/22/17.
  */
trait BaseLongConfiguration extends BaseConfiguration {
  type TMeasurement = Long
  protected val rootPath: Path

  override val num = LongIsAverageable
  override val createPersistentHistoryStorage: () => Option[PersistentHistoryStorage[TMeasurement]] = () =>
    Some(new PersistentHistoryStorage[TMeasurement](
        createHistoryStorage(),
        new BasicHistorySerializer(
          rootPath,
          new BasicFileNameForKeyProvider,
          new LongRunDataSerializer(','),
          createLogger())
    ))
}
