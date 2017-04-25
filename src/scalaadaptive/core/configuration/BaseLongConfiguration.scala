package scalaadaptive.core.configuration

import java.nio.file.{Path, Paths}

import scalaadaptive.core.runtime.history._
import scalaadaptive.core.runtime.history.serialization.{BasicFileNameForKeyProvider, BasicHistorySerializer, LongRunDataSerializer}

import scala.collection.mutable.ArrayBuffer

/**
  * Created by pk250187 on 4/22/17.
  */
trait BaseLongConfiguration extends BaseConfiguration {
  type MeasurementType = Long
  protected val rootPath: Path
  override val num = Numeric.LongIsIntegral
  override val persistentHistoryStorageFactory: () => Option[PersistentHistoryStorage[MeasurementType]] = () =>
    Some(new PersistentHistoryStorage[MeasurementType](
        new MapHistoryStorage[MeasurementType](key =>
          new FullRunHistory[MeasurementType](key, new ArrayBuffer[RunData[MeasurementType]]())(num)
        ),
        new BasicHistorySerializer(rootPath, new BasicFileNameForKeyProvider, new LongRunDataSerializer(','))
    ))
}
