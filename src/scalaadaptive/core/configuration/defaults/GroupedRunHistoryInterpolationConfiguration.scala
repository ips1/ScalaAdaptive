package scalaadaptive.core.configuration.defaults

import scalaadaptive.core.configuration.BaseLongConfiguration
import scalaadaptive.core.runtime.history.historystorage.{HistoryStorage, MapHistoryStorage}
import scalaadaptive.core.runtime.history.runhistory.{GroupedRunHistory, ImmutableFullRunHistory}

/**
  * Created by pk250187 on 5/1/17.
  */
object GroupedRunHistoryInterpolationConfiguration
  extends BaseLongConfiguration
  with InterpolationSelection
  with RunTimeMeasurement
  with DefaultPath
  with BufferedSerialization
  // Temporary change:
  with NoGrouping {
    override val historyStorageFactory: () => HistoryStorage[MeasurementType] = () => {
      new MapHistoryStorage[MeasurementType](key =>
        new GroupedRunHistory[Long](key)(num)
      )
    }
}
